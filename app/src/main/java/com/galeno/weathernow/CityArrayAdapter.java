package com.galeno.weathernow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CityArrayAdapter extends ArrayAdapter<City> {
    public CityArrayAdapter (Context context, List<City> listcity){
        super (context, -1, listcity);
    }

    public class ViewHolder{
        public TextView cityTextView;
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        City caraDaVez = getItem(position);
        ViewHolder vh = null;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item1,parent, false);
            vh = new ViewHolder();
            vh.cityTextView = convertView.findViewById(R.id.cityTextView);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        vh.cityTextView.setText(getContext().getString(R.string.city,caraDaVez.city));
        return  convertView;
    }
}
