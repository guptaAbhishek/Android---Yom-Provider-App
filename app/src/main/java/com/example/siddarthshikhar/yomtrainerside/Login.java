package com.example.siddarthshikhar.yomtrainerside;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity implements CustomErrorDialog.ErrorDialogTaskDone, PostRequestAsyncTask.RequestDoneTaskListener {
    Button btnLogin;
    Button Btnregister;
    TextView passreset;
    MaterialEditText inputEmail;
    EditText inputPassword;
    ProgressDialog dlg;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (MaterialEditText) findViewById(R.id.login_phoneno);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (TextView) findViewById(R.id.passres);

        ((TextView)findViewById(R.id.textView2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText)findViewById(R.id.login_phoneno)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText)findViewById(R.id.login_phoneno)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((EditText)findViewById(R.id.pword)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((Button)findViewById(R.id.registerbtn)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((Button)findViewById(R.id.login)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.passres)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
    }
    public void onClick(View v){
        int id=v.getId();
        if(id==R.id.login){
            CustomErrorDialog dialog=new CustomErrorDialog();
            dialog.listener=this;
            if(inputEmail.getText().toString().length()!=10){
                dialog.dialogString="Please Enter a 10-digit Mobile No.";
                dialog.show(getFragmentManager(),"");
                return;
            }else if(inputPassword.getText().toString().length()<5){
                dialog.dialogString="Password should be of Minimum 5 characters";
                dialog.show(getFragmentManager(),"");
                return;
            }else if(inputPassword.getText().toString().length()>15) {
                dialog.dialogString = "Password should be of Maximum 15 characters";
                dialog.show(getFragmentManager(), "");
                return;
            }
            dlg = new ProgressDialog(Login.this);
            dlg.setTitle("Please wait.");
            dlg.setMessage("Logging in.  Please wait.");
            dlg.show();
            JSONObject toBePosted=new JSONObject();
            try {
                toBePosted.put("username",inputEmail.getText().toString());
                toBePosted.put("password",inputPassword.getText().toString());
                toBePosted.put("pushDeviceId", Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
                toBePosted.put("deviceOS","Android");
                toBePosted.put("osVersion", Build.VERSION.RELEASE);
            } catch (JSONException e) {
            }
            PostRequestAsyncTask puttingTask=new PostRequestAsyncTask();
            puttingTask.toBePosted=toBePosted;
            puttingTask.listener=this;
            puttingTask.execute("http://"+Constants.IP_ADDRESS+"loginprovider");
        }else if(id==R.id.registerbtn){
            Intent mainIntent = new Intent(this, Register.class);
            startActivity(mainIntent);
        }else if(id==R.id.passres){
            Intent mainIntent = new Intent(this, PasswordReset.class);
            startActivity(mainIntent);
        }
    }
    @Override
    public void processEnquiries(Boolean ifExecuted, String output, int typeOfError) {
        dlg.dismiss();
        if(ifExecuted==true){
            JSONObject obj = null;
            String responseType="",responseMessage="",authKey="";
            try {
                obj = new JSONObject(output);
                responseType=obj.getString("Response_Type");
                responseMessage=obj.getString("Response_Message");
                authKey=obj.getString("Auth_Key");
            } catch (JSONException e) {
            }
            if(responseType.equals("Success")){
                sp=getSharedPreferences("Authorization", 0);
                editor=sp.edit();
                editor.putString("Auth_key", authKey);
                editor.commit();
                sp=getSharedPreferences("profilephone", 0);
                editor=sp.edit();
                editor.putString("phone",inputEmail.getText().toString());
                editor.commit();
                Toast.makeText(this, "User Successfully Logged In", Toast.LENGTH_SHORT);
                Intent mainIntent = new Intent(Login.this, MainUserPanel.class);
                Login.this.startActivity(mainIntent);
                Login.this.finish();
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
    }
}