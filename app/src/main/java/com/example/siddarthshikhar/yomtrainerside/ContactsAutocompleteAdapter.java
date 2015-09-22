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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Siddarth Shikhar on 7/25/2015.
 */
public class ContactsAutocompleteAdapter extends ArrayAdapter<Contact> implements Filterable {
    Context context;
    LayoutInflater l;
    Contact[] list;
    ArrayList<Contact> resultList;

    public ContactsAutocompleteAdapter(Context context, int resource, Contact[] objects,LayoutInflater l) {
        super(context, resource,objects);
        this.context=context;
        this.list=objects;
        this.l=l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null)
            v=l.inflate(R.layout.contact_autocomplete_view,null);
        TextView autoCompName=(TextView)v.findViewById(R.id.contact_name);
        TextView autoCompPhone=(TextView)v.findViewById(R.id.contact_phone_no);
        autoCompName.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf"));
        autoCompPhone.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf"));
        if(position<resultList.size()){
            autoCompName.setText(resultList.get(position).name);
            autoCompPhone.setText(resultList.get(position).phoneNo);
        }
        return v;
    }
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Contact getItem(int index) {
        return resultList.get(index);
    }
    public ArrayList<Contact> autocomplete(String input) {
        resultList=new ArrayList<Contact>();
        for(int i=0;i<list.length;i++){
            if(list[i]!=null && list[i].name.substring(0, input.length()).equalsIgnoreCase(input)){
                resultList.add(list[i]);
            }
        }
        int x=0;
        x=x+2;
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
