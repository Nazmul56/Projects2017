package com.droidking.diabetes;

import android.content.Context;

public class GlucoseRanges {

  //  private DatabaseHandler dB;
    private Context mContext;
    private String preferredRange;
    private int customMin;
    private int customMax;


    public String colorFromRange(int reading) {
        // Check for Hypo/Hyperglycemia
        if (reading < 70) {
            return "purple";
        } else if (reading > 200){
            return "red";
        } else if (reading > 70 | reading < 200 ) {
            // if not check with custom ranges
            switch (preferredRange) {
                case "ADA":
                    if (reading >= 70 & reading <= 180) {
                        return "green";
                    } else if (reading < 70) {
                        return "blue";
                    } else if (reading > 180){
                        return "orange";
                    }
                case "AACE":
                    if (reading >= 110 & reading <= 140) {
                        return "green";
                    } else if (reading < 110) {
                        return "blue";
                    } else if (reading > 140){
                        return "orange";
                    }
                case "UK NICE":
                    if (reading >= 72 & reading <= 153) {
                        return "green";
                    } else if (reading < 72) {
                        return "blue";
                    } else if (reading > 153){
                        return "orange";
                    }
                default:
                    if (reading >= customMin & reading <= customMax) {
                        return "green";
                    } else if (reading < customMin) {
                        return "blue";
                    } else if (reading > customMax){
                        return "orange";
                    }
            }
        }
    return "red";}

    public int stringToColor (String color){
        switch (color) {
            case "green":
                return mContext.getResources().getColor(R.color.glucosio_reading_ok);
            case "red":
                return mContext.getResources().getColor(R.color.glucosio_reading_hyper);
            case "blue":
                return mContext.getResources().getColor(R.color.glucosio_reading_low);
            case "orange":
                return mContext.getResources().getColor(R.color.glucosio_reading_high);
            default:
                return mContext.getResources().getColor(R.color.glucosio_reading_hypo);
        }
    };
}
