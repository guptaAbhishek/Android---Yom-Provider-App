package com.example.siddarthshikhar.yomtrainerside;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EditProfileActivity extends ActionBarActivity implements YearPickerDialog.yearPicked, setDateFragment.datePicked, GenderPickerDialog.GenderDialogTaskDone, AdapterView.OnItemClickListener {
    Spinner experienceYearSpinner;
    String[] experienceYearList={"0","1","2","3","4","5","6","7","8","9","10+"};
    Spinner experienceMonthSpinner;
    String[] experienceMonthList={"0","1","2","3","4","5","6","7","8","9","10","11","12"};


    int qual1Present,qual2Present,qual3Present;
    FrameLayout qual1,qual2,qual3;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    MaterialEditText name,age,phone,email,gender;

    int year,month,day;

    String[] institutesList={"Bihar School of Yoga","S-Vyasa","Patanjali Yog Peeth","Kaivalyadhama","Morarji Desai (MDNIY)","DSVV",
            "Parmath Niketan","Bharathidasan University","Gurukul Kangri Vishwavidyalaya","Jiwaji University","Karpagam University",
            "M.P. Bhoj Open University","Madurai Kamaraj University","Nalanda Open University","Periyar University, Salem",
            "Pt. Sunderlal Sharma Open University","Rashtriya Sanskrit Vidyapeeth, Tirupati", "Tamil University, Thanjavur",
            "U.P. Rajarshi Tandon Open University","University of Madras, Chennai","Vinayaka Mission's University",
            "School of Santhi Yoga Teacher Training"};
    String[] courseTypeList={"Full Time","Part Time","Distance Learning Program","Executive Program"};
    String[] degreeType={"B.Sc","B.A","M.Sc","M.A","Diploma","Certificate","TTC(Teacher's Training Course)","Phd"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar bar=getSupportActionBar();
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.actionbarview, null);
        bar.setCustomView(v);
        TextView title=(TextView)v.findViewById(R.id.actbartitle);
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        title.setText("Edit Profile");
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));

        QualificationsCustomAdapter instituteAdapter=new QualificationsCustomAdapter(this,0,institutesList,getLayoutInflater());
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_institute)).setThreshold(1);
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_institute)).setAdapter(instituteAdapter);
        ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_institute)).setThreshold(1);
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_institute)).setAdapter(instituteAdapter);
        ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_institute)).setThreshold(1);
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_institute)).setAdapter(instituteAdapter);

