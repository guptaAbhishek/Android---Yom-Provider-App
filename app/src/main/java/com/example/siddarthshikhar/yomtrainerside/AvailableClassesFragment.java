package com.example.siddarthshikhar.yomtrainerside;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AvailableClassesFragment extends Fragment implements CustomErrorDialog.ErrorDialogTaskDone,PostRequestAsyncTask.RequestDoneTaskListener{
    Context context;
    private List<GetConsumer> getConsumerList;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    String output;
    @Override
    public void DialogEnded(String dialogString) {

    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected AvailableClassesRecycleAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private ProgressBar progressBar;

    public AvailableClassesFragment(){
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_available_classes, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        SharedPreferences phone = getActivity().getSharedPreferences("profilephone", 0);
        SharedPreferences authKey = getActivity().getSharedPreferences("Authorization", 0);
        String phoneStr = phone.getString("phone",null);
        String authorization = authKey.getString("Auth_key",null);

        JSONObject toBePosted=new JSONObject();
        try{
            toBePosted.put("phone",phoneStr);
            toBePosted.put("authKey",authorization);

        }catch (JSONException e){

        }

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        PostRequestAsyncTask postRequestAsyncTask = new PostRequestAsyncTask();
        postRequestAsyncTask.toBePosted=toBePosted;
        postRequestAsyncTask.listener = this;
        postRequestAsyncTask.execute("http://" + Constants.IP_ADDRESS + "getupcomingclass");

        mAdapter = new AvailableClassesRecycleAdapter(context,getConsumerList);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        return rootView;
    }


    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }

    ProgressDialog dlg;
    @Override
    public void processEnquiries(Boolean ifExecuted, String output, int typeOfError) {
        android.app.FragmentManager fm = getActivity().getFragmentManager();
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
                try{
                    JSONArray consumerArr= new JSONArray(output);
                    getConsumerList = new ArrayList<>();
                    for(int i=0;i<consumerArr.length();i++){
                        JSONObject consumerObj = consumerArr.optJSONObject(i);
                        GetConsumer getConsumer = new GetConsumer();
                        getConsumer.setConsumerName(consumerObj.optString("NAME"));
                        getConsumer.setConsumerAddress(consumerObj.optString("ADDRESS"));
                        getConsumerList.add(getConsumer);
                    }
                }catch (JSONException e){

                }
                Log.v("List",getConsumerList.toString());
                mAdapter = new AvailableClassesRecycleAdapter(context,getConsumerList);
                // Set CustomAdapter as the adapter for RecyclerView.
                mRecyclerView.setAdapter(mAdapter);
                dialog.dialogString="Schedule Has been successfully Updated";
                dialog.show(fm, "");
//                dialog.dialogString=output;
//                dialog.show(fm,output);


            }else{
                CustomErrorDialog dialog=new CustomErrorDialog();
                dialog.listener=this;
                dialog.dialogString="We are experiencing some Technical difficulties.Please contact us.";
                dialog.show(fm, "");
//                dialog.dialogString=output;
//                dialog.show(fm,output);
            }
        }else{
            CustomErrorDialog dialog=new CustomErrorDialog();
            dialog.listener=this;
            if(typeOfError==1){
                dialog.dialogString="No Internet Connection!";
                dialog.show(fm, "");
            }
            else{
                dialog.dialogString="Oops! Server Error! Please try after some time";
                dialog.show(fm, "");
//                dialog.dialogString=output;
//                dialog.show(fm,output);
            }
        }
    }


}