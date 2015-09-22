package com.example.siddarthshikhar.yomtrainerside;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ChangePassword extends ActionBarActivity implements CustomErrorDialog.ErrorDialogTaskDone, PostRequestAsyncTask.RequestDoneTaskListener {
    MaterialEditText newpass,oldpass,newpassagain;
    Button changepass;
    ProgressDialog dlg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar bar=getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.actionbarview, null);
        bar.setCustomView(v);
        TextView title=(TextView)v.findViewById(R.id.actbartitle);
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        title.setText("CHANGE PASSWORD");
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Comfortaa-Bold.ttf"));


        newpass = (MaterialEditText) findViewById(R.id.newpass);
        oldpass=(MaterialEditText)findViewById(R.id.oldpass);
        newpassagain=(MaterialEditText)findViewById(R.id.newpassagain);
        changepass = (Button) findViewById(R.id.btchangepass);


        ((TextView)findViewById(R.id.change_pass_mess_1)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((TextView)findViewById(R.id.change_pass_mess_2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((MaterialEditText)findViewById(R.id.newpassagain)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((MaterialEditText)findViewById(R.id.newpassagain)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((MaterialEditText)findViewById(R.id.newpass)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((MaterialEditText)findViewById(R.id.newpass)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((MaterialEditText)findViewById(R.id.oldpass)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((MaterialEditText)findViewById(R.id.oldpass)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((Button)findViewById(R.id.btchangepass)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Comfortaa-Bold.ttf"));
    }
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.btchangepass){
            CustomErrorDialog dialog=new CustomErrorDialog();
            dialog.listener=this;
            if(oldpass.getText().toString().length()<5){
                dialog.dialogString="Old Password should be of Minimum 5 characters";
                dialog.show(getFragmentManager(),"");
                return;
            }else if(oldpass.getText().toString().length()>15) {
                dialog.dialogString = "Old Password should be of Maximum 15 characters";
                dialog.show(getFragmentManager(), "");
                return;
            }else if(newpass.getText().toString().length()<5){
                dialog.dialogString="New Password should be of Minimum 5 characters";
                dialog.show(getFragmentManager(),"");
                return;
            }else if(newpass.getText().toString().length()>15) {
                dialog.dialogString = "New Password should be of Maximum 15 characters";
                dialog.show(getFragmentManager(), "");
                return;
            }else if(newpass.getText().toString().equals(newpassagain.getText().toString())==false) {
                dialog.dialogString = "New Passwords do not match";
                dialog.show(getFragmentManager(), "");
                return;
            }
            dlg = new ProgressDialog(ChangePassword.this);
            dlg.setTitle("Please wait.");
            dlg.setMessage("Logging in.  Please wait.");
            dlg.show();
            JSONObject toBePosted=new JSONObject();
            try {
                toBePosted.put("phone",getSharedPreferences("profilephone",0).getString("phone", null));
                toBePosted.put("authKey",getSharedPreferences("Authorization",0).getString("Auth_key", null));
                toBePosted.put("oldpass",oldpass.getText().toString());
                toBePosted.put("newpass",newpass.getText().toString());
            } catch (JSONException e) {
            }
            PostRequestAsyncTask puttingTask=new PostRequestAsyncTask();
            puttingTask.toBePosted=toBePosted;
            puttingTask.listener=this;
            puttingTask.execute("http://"+Constants.IP_ADDRESS+"changepasswordpro");
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
    public void DialogEnded(String dialogString) {
        if(dialogString.equals("Password Changed Successfully")){
            Intent mainIntent = new Intent(ChangePassword.this, MainUserPanel.class);
            ChangePassword.this.startActivity(mainIntent);
            ChangePassword.this.finish();
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
//                responseMessage=obj.getString("Response_Message");
            } catch (JSONException e) {
            }
            if(responseType.equals("Success")){
                CustomErrorDialog dialog=new CustomErrorDialog();
                dialog.listener=this;
                dialog.dialogString="Password Changed Successfully";
                dialog.show(getFragmentManager(), "");
            }else{
                CustomErrorDialog dialog=new CustomErrorDialog();
                dialog.listener=this;
                dialog.dialogString="Incorrect Current Password. Please contact 9873805309";
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






















