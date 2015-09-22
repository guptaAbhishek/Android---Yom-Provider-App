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
import android.view.LayoutInflater;
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


public class PasswordReset extends ActionBarActivity implements CustomErrorDialog.ErrorDialogTaskDone, PostRequestAsyncTask.RequestDoneTaskListener {

    MaterialEditText email;
    Button resetpass;
    ProgressDialog dlg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        ActionBar bar=getSupportActionBar();
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.actionbarview, null);
        bar.setCustomView(v);
        TextView title=(TextView)v.findViewById(R.id.actbartitle);
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        title.setText("Forgot Password");
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Regular-Bold.ttf"));

        email = (MaterialEditText) findViewById(R.id.forpas);
        resetpass = (Button) findViewById(R.id.respass);

        ((TextView)findViewById(R.id.forgot_pass_mess_1)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.forgot_pass_mess_2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText)findViewById(R.id.forpas)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText)findViewById(R.id.forpas)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((Button)findViewById(R.id.respass)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
    }
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.respass){
            CustomErrorDialog dialog=new CustomErrorDialog();
            dialog.listener=this;
            if(email.getText().toString().length()!=10){
                dialog.dialogString="Please Enter a 10-digit Mobile No.";
                dialog.show(getFragmentManager(),"");
                return;
            }
            dlg = new ProgressDialog(PasswordReset.this);
            dlg.setTitle("Please wait.");
            dlg.setMessage("Requesting Password Change");
            dlg.show();
            JSONObject toBePosted=new JSONObject();
            try {
                toBePosted.put("phone",email.getText().toString());
            } catch (JSONException e) {
            }
            PostRequestAsyncTask puttingTask=new PostRequestAsyncTask();
            puttingTask.toBePosted=toBePosted;
            puttingTask.listener=this;
            puttingTask.execute("http://"+Constants.IP_ADDRESS+"forgotpasswordpro");
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
                dialog.dialogString="A New Password Has been sent to your registered mail id";
                dialog.show(getFragmentManager(), "");
            }else{
                CustomErrorDialog dialog=new CustomErrorDialog();
                dialog.listener=this;
                dialog.dialogString=responseMessage;
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

    @Override
    public void DialogEnded(String dialogString) {
        if(dialogString.equals("A New Password Has been sent to your registered mail id")){
            startActivity(new Intent(this,Login.class));
        }
    }
}





