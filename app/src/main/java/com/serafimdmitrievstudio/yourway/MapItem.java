package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Serafim on 06.05.2017.
 */

public abstract class MapItem {
    private static Integer MaxId = 0;
    private Integer Id = 0;

    MapItemType type;
    MapItemType getType() {
        return type;
    }

    MapItemQuality quality;
    MapItemQuality getQuality() {
        return quality;
    }
    void setQuality(MapItemQuality quality) {
        this.quality = quality;
    }

    MapItem() {
        MaxId += 1;
        Id = MaxId;
    }

    Integer getId() {
        return Id;
    }

    void setId(int Id) {
        this.Id = Id;
    }

    void setType(short type, GoogleMap googleMap) {
        this.type.setType(type);
        if (this instanceof MapPoint) {
            if (((MapPoint)this).isVisible()) {
                ((MapPoint)this).redraw(googleMap);
            }
        }
    }
}
