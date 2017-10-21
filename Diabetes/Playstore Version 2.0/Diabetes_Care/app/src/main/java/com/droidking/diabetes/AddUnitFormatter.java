package com.droidking.diabetes;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

import static com.droidking.diabetes.MainActivity.base_unit_id;

/**
 * This ValueFormatter is just for convenience and simply puts a "%" sign after
 * each value. (Recommeded for PieChart)
 *
 * @author Philipp Jahoda
 */

/**
 * Created by nazmul on 7/30/16.
 */

public class AddUnitFormatter implements ValueFormatter, YAxisValueFormatter {

    protected DecimalFormat mFormat;

    public AddUnitFormatter() {
        mFormat = new DecimalFormat("####");
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    public AddUnitFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    // ValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        String unit;
        if(base_unit_id == 1) {
            unit = ""; //" mg/d";
        }
        else
        {
            unit = "";//"mmol/dL" ;    /*** Unit*/
        }
        return mFormat.format(value) + unit;
    }

    // YAxisValueFormatter
    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        String unit;
        if(base_unit_id == 1) {
            unit = "";//" mg/dL";
        }
        else
        {
            unit = "";//"mmol/dL" ;    /*** Unit*/
        }
        return mFormat.format(value) + unit;
    }
}
