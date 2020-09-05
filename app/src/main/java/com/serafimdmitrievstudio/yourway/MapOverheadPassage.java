package com.serafimdmitrievstudio.yourway;

/**
 * Created by Serafim on 01.09.2017.
 */

public class MapOverheadPassage extends MapSegment {
    MapOverheadPassage(MapPoint point1, MapPoint point2, MapItemQuality quality) {
        super(point1, point2, MapItemType.OverheadPassage, quality, "");
    }
}
