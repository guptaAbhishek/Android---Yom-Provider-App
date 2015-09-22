package com.example.siddarthshikhar.yomtrainerside;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddClass extends ActionBarActivity implements DayPickerDialog.dayPicked, setDateFragment.datePicked, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemClickListener, CustomErrorDialog.ErrorDialogTaskDone, PostRequestAsyncTask.RequestDoneTaskListener {
    ArrayList mSelectedItems;
    TextView starttime,sunday,monday,tuesday,wednesday,thursday,friday,saturday,startdate,enddate;
    boolean start,end;
    int x;
    String endtime;
    MaterialEditText phoneNo;
    MaterialAutoCompleteTextView clientName;
    MaterialAutoCompleteTextView finalAdd;
    Contact[] list;
    ProgressDialog dlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        x=0;
        start=false;
        end=false;
        mSelectedItems=new ArrayList();
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
        title.setText("Add Client");
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));

        starttime=(TextView)findViewById(R.id.book_class_start_time);
        starttime.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        endtime="";
        sunday=(TextView)findViewById(R.id.sunday);
        monday=(TextView)findViewById(R.id.monday);
        tuesday=(TextView)findViewById(R.id.tuesday);
        wednesday=(TextView)findViewById(R.id.wednesday);
        thursday=(TextView)findViewById(R.id.thursday);
        friday=(TextView)findViewById(R.id.friday);
        saturday=(TextView)findViewById(R.id.saturday);

        int size=0;
        String[] columns=new String[2];
        columns[0]= ContactsContract.CommonDataKinds.Phone.NUMBER;
        columns[1]=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor c=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,columns,null,null,null);
        list=new Contact[c.getCount()];
        while(c.moveToNext()){
            String name=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNo=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNo=phoneNo.replaceAll("\\s","");
            if(phoneNo.length()>=10){
                list[size]=new Contact(name,phoneNo.substring(phoneNo.length()-10,phoneNo.length()));
                size++;
            }
        }
        finalAdd=(MaterialAutoCompleteTextView)findViewById(R.id.final_address);
        finalAdd.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        finalAdd.setAdapter(new GooglePlacesAutocompleteAdapter(this, 0, getLayoutInflater()));
        finalAdd.setOnItemClickListener(this);

        clientName=(MaterialAutoCompleteTextView)findViewById(R.id.client_name);
        clientName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        clientName.setThreshold(1);
        clientName.setAdapter(new ContactsAutocompleteAdapter(this, 0,list, getLayoutInflater()));
        clientName.setOnItemClickListener(this);

        phoneNo=(MaterialEditText)findViewById(R.id.final_phone_no);
        phoneNo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));


        startdate=(TextView)findViewById(R.id.book_class_start_date);
        enddate=(TextView)findViewById(R.id.book_class_end_date);
        startdate.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        enddate.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));


        ((TextView)findViewById(R.id.enter_start_time_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.pick_your_days_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((Button)findViewById(R.id.book_class_confirm)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));
        ((MaterialAutoCompleteTextView)findViewById(R.id.client_name)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText)findViewById(R.id.final_phone_no)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView)findViewById(R.id.final_address)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
    }
    public void onClick(View v){
        int id=v.getId();
        if(id==R.id.book_class_start_time){
            CustomTimePickerDialog dialog=new CustomTimePickerDialog(this,this, Calendar.HOUR_OF_DAY,Calendar.MINUTE,true);
            dialog.show();
        }
        else if(id==R.id.book_class_confirm){
            putInDatabase();
        }
        else if(id==R.id.book_class_start_date){
            start=true;
            DialogFragment newFragment = new setDateFragment();
            setDateFragment temp=(setDateFragment)newFragment;
            temp.listener=this;
            final Calendar c = Calendar.getInstance();
            temp.presetYear=c.get(Calendar.YEAR);
            temp.presetMonth= c.get(Calendar.MONTH);
            temp.presetDay= c.get(Calendar.DAY_OF_MONTH);
            newFragment.show(getFragmentManager(), "DatePicker");
        }
        else if(id==R.id.book_class_end_date){
            end=true;
            DialogFragment newFragment = new setDateFragment();
            setDateFragment temp=(setDateFragment)newFragment;
            final Calendar c = Calendar.getInstance();
            temp.presetYear=c.get(Calendar.YEAR);
            temp.presetMonth= c.get(Calendar.MONTH);
            temp.presetDay= c.get(Calendar.DAY_OF_MONTH);
            temp.listener=this;
            newFragment.show(getFragmentManager(),"DatePicker");
        }
        else if(id==R.id.initially_day_picker){
            DayPickerDialog newFragment=new DayPickerDialog();
            newFragment.listener=this;
            newFragment.temp=mSelectedItems;
            newFragment.show(getFragmentManager(), "DayPicker");
        }
        else if(id==R.id.book_class_day_picker){
            DayPickerDialog newFragment=new DayPickerDialog();
            newFragment.listener=this;
            newFragment.temp=mSelectedItems;
            newFragment.show(getFragmentManager(), "DayPicker");
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hours="",mins="";
        if(hourOfDay<10)
            hours+="0"+hourOfDay;
        else
            hours+=hourOfDay;
        if(minute<10)
            mins+="0"+minute;
        else
            mins+=minute;
        starttime.setText(hours + ":" + mins + " hrs");
        hours="";
        hourOfDay++;
        if(hourOfDay<10)
            hours+="0"+hourOfDay;
        else
            hours+=hourOfDay;
        endtime=hours + ":" + mins + " hrs";
    }
    public void populateSetDate(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        if(start==true)
            startdate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
        else if(end==true)
            enddate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
        start=false;
        end=false;
    }
    @Override
    public void populatedays(ArrayList selectedItems) {
        if(x==0){
            x++;
            View C = findViewById(R.id.replacable_parent_layout);
            ViewGroup parent = (ViewGroup) C.getParent();
            int index = parent.indexOfChild(C);
            parent.removeView(C);
            C = getLayoutInflater().inflate(R.layout.day_picker_to_be_loaded, parent, false);
            parent.addView(C, index);
            sunday=(TextView)C.findViewById(R.id.sunday);
            monday=(TextView)C.findViewById(R.id.monday);
            tuesday=(TextView)C.findViewById(R.id.tuesday);
            wednesday=(TextView)C.findViewById(R.id.wednesday);
            thursday=(TextView)C.findViewById(R.id.thursday);
            friday=(TextView)C.findViewById(R.id.friday);
            saturday=(TextView)C.findViewById(R.id.saturday);
            sunday.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
            monday.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
            tuesday.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
            wednesday.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
            thursday.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
            friday.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
            saturday.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        }
        mSelectedItems=new ArrayList();
        this.mSelectedItems=selectedItems;
        sunday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglewhitebackgr));
        monday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglewhitebackgr));
        tuesday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglewhitebackgr));
        wednesday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglewhitebackgr));
        thursday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglewhitebackgr));
        friday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglewhitebackgr));
        saturday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglewhitebackgr));
        for(int i=0;i<selectedItems.size();i++){
            int x=(Integer)selectedItems.get(i);
            switch (x){
                case 0:
                    sunday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglebluebackgr));
                    break;
                case 1:
                    monday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglebluebackgr));
                    break;
                case 2:
                    tuesday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglebluebackgr));
                    break;
                case 3:
                    wednesday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglebluebackgr));
                    break;
                case 4:
                    thursday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglebluebackgr));
                    break;
                case 5:
                    friday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglebluebackgr));
                    break;
                case 6:
                    saturday.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglebluebackgr));
                    break;
            }
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
    public void putInDatabase(){
        LatLng locationOfAddress=getLocationFromAddress(finalAdd.getText().toString());
        CustomErrorDialog dialog=new CustomErrorDialog();
        dialog.listener=this;
        if(locationOfAddress==null) {
            dialog.dialogString = "Please Enter A Valid Address seperated by proper punctuation";
            dialog.show(getFragmentManager(), "");
            return;
        }else if(phoneNo.getText().toString().length()!=10){
            dialog.dialogString="Please Enter a 10-digit Mobile No.";
            dialog.show(getFragmentManager(),"");
            return;
        }else if(clientName.getText().toString().length()==0){
            dialog.dialogString="Please Enter Client's Name";
            dialog.show(getFragmentManager(),"");
            return;
        }
        dlg = new ProgressDialog(AddClass.this);
        dlg.setTitle("Please wait.");
        dlg.setMessage("Logging in.  Please wait.");
        dlg.show();

        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        Date start=null,end=null;
        try {
            start=format.parse(startdate.getText().toString());
            end=format.parse(enddate.getText().toString());
        } catch (ParseException e) {
        }
        format=new SimpleDateFormat("yyyy-MM-dd",Locale.US);

        String daysSelected="";
        for(int i=1;i<=6;i++){
            if(mSelectedItems.contains(i)){
                daysSelected+="Y";
            }else
                daysSelected+="N";
            if(i==0)
                break;
            else if(i==6)
                i=-1;
        }
        JSONObject toBePosted=new JSONObject();
        try {
            toBePosted.put("phone",getSharedPreferences("profilephone", 0).getString("phone", null));
            toBePosted.put("authKey",getSharedPreferences("Authorization", 0).getString("Auth_key", null));
            toBePosted.put("name", clientName.getText().toString());
            toBePosted.put("con_phone", phoneNo.getText().toString());
            toBePosted.put("venue", finalAdd.getText().toString());
            toBePosted.put("venue_lat",locationOfAddress.latitude);
            toBePosted.put("venue_long",locationOfAddress.longitude);
            toBePosted.put("start_date",format.format(start));
            toBePosted.put("end_date",format.format(end));
            toBePosted.put("start_time", starttime.getText().toString().substring(0,5)+":00");
            toBePosted.put("end_time", endtime.substring(0,5)+":00");
            toBePosted.put("days",daysSelected);
        } catch (JSONException e) {
        }
        PostRequestAsyncTask puttingTask=new PostRequestAsyncTask();
        puttingTask.toBePosted=toBePosted;
        puttingTask.listener=this;
        puttingTask.execute("http://"+Constants.IP_ADDRESS+"addclass");
    }
    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (Exception ex) {
        }
        return p1;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).getClass()==Contact.class){
            clientName.setText(((Contact)parent.getItemAtPosition(position)).name);
            phoneNo.setText(((Contact)parent.getItemAtPosition(position)).phoneNo);
        }
    }

    @Override
    public void DialogEnded(String dialogString) {
        if(dialogString.equals("Schedule Has been successfully Updated")){
            Intent mainIntent = new Intent(AddClass.this, MainUserPanel.class);
            AddClass.this.startActivity(mainIntent);
            AddClass.this.finish();
            //Not addind to the database
        }else if(dialogString.equals("We are experiencing some Technical difficulties.Please contact us.")){
            Intent mainIntent = new Intent(AddClass.this, ContactUsActivity.class);
            AddClass.this.startActivity(mainIntent);
            AddClass.this.finish();
        }
    }

    @Override
    public void processEnquiries(Boolean ifExecuted, String output, int typeOfError) {
        dlg.dismiss();
        if(ifExecuted==true){
            JSONObject obj = null;
            String responseType="",responseMessage="";
            try {
                obj = new JSONObject(output);
                responseType=obj.getString("Response_Type");
                responseMessage=obj.getString("Response_Message");
            } catch (JSONException e) {
            }
            if(responseType.equals("Success")){
                CustomErrorDialog dialog=new CustomErrorDialog();
                dialog.listener=this;
                dialog.dialogString="Schedule Has been successfully Updated";
                dialog.show(getFragmentManager(), "");
            }else{
                CustomErrorDialog dialog=new CustomErrorDialog();
                dialog.listener=this;
                dialog.dialogString="We are experiencing some Technical difficulties.Please contact us.";
                dialog.show(getFragmentManager(), "");
            }
        }else{
            CustomErrorDialog dialog=new CustomErrorDialog();
            dialog.listener=this;
            if(typeOfError==1){
                dialog.dialogString="No Internet Connection!";
                dialog.show(getFragmentManager(), "");
            }
            else{
                dialog.dialogString="Oops! Server Error! Please try after some time";
                dialog.show(getFragmentManager(),"");
            }
        }
    }
}
