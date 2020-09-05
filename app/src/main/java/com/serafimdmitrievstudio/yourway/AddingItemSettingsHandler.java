package com.serafimdmitrievstudio.yourway;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Serafim on 18.02.2018.
 */

public class AddingItemSettingsHandler {

    private Switch wheelchairAccessible;
    private Switch electricWheelchairAccessible;

    private RadioGroup grade;
    private RadioGroup generalState;

    private TextView gradeTextView;

    private Button save;

    private TextView streetNameTextView;
    private EditText streetNameEditText;

    static final int AddRoadMode = 0;
    static final int AddPassageMode = 2;
    static final int AddNongroundPassageMode = 1;

    void initialize(final Activity activity) {
        if (wheelchairAccessible == null) {
            wheelchairAccessible = (Switch) activity.findViewById(R.id.SwitchAFW);
        }
        if (electricWheelchairAccessible == null) {
            electricWheelchairAccessible = (Switch) activity.findViewById(R.id.SwitchAFEW);
        }
        if (grade == null) {
            grade = (RadioGroup) activity.findViewById(R.id.gradeRadioGroup);
        }
        if (generalState == null) {
            generalState = (RadioGroup) activity.findViewById(R.id.generalStateRadioGroup);
        }
        if (save == null) {
            save = (Button) activity.findViewById(R.id.buttonSettingsSave);
        }
        if (gradeTextView == null) {
            gradeTextView = (TextView) activity.findViewById(R.id.gradeTextView);
        }
        if (streetNameTextView == null) {
            streetNameTextView = (TextView) activity.findViewById(R.id.streetNameTextView);
        }
        if (streetNameEditText == null) {
            streetNameEditText = (EditText) activity.findViewById(R.id.streetNameEditText);
            streetNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        System.hideSoftKeyboard(activity);
                    }
                }
            });
        }
    }

    String getStreetName() {
        if (streetNameEditText != null) {
            return streetNameEditText.getText().toString();
        }
        return "";
    }

    Button getSave() {
        return save;
    }

    void setMode(int Mode) {

        switch (Mode) {
            case AddRoadMode: {
                if (gradeTextView != null && grade != null) {
                    gradeTextView.setVisibility(View.VISIBLE);
                    grade.setVisibility(View.VISIBLE);
                }
                if (streetNameTextView != null && streetNameEditText != null) {
                    streetNameTextView.setVisibility(View.VISIBLE);
                    streetNameEditText.setVisibility(View.VISIBLE);
                    streetNameTextView.setText(R.string.name_of_this_street_on_local_language);
                    streetNameEditText.setText("");
                }
            } break;
            case AddPassageMode: {
                if (gradeTextView != null && grade != null) {
                    gradeTextView.setVisibility(View.VISIBLE);
                    grade.setVisibility(View.VISIBLE);
                }
                if (streetNameTextView != null && streetNameEditText != null) {
                    streetNameTextView.setVisibility(View.VISIBLE);
                    streetNameEditText.setVisibility(View.VISIBLE);
                    streetNameTextView.setText(R.string.this_is_passage_through);
                    streetNameEditText.setText("");
                }
            } break;
            case AddNongroundPassageMode: {
                if (gradeTextView != null && grade != null) {
                    gradeTextView.setVisibility(View.GONE);
                    grade.setVisibility(View.GONE);
                }
                if (streetNameTextView != null && streetNameEditText != null) {
                    streetNameTextView.setVisibility(View.GONE);
                    streetNameEditText.setVisibility(View.GONE);
                }
            } break;
        }
    }

    void reset() {
        wheelchairAccessible.setChecked(false);
        electricWheelchairAccessible.setChecked(false);

        RadioButton rb;
        rb = (RadioButton) grade.getChildAt(1);
        rb.setChecked(false);
        rb = (RadioButton) grade.getChildAt(2);
        rb.setChecked(false);
        rb = (RadioButton) grade.getChildAt(0);
        rb.setChecked(true);

        rb = (RadioButton) generalState.getChildAt(0);
        rb.setChecked(false);
        rb = (RadioButton) generalState.getChildAt(2);
        rb.setChecked(false);
        rb = (RadioButton) generalState.getChildAt(1);
        rb.setChecked(true);
    }

    boolean getWheelchairAccessible(Activity activity) {
        if (wheelchairAccessible == null) {
            wheelchairAccessible = (Switch) activity.findViewById(R.id.SwitchAFW);
        }

        return wheelchairAccessible.isChecked();
    }

    boolean getElectricWheelchairAccessible(Activity activity) {
        if (electricWheelchairAccessible == null) {
            electricWheelchairAccessible = (Switch) activity.findViewById(R.id.SwitchAFEW);
        }

        return electricWheelchairAccessible.isChecked();
    }

    short getGrade(Activity activity) {
        if (grade == null) {
            grade = (RadioGroup) activity.findViewById(R.id.gradeRadioGroup);
        }

        int radioButtonID = grade.getCheckedRadioButtonId();
        View radioButton = grade.findViewById(radioButtonID);
        return (short) grade.indexOfChild(radioButton);
    }

    short getGeneralState(Activity activity) {
        if (generalState == null) {
            generalState = (RadioGroup) activity.findViewById(R.id.generalStateRadioGroup);
        }

        int radioButtonID = generalState.getCheckedRadioButtonId();
        View radioButton = generalState.findViewById(radioButtonID);
        return (short) generalState.indexOfChild(radioButton);
    }



}
