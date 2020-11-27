package com.hgosot.skopje;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private Activity activity;
    public  int selectedPosition = -1;

    public ListViewAdapter(Activity context, int resource, List<String> objects) {
        super(context, resource, objects);



        this.activity = context;
        items = objects;
        selectedPosition = getSavedLngPostion();
    }


    public  int getSelectedPosition(){

        return selectedPosition;
    }
    private int getSavedLngPostion() {

        String lng = MainApplication.localeManager.getLanguage();

        int index = -1;
        for(int i = 0; i< items.size();i++){

            if (items.get(i).equalsIgnoreCase(lng)){

                index = i;
                break;
            }

        }
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.item_lng_list, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setTag(position); // This line is important.

        holder.lngName.setText(getItem(position).toUpperCase());
        if (position == selectedPosition) {
            holder.checkBox.setChecked(true);
        } else holder.checkBox.setChecked(false);

        holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

        return convertView;
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {
        private TextView lngName;
        private CheckBox checkBox;

        public ViewHolder(View v) {
            checkBox = (CheckBox) v.findViewById(R.id.check);
            lngName = (TextView) v.findViewById(R.id.name);
        }
    }
}