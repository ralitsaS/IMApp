package com.katakiari.contest2;
 
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
 
public class ChatRooms extends Fragment {
 
	ListView listviewR;
	ArrayList<Rooms> listR = new ArrayList<Rooms>();
    MyRoomsAdapter adapterR;
    View view_frag2;
    String selectedRoom;
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		view_frag2 = inflater.inflate(R.layout.chat_rooms, container, false);
		
		listviewR = (ListView) view_frag2.findViewById(R.id.list_rooms);

		//adapterR =
	     //       new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        listR.add(new Rooms("room1", "room2", "room2"));
		adapterR = new MyRoomsAdapter(getActivity(), R.layout.roomslist_row, listR);
        adapterR.setNotifyOnChange(true);
        listviewR.setAdapter(adapterR);
        
        listviewR.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
            	Rooms selected_room= (Rooms) arg0.getItemAtPosition(arg2);
            	String srj= selected_room.getJID();
            	
            	Intent intent = new Intent("enter_room");
    			intent.putExtra("proom_jid",srj);
    			//intent.putExtra("subject", subject);
    			//intent.putExtra("description", desc);
    			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    			
    			/*
            	Intent start_a = new Intent(view_frag2.getContext(), PublicChat.class);
    			start_a.putExtra("proom_jid", srj);
    			startActivity(start_a);
    			*/
            }

        });
        
        Button add_btnR = (Button) view_frag2.findViewById(R.id.button1);
        add_btnR.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	LayoutInflater layoutInflater = LayoutInflater.from(view_frag2.getContext());
            	final View promptViewAdd = layoutInflater.inflate(R.layout.prompt_create, null);
            	final AlertDialog alertA = new AlertDialog.Builder(view_frag2.getContext()).create();
            	
            	Button btnAddR = (Button) promptViewAdd.findViewById(R.id.btnAddR);
            	
            	btnAddR.setOnClickListener(new OnClickListener() {
            	    public void onClick(View v) {

            	    	EditText et1 = (EditText) promptViewAdd.findViewById(R.id.editText1);
                    	String name =  et1.getText().toString();
                    	EditText et2 = (EditText) promptViewAdd.findViewById(R.id.editText2);
                    	String subject =  et2.getText().toString();
                    	EditText et3 = (EditText) promptViewAdd.findViewById(R.id.editText3);
                    	String desc =  et3.getText().toString();
                    	
                    	Intent intent = new Intent("create_room");
            			intent.putExtra("roomname",name);
            			intent.putExtra("subject", subject);
            			intent.putExtra("description", desc);
            			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        
                        alertA.dismiss();

            	    }
            	});
            	
            	alertA.setView(promptViewAdd);

            	alertA.show();
            }
        });
        
		return view_frag2;
	}
	
	BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Get extra data included in the Intent
			 ArrayList<String> rjids= new ArrayList<String>();
			 ArrayList<String> rsubs= new ArrayList<String>();
			 ArrayList<String> rdescs= new ArrayList<String>();
			 rjids = intent.getStringArrayListExtra("rooms_j");
			 rsubs = intent.getStringArrayListExtra("rooms_s");
			 rdescs = intent.getStringArrayListExtra("rooms_d");
		     
		     adapterR.clear();

		     for (int i = 0; i < rjids.size(); i++) {
		    	 adapterR.add(new Rooms(rjids.get(i), rsubs.get(i), rdescs.get(i)));
		     }
		     
		     
		    Log.d("receiver", "Got message: " + rjids);
		  }
		};
		
	
		@Override
		  public void onResume() {

			//mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity().getApplicationContext());
			LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
				      new IntentFilter("public_rooms"));
		     super.onResume();
		  }

		  @Override
		  public void onPause() {
			  LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
		     super.onPause();
		  }
 
}