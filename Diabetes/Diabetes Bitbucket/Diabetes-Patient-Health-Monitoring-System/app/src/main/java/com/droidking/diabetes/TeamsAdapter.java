package com.droidking.diabetes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kstanoev on 1/14/2015.
 */
public class TeamsAdapter extends ArrayAdapter<Team> {

    Context context;
    private ArrayList<Team> teams;

    public TeamsAdapter(Context context, int textViewResourceId, ArrayList<Team> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.teams = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.team, null);
        }
        Team o = teams.get(position);
        if (o != null) {
            TextView time = (TextView) v.findViewById(R.id.tvTime);
            TextView beat = (TextView) v.findViewById(R.id.tvBEat);
            TextView aeat = (TextView) v.findViewById(R.id.tvAEat);
            TextView bpsys = (TextView) v.findViewById(R.id.tvBPsys);
            TextView bpdys = (TextView) v.findViewById(R.id.tvBPdys);


            time.setText(String.valueOf(o.getTime()));
            beat.setText(String.valueOf(o.getBEat()));
            aeat.setText(String.valueOf(o.getAEat()));
            bpsys.setText(String.valueOf(o.getBPsys()));
            bpdys.setText(String.valueOf(o.getBPdys()));
        }
        return v;
    }
}
