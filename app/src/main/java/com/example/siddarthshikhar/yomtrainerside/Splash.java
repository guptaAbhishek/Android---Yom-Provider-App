package com.example.siddarthshikhar.yomtrainerside;
        import android.app.Activity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.Window;
        import android.view.WindowManager;

public class Splash extends Activity {


    private static int splashInterval = 3000;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashz);
        sp=getSharedPreferences("profilename", 0);
        editor=sp.edit();
        editor.putString("name", "Paras");
        editor.commit();
        sp=getSharedPreferences("profileage", 0);
        editor=sp.edit();
        editor.putString("age", "1996/06/03");
        editor.commit();
        sp=getSharedPreferences("profilegender", 0);
        editor=sp.edit();
        editor.putString("gender", "Male");
        editor.commit();
        sp=getSharedPreferences("profilequalification1", 0);
        editor=sp.edit();
        editor.putString("qualification1", "");
        editor.commit();
        sp=getSharedPreferences("profilequalification2", 0);
        editor=sp.edit();
        editor.putString("qualification2", "");
        editor.commit();
        sp=getSharedPreferences("profilequalification3", 0);
        editor=sp.edit();
        editor.putString("qualification3", "");
        editor.commit();
        sp=getSharedPreferences("profileexperience", 0);
        editor=sp.edit();
        editor.putString("experience","2 years 5 months");
        editor.commit();
        sp=getSharedPreferences("profilephone", 0);
        editor=sp.edit();
        editor.putString("phone","9999999999");
        editor.commit();
        sp=getSharedPreferences("profileemail", 0);
        editor=sp.edit();
        editor.putString("email","parasgupta@gmail.com");
        editor.commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getSharedPreferences("Authorization",0).getString("Auth_key",null)!=null){
                    Intent i = new Intent(Splash.this, MainUserPanel.class);
                    startActivity(i);
                    this.finish();
                }else{
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                    this.finish();
                }
            }

            private void finish() {

            }
        }, splashInterval);
    };

}