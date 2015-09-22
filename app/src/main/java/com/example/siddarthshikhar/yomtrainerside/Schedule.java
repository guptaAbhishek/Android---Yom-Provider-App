package com.example.siddarthshikhar.yomtrainerside;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class Schedule extends ActionBarActivity {

    TextView showSelectedDate;
    CaldroidFragment caldroidFragment;
    Date savedDate;
    HashMap<Date,String> map;
    private SlidingUpPanelLayout mLayout;
    String currDate;
    ArrayList<YogaClass> list;
    ListView lv;
    final Context c=this;
    ClassAdapter adapter;
    ImageView up1,up2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        list=new ArrayList<YogaClass>();
        lv =(ListView)findViewById(R.id.yoga_class_list);

        up1=(ImageView)findViewById(R.id.up_icon_1);
        up2=(ImageView)findViewById(R.id.up_icon_2);

        map=new HashMap<Date,String>();
        savedDate=null;

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelExpanded(View panel) {
                up1.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                up2.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }

            @Override
            public void onPanelCollapsed(View panel) {
                up1.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                up2.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            }

            @Override
            public void onPanelAnchored(View panel) {
            }

            @Override
            public void onPanelHidden(View panel) {
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBar bar=getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.actionbarview, null);
        bar.setCustomView(v);
        TextView title=(TextView)v.findViewById(R.id.actbartitle);
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        title.setText("My Schedule");
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));

        caldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, true);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(listener);

        setDatesWithColours();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        showSelectedDate=(TextView)findViewById(R.id.date_selected);
        showSelectedDate.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));

        ((Button)findViewById(R.id.add_client)).setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Bold.ttf"));
    }
    public void populateClassList(){
        SQLiteHelper helper = new SQLiteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = new String[6];
        columns[0] = SQLiteHelper.COLUMN_VENUE ;
        columns[1] = SQLiteHelper.COLUMN_DATE ;
        columns[2] = SQLiteHelper.COLUMN_STARTTIME ;
        columns[3] = SQLiteHelper.COLUMN_ENDTIME ;
        columns[4] = SQLiteHelper.COLUMN_IFTAKEN ;
        columns[5] = SQLiteHelper.COLUMN_NAME ;
        Cursor c1 = db.query(SQLiteHelper.CLASS_TABLE,columns,null,null,null,null,null);
        while(c1.moveToNext()){
            String date = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_DATE));
            String startTime = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_STARTTIME));
            String endTime = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_ENDTIME));
            String venue = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_VENUE));
            String name = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_NAME));
            int ifAdded=c1.getInt(c1.getColumnIndex(SQLiteHelper.COLUMN_IFTAKEN));
            if(date.equals(currDate))
                list.add(new YogaClass(date,startTime,endTime,venue,name,ifAdded));
        }
        String temp=currDate+" 23:59";
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateFromTemp=null;
        try {
            dateFromTemp=format.parse(temp);
        } catch (ParseException e) {
        }
        if(list.size()==0 && dateFromTemp.getTime()<System.currentTimeMillis()){
            list.add(new YogaClass(currDate,"00:00","00:00","bogus","bogus",0));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
    public void onClick(View v){
        int id=v.getId();
        if(id==R.id.add_client){
            startActivity(new Intent(this,AddClass.class));
        }
    }
    public  void setDatesWithColours(){
        SQLiteHelper helper = new SQLiteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = new String[5];
        columns[0] = SQLiteHelper.COLUMN_VENUE ;
        columns[1] = SQLiteHelper.COLUMN_DATE ;
        columns[2] = SQLiteHelper.COLUMN_STARTTIME ;
        columns[3] = SQLiteHelper.COLUMN_ENDTIME ;
        columns[4]=SQLiteHelper.COLUMN_IFTAKEN;
        Cursor c1 = db.query(SQLiteHelper.CLASS_TABLE,columns,null,null,null,null,null);
        while(c1.moveToNext()){
            String date = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_DATE));
            int ifAttended=c1.getInt(c1.getColumnIndex(SQLiteHelper.COLUMN_IFTAKEN));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date currDate=null;
            try {
                currDate=format.parse(date);
            } catch (ParseException e) {
            }
            if(map.containsKey(currDate)){
                if(map.get(currDate).equals("Yellow")){
                    continue;
                }
                else if(map.get(currDate).equals("Green")){
                    if(ifAttended!=1) {
                        caldroidFragment.setBackgroundResourceForDate(R.color.Yellow, currDate);
                        map.remove(currDate);
                        map.put(currDate,"Yellow");
                    }else
                        continue;
                }
                else{
                    if(ifAttended!=-1){
                        caldroidFragment.setBackgroundResourceForDate(R.color.Yellow, currDate);
                        map.remove(currDate);
                        map.put(currDate,"Yellow");
                    }else
                        continue;
                }
            }else{
                if(ifAttended==-1){
                    map.put(currDate,"Red");
                    caldroidFragment.setBackgroundResourceForDate(R.color.Red,currDate);
                }else if(ifAttended==0){
                    map.put(currDate,"Yellow");
                    caldroidFragment.setBackgroundResourceForDate(R.color.Yellow,currDate);
                }else if(ifAttended==1){
                    map.put(currDate,"Green");
                    caldroidFragment.setBackgroundResourceForDate(R.color.Green,currDate);
                }
            }
        }
    }
    final CaldroidListener listener = new CaldroidListener() {
        @Override
        public void onSelectDate(Date date, View view) {
            SimpleDateFormat format=new SimpleDateFormat("dd MMMM yyyy",Locale.US);
            showSelectedDate.setText(format.format(date));
            if(savedDate!=null){
                if(map.containsKey(savedDate)){
                    if(map.get(savedDate).equals("Red")){
                        caldroidFragment.setBackgroundResourceForDate(R.color.Red,savedDate);
                    }else if(map.get(savedDate).equals("Yellow")){
                        caldroidFragment.setBackgroundResourceForDate(R.color.Yellow,savedDate);
                    }else if(map.get(savedDate).equals("Green")){
                        caldroidFragment.setBackgroundResourceForDate(R.color.Green,savedDate);
                    }
                }
                else
                    caldroidFragment.clearBackgroundResourceForDate(savedDate);
            }
            savedDate=date;
            caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_sky_blue,savedDate);
            caldroidFragment.refreshView();
            list.clear();
            currDate="";
            format = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
            try {
                Date temp=format.parse(showSelectedDate.getText().toString());
                format=new SimpleDateFormat("dd/MM/yyyy",Locale.US);
                currDate=format.format(temp);
            } catch (ParseException e) {
            }
            populateClassList();

            adapter = new ClassAdapter(c,0,list,Schedule.this.getLayoutInflater(),Schedule.this.getFragmentManager());
            lv.setAdapter(adapter);
        }
        @Override
        public void onChangeMonth(int month, int year) {
        }
        @Override
        public void onLongClickDate(Date date, View view) {
            onSelectDate(date, view);
        }
        @Override
        public void onCaldroidViewCreated() {
            Date temp=new Date(System.currentTimeMillis());
            savedDate=temp;
            SimpleDateFormat format=new SimpleDateFormat("dd MMMM yyyy",Locale.US);
            showSelectedDate.setText(format.format(temp));
            caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_sky_blue, temp);
            caldroidFragment.refreshView();
            list.clear();
            currDate="";
            format=new SimpleDateFormat("dd/MM/yyyy",Locale.US);
            currDate=format.format(temp);
            populateClassList();

            adapter = new ClassAdapter(c,0,list,Schedule.this.getLayoutInflater(),Schedule.this.getFragmentManager());
            lv.setAdapter(adapter);

            up1.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            up2.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        }
    };
}
