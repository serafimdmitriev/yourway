package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by Serafim on 03.06.2017.
 */

public class ViewsDeparture extends CustomViewGroup {
    CustomImageButton ButtonClose;
    CustomImageButton ButtonSearch;
    CustomScrollView DepartureScrollView;
    CustomEditText DepartureEditText;
    CustomImageButton ButtonClearDepartureEditText;
    CustomRelativeLayout BackgroundBlackout;

    public ViewsDeparture(final Activity activity) {
        super("DepartureViews");
        LocationHandler.setDepartureRelatedViews("DepartureViews");

        ButtonClose = (CustomImageButton) CustomView.getViewById(R.id.buttonClose);
        ButtonSearch = (CustomImageButton) CustomView.getViewById(R.id.buttonSearch);
        DepartureScrollView = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewDeparture);
        DepartureEditText = (CustomEditText) CustomView.getViewById(R.id.editTextDeparture);
        ButtonClearDepartureEditText = (CustomImageButton) CustomView.getViewById(R.id.clearEditText1);
        BackgroundBlackout = (CustomRelativeLayout) CustomView.getViewById(R.id.RelativeLayoutBlackout);

        final CustomScrollViewDefaultItem GPSItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.use_your_gps_location), R.drawable.icon_geo);
        GPSItem.setTextTypeface(Typeface.BOLD);
        GPSItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHandler.handleGPSAndShowDeparture(activity);
            }
        });

        final CustomScrollViewDefaultItem SetLocationItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.set_location_on_the_map), R.drawable.icon_marker);
        SetLocationItem.setTextTypeface(Typeface.BOLD);
        SetLocationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHandler.removeDeparture();
                System.hideSoftKeyboard(activity);
                CustomViewGroup.open("DepartureChooseViews", true, false);
            }
        });

        DepartureScrollView.setReloader(new CustomScrollViewReloader() {
            @Override
            void reload(final boolean useAnimation) {

                if (DepartureEditText.getView().getText().length() == 0) {
                    DepartureScrollView.clear();

                    DepartureScrollView.addItem(GPSItem.getView());

                    DepartureScrollView.addItem(SetLocationItem.getView());

                    for (final CustomPlace historyPlace : HistoryHandler.getHistory(activity)) {
                        CustomScrollViewDefaultItem historyItem = new CustomScrollViewDefaultItem(activity, historyPlace.title, R.drawable.icon_clock);
                        historyItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.hideSoftKeyboard(activity);
                                LocationHandler.handleCustomPlaceAndShowDeparture(activity, historyPlace);
                            }
                        });
                        DepartureScrollView.addItem(historyItem.getView());
                    }

                    if (useAnimation) DepartureScrollView.appearFromVoidAnimation();
                } else {
                    final PendingResult<AutocompletePredictionBuffer> result =
                            Places.GeoDataApi.getAutocompletePredictions(((MainActivity)activity).getGoogleApiClient(), DepartureEditText.getView().getText().toString(),
                                    GoogleMapHandler.getGoogleMap().getProjection().getVisibleRegion().latLngBounds,
                                    new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build());

                    result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                        @Override
                        public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                            DepartureScrollView.clear();
                            DepartureScrollView.addItem((new PoweredByGoogleItem(activity)).getView());

                            for (final AutocompletePrediction autocompletePrediction : autocompletePredictions)  {
                                final String Id = autocompletePrediction.getPlaceId();
                                final String Title = autocompletePrediction.getFullText(null).toString();

                                CustomScrollViewDefaultItem autocompleteItem = new CustomScrollViewDefaultItem(activity, Title, R.drawable.icon_geo);
                                autocompleteItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        System.hideSoftKeyboard(activity);
                                        LocationHandler.handlePlaceAndShowDeparture(Id, Title, activity);
                                    }
                                });
                                DepartureScrollView.addItem(autocompleteItem.getView());
                            }

                            autocompletePredictions.release();

                            if (useAnimation) DepartureScrollView.appearFromVoidAnimation();
                        }
                    });
                }
            }
        });



        DepartureEditText.getView().addTextChangedListener(new TextWatcher() {
            int LastLength;
            int NewLength;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LastLength = DepartureEditText.getView().getText().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                NewLength = DepartureEditText.getView().getText().length();
                if (LastLength == NewLength + 1 || LastLength == NewLength - 1 || NewLength == 0) {
                    DepartureScrollView.reload(false);
                }
            }
        });

        super.addView(ButtonClose);
        super.addView(ButtonSearch);
        super.addView(DepartureScrollView);
        super.addView(DepartureEditText);
        super.addView(ButtonClearDepartureEditText);
        super.addView(BackgroundBlackout);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == ButtonClose.getView().getId() || Id == BackgroundBlackout.getView().getId()) {
            System.hideSoftKeyboard(activity);
            return true;
        }

        if (Id == ButtonSearch.getView().getId()) {
            LocationHandler.handleAddressAndShowDeparture(activity);
            return true;
        }
        return false;
    }

    public void specialOpen() {
        DepartureScrollView.reload(false);
    }

    public void handleCameraMove() {

    };

    public void specialClose() {

    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
