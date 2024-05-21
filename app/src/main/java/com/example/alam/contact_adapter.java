package com.example.alam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class contact_adapter extends ArrayAdapter<String> {
    private Activity context;
    private List<String> name;
    private List<String> ph;
    public contact_adapter(Activity context,List<String> names, List<String>ph) {
        super(context, R.layout.contact_ui, ph);
        this.context=context;
        this.name=names;
        this.ph=ph;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(R.layout.contact_ui, null,true);
        TextView Textname = (TextView) rowView.findViewById(R.id.name);
        TextView Textph =(TextView)  rowView.findViewById(R.id.ph);
        Textname.setText(name.get(position));
        Textph.setText(ph.get(position));
        return rowView;
    }
}
