package com.serafimdmitrievstudio.yourway;

import android.os.Environment;
import android.os.StrictMode;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Serafim on 07.06.2017.
 */

public class DatabaseHandler {
    private static String FILENAME = null;

    private static void checkFilenames() {
        if (FILENAME == null) {
            FILENAME = Environment.getExternalStorageDirectory().toString() + "/DataBase.txt";
        }
    }

    static boolean clear() throws Exception{

        FileWriter fwOb = new FileWriter(FILENAME, false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();

        return true;
    }

    static boolean addNewSegment(MapSegment segment) {
        checkFilenames();
        String NameOfSegment = "\n" + Integer.toString(segment.getId());

        String string = NameOfSegment
                + "\n" + Integer.toString(segment.getType().getType())
                + "\n" + Double.toString(segment.getPoint1().getLatLng().latitude)
                + "\n" + Double.toString(segment.getPoint1().getLatLng().longitude)
                + "\n" + Double.toString(segment.getPoint2().getLatLng().latitude)
                + "\n" + Double.toString(segment.getPoint2().getLatLng().longitude)
                + "\n" + Boolean.toString(segment.getQuality().isAccessibleForWheelchair())
                + "\n" + Boolean.toString(segment.getQuality().isAccessibleForElectricWheelchair())
                + "\n" + Short.toString(segment.getQuality().getGrade())
                + "\n" + Short.toString(segment.getQuality().getGeneralState());

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILENAME, true)));
            out.println(string);
            out.close();

            return true;
        } catch (IOException e) {

            return false;
        }

    }

    static boolean addNewPoint(MapPoint point) {
        checkFilenames();
        String NameOfSegment = "\n" + Integer.toString(point.getId()) + "\n" + Short.toString(point.getType().getType());

        String string = NameOfSegment
                + "\n" + Double.toString(point.getLatLng().latitude)
                + "\n" + Double.toString(point.getLatLng().longitude);

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILENAME, true)));
            out.println(string);
            out.close();

            return true;
        } catch (IOException e) {

            return false;
        }
    }

    private static MapPoint addPoint(ArrayList<MapPoint> DatabasePoints, LatLng latLng) {
        for (int i = 0; i < DatabasePoints.size(); i++) {
            if (DatabasePoints.get(i).getLatLng().latitude == latLng.latitude &&
                    DatabasePoints.get(i).getLatLng().longitude == latLng.longitude) {
                return DatabasePoints.get(i);
            }
        }

        MapPoint point = new MapPoint(latLng);
        DatabasePoints.add(point);
        Log.write("Point added");
        return point;
    }

    static boolean loadDatabase(final ArrayList<MapPoint> DatabasePoints, final ArrayList<MapSegment> DatabaseSegment) {
        checkFilenames();

        try {
            Log.write("RETROFIT 0");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Log.write("RETROFIT 1");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://accesspassed.com:8080/") //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();

            Log.write("RETROFIT 2");
            ServerApi serverApi = retrofit.create(ServerApi.class);

            Log.write("RETROFIT 3");
            List<ServerGetMapResponse> response;
            try {
                Log.write("RETROFIT 4");
                response = serverApi.getMap("getMap").execute().body();
            } catch (Exception e) {
                Log.write("RETROFIT 5");
                ErrorLayout.show(StringHandler.getString(R.string.map_loading_was_failed));
                Log.write(e.getMessage());
                for (StackTraceElement el : e.getStackTrace()) {
                    Log.write(el.toString());
                }
                return false;
            }

            for (int WaitingTime = 0;;) {
                if (response != null) {
                    break;
                } else {
                    if (WaitingTime < 7000) {
                        try {
                            Thread.sleep(1);
                            WaitingTime++;
                        } catch (Exception e) {
                            Log.write(e.getMessage());
                        }
                    } else {
                        ErrorLayout.show(StringHandler.getString(R.string.map_loading_was_failed));
                        return false;
                    }
                }
            }

            Log.write("Size: " + Integer.toString(response.size()));

            for (int i = 0; i < response.size(); i++) {
                switch (response.get(i).getType()) {
                    case MapItemType.Road :
                    case MapItemType.Crossing:
                    case MapItemType.OverheadPassage :
                    case MapItemType.UndergroundPassage : {

                        Log.write("RETROFIT 6");
                        MapPoint firstPoint = addPoint(DatabasePoints,
                                response.get(i).getLatLng1());
                        MapPoint secondPoint = addPoint(DatabasePoints,
                                response.get(i).getLatLng2());

                        Log.write("RETROFIT 7");
                        DatabaseSegment.add(
                                new MapSegment(firstPoint, secondPoint, response.get(i).getType(),
                                        response.get(i).getMapItemQuality(), URLDecoder.decode(response.get(i).getStreetName(), "UTF-8")));

                        Log.write("RETROFIT 80");
                        DatabaseSegment.get(DatabaseSegment.size()-1).setId(response.get(i).getId());

                        Log.write("Segment added");

                    } break;
                    case MapItemType.Lift :
                    case MapItemType.Ramp : {

                        Log.write("RETROFIT 90");
                        MapPoint point = addPoint(DatabasePoints,
                                response.get(i).getLatLng1());
                        point.setType(response.get(i).getType(), GoogleMapHandler.getGoogleMap());
                        DatabaseSegment.get(DatabaseSegment.size()-1).setId(response.get(i).getId());
                        DatabaseSegment.get(DatabaseSegment.size()-1).setQuality(response.get(i).getMapItemQuality());

                        Log.write("Point added");
                    } break;
                }
            }

            if (GoogleMapHandler.getGoogleMap() != null) {

                Map.draw(GoogleMapHandler.getGoogleMap(), false);

            }

        } catch (Exception e) {
            Log.write("RETROFIT 10");
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }

        return true;



        /*
        try {
            /*
            Scanner scanner = new Scanner(new File(FILENAME));
            //BufferedReader reader = new BufferedReader(new FileReader(FILENAME));



            Log.write("Reading started");
            while (scanner.hasNext()) {
                int id;
                short type;

                for (;;) {
                    String stringId = scanner.nextLine();
                    if (stringId.equals("") || stringId.equals(" ")) {
                        id = Integer.parseInt(scanner.nextLine());
                        break;
                    }
                }

                type = Short.parseShort(scanner.nextLine());

                switch (type) {
                    case MapItemType.Road :
                    case MapItemType.Crossing :
                    case MapItemType.OverheadPassage :
                    case MapItemType.UndergroundPassage : {

                        MapPoint firstPoint = addPoint(DatabasePoints,
                                Double.parseDouble(scanner.nextLine()),
                                Double.parseDouble(scanner.nextLine()));
                        MapPoint secondPoint = addPoint(DatabasePoints,
                                Double.parseDouble(scanner.nextLine()),
                                Double.parseDouble(scanner.nextLine()));

                        DatabaseSegment.add(new MapSegment(firstPoint, secondPoint, type,
                                new MapItemQuality(Boolean.parseBoolean(scanner.nextLine()),
                                        Boolean.parseBoolean(scanner.nextLine()),
                                        Short.parseShort(scanner.nextLine()),
                                        Short.parseShort(scanner.nextLine()))));
                        DatabaseSegment.get(DatabaseSegment.size()-1).setId(id);

                        Log.write("Segment added");

                    } break;
                    case MapItemType.Lift :
                    case MapItemType.Ramp : {

                        MapPoint point = addPoint(DatabasePoints,
                                Double.parseDouble(scanner.nextLine()),
                                Double.parseDouble(scanner.nextLine()));
                        point.setType(type, GoogleMapHandler.getGoogleMap());
                        DatabaseSegment.get(DatabaseSegment.size()-1).setId(id);

                        Log.write("Point added");
                    } break;
                }

            }

            scanner.close();
        }
        catch (Exception e) {
            ErrorLayout.show("Error while loading map. Please, check Database.");
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
            return false;
        }
        */
    }

}
