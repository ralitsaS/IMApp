package com.katakiari.contest2;

import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;

public class FriendsList extends Fragment {

	//LocalBroadcastManager mLocalBroadcastManager;
	ListView listview;
    String[] listRoster;
    ArrayAdapter<String> adapter;
    String selectedText;
    View view_frag1;
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_friends_list);
		view_frag1 = inflater.inflate(R.layout.activity_friends_list, container, false);
		
		
		listview = (ListView) view_frag1.findViewById(R.id.list_friends);

		adapter =
	            new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        
        adapter.setNotifyOnChange(true);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
               // Log.d("############","Items " +  MoreItems[arg2] );
            	LayoutInflater layoutInflater = LayoutInflater.from(view_frag1.getContext());
            	View promptView = layoutInflater.inflate(R.layout.prompt, null);

            	selectedText = (String) arg0.getItemAtPosition(arg2);
            	Log.d("selectedText", ""+selectedText);
            	final AlertDialog alertD = new AlertDialog.Builder(view_frag1.getContext()).create();

            	//EditText userInput = (EditText) promptView.findViewById(R.id.userInput);

            	Button btnChat = (Button) promptView.findViewById(R.id.btnAdd1);

            	Button btnDel = (Button) promptView.findViewById(R.id.btnAdd2);

            	btnChat.setOnClickListener(new OnClickListener() {
            	    public void onClick(View v) {

            	    	/*
            	    	Intent start_chat=new Intent("create_chat");
            	    	start_chat.putExtra("user", selectedText);
            	    	mLocalBroadcastManager.sendBroadcast(start_chat);
            	    	*/
            	    	Intent start_a = new Intent(view_frag1.getContext(), PrivateChat.class);
            			start_a.putExtra("user", selectedText);
            			startActivity(start_a);
            			

            	    }
            	});

            	btnDel.setOnClickListener(new OnClickListener() {
            	    public void onClick(View v) {

            	    	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(view_frag1.getContext());
            	    	dlgAlert.setMessage("Are you sure you want to delete "+selectedText+" from your contacts?");
            	    	dlgAlert.setTitle("Delete Contact");
            	    	dlgAlert.setPositiveButton("Ok",
            	    		    new DialogInterface.OnClickListener() {
            	            public void onClick(DialogInterface dialog, int which) {
            	              //dismiss the dialog 
            	            	Intent intent_del = new Intent("del_entry");
                    	    	intent_del.putExtra("user", selectedText);
                    	    	LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent_del);
        		                //adapter.remove(selectedText);
            	            }
            	        });
            	    	dlgAlert.setCancelable(true);
            	    	dlgAlert.create().show();
            	    	
            	    	
            	    	
		                
		                alertD.dismiss();

            	    }
            	});

            	alertD.setView(promptView);

            	alertD.show();
            }

        });
        
        Button add_btn = (Button) view_frag1.findViewById(R.id.button1);
        add_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	LayoutInflater layoutInflater = LayoutInflater.from(view_frag1.getContext());
            	final View promptViewAdd = layoutInflater.inflate(R.layout.prompt_add, null);
            	final AlertDialog alertA = new AlertDialog.Builder(view_frag1.getContext()).create();
            	
            	Button btnAdd = (Button) promptViewAdd.findViewById(R.id.btnAdd);
            	
            	btnAdd.setOnClickListener(new OnClickListener() {
            	    public void onClick(View v) {

            	    	EditText et1 = (EditText) promptViewAdd.findViewById(R.id.editText1);
                    	String nickname =  et1.getText().toString();
                    	EditText et2 = (EditText) promptViewAdd.findViewById(R.id.editText2);
                    	String username =  et2.getText().toString();
                    	//EditText et3 = (EditText) promptViewAdd.findViewById(R.id.editText3);
                    	//String group =  et3.getText().toString();
                    	
                    	Intent intent = new Intent("add_entry");
            			intent.putExtra("jid",username+"@gladosv2");
            			intent.putExtra("nick", nickname);
            			//intent.putExtra("group", group);
            			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        
                        alertA.dismiss();

            	    }
            	});
            	
            	alertA.setView(promptViewAdd);

            	alertA.show();
            }
        });
      
        return view_frag1;
	}

	BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Get extra data included in the Intent
			 ArrayList<String> jids= new ArrayList<String>();
			 ArrayList<String> presences= new ArrayList<String>();
			 ArrayList<String> nicknames= new ArrayList<String>();
		     jids = intent.getStringArrayListExtra("user_j");
		     presences = intent.getStringArrayListExtra("user_p");
		     nicknames = intent.getStringArrayListExtra("user_n");
		     
		     adapter.clear();
		     listRoster = new String[jids.size()];

		     for (int i = 0; i < jids.size(); i++) {
		    	 listRoster[i] = jids.get(i)+"("+nicknames.get(i)+" is "+presences.get(i)+")";
		    	 adapter.add(listRoster[i]);
		     }
		     
		     
		    Log.d("receiver", "Got message: " + jids);
		    Log.d("receiver", "Got message: " + presences);
		    Log.d("receiver", "Got message: " + nicknames);
		  }
		};
		
	
		@Override
		  public void onResume() {

			//mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity().getApplicationContext());
			LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
				      new IntentFilter("user_roster"));
		     super.onResume();
		  }

		  @Override
		  public void onPause() {
			  LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
		     super.onPause();
		  }
		


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
