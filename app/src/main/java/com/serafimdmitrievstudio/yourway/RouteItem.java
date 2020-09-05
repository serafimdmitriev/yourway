package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.View;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class RouteItem {
    static final int RoutePart = 0;
    static final int Warning = 1;
    static final int Caption = 2;
    private int itemType;

    String title;
    private Integer iconId;
    private Integer markerIconId;
    private Integer qualityColorId;
    private String firstText;
    private String secondText;
    private String additionalText;
    private RouteSplash splash;
    //private boolean yourWayItem;

    private MapItemQuality quality;
    MapItemType type;
    private ArrayList<LatLng> points;

    private Polyline polyline;
    private Marker marker;

    private static final PatternItem DASH = new Dash(30);
    private static final PatternItem GAP = new Gap(10);
    private static final List<PatternItem> PATTERN_POLYGON = Arrays.asList(GAP, DASH);

    RouteItem(int itemType) {
        title = "";
        firstText = "";
        secondText = "";
        additionalText = "";
        iconId = null;
        markerIconId = R.drawable.icon_point_marker;
        qualityColorId = R.color.colorLightGray;

        //yourWayItem = false;

        quality = null;
        type = null;
        polyline = null;
        marker = null;
        points = new ArrayList<>();

        this.itemType = itemType;

        switch (itemType) {
            case Warning: {
                iconId = R.drawable.icon_warning;
            } break;
        }
    }

    void setAdditionalText(String text) {
        additionalText = text;
    }

    @Override
    public String toString() {
        return title + " " + firstText + " " + secondText + " " + additionalText
                + quality.isAccessibleForWheelchair() + " "
                + quality.isAccessibleForElectricWheelchair() + " "
                + quality.getGrade() + " "
                + quality.getGeneralState();
    }

    /*
    void setYourWayItem(boolean is) {
        yourWayItem = is;
    }

    boolean isYourWayItem() {
        return yourWayItem;
    }
    */


    String getAdditionalText() {
        return additionalText;
    }


    boolean isSegment() {
        return points.size() > 1;
    }

    void addPoint(LatLng point) {
        points.add(point);
    }

    void addPoint(MapPoint point) {
        points.add(point.getLatLng());
        type = point.getType();
    }

    public void setType(MapItemType type) {
        this.type = type;
        if (type != null) {
            switch (type.getType()) {
                case MapItemType.Lift: {
                    title = StringHandler.getString(R.string.lift);
                    iconId = R.drawable.icon_lift;
                    markerIconId = R.drawable.icon_lift_marker;
                } break;
                case MapItemType.Ramp: {
                    title = StringHandler.getString(R.string.ramp);
                    iconId = R.drawable.icon_ramp;
                    markerIconId = R.drawable.icon_ramp_marker;
                } break;
                case MapItemType.Crossing: {
                    title = StringHandler.getString(R.string.crossing);
                    iconId = R.drawable.icon_passage;
                } break;
                case MapItemType.OverheadPassage: {
                    title = StringHandler.getString(R.string.overhead_passage);
                    iconId = R.drawable.icon_passage;
                } break;
                case MapItemType.UndergroundPassage: {
                    title = StringHandler.getString(R.string.underground_passage);
                    iconId = R.drawable.icon_passage;
                } break;
            }
        }
    }

    public MapItemType getType() {
        return type;
    }

    public MapItemQuality getQuality() {
        return quality;
    }

    void setQuality(int belongingOfThisItem, MapItemQuality quality) {
        this.quality = quality;

        if (quality != null) {
            switch (quality.getGeneralState()) {
                case MapItemQuality.Tolerantly: {
                    qualityColorId = R.color.colorAttention;
                } break;
                case MapItemQuality.Normal: {
                    qualityColorId = R.color.colorWarning;
                } break;
                case MapItemQuality.Excellent: {
                    qualityColorId = R.color.colorLightGreen;
                } break;
            }
        }
        /*
        if (quality != null) {
            if (quality.getGrade() == MapItemQuality.NoGrade
                    && (belongingOfThisItem == RouteBelonging.Wheelchair
                    && quality.isAccessibleForWheelchair()
                    || belongingOfThisItem == RouteBelonging.ElectricWheelchair
                    && quality.isAccessibleForElectricWheelchair()
                    || belongingOfThisItem == RouteBelonging.ElderlyPerson
                    || belongingOfThisItem == RouteBelonging.Pedestrian)) {
                qualityColorId = R.color.colorLightGreen;
            } else {
                qualityColorId = R.color.colorWarning;

            }
        }
        */
    }


    void draw(GoogleMap googleMap) {
        if (points.size() == 1) {
            if (marker == null) {
                MarkerOptions markerOptions;
                markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(markerIconId))
                        .anchor((float) 0.5, (float) 0.5)
                        .visible(false)
                        .position(new LatLng(0, 0));

                markerOptions.position(points.get(0));
                markerOptions.visible(true);

                marker = googleMap.addMarker(markerOptions);
            }
        } else {
            if (polyline == null) {

                PolylineOptions line = new PolylineOptions();
                line.width(15);
                line.pattern(PATTERN_POLYGON);

                if (quality == null) {
                    line.color(Color.GRAY);
                } else {
                    switch (quality.getGeneralState()) {
                        case MapItemQuality.Tolerantly: {
                            line.color(Color.rgb(255, 0, 0));
                        } break;
                        case MapItemQuality.Normal: {
                            line.color(Color.rgb(255, 255, 0));
                        } break;
                        case MapItemQuality.Excellent: {
                            line.color(Color.rgb(0, 255, 0));
                        } break;
                        default: {
                            line.color(Color.LTGRAY);
                        } break;
                    }
                    /*
                    if (quality.getGrade() == MapItemQuality.NoGrade &&
                            quality.isAccessibleForWheelchair() &&
                            quality.isAccessibleForElectricWheelchair()) {
                        line.color(Color.GREEN);
                    } else {
                        line.color(Color.YELLOW);
                    }
                    */
                }

                for (int i = 0; i < points.size(); i++) {
                    line.add(points.get(i));
                }

                polyline = googleMap.addPolyline(line);
            }
        }
    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setIconId(int iconId) {
        this.iconId = iconId;
    }

    void setMarkerIconId(int iconId) {
        this.markerIconId = iconId;
    }

    void setFirstText(String text) {
        this.firstText = text;
    }

    void setSecondText(String text) {
        this.secondText = text;
    }

    void setQualityColorId(int color) {
        qualityColorId = color;
    }

    int getQualityColorId() {
        return qualityColorId;
    }

    double getLength() {
        double Length = 0;
        for (int i = 1; i < points.size(); i++) {
            Location locationA = new Location("point A");

            locationA.setLatitude(points.get(i-1).latitude);
            locationA.setLongitude(points.get(i-1).longitude);

            Location locationB = new Location("point B");

            locationB.setLatitude(points.get(i).latitude);
            locationB.setLongitude(points.get(i).longitude);

            Length += locationA.distanceTo(locationB);
        }
        return Length;
    }

    void setTextAsLength() {
        if (points.size() > 1) {
            firstText = Integer.toString((int) getLength()) + " " + StringHandler.getString(R.string.meters);
        } else {
            firstText = StringHandler.getString(R.string.error);
        }
    }

    void generateSplash(final Context context, final GoogleMap mMap) {
        switch (itemType) {
            case RoutePart: {
                if (title.equals("")) {
                    splash = new RouteSplash(context, RouteSplash.OnlyText);
                } else {
                    splash = new RouteSplash(context, RouteSplash.TitleAndText);
                }
            } break;
            case Warning: {
                splash = new RouteSplash(context, RouteSplash.Warning);
            } break;
            case Caption: {
                splash = new RouteSplash(context, RouteSplash.TitleAndTwoTexts);
            } break;
        }


        splash.setTitle(title);
        splash.setSecondText(secondText);

        if (splash.getFirstTextView() != null && !firstText.equals(""))  {
            splash.getFirstTextView().setText(firstText);
            splash.getFirstTextView().setVisibility(View.VISIBLE);
        }
        if (splash.getAdditionalTextView() != null && !additionalText.equals(""))  {
            splash.getAdditionalTextView().setText(additionalText);
            splash.getAdditionalTextView().setVisibility(View.VISIBLE);
        }

        splash.setIcon(iconId);
        splash.setQualitySignal(context, qualityColorId);

        if (points.size() != 0) {
            splash.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

                    for (int i = 0; i < points.size(); i++) {
                        latLngBuilder.include(points.get(i));
                    }

                    int size = context.getResources().getDisplayMetrics().widthPixels;
                    LatLngBounds latLngBounds = latLngBuilder.build();
                    CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
                    mMap.animateCamera(track);

                    CustomViewGroup.open("RouteViews", true, false);
                }
            });
        }
    }

    View getSplash() {
        return splash.getView();
    }

    /*
    void setOnClickListener(final Context context, final GoogleMap mMap) {
        splash.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

                for (int i = 0; i < points.size(); i++) {
                    latLngBuilder.include(points.get(i));
                }

                int size = context.getResources().getDisplayMetrics().widthPixels;
                LatLngBounds latLngBounds = latLngBuilder.build();
                CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
                mMap.animateCamera(track);
            }
        });
    }
*/

    /*
    void setDirectionRelativelyToNextSegment(int Direction) {
        switch (Direction) {
            case RouteDirection.AHEAD: actionString = "GO AHEAD"; break;
            case RouteDirection.ALITTLEBITLEFT: actionString = "KEEP TO THE LEFT"; break;
            case RouteDirection.LEFT: actionString = "TURN LEFT"; break;
            case RouteDirection.ABRUPTLYLEFT: actionString = "TURN ABRUPTLY LEFT"; break;
            case RouteDirection.AROUND: actionString = "TURN AROUND"; break;
            case RouteDirection.ALITTLEBITRIGHT: actionString = "KEEP TO THE RIGHT"; break;
            case RouteDirection.RIGHT: actionString = "TURN RIGHT"; break;
            case RouteDirection.ABRUPTLYRIGHT: actionString = "TURN ABRUPTLY RIGHT"; break;
        }
    }

    void setDirection(int Direction) {
        switch (Direction) {
            case RouteDirection.NORTH: actionString = "GO TO THE NORTH"; break;
            case RouteDirection.NORTHEAST: actionString = "GO TO THE NORTH-EAST"; break;
            case RouteDirection.EAST: actionString = "GO TO THE EAST"; break;
            case RouteDirection.SOUTHEAST: actionString = "GO TO THE SOUTH-EAST"; break;
            case RouteDirection.SOUTH: actionString = "GO TO THE SOUTH"; break;
            case RouteDirection.SOUTHWEST: actionString = "GO THE SOUTH-WEST"; break;
            case RouteDirection.WEST: actionString = "GO TO THE WEST"; break;
            case RouteDirection.NORTHWEST: actionString = "GO TO THE NORTH-WEST"; break;
        }
    }
    */
/*
    View getView(Context context) {

        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        column =  inflater.inflate(R.layout.column, null);

        TextView TitleView = (TextView) column.findViewById(R.id.title);
        ImageView RoadLeft = (ImageView) column.findViewById(R.id.roadLeft);
        ImageView IconView = (ImageView) column.findViewById(R.id.icon);
        ImageView RoadRight = (ImageView) column.findViewById(R.id.roadRight);
        TextView Description1 = (TextView) column.findViewById(R.id.description1);
        TextView Description2 = (TextView) column.findViewById(R.id.description2);
        TextView Action = (TextView) column.findViewById(R.id.description3);

        TitleView.setText(title);
        RoadLeft.setImageResource(icon.roadLeft);
        IconView.setImageResource(icon.icon);
        RoadRight.setImageResource(icon.roadRight);
        Description1.setText(description1);
        Description2.setText(description2);
        Action.setText(actionString);

        return column;

    }
    */
}
