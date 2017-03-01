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

public class MyChatsAdapter extends ArrayAdapter<ChatM> {
    private final Activity activity;
    private final ArrayList<ChatM> list;

    public MyChatsAdapter(Activity activity, int resource, ArrayList<ChatM> list)
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
            rowView = inflater.inflate(R.layout.chat_m_row, null);

            view = new ViewHolder();
            view.cm_sender= (TextView) rowView.findViewById(R.id.textView2);
            //view.cm_time= (TextView) rowView.findViewById(R.id.textView1);
            view.cm_message= (TextView) rowView.findViewById(R.id.textView3);

            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();
        }

        /** Set data to your Views. */
        ChatM item = (ChatM)list.get(position);
        view.cm_sender.setText(item.getSENDER());
        //view.cm_time.setText(item.getTIME());
        view.cm_message.setText(item.getMES());

        return rowView;
    }

    protected static class ViewHolder{
        protected TextView cm_sender;
        //protected TextView cm_time;
        protected TextView cm_message;
    }
}