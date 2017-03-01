package com.katakiari.contest2;

import java.util.ArrayList;
import java.util.Objects;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class GroupChat extends Activity {

	private LocalBroadcastManager mLocalBroadcastManager;
	ListView listview;
	ArrayList<ChatM> listM = new ArrayList<ChatM>();
	ArrayList<String> old_nicks = new ArrayList<String>();
	ArrayList<String> old_messages = new ArrayList<String>();
	MyChatsAdapter adapterC;
	String room_jid;
	String priv;
	ArrayList<String> mem_jids;
	ArrayAdapter<String> mem_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_chat);
		
		Intent intent = getIntent();
		room_jid = intent.getStringExtra("room_jid");
		priv = intent.getStringExtra("priv");
		mem_jids=intent.getStringArrayListExtra("mem_jids");
		old_nicks = intent.getStringArrayListExtra("his_nick");
		old_messages = intent.getStringArrayListExtra("his_mes");
		for (int i = 0; i < old_messages.size(); i++) {
	    	 listM.add(new ChatM(old_nicks.get(i), old_messages.get(i)));
	     }
		
		mem_adapter =
	            new ArrayAdapter<String>(GroupChat.this, android.R.layout.simple_list_item_1);
		
		for(int i=0; i<mem_jids.size(); i++){
			
			mem_adapter.add(mem_jids.get(i));
		}
        
        mem_adapter.setNotifyOnChange(true);
        
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mLocalBroadcastManager.registerReceiver(mMessageReceiver1,
			      new IntentFilter("recv_msg_group"));
		
		listview = (ListView)findViewById(R.id.chat_view);
		
		adapterC = new MyChatsAdapter(GroupChat.this, R.layout.chat_m_row, listM);
        adapterC.setNotifyOnChange(true);
        listview.setAdapter(adapterC);
	}
	
	private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Get extra data included in the Intent
			 String sender=intent.getStringExtra("sender");
			 String msg=intent.getStringExtra("msg");
			 Log.d("msg from ", sender);
			 adapterC.add(new ChatM(sender, msg));
			 
			 
		  }
		};
		
		
		

	public void SendMsg(View view)
	{
		//new ConnectToXmpp().execute();
		EditText et1 = (EditText) findViewById(R.id.editText1);
		String m =  et1.getText().toString();
		adapterC.add( new ChatM("You", m));
		et1.setText("");
		Intent int_send=new Intent("send_msg_group");
		int_send.putExtra("message", m);
		int_send.putExtra("to", room_jid);
		mLocalBroadcastManager.sendBroadcast(int_send);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_chat, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
	    if (Objects.equals("yes", priv))
	    	return true;
	    else 
	    	return false;
	    
	    
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.delete) {
			Intent i=new Intent("del_group");
			mLocalBroadcastManager.sendBroadcast(i);
		}
		if (id == R.id.ban) {
			LayoutInflater layoutInflater = LayoutInflater.from(GroupChat.this);
        	final View promptViewAdd = layoutInflater.inflate(R.layout.group_mems, null);
        	final AlertDialog alertA = new AlertDialog.Builder(GroupChat.this).create();
        	
        	ListView view_mems = (ListView) promptViewAdd.findViewById(R.id.list_members);

    		
            view_mems.setAdapter(mem_adapter);

            view_mems.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                        long arg3) {
                    

                	String sel_mem = (String) arg0.getItemAtPosition(arg2);
                	Intent i=new Intent("ban_member");
                	i.putExtra("ban_jid", sel_mem);
        			mLocalBroadcastManager.sendBroadcast(i);

        			mem_adapter.remove(sel_mem);
        			alertA.dismiss();
                }

            });
        	
        	alertA.setView(promptViewAdd);

        	alertA.show();
			
		}
		if (id == R.id.invite) {
			Intent i=new Intent("invite_member");
			mLocalBroadcastManager.sendBroadcast(i);
		}
		return super.onOptionsItemSelected(item);
	}
}