//        QualificationsCustomAdapter courseTypeAdapter=new QualificationsCustomAdapter(this,0,courseTypeList,getLayoutInflater());
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_course_type)).setThreshold(0);
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_course_type)).setAdapter(courseTypeAdapter);
//        ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_course_type)).setThreshold(0);
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_course_type)).setAdapter(courseTypeAdapter);
//        ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_course_type)).setThreshold(0);
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_course_type)).setAdapter(courseTypeAdapter);
//
//        QualificationsCustomAdapter degreeAdapter=new QualificationsCustomAdapter(this,0,degreeType,getLayoutInflater());
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_degree)).setThreshold(0);
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_degree)).setAdapter(degreeAdapter);
//        ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_degree)).setThreshold(0);
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_degree)).setAdapter(degreeAdapter);
//        ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_degree)).setThreshold(0);
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_degree)).setAdapter(degreeAdapter);

        year = 1980;
        month = 0;
        day = 1;

        name=(MaterialEditText)findViewById(R.id.edit_profile_name);
        name.setText(getSharedPreferences("profilename", 0).getString("name", null));
        age=(MaterialEditText)findViewById(R.id.edit_profile_age);
        age.setText(getSharedPreferences("profileage", 0).getString("age", null));
        gender=(MaterialEditText)findViewById(R.id.edit_profile_gender);
        phone=(MaterialEditText)findViewById(R.id.edit_profile_phone);
        phone.setText(getSharedPreferences("profilephone", 0).getString("phone", null));
        email=(MaterialEditText)findViewById(R.id.edit_profile_email);
        email.setText(getSharedPreferences("profileemail", 0).getString("email", null));

        age.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    age.clearFocus();
                    DialogFragment newFragment = new setDateFragment();
                    setDateFragment temp = (setDateFragment) newFragment;
                    temp.listener = EditProfileActivity.this;
                    temp.presetDay = day;
                    temp.presetMonth = month;
                    temp.presetYear = year;
                    newFragment.show(getFragmentManager(), "DatePicker");
                }
                return false;
            }
        });
        gender.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    gender.clearFocus();
                    GenderPickerDialog dialog = new GenderPickerDialog();
                    dialog.listener = EditProfileActivity.this;
                    dialog.show(getFragmentManager(), "");
                }
                return false;
            }
        });
        if(getSharedPreferences("profilegender", 0).getString("gender", null).equals("Male"))
            gender.setText("Male");
        else if(getSharedPreferences("profilegender", 0).getString("gender", null).equals("Female"))
            gender.setText("Female");

        experienceYearSpinner = (Spinner) findViewById(R.id.edit_profile_experience_y);
        SpinnerAdapter experienceYearAdapter = new SpinnerAdapter(this,0,experienceYearList,getLayoutInflater());
        experienceYearAdapter.setDropDownViewResource(R.layout.spinner_individual_item);
        experienceYearSpinner.setAdapter(experienceYearAdapter);

        experienceMonthSpinner = (Spinner) findViewById(R.id.edit_profile_experience);
        SpinnerAdapter experienceMonthAdapter = new SpinnerAdapter(this,0,experienceMonthList,getLayoutInflater());
        experienceMonthAdapter.setDropDownViewResource(R.layout.spinner_individual_item);
        experienceMonthSpinner.setAdapter(experienceMonthAdapter);

        SpinnerAdapter degreeAdapter = new SpinnerAdapter(this,0,degreeType,getLayoutInflater());
        degreeAdapter.setDropDownViewResource(R.layout.spinner_individual_item);
        ((Spinner) findViewById(R.id.qual_1_degree)).setAdapter(degreeAdapter);
        ((Spinner) findViewById(R.id.qual_2_degree)).setAdapter(degreeAdapter);
        ((Spinner) findViewById(R.id.qual_3_degree)).setAdapter(degreeAdapter);

        SpinnerAdapter courseTypeAdapter = new SpinnerAdapter(this,0,courseTypeList,getLayoutInflater());
        degreeAdapter.setDropDownViewResource(R.layout.spinner_individual_item);
        ((Spinner) findViewById(R.id.qual_1_course_type)).setAdapter(courseTypeAdapter);
        ((Spinner) findViewById(R.id.qual_2_course_type)).setAdapter(courseTypeAdapter);
        ((Spinner) findViewById(R.id.qual_3_course_type)).setAdapter(courseTypeAdapter);

        qual1Present=0;
        qual2Present=0;
        qual3Present=0;
        qual1=(FrameLayout)findViewById(R.id.qual_1_outer);
        qual2=(FrameLayout)findViewById(R.id.qual_2_outer);
        qual3=(FrameLayout)findViewById(R.id.qual_3_outer);
        ((MaterialEditText) findViewById(R.id.qual_1_from)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new YearPickerDialog();
                    YearPickerDialog temp = (YearPickerDialog) newFragment;
                    temp.listener = EditProfileActivity.this;
                    temp.qualificationNo = 1;
                    temp.fromOrTo = 0;
                    newFragment.show(getFragmentManager(), "DatePicker");
                }
                return false;
            }
        });
        ((MaterialEditText) findViewById(R.id.qual_1_to)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new YearPickerDialog();
                    YearPickerDialog temp = (YearPickerDialog) newFragment;
                    temp.listener = EditProfileActivity.this;
                    temp.qualificationNo = 1;
                    temp.fromOrTo = 1;
                    newFragment.show(getFragmentManager(), "DatePicker");
                }
                return false;
            }
        });
        ((MaterialEditText) findViewById(R.id.qual_2_from)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new YearPickerDialog();
                    YearPickerDialog temp = (YearPickerDialog) newFragment;
                    temp.listener = EditProfileActivity.this;
                    temp.qualificationNo = 2;
                    temp.fromOrTo = 0;
                    newFragment.show(getFragmentManager(), "DatePicker");
                }
                return false;
            }
        });
        ((MaterialEditText) findViewById(R.id.qual_2_to)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new YearPickerDialog();
                    YearPickerDialog temp = (YearPickerDialog) newFragment;
                    temp.listener = EditProfileActivity.this;
                    temp.qualificationNo = 2;
                    temp.fromOrTo = 1;
                    newFragment.show(getFragmentManager(), "DatePicker");
                }
                return false;
            }
        });
        ((MaterialEditText) findViewById(R.id.qual_3_from)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new YearPickerDialog();
                    YearPickerDialog temp = (YearPickerDialog) newFragment;
                    temp.listener = EditProfileActivity.this;
                    temp.qualificationNo = 3;
                    temp.fromOrTo = 0;
                    newFragment.show(getFragmentManager(), "DatePicker");
                }
                return false;
            }
        });
        ((MaterialEditText) findViewById(R.id.qual_3_to)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new YearPickerDialog();
                    YearPickerDialog temp = (YearPickerDialog) newFragment;
                    temp.listener = EditProfileActivity.this;
                    temp.qualificationNo = 3;
                    temp.fromOrTo = 1;
                    newFragment.show(getFragmentManager(), "DatePicker");
                }
                return false;
            }
        });

        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_address)).setAdapter(new GooglePlacesAutocompleteAdapter(this, 0, getLayoutInflater()));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_address)).setOnItemClickListener(this);
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_1)).setAdapter(new GooglePlacesAutocompleteAdapter(this, 0, getLayoutInflater()));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_1)).setOnItemClickListener(this);
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_2)).setAdapter(new GooglePlacesAutocompleteAdapter(this, 0, getLayoutInflater()));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_2)).setOnItemClickListener(this);
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_3)).setAdapter(new GooglePlacesAutocompleteAdapter(this, 0, getLayoutInflater()));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_3)).setOnItemClickListener(this);


        ((MaterialEditText) findViewById(R.id.edit_profile_name)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_name)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_age)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_age)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_gender)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_gender)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_phone)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_phone)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_email)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.edit_profile_email)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.personal_info_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.contact_info_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.qual_yrs_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.qual_mths_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.experience_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_1)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_1)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_2)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_3)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_pref_address_3)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_address)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.edit_profile_address)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.preferred_addresses_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.qualifications_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView) findViewById(R.id.qual_1_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView) findViewById(R.id.qual_2_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView) findViewById(R.id.qual_3_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_1_from)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_1_from)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_institute)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_institute)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_1_to)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_1_to)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_degree)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_degree)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_course_type)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_1_course_type)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_2_from)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_2_from)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_2_to)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_2_to)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_institute)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regulars.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_institute)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_degree)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_degree)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_course_type)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_2_course_type)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_3_from)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_3_from)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_institute)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regulars.ttf"));
        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_institute)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_3_to)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((MaterialEditText) findViewById(R.id.qual_3_to)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_degree)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_degree)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_course_type)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
