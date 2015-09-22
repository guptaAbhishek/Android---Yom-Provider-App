package com.example.siddarthshikhar.yomtrainerside;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Siddarth Shikhar on 7/22/2015.
 */
public class QualificationsCustomAdapter extends ArrayAdapter<String> implements Filterable {
    Context context;
    LayoutInflater l;
    String[] list;
    ArrayList<String> resultList;

    public QualificationsCustomAdapter(Context context, int resource, String[] objects,LayoutInflater l) {
        super(context, resource,objects);
        this.context=context;
        this.list=objects;
        this.l=l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null)
            v=l.inflate(R.layout.autocomplete_individual_item,null);
        TextView autoCompItem=(TextView)v.findViewById(R.id.autocomplete_individual_text);
        autoCompItem.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf"));
        if(position<resultList.size())
            autoCompItem.setText(resultList.get(position));
        return v;
    }
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }
    public ArrayList<String> autocomplete(String input) {
        resultList=new ArrayList<String >();
        for(int i=0;i<list.length;i++){
            if(list[i].substring(0,input.length()).equalsIgnoreCase(input)){
                resultList.add(list[i]);
            }
        }
        return resultList;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
