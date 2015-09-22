package com.example.siddarthshikhar.yomtrainerside;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


public class MainUserPanel extends ActionBarActivity implements ActionBar.TabListener {

    ArrayList<YogaClass> upcomingClassesList;

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    final String drawerListTitles[]={"Home","Schedule","Profile","Share App","Settings","Contact"};

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    int locationOfDateHeader[];

    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_panel);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        locationOfDateHeader=new int[28];

        upcomingClassesList= new ArrayList<YogaClass>();

        updateUpcomingClassesList();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new DrawerListAdapter(this, 0, drawerListTitles, getLayoutInflater()));
        DrawerItemClickListener temp = new DrawerItemClickListener();
        temp.c = this;
        temp.compare=0;
        temp.currDrawer=mDrawerLayout;
        temp.currDrawerList=mDrawerList;
        mDrawerList.setOnItemClickListener(temp);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.actionbarviewmainuserpanel, null);
        bar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
        bar.setCustomView(v);
        title = (TextView) v.findViewById(R.id.actbartitle);
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            ActionBar.Tab curr=actionBar.newTab();
            v=getLayoutInflater().inflate(R.layout.tab_custom_view,null);
            switch (i) {
                case 0:
                    curr.setIcon(R.drawable.ic_view_list_white_24dp);
                    break;
                case 1:
                    curr.setIcon(R.drawable.ic_assignment_white_24dp);
                    break;
                case 2:
                    curr.setIcon(R.drawable.ic_accessibility_white_24dp);
                    break;
                case 3:
                    curr.setIcon(R.drawable.ic_grade_white_24dp);
                    break;
            }
            curr.setTabListener(this);
            actionBar.addTab(curr);
        }

    }
    public void onClick(View v){
        int id=v.getId();
        if(id==R.id.add_client_from_action_bar){
            startActivity(new Intent(this,AddClass.class));
        }else if(id==R.id.notifications_from_action_bar){
            mViewPager.setCurrentItem(1);
        }
    }

    public void classListFromDatabase(){

    }


    public void updateUpcomingClassesList(){
        long startTime=System.currentTimeMillis();
        SQLiteHelper helper = new SQLiteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = new String[6];
        columns[0] = SQLiteHelper.COLUMN_VENUE ;
        columns[1] = SQLiteHelper.COLUMN_DATE ;
        columns[2] = SQLiteHelper.COLUMN_STARTTIME ;
        columns[3] = SQLiteHelper.COLUMN_ENDTIME ;
        columns[4]=SQLiteHelper.COLUMN_IFTAKEN;
        columns[5]=SQLiteHelper.COLUMN_NAME;
        Cursor c1 = db.query(SQLiteHelper.CLASS_TABLE,columns,null,null,null,null,null);
        while(c1.moveToNext()){
            String date = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_DATE));
            String sTime = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_STARTTIME));
            String eTime = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_ENDTIME));
            String venue = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_VENUE));
            String name = c1.getString(c1.getColumnIndex(SQLiteHelper.COLUMN_NAME));
            int ifAdded=c1.getInt(c1.getColumnIndex(SQLiteHelper.COLUMN_IFTAKEN));
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            Date currDate=null;
            try {
                currDate=format.parse(date + " 23:59");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(currDate.getTime()>=startTime)
                upcomingClassesList.add(new YogaClass(date, sTime, eTime, venue, name, ifAdded));
        }
        Collections.sort(upcomingClassesList);
        startTime/=1000;
        long currTime=startTime;
        int i=0,j=1,diff=0;
        while(currTime<startTime+(4*7*24*60*60)){
            if(upcomingClassesList.size()==0)
                break;
            Date dateFromCurrTime=new Date(currTime*1000);
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy",Locale.US);
            String currDate=format.format(dateFromCurrTime);
              if(upcomingClassesList.get(i).date.equals(currDate)==false){
                locationOfDateHeader[j]=diff+locationOfDateHeader[j-1]+1;
                j++;
                if(j==28)
                    break;
                diff=0;
                currTime+=24*60*60;
            }else{
                i++;
                diff++;
            }
            if(i==upcomingClassesList.size()) {
                locationOfDateHeader[j]=diff+locationOfDateHeader[j-1]+1;
                j++;
                break;
            }
        }
        while(j<=27) {
            locationOfDateHeader[j] = locationOfDateHeader[j - 1] + 1;
            j++;
        }
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
        switch (tab.getPosition()){
            case 0:
                title.setText("Upcoming Classes");
                break;
            case 1:
                title.setText("Available Classes");
                break;
            case 2:
                title.setText("My Clients");
                break;
            case 3:
                title.setText("Favourites");
                break;
        }
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if(position==0){
                MyClassesFragment temp=new MyClassesFragment();
                temp.c=MainUserPanel.this;
                temp.upcomingClasses=upcomingClassesList;
                temp.headerLocations=locationOfDateHeader;
                title.setText("Upcoming Classes");
                return temp;
            }
            else if(position==1){
                return new AvailableClassesFragment();
            }
            else if(position==2){
                return new MyClientsFragment();
            }else{
                return new FavouritesFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