//        ((MaterialAutoCompleteTextView) findViewById(R.id.qual_3_course_type)).setAccentTypeface(Typeface.createFromAsset(getAssets(), "fonts/Los Angeleno Sans.ttf"));
        ((TextView)findViewById(R.id.add_qual_1_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.add_qual_2_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.remove_qual_1_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.remove_qual_2_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((TextView)findViewById(R.id.remove_qual_3_label)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((Button)findViewById(R.id.edit_profile_save)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        ((Button)findViewById(R.id.edit_profile_discard)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
    }
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.edit_profile_save) {
            sp = getSharedPreferences("profilename", 0);
            editor = sp.edit();
            editor.putString("name", name.getText().toString());
            editor.commit();
            sp = getSharedPreferences("profileage", 0);
            editor = sp.edit();
            editor.putString("age", age.getText().toString());
            editor.commit();
            sp=getSharedPreferences("profilegender", 0);
            editor=sp.edit();
            editor.putString("gender",gender.getText().toString());
            editor.commit();
//            sp = getSharedPreferences("profilequalification", 0);
//            editor = sp.edit();
//            editor.putString("qualification", qualification.getText().toString());
//            editor.commit();
//            sp = getSharedPreferences("profileexperience_y", 0);
//            editor = sp.edit();
//            editor.putString("experience_y", experience_y.getText().toString());
//            editor.commit();
//            sp = getSharedPreferences("profileexperience", 0);
//            editor = sp.edit();
//            editor.putString("experience", experience.getText().toString());
//            editor.commit();
            sp = getSharedPreferences("profilephone", 0);
            editor = sp.edit();
            editor.putString("phone", phone.getText().toString());
            editor.commit();
            sp = getSharedPreferences("profileemail", 0);
            editor = sp.edit();
            editor.putString("email", email.getText().toString());
            editor.commit();
            Toast.makeText(getApplicationContext(), "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,MainUserPanel.class);
            startActivity(i);
            finish();
        }else if(id==R.id.add_qual_0){
            qual1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 700));
            ((FrameLayout)findViewById(R.id.add_qual_0)).setVisibility(View.INVISIBLE);
            ((FrameLayout)findViewById(R.id.add_qual_0)).setEnabled(false);
            qual1Present=1;
            changeAddButtonVisibility();
        } else if(id==R.id.add_qual_1){
            qual2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 700));
            qual2Present=1;
            changeAddButtonVisibility();
        } else if(id==R.id.add_qual_2){
            qual3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,700));
            qual3Present=1;
            changeAddButtonVisibility();
        }else if(id==R.id.remove_qual_3){
            qual3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,0));
            ((MaterialEditText)findViewById(R.id.qual_3_from)).setText("");
            ((MaterialEditText)findViewById(R.id.qual_3_to)).setText("");
            ((Spinner)findViewById(R.id.qual_3_degree)).setSelection(0);
            ((Spinner)findViewById(R.id.qual_3_course_type)).setSelection(0);
