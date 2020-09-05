package com.serafimdmitrievstudio.yourway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Serafim on 27.08.2017.
 */

public class SaveOrNextStepItem {
    private View Item;

    private Button AddOneMoreButton;
    private Button NextStepButton;

    SaveOrNextStepItem(final Context context) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Item = inflater.inflate(R.layout.save_nextstep, null);


        AddOneMoreButton = (Button) Item.findViewById(R.id.buttonAddOneMore);
        NextStepButton = (Button) Item.findViewById(R.id.buttonNextStep);
    }

    public Button getAddOneMoreButton() {
        return AddOneMoreButton;
    }

    public Button getNextStepButton() {
        return NextStepButton;
    }

    public View getView() {
        return Item;
    }
}
