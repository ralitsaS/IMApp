package com.katakiari.contest2;

import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyRoomsAdapter extends ArrayAdapter<Rooms> {
    private final Activity activity;
    private final ArrayList<Rooms> list;

    public MyRoomsAdapter(Activity activity, int resource, ArrayList<Rooms> list)
    {
    	super(activity, resource, list);
    	this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder view;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.roomslist_row, null);

            view = new ViewHolder();
            view.room_jid= (TextView) rowView.findViewById(R.id.textView1);
            view.room_subject= (TextView) rowView.findViewById(R.id.textView2);
            view.room_desc= (TextView) rowView.findViewById(R.id.textView3);

            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();
        }

        /** Set data to your Views. */
        Rooms item = (Rooms)list.get(position);
        view.room_jid.setText(item.getJID());
        view.room_subject.setText(item.getSUB());
        view.room_desc.setText(item.getDES());

        return rowView;
    }

    protected static class ViewHolder{
        protected TextView room_jid;
        protected TextView room_subject;
        protected TextView room_desc;
    }
}