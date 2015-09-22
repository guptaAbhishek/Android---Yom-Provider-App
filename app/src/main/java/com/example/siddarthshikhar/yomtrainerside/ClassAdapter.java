package com.example.siddarthshikhar.yomtrainerside;

/**
 * Created by Rakesh on 7/2/2015.
 */

        import android.app.FragmentManager;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Typeface;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.List;


public class ClassAdapter extends ArrayAdapter<YogaClass> {
    Context context ;
    LayoutInflater l ;
    List<YogaClass> list ;
    FragmentManager fman;
    public ClassAdapter(Context context , int resource , List<YogaClass> objects , LayoutInflater l,FragmentManager fman) {
        super(context, resource, objects);
        this.context = context ;
        this.list = objects ;
        this.l = l;
        this.fman=fman;
    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        String temp=list.get(position).date+" 23:59";
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateFromTemp=null;
        try {
            dateFromTemp=format.parse(temp);
        } catch (ParseException e) {
        }
        final YogaClass cur = list.get(position);
        if(System.currentTimeMillis()<dateFromTemp.getTime()){
            View v = convertView ;
            if(v==null)
                v = l.inflate(R.layout.specific_yoga_class,null);
            TextView venue = (TextView) v.findViewById(R.id.venue);
            TextView start = (TextView) v.findViewById(R.id.stime);
            TextView end = (TextView) v.findViewById(R.id.etime);
            TextView status=(TextView)v.findViewById(R.id.status);
            TextView name=(TextView)v.findViewById(R.id.class_client_name);
            ((TextView)v.findViewById(R.id.class_client_name)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.venue)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.stime)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.etime)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.status)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            venue.setText("Venue: " + cur.venue);
            start.setText("Starts at " + cur.startTime);
            end.setText("Ends at " + cur.endTime);
            name.setText("Name: " + cur.name);
            status.setText("UPCOMING CLASS");
            ((LinearLayout)v.findViewById(R.id.layout_to_be_changed_acc_to_attended)).setBackgroundColor(context.getResources().getColor(R.color.action_bar_color));
            return v;
        }
        else if(cur.ifTaken!=0){
            View v =l.inflate(R.layout.specific_yoga_class,null);
            TextView venue = (TextView) v.findViewById(R.id.venue);
            TextView start = (TextView) v.findViewById(R.id.stime);
            TextView end = (TextView) v.findViewById(R.id.etime);
            TextView status=(TextView)v.findViewById(R.id.status);
            TextView name=(TextView)v.findViewById(R.id.class_client_name);
            ((TextView)v.findViewById(R.id.class_client_name)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.venue)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.stime)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.etime)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.status)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            LinearLayout toBeChanged=(LinearLayout)v.findViewById(R.id.layout_to_be_changed_acc_to_attended);
            venue.setText("Venue: "+cur.venue);
            start.setText("Started at "+cur.startTime);
            end.setText("Ended at "+cur.endTime);
            name.setText("Name: " + cur.name);
            ImageView cancel=(ImageView)v.findViewById(R.id.class_status);
            if(cur.ifTaken==1){
                status.setText("CLASS ATTENDED");
                toBeChanged.setBackgroundColor(context.getResources().getColor(R.color.Green));
                cancel.setBackground(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));
            }else{
                status.setText("CLASS MISSED");
                toBeChanged.setBackgroundColor(context.getResources().getColor(R.color.Red));
                cancel.setBackground(context.getResources().getDrawable(R.drawable.ic_clear_white_24dp));
            }
            return v;
        }else if(list.size()==1 && cur.startTime.equals("00:00") && cur.endTime.equals("00:00")){
            View v= l.inflate(R.layout.class_shifted_to_unscheduled_date,null);
            Button yes,no;
            ((TextView)v.findViewById(R.id.did_you_attend_label)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((Button)v.findViewById(R.id.yes_unscheduled_class_attended)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((Button)v.findViewById(R.id.no_unscheduled_class_not_attended)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            yes=(Button)v.findViewById(R.id.yes_unscheduled_class_attended);
            no=(Button)v.findViewById(R.id.no_unscheduled_class_not_attended);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddClass.class);
                    context.startActivity(intent);
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,Schedule.class));
                }
            });
            return v;
        } else{
            View v =l.inflate(R.layout.specific_unresponded_yoga_class,null);
            TextView venue = (TextView) v.findViewById(R.id.venue);
            TextView start = (TextView) v.findViewById(R.id.stime);
            TextView end = (TextView) v.findViewById(R.id.etime);
            TextView name=(TextView)v.findViewById(R.id.class_client_name);
            ((TextView)v.findViewById(R.id.class_client_name)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.status)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.stime)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.etime)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.venue)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((TextView)v.findViewById(R.id.did_you_attend_label)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((Button)v.findViewById(R.id.yes_unscheduled_class_attended)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            ((Button)v.findViewById(R.id.no_unscheduled_class_not_attended)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Los Angeleno Sans.ttf"));
            venue.setText("Venue: " + cur.venue);
            start.setText("Started at " + cur.startTime);
            end.setText("Ended at " + cur.endTime);
            name.setText("Name: " + cur.name);
            Button yes,no;
            yes=(Button)v.findViewById(R.id.yes_unscheduled_class_attended);
            no=(Button)v.findViewById(R.id.no_unscheduled_class_not_attended);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteHelper helper = new SQLiteHelper(context);
                    SQLiteDatabase db = helper.getReadableDatabase();
                    db.delete(SQLiteHelper.CLASS_TABLE,"date = '"+cur.date+"' and starttime = '"+cur.startTime+"'", null);
                    helper=new SQLiteHelper(context);
                    db=helper.getWritableDatabase();
                    ContentValues cv=new ContentValues();
                    cv.put(SQLiteHelper.COLUMN_DATE,cur.date);
                    cv.put(SQLiteHelper.COLUMN_STARTTIME, cur.startTime);
                    cv.put(SQLiteHelper.COLUMN_ENDTIME,cur.endTime);
                    cv.put(SQLiteHelper.COLUMN_VENUE,cur.venue);
                    cv.put(SQLiteHelper.COLUMN_NAME,cur.name);
                    cv.put(SQLiteHelper.COLUMN_IFTAKEN, 1);
                    db.insert(SQLiteHelper.CLASS_TABLE, null, cv);
                    Intent intent = new Intent(context, Schedule.class);
                    context.startActivity(intent);
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteHelper helper = new SQLiteHelper(context);
                    SQLiteDatabase db = helper.getReadableDatabase();
                    db.delete(SQLiteHelper.CLASS_TABLE,"date = '"+cur.date+"' and starttime = '"+cur.startTime+"'", null);
                    helper=new SQLiteHelper(context);
                    db=helper.getWritableDatabase();
                    ContentValues cv=new ContentValues();
                    cv.put(SQLiteHelper.COLUMN_DATE,cur.date);
                    cv.put(SQLiteHelper.COLUMN_STARTTIME, cur.startTime);
                    cv.put(SQLiteHelper.COLUMN_ENDTIME,cur.endTime);
                    cv.put(SQLiteHelper.COLUMN_VENUE,cur.venue);
                    cv.put(SQLiteHelper.COLUMN_IFTAKEN, -1);
                    cv.put(SQLiteHelper.COLUMN_NAME,cur.name);
                    db.insert(SQLiteHelper.CLASS_TABLE, null, cv);
                    Intent intent = new Intent(context, Schedule.class);
                    context.startActivity(intent);
                }
            });
            return v;
        }
    }
}
