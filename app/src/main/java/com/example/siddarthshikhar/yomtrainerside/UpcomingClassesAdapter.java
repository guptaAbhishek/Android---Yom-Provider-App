package com.example.siddarthshikhar.yomtrainerside;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Siddarth Shikhar on 7/10/2015.
 */
public class UpcomingClassesAdapter extends ArrayAdapter<YogaClass>{
    public static final int HEADER_VIEW = 0;
    public static final int ITEM_VIEW = 1;
    Context context ;
    LayoutInflater l ;
    List<YogaClass> list ;
    FragmentManager fman;
    int[] headerLocations;
    @Override
    public int getCount() {
        return list.size()+headerLocations.length;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        for(int i=0;i<headerLocations.length;i++)
            if (position==headerLocations[i])
                return HEADER_VIEW;
        return ITEM_VIEW;
    }

    public UpcomingClassesAdapter(Context context , int resource , List<YogaClass> objects , LayoutInflater l,FragmentManager fman,int headerLocations[]) {
        super(context, resource, objects);
        this.context = context ;
        this.list = objects ;
        this.headerLocations=headerLocations;
        this.l = l;
        this.fman=fman;
    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        int chck=0,i;
        for(i=0;i<headerLocations.length;i++) {
            if (position == headerLocations[i]) {
                chck = 1;
                break;
            }
            if(position<headerLocations[i])
                break;
        }
        if(chck==0){
            View v = convertView ;
            YogaClass cur=list.get(position-i);
            if(v==null)
                v = l.inflate(R.layout.specific_yoga_class,null);
            TextView venue = (TextView) v.findViewById(R.id.venue);
            TextView start = (TextView) v.findViewById(R.id.stime);
            TextView end = (TextView) v.findViewById(R.id.etime);
            TextView status=(TextView)v.findViewById(R.id.status);
            TextView name=(TextView)v.findViewById(R.id.class_client_name);
            ((TextView)v.findViewById(R.id.class_client_name)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
            ((TextView)v.findViewById(R.id.venue)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
            ((TextView)v.findViewById(R.id.stime)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
            ((TextView)v.findViewById(R.id.etime)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
            ((TextView)v.findViewById(R.id.status)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
            venue.setText("Venue: " + cur.venue);
            start.setText("Starts at " + cur.startTime);
            end.setText("Ends at " + cur.endTime);
            name.setText("Name: " + cur.name);
            status.setText("Upcoming Class");
            ((LinearLayout)v.findViewById(R.id.layout_to_be_changed_acc_to_attended)).setBackgroundColor(context.getResources().getColor(R.color.action_bar_color));
            return v;
        }
        else{
            View v = convertView;
            if(v==null)
                v = l.inflate(R.layout.main_user_panel_date_view,null);
            long x=System.currentTimeMillis()/1000;
            long y=x+i*24*60*60;
            Date reqdDate=new Date(y*1000);
            SimpleDateFormat format=new SimpleDateFormat("dd MMMM yyyy", Locale.US);
            TextView date=(TextView)v.findViewById(R.id.main_user_panel_date);
            date.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
            date.setText(format.format(reqdDate));

            TextView noOfClasses=(TextView)v.findViewById(R.id.main_user_panel_no_of_classes);
            noOfClasses.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
            int classes=0;
            if (i != 27) {
                classes=headerLocations[i+1]-headerLocations[i]-1;
            }else{
                classes=list.size()+headerLocations.length-headerLocations[i]-1;
            }
            if(classes==1)
                noOfClasses.setText("1 Class");
            else
                noOfClasses.setText(classes+" Classes");
            return v;
        }
    }
}