//            ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_degree)).setText("");
//            ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_course_type)).setText("");
            ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_institute)).setText("");
            qual3Present=0;
            changeAddButtonVisibility();
        }else if(id==R.id.remove_qual_2){
            if(qual3Present==1){
                qual3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,0));
                ((MaterialEditText)findViewById(R.id.qual_2_from)).setText(((MaterialEditText) findViewById(R.id.qual_3_from)).getText());
                ((MaterialEditText)findViewById(R.id.qual_2_to)).setText(((MaterialEditText)findViewById(R.id.qual_3_to)).getText());
                ((Spinner)findViewById(R.id.qual_2_degree)).setSelection(((Spinner) findViewById(R.id.qual_3_degree)).getSelectedItemPosition());
                ((Spinner)findViewById(R.id.qual_2_course_type)).setSelection(((Spinner) findViewById(R.id.qual_3_course_type)).getSelectedItemPosition());
                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_institute)).setText(((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_institute)).getText());
                ((MaterialEditText)findViewById(R.id.qual_3_from)).setText("");
                ((MaterialEditText)findViewById(R.id.qual_3_to)).setText("");
                ((Spinner)findViewById(R.id.qual_3_degree)).setSelection(0);
                ((Spinner)findViewById(R.id.qual_3_course_type)).setSelection(0);
                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_institute)).setText("");
                qual3Present=0;
            }else{
                qual2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,0));
                ((MaterialEditText)findViewById(R.id.qual_2_from)).setText("");
                ((MaterialEditText)findViewById(R.id.qual_2_to)).setText("");
                ((Spinner)findViewById(R.id.qual_2_degree)).setSelection(0);
                ((Spinner)findViewById(R.id.qual_2_course_type)).setSelection(0);
//                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_degree)).setText("");
//                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_course_type)).setText("");
                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_institute)).setText("");
                qual2Present=0;
            }
            changeAddButtonVisibility();
        }else if(id==R.id.remove_qual_1){
            if(qual2Present==1){
                ((MaterialEditText)findViewById(R.id.qual_1_from)).setText(((MaterialEditText)findViewById(R.id.qual_2_from)).getText());
                ((MaterialEditText)findViewById(R.id.qual_1_to)).setText(((MaterialEditText) findViewById(R.id.qual_2_to)).getText());
                ((Spinner)findViewById(R.id.qual_1_degree)).setSelection(((Spinner)findViewById(R.id.qual_2_degree)).getSelectedItemPosition());
                ((Spinner)findViewById(R.id.qual_1_course_type)).setSelection(((Spinner) findViewById(R.id.qual_2_course_type)).getSelectedItemPosition());
                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_1_institute)).setText(((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_institute)).getText());
                if(qual3Present==0){
                    qual2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,0));
                    ((MaterialEditText)findViewById(R.id.qual_2_from)).setText("");
                    ((MaterialEditText)findViewById(R.id.qual_2_to)).setText("");
                    ((Spinner)findViewById(R.id.qual_2_degree)).setSelection(0);
                    ((Spinner)findViewById(R.id.qual_2_course_type)).setSelection(0);
