package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by Serafim on 07.05.2017.
 */

public class CustomScrollView extends CustomView{
    LinearLayout Items;
    CustomScrollViewReloader reloader;

    Animation appearFromVoidAnimation;
    Animation removeToVoidAnimation;
    Animation stayingInVoidAnimation;

    public CustomScrollView(View view, View RouteItems) {
        super(view);
        this.Items = (LinearLayout) RouteItems;

        appearFromVoidAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.appear_from_void);
        appearFromVoidAnimation.setDuration(90);
        removeToVoidAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.remove_to_void);
        removeToVoidAnimation.setDuration(90);
        stayingInVoidAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.staying_in_void);
        removeToVoidAnimation.setDuration(500000);

        removeToVoidAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getView().startAnimation(stayingInVoidAnimation);
                reloader.reload(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    };

    public void addItem(View Item) {
        Items.addView(Item);
    }

    public void setReloader(CustomScrollViewReloader reloader) {
        this.reloader = reloader;
    }

    void reload(boolean useAnimation) {
        if (useAnimation) {
            view.startAnimation(removeToVoidAnimation);
        } else {
            reloader.reload(false);
        }
    }

    void appearFromVoidAnimation() {
        //getView().clearAnimation();
        getView().startAnimation(appearFromVoidAnimation);
    }

    Animation getRemoveToVoidAnimation() {
        return  removeToVoidAnimation;
    }

    /*

    public void fill(final ArrayList<RouteItem> ItemList, final Context context, GoogleMap mMap) {
        clear();

        for (int i = 0; i < ItemList.size(); i++) {
            RouteItems.addView(ItemList.get(i).getView(context));
            ItemList.get(i).setOnClickListener(context, mMap);
        }

        Log.write("ItemList added to RouteItems");
    }
    */

    public ScrollView getView() {
        return (ScrollView) view;
    }

    public int size() {
        return Items.getChildCount();
    }

    public void clear() {
        if (Items.getChildCount() > 0) {
            Items.removeAllViews();
        }
    };
}
