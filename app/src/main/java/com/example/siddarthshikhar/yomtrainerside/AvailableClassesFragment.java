package com.example.siddarthshikhar.yomtrainerside;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Abhishek on 22-Sep-15.
 */
public class AvailableClassesRecycleAdapter extends RecyclerView.Adapter<CustomViewHolder> {
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
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {
        GetConsumer getConsumer = getConsumerList.get(position);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getPosition();
                GetConsumer feedItem = getConsumerList.get(position);
                Intent consumerDetailIntent = new Intent(v.getContext(),ConsumerProfileComplete.class);
                consumerDetailIntent.putExtra("name",feedItem.getConsumerName());
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
}


