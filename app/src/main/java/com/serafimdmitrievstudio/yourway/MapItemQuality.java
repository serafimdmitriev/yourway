package com.serafimdmitrievstudio.yourway;

/**
 * Created by Serafim on 06.06.2017.
 */

public class MapItemQuality {
    boolean accessibleForWheelchair;
    boolean accessibleForElectricWheelchair;
    short grade;
    short generalState;

    static final short NoGrade = 0;
    static final short GradeFromFirstPointToSecond = 1;
    static final short GradeFromSecondPointToFirst = 2;

    static final short Tolerantly = 0;
    static final short Normal = 1;
    static final short Excellent = 2;

    MapItemQuality(boolean accessibleForWheelchair,
            boolean accessibleForElectricWheelchair,
            short grade,
            short generalState) {
        this.accessibleForWheelchair = accessibleForWheelchair;
        this.accessibleForElectricWheelchair = accessibleForElectricWheelchair;
        this.grade = grade;
        this.generalState = generalState;
    }

    boolean isAccessibleForWheelchair() {
        return  accessibleForWheelchair;
    }

    boolean isAccessibleForElectricWheelchair() {
        return  accessibleForElectricWheelchair;
    }

    Short getGrade() {
        return grade;
    }

    short getGeneralState() {
        return generalState;
    }

    void setGrade(short grade){
        this.grade = grade;
    }

    MapItemQuality getItemWithReversedGrade() {
        if (grade == GradeFromFirstPointToSecond) {
            return new MapItemQuality(accessibleForWheelchair,
                    accessibleForElectricWheelchair,
                    GradeFromSecondPointToFirst,
                    generalState);
        }
        if (grade == GradeFromSecondPointToFirst) {
            return new MapItemQuality(accessibleForWheelchair,
                    accessibleForElectricWheelchair,
                    GradeFromFirstPointToSecond,
                    generalState);
        }
        return new MapItemQuality(accessibleForWheelchair,
                accessibleForElectricWheelchair,
                NoGrade,
                generalState);
    }


    /*
    boolean AFW_I;
    boolean AFW_WH;
    boolean AFEW_I;
    boolean AFEW_WH;

    static final int Independent = 1;
    static final int WithHelp = 2;

    static final int NonAccessible = 0;
    static int Accessible(int state) {
        if (state == Independent) {
            return Independent;
        }
        if (state == WithHelp) {
            return WithHelp;
        }
        return NonAccessible;
    }

    MapItemQuality (int AccessibleForWheelchair, int AccessibleForElectricWheelchair) {
        switch (AccessibleForWheelchair) {
            case Independent: {
                AFW_I = true;
                AFW_WH = false;
            } break;
            case WithHelp: {
                AFW_I = false;
                AFW_WH = true;
            } break;
            default:{
                AFW_I = false;
                AFW_WH = false;
            } break;
        }
        switch (AccessibleForElectricWheelchair) {
            case Independent: {
                AFEW_I = true;
                AFEW_WH = false;
            } break;
            case WithHelp: {
                AFEW_I = false;
                AFEW_WH = true;
            } break;
            default:{
                AFEW_I = false;
                AFEW_WH = false;
            } break;
        }
    }

    int getAccessibleForWheelchair() {
        if (AFW_I) {
            return 1;
        }
        if (AFW_WH) {
            return 2;
        }
        return 0;
    }


    int getAccessibleForElectricWheelchair() {
        if (AFEW_I) {
            return 1;
        }
        if (AFEW_WH) {
            return 2;
        }
        return 0;
    }


    boolean isAccessibleForWheelchairIndependently() {
        return  AFW_I;
    }

    boolean isAccessibleForWheelchairWithHelp() {
        return  AFW_WH;
    }

    boolean isAccessibleForElectricWheelchairIndependently() {
        return  AFEW_I;
    }

    boolean isAccessibleForElectricWheelchairWithHelp() {
        return  AFEW_WH;
    }

    boolean equals(MapItemQuality quality) {
        return getAccessibleForWheelchair() == quality.getAccessibleForWheelchair() &&
                getAccessibleForElectricWheelchair() == quality.getAccessibleForElectricWheelchair();
    }
    */

}
