package com.example.admin.firebsaseuploadd;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 16/12/2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<Upload> {
        CustomArrayAdapter(Context context, ArrayList<Upload> arrayList) {
            super(context, 0, arrayList);
        }




        @NonNull
    @Override
    public View getView(int position, @Nullable View listItemView, @NonNull ViewGroup parent) {
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView firstTextView = listItemView.findViewById(R.id.first_text_view);
        TextView secondTextView = listItemView.findViewById(R.id.second_text_view);
        TextView dateview=listItemView.findViewById(R.id.dateview);

        Upload upload = getItem(position);
        if (upload != null) {
            firstTextView.setText(upload.getName());
            secondTextView.setText(upload.getAr());
            dateview.setText(upload.getDate());

        }


        return listItemView;
    }


}