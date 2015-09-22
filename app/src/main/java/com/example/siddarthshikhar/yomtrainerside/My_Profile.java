package com.example.siddarthshikhar.yomtrainerside;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class My_Profile extends ActionBarActivity {

    ImageView profilePic,idProof;
    int profileOrId;
    Spinner idProofType;
    String[] idProofTypeList={"Aadhaar Card","Passport","Ration Card","Driving License"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);

        profileOrId=0;
        profilePic=(ImageView)findViewById(R.id.profilePic);
        idProof=(ImageView)findViewById(R.id.idProof);

        ActionBar bar=getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.actionbarviewmyprofile, null);
        bar.setCustomView(v);
        TextView title=(TextView)v.findViewById(R.id.actbartitle);
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        title.setText("My Profile");
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));

        idProofType = (Spinner) findViewById(R.id.id_proof_type);
        SpinnerAdapter idProofTypeAdapter = new SpinnerAdapter(this,0,idProofTypeList,getLayoutInflater());
        idProofTypeAdapter.setDropDownViewResource(R.layout.spinner_individual_item);
        idProofType.setAdapter(idProofTypeAdapter);

        TextView temp=(TextView)findViewById(R.id.my_profile_name);
        temp.setText(getSharedPreferences("profilename", 0).getString("name", null));
        temp=(TextView)findViewById(R.id.my_profile_age);
        temp.setText(getSharedPreferences("profileage",0).getString("age",null));
        temp=(TextView)findViewById(R.id.my_profile_gender);
        temp.setText(getSharedPreferences("profilegender",0).getString("gender",null));
        temp=(TextView)findViewById(R.id.my_profile_qual_1);
        temp.setText(getSharedPreferences("profilequalification1", 0).getString("qualification1", null));
        temp=(TextView)findViewById(R.id.my_profile_qual_2);
        temp.setText(getSharedPreferences("profilequalification2",0).getString("qualification2",null));
        temp=(TextView)findViewById(R.id.my_profile_qual_3);
        temp.setText(getSharedPreferences("profilequalification3",0).getString("qualification3",null));
        temp=(TextView)findViewById(R.id.my_profile_experience);
        temp.setText(getSharedPreferences("profileexperience",0).getString("experience",null));
        temp=(TextView)findViewById(R.id.my_profile_mobile);
        temp.setText(getSharedPreferences("profilephone",0).getString("phone",null));
        temp=(TextView)findViewById(R.id.my_profile_email);
        temp.setText(getSharedPreferences("profileemail",0).getString("email",null));

        ((TextView)findViewById(R.id.id_proof_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.profile_picture_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.personal_info_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.name_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_name)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.age_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_age)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.gender_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_gender)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.experience_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_experience)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.address_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_address)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.qualifications_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.qual_1_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.qual_2_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.qual_3_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_qual_1)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_qual_2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_qual_3)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.contact_info_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.mobile_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_mobile)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.email_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.my_profile_email)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((Button)findViewById(R.id.my_profile_edit)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));
        ((TextView)findViewById(R.id.preferred_addresses_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.pref_add_1)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.pref_add_2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.pref_add_3)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.pref_add_1_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.pref_add_2_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.pref_add_3_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));

    }
    public void onClick(View view)
    {
        int id=view.getId();
        if(id==R.id.my_profile_edit) {
            Intent i = new Intent(this, EditProfileActivity.class);
            startActivity(i);
            finish();
        }else if(id==R.id.edit_profile_pic){
            profileOrId=0;
            selectImage();
        }else if(id==R.id.edit_id_proof){
            profileOrId=1;
            selectImage();
        }else if(id==R.id.save_profile_pic){
        }else if(id==R.id.save_id_proof){
        }else if(id==R.id.edit_profile_from_action_bar){
            Intent i = new Intent(this, EditProfileActivity.class);
            startActivity(i);
            finish();
        }
    }
    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(My_Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if(profileOrId==0){
                    profilePic.setImageBitmap(photo);
                    ((ImageView)findViewById(R.id.profile_pic_not_loaded)).setVisibility(View.INVISIBLE);
                }
                else{
                    idProof.setImageBitmap(photo);
                    ((ImageView)findViewById(R.id.id_proof_pic_not_loaded)).setVisibility(View.INVISIBLE);
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                if(profileOrId==0){
                    profilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    ((ImageView)findViewById(R.id.profile_pic_not_loaded)).setVisibility(View.INVISIBLE);
                }
                else{
                    idProof.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    ((ImageView)findViewById(R.id.id_proof_pic_not_loaded)).setVisibility(View.INVISIBLE);
                }
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
}
