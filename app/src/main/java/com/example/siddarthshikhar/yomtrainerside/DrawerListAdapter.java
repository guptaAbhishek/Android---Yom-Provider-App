package com.example.siddarthshikhar.yomtrainerside;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawerListAdapter extends ArrayAdapter<String> {
    Context context;
    LayoutInflater l;
    String[] list;

    public DrawerListAdapter(Context context, int resource, String[] objects,LayoutInflater l) {
        super(context, resource,objects);
        this.context=context;
        this.list=objects;
        this.l=l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null)
            v=l.inflate(R.layout.drawer_individual_item,null);
        TextView drawIndItem=(TextView)v.findViewById(R.id.drawer_item_text);
        drawIndItem.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf"));
        drawIndItem.setText(list[position]);
        ImageView temp=(ImageView)v.findViewById(R.id.drawer_item_icon);
        if(position==0)
            temp.setImageResource(R.drawable.ic_home_black_24dp);
        if(position==1)
            temp.setImageResource(R.drawable.ic_event_available_black_24dp);
        if(position==2)
            temp.setImageResource(R.drawable.ic_account_circle_black_24dp);
        if(position==3)
            temp.setImageResource(R.drawable.ic_share_black_24dp);
        if (position==4)
            temp.setImageResource(R.drawable.ic_settings_black_24dp);
        if (position==5)
            temp.setImageResource(R.drawable.ic_contacts_black_24dp);
        return v;
    }
}
