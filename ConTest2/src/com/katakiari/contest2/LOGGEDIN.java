package com.katakiari.contest2;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

public class LOGGEDIN extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loggedin);
		
		

	}

	

	public void XMPP(View view)
	{
		
		//new ConnectToXmpp().execute();
		EditText et1 = (EditText) findViewById(R.id.editText1);
		String user =  et1.getText().toString();
		EditText et2 = (EditText) findViewById(R.id.editText2);
		String pass =  et2.getText().toString();
		
		//startService(new Intent(this, CTService.class));
		Intent intent = new Intent(LOGGEDIN.this, CTService.class);
		intent.putExtra("user", user);
		intent.putExtra("pass", pass);
		startService(intent);
		
		//Toast.makeText(LOGGEDIN.this, "Logging in...", Toast.LENGTH_SHORT).show();
		Intent start_a = new Intent(LOGGEDIN.this, AccountActivity.class);
		startActivity(start_a);
		BroadcastReceiver login_yay = new BroadcastReceiver() {
			  @Override
			  public void onReceive(Context context, Intent intent) {
				  
				

				  
			  }
			};
			
			LocalBroadcastManager.getInstance(LOGGEDIN.this).registerReceiver(login_yay,
				      new IntentFilter("login_success"));
			
			BroadcastReceiver login_nay = new BroadcastReceiver() {
				  @Override
				  public void onReceive(Context context, Intent intent) {
					  Toast.makeText(LOGGEDIN.this, "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
				  }
				};
				
				LocalBroadcastManager.getInstance(LOGGEDIN.this).registerReceiver(login_nay,
					      new IntentFilter("login_failure"));
		
	}
	
	public void Reset(View view)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(LOGGEDIN.this);
    	final View promptReset = layoutInflater.inflate(R.layout.prompt_reset, null);
    	final AlertDialog alertA = new AlertDialog.Builder(LOGGEDIN.this).create();
    	
    	Button btnR = (Button) promptReset.findViewById(R.id.btnR);
    	
    	btnR.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {

    	    	EditText et1 = (EditText) promptReset.findViewById(R.id.editText1);
            	String name =  et1.getText().toString();
            	
            	/*
            	Intent intent = new Intent("add_entry");
    			intent.putExtra("jid",username+"@gladosv2");
    			intent.putExtra("nick", nickname);
    			//intent.putExtra("group", group);
    			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    			*/
                
                alertA.dismiss();

                Toast.makeText(LOGGEDIN.this, "An email with a reset link has been sent.", Toast.LENGTH_SHORT).show();
    	    }
    	});
    	
    	alertA.setView(promptReset);

    	alertA.show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loggedin, menu);
		return true;
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
