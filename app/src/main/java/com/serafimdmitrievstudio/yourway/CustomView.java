package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Serafim on 06.05.2017.
 */

public class CustomView {
    View view;
    private Animation OpeningAnimation;
    private Animation ClosingAnimation;

    boolean isOpeningOrClosing = false;

    private static ArrayList<CustomView> AllViews;
    private static HashMap<Integer, Boolean> AllViewsVisibility;

    public CustomView(View view){
        this.view = view;

        if (AllViews == null) {
            AllViews = new ArrayList<>();
        }
        AllViews.add(this);
        if (AllViewsVisibility == null) {
            AllViewsVisibility = new HashMap<>();
        }
        AllViewsVisibility.put(view.getId(), false);



        OpeningAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.appear_leftward);
        ClosingAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.remove_leftward);
    }

    public static ArrayList<CustomView> getAllViews() {
        return AllViews;
    }

    public static CustomView getViewById(int Id) {
        for (int i = 0; i < AllViews.size(); i++) {
            if (AllViews.get(i).getView().getId() == Id) {
                return AllViews.get(i);
            }
        }

        return null;
    }

    /*
    public static boolean checkVisibilityOfAllViews() {
        for (int i = 0; i < AllViews.size(); i++) {
            if (AllViews.get(i).getView().getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return false;
    }
*/

    public Animation getOpeningAnimation() {
        return OpeningAnimation;
    }

    public Animation getClosingAnimation() {
        return ClosingAnimation;
    }

    public void setAnimations(Context context, int OA, int CA) {
        OpeningAnimation = AnimationUtils.loadAnimation(context, OA);
        ClosingAnimation = AnimationUtils.loadAnimation(context, CA);

    }

    public boolean isOpeningOrClosing() {
        return isOpeningOrClosing;
    }

    public void open() {
        if (view.getVisibility() == View.INVISIBLE && !isOpeningOrClosing) {
            OpeningAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isOpeningOrClosing = true;
                    AllViewsVisibility.put(view.getId(), true);
                    view.setVisibility(View.VISIBLE);
                    view.invalidate();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isOpeningOrClosing = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(OpeningAnimation);
        }
    }
    public void close() {
        if (view.getVisibility() == View.VISIBLE && !isOpeningOrClosing) {
            ClosingAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isOpeningOrClosing = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    AllViewsVisibility.put(view.getId(), false);
                    view.setVisibility(View.INVISIBLE);
                    isOpeningOrClosing = false;
                    view.invalidate();


                    try {
                        if (!CustomViewGroup.getOpenedViewGroup().isVisible()) {
                            CustomViewGroup.open("MainViews", true, false);
                        }
                    } catch (Exception e) {
                        CustomViewGroup.open("MainViews", true, false);
                    }

                    /*if (!AllViewsVisibility.containsValue(true)) {
                        CustomViewGroup.open("MainViews", true, false);
                    }
                    */
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(ClosingAnimation);
        }
    }

    public int getVisibility() {
        return view.getVisibility();
    }

    public View getView() {
        return view;
    }

    public static void initializeAllViews(final Activity activity) {
        final CustomImageButton ButtonOptions;
        ButtonOptions = new CustomImageButton(activity.findViewById(R.id.buttonOptions));
        ButtonOptions.setImages(activity, R.drawable.icon_options, R.drawable.icon_options_pressed);
        ButtonOptions.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonOptions.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.getOpenedViewGroup().handleClick(activity, ButtonOptions.getView().getId());
            }
        });

        final CustomEditText DepartureEditText;
        DepartureEditText = new CustomEditText(activity.findViewById(R.id.editTextDeparture));
        DepartureEditText.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);
        DepartureEditText.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomViewGroup.open("DepartureViews", true, false);
                return false;
            }
        });
        LocationHandler.setDepartureEditTextId(DepartureEditText.getView().getId());

        final CustomScrollView ScrollViewDeparture;
        ScrollViewDeparture = new CustomScrollView(activity.findViewById(R.id.ScrollViewDeparture), activity.findViewById(R.id.LinearLayoutDeparture));
        ScrollViewDeparture.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomEditText DestinationEditText;
        DestinationEditText = new CustomEditText(activity.findViewById(R.id.editTextDestination));
        DestinationEditText.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);
        DestinationEditText.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);
        DestinationEditText.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomViewGroup.open("DestinationViews", true, false);
                return false;
            }
        });
        LocationHandler.setDestinationEditTextId(DestinationEditText.getView().getId());

        final CustomScrollView ScrollViewDestination;
        ScrollViewDestination = new CustomScrollView(activity.findViewById(R.id.ScrollViewDestination), activity.findViewById(R.id.LinearLayoutDestination));
        ScrollViewDestination.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomImageButton ButtonIncreaseZoom;
        ButtonIncreaseZoom = new CustomImageButton(activity.findViewById(R.id.buttonIncreaseZoom));
        ButtonIncreaseZoom.setImages(activity, R.drawable.icon_plus, R.drawable.icon_plus_pressed);
        ButtonIncreaseZoom.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonIncreaseZoom.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleMapHandler.getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition
                        (CameraPosition.fromLatLngZoom
                                (GoogleMapHandler.getGoogleMap().getCameraPosition().target, GoogleMapHandler.getGoogleMap().getCameraPosition().zoom + 3)));
            }
        });

        final CustomImageButton ButtonDecreaseZoom;
        ButtonDecreaseZoom = new CustomImageButton(activity.findViewById(R.id.buttonDecreaseZoom));
        ButtonDecreaseZoom.setImages(activity, R.drawable.icon_minus, R.drawable.icon_minus_pressed);
        ButtonDecreaseZoom.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonDecreaseZoom.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleMapHandler.getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition
                        (CameraPosition.fromLatLngZoom
                                (GoogleMapHandler.getGoogleMap().getCameraPosition().target, GoogleMapHandler.getGoogleMap().getCameraPosition().zoom - 3)));
            }
        });

        final CustomImageButton ButtonClose;
        ButtonClose = new CustomImageButton(activity.findViewById(R.id.buttonClose));
        ButtonClose.setImages(activity, R.drawable.icon_close, R.drawable.icon_close_pressed);
        ButtonClose.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonClose.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.getOpenedViewGroup().handleClick(activity, ButtonClose.getView().getId());
                CustomViewGroup.open("MainViews", true, false);
            }
        });

        final CustomImageButton ButtonSearch;
        ButtonSearch = new CustomImageButton(activity.findViewById(R.id.buttonSearch));
        ButtonSearch.setImages(activity, R.drawable.icon_search, R.drawable.icon_search_pressed);
        ButtonSearch.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonSearch.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.hideSoftKeyboard(activity);
                if (!CustomViewGroup.getOpenedViewGroup().handleClick(activity, ButtonSearch.getView().getId())) {
                    CustomViewGroup.open("MainViews", true, false);
                };
            }
        });


        final CustomImageButton ButtonClearDepartureEditText;
        ButtonClearDepartureEditText = new CustomImageButton(activity.findViewById(R.id.clearEditText1));
        ButtonClearDepartureEditText.setImages(activity, R.drawable.icon_close, R.drawable.icon_close_pressed);
        ButtonClearDepartureEditText.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonClearDepartureEditText.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHandler.removeDeparture();
            }
        });

        final CustomImageButton ButtonNext;
        ButtonNext = new CustomImageButton(activity.findViewById(R.id.buttonNext));
        ButtonNext.setImages(activity, R.drawable.icon_next, R.drawable.icon_next_pressed);
        ButtonNext.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonNext.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.getOpenedViewGroup().handleClick(activity, ButtonNext.getView().getId());
            }
        });

        final CustomImageButton ButtonClearDestinationEditText;
        ButtonClearDestinationEditText = new CustomImageButton(activity.findViewById(R.id.clearEditText2));
        ButtonClearDestinationEditText.setImages(activity, R.drawable.icon_close, R.drawable.icon_close_pressed);
        ButtonClearDestinationEditText.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonClearDestinationEditText.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHandler.removeDestination();
            }
        });

        final CustomImageView CenterIcon;
        CenterIcon = new CustomImageView(activity.findViewById(R.id.centerIcon));
        CenterIcon.setImage(R.drawable.icon_point);
        CenterIcon.setAnimations(activity, R.anim.appear_from_void, R.anim.remove_to_void);

        final CustomRelativeLayout BackgroundBlackout;
        BackgroundBlackout = new CustomRelativeLayout(activity.findViewById(R.id.RelativeLayoutBlackout));
        BackgroundBlackout.setAnimations(activity, R.anim.appear_from_void, R.anim.remove_to_void);
        BackgroundBlackout.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.getOpenedViewGroup().handleClick(activity, BackgroundBlackout.getView().getId());
            }
        });

        final CustomImageButton ButtonCloseList;
        ButtonCloseList = new CustomImageButton(activity.findViewById(R.id.buttonCloseList));
        ButtonCloseList.setImages(activity, R.drawable.icon_close, R.drawable.icon_close_pressed);
        ButtonCloseList.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonCloseList.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.getOpenedViewGroup().handleClick(activity, ButtonCloseList.getView().getId());
            }
        });

        final CustomImageButton ButtonOpenRouteList;
        ButtonOpenRouteList = new CustomImageButton(activity.findViewById(R.id.buttonOpenRouteList));
        ButtonOpenRouteList.setImages(activity, R.drawable.icon_options, R.drawable.icon_options_pressed);
        ButtonOpenRouteList.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonOpenRouteList.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.open("RouteListViews", true, false);
            }
        });

        final CustomImageButton ButtonBack;
        ButtonBack = new CustomImageButton(activity.findViewById(R.id.buttonBack));
        ButtonBack.setImages(activity, R.drawable.icon_back, R.drawable.icon_back_pressed);
        ButtonBack.setAnimations(activity, R.anim.appear_rightward_button, R.anim.remove_leftward_button);
        ButtonBack.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.getOpenedViewGroup().handleClick(activity, ButtonBack.getView().getId());
            }
        });

        final CustomScrollView RouteListView;
        RouteListView = new CustomScrollView(activity.findViewById(R.id.ScrollViewRoute), activity.findViewById(R.id.LinearLayoutRoute));
        RouteListView.setAnimations(activity, R.anim.appear_rightward, R.anim.remove_leftward);

        final CustomScrollView OptionsListView;
        OptionsListView = new CustomScrollView(activity.findViewById(R.id.ScrollViewOptions), activity.findViewById(R.id.LinearLayoutOptions));
        OptionsListView.setAnimations(activity, R.anim.appear_rightward, R.anim.remove_leftward);

        final CustomScrollView EditorListView;
        EditorListView = new CustomScrollView(activity.findViewById(R.id.ScrollViewEditor), activity.findViewById(R.id.LinearLayoutEditor));
        EditorListView.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_rightward);

        final CustomScrollView SettingsListView;
        SettingsListView = new CustomScrollView(activity.findViewById(R.id.ScrollViewSettings), activity.findViewById(R.id.LinearLayoutSettings));
        SettingsListView.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_rightward);

        final CustomScrollView LiftOrRampListView;
        LiftOrRampListView = new CustomScrollView(activity.findViewById(R.id.ScrollViewLiftOrRamp), activity.findViewById(R.id.LinearLayoutLiftOrRamp));
        LiftOrRampListView.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_rightward);

        final CustomRelativeLayout NongroundPassagesLayout;
        NongroundPassagesLayout = new CustomRelativeLayout(activity.findViewById(R.id.NongroundPassagesRelativeLayout));
        NongroundPassagesLayout.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomLinearLayout RouteParamsLayout;
        RouteParamsLayout = new CustomLinearLayout(activity.findViewById(R.id.LinearLayoutParams));
        RouteParamsLayout.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomRelativeLayout BuildRouteLayout;
        BuildRouteLayout = new CustomRelativeLayout(activity.findViewById(R.id.buildRouteRelativeLayout));
        BuildRouteLayout.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomTextView settingsTitleTextView;
        settingsTitleTextView = new CustomTextView(activity.findViewById(R.id.settingsTitle));
        settingsTitleTextView.setAnimations(activity, R.anim.appear_rightward, R.anim.remove_rightward);

        final CustomTextView setAsDepartureTextView;
        setAsDepartureTextView = new CustomTextView(activity.findViewById(R.id.setAsDepartureTextView));
        setAsDepartureTextView.setAnimations(activity, R.anim.appear_rightward, R.anim.remove_rightward);

        final CustomTextView setAsDestinationTextView;
        setAsDestinationTextView = new CustomTextView(activity.findViewById(R.id.setAsDestinationTextView));
        setAsDestinationTextView.setAnimations(activity, R.anim.appear_rightward, R.anim.remove_rightward);

        final CustomEditText MessageEditText;
        MessageEditText = new CustomEditText(activity.findViewById(R.id.messageEditText));
        MessageEditText.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomImageButton ButtonSendMessage;
        ButtonSendMessage = new CustomImageButton(activity.findViewById(R.id.buttonSendMessage));
        ButtonSendMessage.setImages(activity, R.drawable.icon_send, R.drawable.icon_send_pressed);
        ButtonSendMessage.setAnimations(activity, R.anim.appear_leftward_button, R.anim.remove_rightward_button);
        ButtonSendMessage.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewGroup.getOpenedViewGroup().handleClick(activity, ButtonSendMessage.getView().getId());
            }
        });

        final CustomLinearLayout AboutLinearLayout;
        AboutLinearLayout = new CustomLinearLayout(activity.findViewById(R.id.aboutLinearLayout));
        AboutLinearLayout.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomLinearLayout EditorIntroLinearLayout;
        EditorIntroLinearLayout = new CustomLinearLayout(activity.findViewById(R.id.editorIntroLinearLayout));
        EditorIntroLinearLayout.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomRelativeLayout RateUsRelativeLayout;
        RateUsRelativeLayout = new CustomRelativeLayout(activity.findViewById(R.id.rateUsRelativeLayout));
        RateUsRelativeLayout.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);

        final CustomRelativeLayout IntroductionRelativeLayout;
        IntroductionRelativeLayout = new CustomRelativeLayout(activity.findViewById(R.id.introductionRelativeLayout));
        IntroductionRelativeLayout.setAnimations(activity, R.anim.appear_leftward, R.anim.remove_leftward);
    }
}
