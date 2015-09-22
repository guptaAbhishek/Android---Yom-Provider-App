package com.example.siddarthshikhar.yomtrainerside;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class DrawerItemClickListener implements ListView.OnItemClickListener {
    Context c;
    DrawerLayout currDrawer;
    ListView currDrawerList;
    int compare;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position){
            case 0:
                if(compare==0){
                    currDrawer.closeDrawer(currDrawerList);
                    break;
                }
                break;
            case 1:
                intent = new Intent(c, Schedule.class);
                c.startActivity(intent);
                break;
            case 2:
                intent = new Intent(c, My_Profile.class);
                c.startActivity(intent);
                break;
            case 3:
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Check out Yom App www.google.com");
                intent.setType("text/plain");
                c.startActivity(intent);
                break;
            case 4:
                intent = new Intent(c, SettingsActivity.class);
                c.startActivity(intent);
                break;
            case 5:
                intent = new Intent(c, ContactUsActivity.class);
                c.startActivity(intent);
                break;
        }
    }
}
