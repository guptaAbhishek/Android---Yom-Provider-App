package com.example.siddarthshikhar.yomtrainerside;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rakesh on 7/2/2015.
 */
public class YogaClass implements Comparable {
    String date,startTime,endTime,venue,name;
    int ifTaken;
    public YogaClass(String date,String startTime,String endTime,String venue,String name,int ifTaken){
        this.date=date;
        this.startTime=startTime;
        this.endTime=endTime;
        this.venue=venue;
        this.name=name;
        this.ifTaken=ifTaken;
    }

    @Override
    public int compareTo(Object another) {
        YogaClass temp=(YogaClass)another;
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date argDate=null,currDate=null;
        try {
            argDate=format.parse(temp.date);
            currDate=format.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(argDate.getTime()<currDate.getTime())
            return 1;
        else if(argDate.getTime()>currDate.getTime())
            return -1;
        return 0;
    }
}