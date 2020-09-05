package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.EditText;

import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewsSendMessage extends CustomViewGroup {

    CustomEditText MessageEditText;
    CustomImageButton SendMessageButton;
    CustomRelativeLayout BackgroundBlackout;

    ViewsSendMessage(Activity activity) {
        super("SendMessageViews");

        BackgroundBlackout = (CustomRelativeLayout) CustomView.getViewById(R.id.RelativeLayoutBlackout);
        MessageEditText = (CustomEditText) CustomView.getViewById(R.id.messageEditText);
        SendMessageButton = (CustomImageButton) CustomView.getViewById(R.id.buttonSendMessage);

        super.addView(BackgroundBlackout);
        super.addView(MessageEditText);
        super.addView(SendMessageButton);
    }

    public void handleTouch(int Id) {
    }

    public boolean handleClick(Activity activity, int Id) {
        if (Id == SendMessageButton.getView().getId()) {
            String message = "";

            for (int i = 0; i < MessageEditText.getView().getText().toString().length(); i++) {
                if (MessageEditText.getView().getText().toString().charAt(i) != ';'
                        && MessageEditText.getView().getText().toString().charAt(i) != '\'') {
                    message += MessageEditText.getView().getText().toString().charAt(i);
                }
            }

            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://accesspassed.com:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServerApi serverApi = retrofit.create(ServerApi.class);

                Call<ServerSimpleResponse> call = serverApi.postMessage("postMessage"
                        , SharedPreferencesHandler.mSettings.getInt(SharedPreferencesHandler.A_P_USERID, 0)
                        , URLEncoder.encode(message, "UTF-8"));

                call.enqueue(new Callback<ServerSimpleResponse>() {
                    @Override
                    public void onResponse(Call<ServerSimpleResponse> call, Response<ServerSimpleResponse> response) {
                        TipLayout.openForTime(StringHandler.getString(R.string.thank_you_for_your_message));
                    }

                    @Override
                    public void onFailure(Call<ServerSimpleResponse> call, Throwable t) {
                        ErrorLayout.show(StringHandler.getString(R.string.sorry_something_went_wrong_with_yourway_server));
                    }
                });
            } catch (Exception e) {
                ErrorLayout.show(StringHandler.getString(R.string.sorry_something_went_wrong_with_yourway_server));
            }

            CustomViewGroup.back();

            System.hideSoftKeyboard(activity);

            return false;
        }

        return false;
    }

    public void specialOpen() {

    }

    public void handleCameraMove() {

    };

    public void specialClose() {

    };

    public void handleConfigurationChange(Context context, Configuration newConfig) {

    };
}
