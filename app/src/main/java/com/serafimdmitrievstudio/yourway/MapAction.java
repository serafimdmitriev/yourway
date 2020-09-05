package com.serafimdmitrievstudio.yourway;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Serafim on 31.08.2017.
 */

public class MapAction {
    Short LastTypeOfPoint;
    MapPoint PointToAdd;
    MapSegment SegmentToAdd;
    MapSegment SegmentToAdd2;
    MapSegment SegmentToAdd3;
    MapSegment SegmentToDelete;

    void drawPoint() {
        if (!PointToAdd.isVisible()) {
            PointToAdd.draw(GoogleMapHandler.getGoogleMap());
        }
    }

    void removePoint() {
        PointToAdd.remove();
    }

    void drawSegment() {
        if (!SegmentToAdd.isVisible()) {
            SegmentToAdd.draw(GoogleMapHandler.getGoogleMap());
        }
    }

    void removeSegment() {
        SegmentToAdd.remove();
    }

    JSONObject getJSON() {
        JSONObject ActionObject = new JSONObject();

        try {

            if (PointToAdd != null) {
                ActionObject.put("pointToAddType", PointToAdd.getType().getType());
                ActionObject.put("pointToAddLatitude1", PointToAdd.getLatLng().latitude);
                ActionObject.put("pointToAddLongitude1", PointToAdd.getLatLng().longitude);
                ActionObject.put("pointToAddWheelchairAccessible", PointToAdd.getQuality().isAccessibleForWheelchair());
                ActionObject.put("pointToAddElectricWheelchairAccessible", PointToAdd.getQuality().isAccessibleForElectricWheelchair());
                ActionObject.put("pointToAddGeneralState", PointToAdd.getQuality().getGeneralState());
            } else {
                ActionObject.put("pointToAddType", null);
                ActionObject.put("pointToAddLatitude1", null);
                ActionObject.put("pointToAddLongitude1", null);
                ActionObject.put("pointToAddWheelchairAccessible", null);
                ActionObject.put("pointToAddElectricWheelchairAccessible", null);
                ActionObject.put("pointToAddGeneralState", null);
            }

            if (SegmentToAdd != null) {
                ActionObject.put("segmentToAddType", SegmentToAdd.getType().getType());
                ActionObject.put("segmentToAddLatitude1", SegmentToAdd.getPoint1().getLatLng().latitude);
                ActionObject.put("segmentToAddLongitude1", SegmentToAdd.getPoint1().getLatLng().longitude);
                ActionObject.put("segmentToAddLatitude2", SegmentToAdd.getPoint2().getLatLng().latitude);
                ActionObject.put("segmentToAddLongitude2", SegmentToAdd.getPoint2().getLatLng().longitude);
                ActionObject.put("segmentToAddWheelchairAccessible", SegmentToAdd.getQuality().isAccessibleForWheelchair());
                ActionObject.put("segmentToAddElectricWheelchairAccessible", SegmentToAdd.getQuality().isAccessibleForElectricWheelchair());
                ActionObject.put("segmentToAddGrade", SegmentToAdd.getQuality().getGrade());
                ActionObject.put("segmentToAddGeneralState", SegmentToAdd.getQuality().getGeneralState());
                try {
                    ActionObject.put("segmentToAddStreetName", URLEncoder.encode(SegmentToAdd.getStreetName(), "UTF-8"));
                } catch (Exception e) {
                    Log.write(e.getMessage());
                    for (StackTraceElement el : e.getStackTrace()) {
                        Log.write(el.toString());
                    }
                    ActionObject.put("segmentToAddStreetName", "");
                }
            } else {
                ActionObject.put("segmentToAddType", null);
                ActionObject.put("segmentToAddLatitude1", null);
                ActionObject.put("segmentToAddLongitude1", null);
                ActionObject.put("segmentToAddLatitude2", null);
                ActionObject.put("segmentToAddLongitude2", null);
                ActionObject.put("segmentToAddWheelchairAccessible", null);
                ActionObject.put("segmentToAddElectricWheelchairAccessible", null);
                ActionObject.put("segmentToAddGrade", null);
                ActionObject.put("segmentToAddGeneralState", null);
                ActionObject.put("segmentToAddStreetName", null);
            }

            if (SegmentToAdd2 != null) {
                ActionObject.put("segmentToAdd2Type", SegmentToAdd2.getType().getType());
                ActionObject.put("segmentToAdd2Latitude1", SegmentToAdd2.getPoint1().getLatLng().latitude);
                ActionObject.put("segmentToAdd2Longitude1", SegmentToAdd2.getPoint1().getLatLng().longitude);
                ActionObject.put("segmentToAdd2Latitude2", SegmentToAdd2.getPoint2().getLatLng().latitude);
                ActionObject.put("segmentToAdd2Longitude2", SegmentToAdd2.getPoint2().getLatLng().longitude);
                ActionObject.put("segmentToAdd2WheelchairAccessible", SegmentToAdd2.getQuality().isAccessibleForWheelchair());
                ActionObject.put("segmentToAdd2ElectricWheelchairAccessible", SegmentToAdd2.getQuality().isAccessibleForElectricWheelchair());
                ActionObject.put("segmentToAdd2Grade", SegmentToAdd2.getQuality().getGrade());
                ActionObject.put("segmentToAdd2GeneralState", SegmentToAdd2.getQuality().getGeneralState());
            } else {
                ActionObject.put("segmentToAdd2Type", null);
                ActionObject.put("segmentToAdd2Latitude1", null);
                ActionObject.put("segmentToAdd2Longitude1", null);
                ActionObject.put("segmentToAdd2Latitude2", null);
                ActionObject.put("segmentToAdd2Longitude2", null);
                ActionObject.put("segmentToAdd2WheelchairAccessible", null);
                ActionObject.put("segmentToAdd2ElectricWheelchairAccessible", null);
                ActionObject.put("segmentToAdd2Grade", null);
                ActionObject.put("segmentToAdd2GeneralState", null);
            }

            if (SegmentToAdd3 != null) {
                ActionObject.put("segmentToAdd3Type", SegmentToAdd3.getType().getType());
                ActionObject.put("segmentToAdd3Latitude1", SegmentToAdd3.getPoint1().getLatLng().latitude);
                ActionObject.put("segmentToAdd3Longitude1", SegmentToAdd3.getPoint1().getLatLng().longitude);
                ActionObject.put("segmentToAdd3Latitude2", SegmentToAdd3.getPoint2().getLatLng().latitude);
                ActionObject.put("segmentToAdd3Longitude2", SegmentToAdd3.getPoint2().getLatLng().longitude);
                ActionObject.put("segmentToAdd3WheelchairAccessible", SegmentToAdd3.getQuality().isAccessibleForWheelchair());
                ActionObject.put("segmentToAdd3ElectricWheelchairAccessible", SegmentToAdd3.getQuality().isAccessibleForElectricWheelchair());
                ActionObject.put("segmentToAdd3Grade", SegmentToAdd3.getQuality().getGrade());
                ActionObject.put("segmentToAdd3GeneralState", SegmentToAdd3.getQuality().getGeneralState());
            } else {
                ActionObject.put("segmentToAdd3Type", null);
                ActionObject.put("segmentToAdd3Latitude1", null);
                ActionObject.put("segmentToAdd3Longitude1", null);
                ActionObject.put("segmentToAdd3Latitude2", null);
                ActionObject.put("segmentToAdd3Longitude2", null);
                ActionObject.put("segmentToAdd3WheelchairAccessible", null);
                ActionObject.put("segmentToAdd3ElectricWheelchairAccessible", null);
                ActionObject.put("segmentToAdd3Grade", null);
                ActionObject.put("segmentToAdd3GeneralState", null);
            }

            if (SegmentToDelete != null) {
                ActionObject.put("segmentToDeleteType", SegmentToDelete.getType().getType());
                ActionObject.put("segmentToDeleteLatitude1", SegmentToDelete.getPoint1().getLatLng().latitude);
                ActionObject.put("segmentToDeleteLongitude1", SegmentToDelete.getPoint1().getLatLng().longitude);
                ActionObject.put("segmentToDeleteLatitude2", SegmentToDelete.getPoint2().getLatLng().latitude);
                ActionObject.put("segmentToDeleteLongitude2", SegmentToDelete.getPoint2().getLatLng().longitude);
            } else {
                ActionObject.put("segmentToDeleteType", null);
                ActionObject.put("segmentToDeleteLatitude1", null);
                ActionObject.put("segmentToDeleteLongitude1", null);
                ActionObject.put("segmentToDeleteLatitude2", null);
                ActionObject.put("segmentToDeleteLongitude2", null);
            }
        } catch (Exception e) {
            Log.write(e.getMessage());
        }

        return ActionObject;
    }

}
