package com.example.siddarthshikhar.yomtrainerside;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siddarthshikhar.yomtrainerside.chat.Chat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Abhishek on 22-Sep-15.
 */
public class AvailableClassesRecycleAdapter extends RecyclerView.Adapter<CustomViewHolder> implements PostRequestAsyncTask.RequestDoneTaskListener{
    private List<GetConsumer> getConsumerList;
    private Context mContext;


    public AvailableClassesRecycleAdapter(Context context,List<GetConsumer> getConsumerList){
        this.getConsumerList = getConsumerList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.consumer_list_row,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position){
        final GetConsumer getConsumer = getConsumerList.get(position);

        customViewHolder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:01143583720"));
                v.getContext().startActivity(phoneIntent);
            }
        });

        customViewHolder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
                SharedPreferences phone = v.getContext().getSharedPreferences("profilephone", 0);
                SharedPreferences authKey = v.getContext().getSharedPreferences("Authorization", 0);
                String phoneStr = phone.getString("phone",null);
                String authorization = authKey.getString("Auth_key",null);

                JSONObject toBePosted=new JSONObject();
                try{
                    toBePosted.put("phone",phoneStr);
                    toBePosted.put("authKey",authorization);
                    toBePosted.put("map_status","accepted");
                    toBePosted.put("con_phone",getConsumer.getConsumerPhone());
                    toBePosted.put("schedule_id",getConsumer.getScheduleId());
                }catch (JSONException e){

                }
                PostRequestAsyncTask postRequestAsyncTask = new PostRequestAsyncTask();
                postRequestAsyncTask.toBePosted=toBePosted;
                postRequestAsyncTask.listener = ;
                postRequestAsyncTask.execute("http://" + Constants.IP_ADDRESS + "updaterequest");
                Log.v("Data To be Posted", toBePosted.toString());
                Toast.makeText(v.getContext(),
                        "Moved to favourites", Toast.LENGTH_SHORT).show();
            }
        });

        customViewHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntnet = new Intent(android.content.Intent.ACTION_SEND);
                shareIntnet.setType("text/plain");
                shareIntnet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                //shareIntnet.putExtra(Intent.EXTRA_SUBJECT,getConsumer.getConsumerName());
                String info = getConsumer.getConsumerName()+ getConsumer.getConsumerAddress()+" Kindly Download the App from http://play.google.com";
                shareIntnet.putExtra(Intent.EXTRA_TEXT, info);
                v.getContext().startActivity(Intent.createChooser(shareIntnet, "Share Consumer!"));
            }
        });

        customViewHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
                Toast.makeText(v.getContext(), "Removed", Toast.LENGTH_SHORT).show();

            }
        });

        customViewHolder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(v.getContext(), Chat.class);
                v.getContext().startActivity(chatIntent);
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getPosition();

                Intent consumerDetailIntent = new Intent(v.getContext(),ConsumerProfileComplete.class);
                consumerDetailIntent.putExtra("name", getConsumer.getConsumerName());
                v.getContext().startActivity(consumerDetailIntent);
            }
        };

        customViewHolder.consumerName.setText(Html.fromHtml(getConsumer.getConsumerName()));
        //customViewHolder.consumerGender.setText(Html.fromHtml(getConsumer.getConsumerGender()));
        customViewHolder.consumerAddress.setText(Html.fromHtml(getConsumer.getConsumerAddress()));

        //Handle click event on both title and image click
        customViewHolder.consumerName.setOnClickListener(clickListener);
        customViewHolder.consumerAddress.setOnClickListener(clickListener);

        customViewHolder.consumerName.setTag(customViewHolder);
        customViewHolder.consumerAddress.setTag(customViewHolder);

    }

    @Override
    public int getItemCount() {
        return (null !=  getConsumerList ? getConsumerList.size() : 0);
    }

    public void removeAt(int position){
        getConsumerList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getConsumerList.size());
    }

    @Override
    public void processEnquiries(Boolean ifExecuted, String output, int typeOfError) {

    }
}