//                    ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_degree)).setText("");
//                    ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_course_type)).setText("");
                    ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_institute)).setText("");
                    qual2Present=0;
                }else{
                    qual3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,0));
                    ((MaterialEditText)findViewById(R.id.qual_2_from)).setText(((MaterialEditText) findViewById(R.id.qual_3_from)).getText());
                    ((MaterialEditText)findViewById(R.id.qual_2_to)).setText(((MaterialEditText) findViewById(R.id.qual_3_to)).getText());
                    ((Spinner)findViewById(R.id.qual_2_degree)).setSelection(((Spinner) findViewById(R.id.qual_3_degree)).getSelectedItemPosition());
                    ((Spinner)findViewById(R.id.qual_2_course_type)).setSelection(((Spinner) findViewById(R.id.qual_3_course_type)).getSelectedItemPosition());
                    ((MaterialAutoCompleteTextView)findViewById(R.id.qual_2_institute)).setText(((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_institute)).getText());
                    ((MaterialEditText)findViewById(R.id.qual_3_from)).setText("");
                    ((MaterialEditText)findViewById(R.id.qual_3_to)).setText("");
                    ((Spinner)findViewById(R.id.qual_3_degree)).setSelection(0);
                    ((Spinner)findViewById(R.id.qual_3_course_type)).setSelection(0);
//                    ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_degree)).setText("");
//                    ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_course_type)).setText("");
                    ((MaterialAutoCompleteTextView)findViewById(R.id.qual_3_institute)).setText("");
                    qual3Present=0;
                }
                changeAddButtonVisibility();
            }else{
                qual1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,0));
                ((MaterialEditText)findViewById(R.id.qual_1_from)).setText("");
                ((MaterialEditText)findViewById(R.id.qual_1_to)).setText("");
                ((Spinner)findViewById(R.id.qual_1_degree)).setSelection(0);
                ((Spinner)findViewById(R.id.qual_1_course_type)).setSelection(0);
//                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_1_degree)).setText("");
//                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_1_course_type)).setText("");
                ((MaterialAutoCompleteTextView)findViewById(R.id.qual_1_institute)).setText("");
                qual1Present=0;
                ((FrameLayout)findViewById(R.id.add_qual_0)).setVisibility(View.VISIBLE);
                ((FrameLayout)findViewById(R.id.add_qual_0)).setEnabled(true);
            }
        }else if(id==R.id.edit_profile_discard){
            Intent i = new Intent(this,MainUserPanel.class);
            startActivity(i);
            finish();
        }
    }
    public void changeAddButtonVisibility(){
        ((FrameLayout)findViewById(R.id.add_qual_1)).setVisibility(View.INVISIBLE);
        ((FrameLayout)findViewById(R.id.add_qual_1)).setEnabled(false);
        ((FrameLayout)findViewById(R.id.add_qual_2)).setVisibility(View.INVISIBLE);
        ((FrameLayout)findViewById(R.id.add_qual_2)).setEnabled(false);
        if(qual3Present==1){
        }else if(qual2Present==1){
            ((FrameLayout)findViewById(R.id.add_qual_2)).setVisibility(View.VISIBLE);
            ((FrameLayout)findViewById(R.id.add_qual_2)).setEnabled(true);
        }else if(qual1Present==1){
            ((FrameLayout)findViewById(R.id.add_qual_1)).setVisibility(View.VISIBLE);
            ((FrameLayout)findViewById(R.id.add_qual_1)).setEnabled(true);
        }
    }
    @Override
    public void populateSetYear(int year, int qualificationNo, int fromOrTo) {
        if(qualificationNo==1 && fromOrTo==0)
            ((MaterialEditText)findViewById(R.id.qual_1_from)).setText(""+year);
        else if(qualificationNo==1 && fromOrTo==1)
            ((MaterialEditText)findViewById(R.id.qual_1_to)).setText(""+year);
        else if(qualificationNo==2 && fromOrTo==0)
            ((MaterialEditText)findViewById(R.id.qual_2_from)).setText(""+year);
        else if(qualificationNo==2 && fromOrTo==1)
            ((MaterialEditText)findViewById(R.id.qual_2_to)).setText(""+year);
        else if(qualificationNo==3 && fromOrTo==0)
            ((MaterialEditText)findViewById(R.id.qual_3_from)).setText(""+year);
        else if(qualificationNo==3 && fromOrTo==1)
            ((MaterialEditText)findViewById(R.id.qual_3_to)).setText(""+year);
    }

    @Override
    public void populateSetDate(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        this.year=year;
        month=monthOfYear;
        day=dayOfMonth;
        age.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
    }

    @Override
    public void genderDialogEnded(String genderPicked) {
        gender.setText(genderPicked);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}