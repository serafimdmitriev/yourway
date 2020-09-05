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

/**
 * Created by Serafim on 03.06.2017.
 */

public class ViewsDestination extends CustomViewGroup {
    CustomImageButton ButtonClose;
    CustomImageButton ButtonSearch;
    CustomEditText DepartureEditText;
    CustomEditText DestinationEditText;
    //CustomListView DestinationListView;
    CustomScrollView DestinationScrollView;
    CustomImageButton ButtonClearDestinationEditText;
    CustomRelativeLayout BackgroundBlackout;


    public ViewsDestination(final Activity activity) {
        super("DestinationViews");
        LocationHandler.setDestinationRelatedViews("DestinationViews");

        ButtonClose = (CustomImageButton) CustomView.getViewById(R.id.buttonClose);
        ButtonSearch = (CustomImageButton) CustomView.getViewById(R.id.buttonSearch);
        DepartureEditText = (CustomEditText) CustomView.getViewById(R.id.editTextDeparture);
        DestinationEditText = (CustomEditText) CustomView.getViewById(R.id.editTextDestination);
        //DestinationListView = (CustomListView) CustomView.getViewById(R.id.listView2);
        DestinationScrollView = (CustomScrollView) CustomView.getViewById(R.id.ScrollViewDestination);
        ButtonClearDestinationEditText = (CustomImageButton) CustomView.getViewById(R.id.clearEditText2);
        BackgroundBlackout = (CustomRelativeLayout) CustomView.getViewById(R.id.RelativeLayoutBlackout);

        final CustomScrollViewDefaultItem SetLocationItem = new CustomScrollViewDefaultItem(activity, StringHandler.getString(R.string.set_location_on_the_map), R.drawable.icon_marker);
        SetLocationItem.setTextTypeface(Typeface.BOLD);
        SetLocationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHandler.removeDestination();
                System.hideSoftKeyboard(activity);
                CustomViewGroup.open("DestinationChooseViews", true, false);
            }
        });

        DestinationScrollView.setReloader(new CustomScrollViewReloader() {
            @Override
            void reload(final boolean useAnimation) {

                if (DestinationEditText.getView().getText().length() == 0) {
                    DestinationScrollView.clear();
                    DestinationScrollView.addItem(SetLocationItem.getView());

                    for (final CustomPlace historyPlace : HistoryHandler.getHistory(activity)) {
                        CustomScrollViewDefaultItem historyItem = new CustomScrollViewDefaultItem(activity, historyPlace.title, R.drawable.icon_clock);
                        historyItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.hideSoftKeyboard(activity);
                                LocationHandler.handleCustomPlaceAndShowDestination(activity, historyPlace);

                            }
                        });
                        DestinationScrollView.addItem(historyItem.getView());
                    }

                    if (useAnimation) DestinationScrollView.appearFromVoidAnimation();
                } else {
                    final PendingResult<AutocompletePredictionBuffer> result =
                            Places.GeoDataApi.getAutocompletePredictions(((MainActivity) activity).getGoogleApiClient(), DestinationEditText.getView().getText().toString(),
                                    GoogleMapHandler.getGoogleMap().getProjection().getVisibleRegion().latLngBounds,
                                    new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build());

                    result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                        @Override
                        public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                            DestinationScrollView.clear();
                            DestinationScrollView.addItem((new PoweredByGoogleItem(activity)).getView());

                            for (final AutocompletePrediction autocompletePrediction : autocompletePredictions) {
                                final String Id = autocompletePrediction.getPlaceId();
                                final String Title = autocompletePrediction.getFullText(null).toString();

                                CustomScrollViewDefaultItem autocompleteItem = new CustomScrollViewDefaultItem(activity, Title, R.drawable.icon_geo);
                                autocompleteItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        System.hideSoftKeyboard(activity);
                                        LocationHandler.handlePlaceAndShowDestination(Id, Title, activity);
                                    }
                                });
                                DestinationScrollView.addItem(autocompleteItem.getView());
                            }

                            autocompletePredictions.release();

                            if (useAnimation) DestinationScrollView.appearFromVoidAnimation();
                        }
                    });
                }
            }
        });

        DestinationEditText.getView().addTextChangedListener(new TextWatcher() {
            int LastLength;
            int NewLength;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LastLength = DestinationEditText.getView().getText().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                NewLength = DestinationEditText.getView().getText().length();
                if (LastLength == NewLength + 1 || LastLength == NewLength - 1 || NewLength == 0) {
                    DestinationScrollView.reload(false);
                }
            }
        });

        super.addView(ButtonClose);
        super.addView(ButtonSearch);
        super.addView(DepartureEditText);
        super.addView(DestinationEditText);
        super.addView(DestinationScrollView);
        super.addView(ButtonClearDestinationEditText);
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
            LocationHandler.handleAddressAndShowDestination(activity);
            return true;
        }
        return false;
    }

    public void specialOpen() {
        DestinationScrollView.reload(false);
    }

    public void handleCameraMove() {

    };

    public void specialClose() {

    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
