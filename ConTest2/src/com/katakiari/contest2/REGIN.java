package com.katakiari.contest2;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;

import org.jivesoftware.smack.packet.XMPPError;

import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class REGIN extends ActionBarActivity {

	private LocalBroadcastManager mLocalBroadcastManager;
	private Pattern my_pattern;
	private Matcher my_matcher;
	private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regin);
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mLocalBroadcastManager.registerReceiver(mMessageReceiver,
			      new IntentFilter("conflict"));
		mLocalBroadcastManager.registerReceiver(mMessageReceiver1,
			      new IntentFilter("reg_success"));
		
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
			  
			  //Log.d("receiver", "Got message: ");
			  Toast.makeText(REGIN.this, "Username already exists",
	    			   Toast.LENGTH_SHORT).show();
		  }
		};
		
		private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
			  @Override
			  public void onReceive(Context context, Intent intent) {
				  
				  //Log.d("receiver", "Got message: ");
				  Toast.makeText(REGIN.this, "Account created",
		    			   Toast.LENGTH_SHORT).show();
				  Intent aintent = new Intent(REGIN.this, LOGGEDIN.class);
			        startActivity(aintent);
			  }
			};

	private class ConnectToXmpp extends AsyncTask<String, Void, Void> {

	    @Override
	    protected Void doInBackground(String...pParams)  {

	    	String username, password;
	    	
	    	username=pParams[0];    
	    	password=pParams[1];
	    	
	    	XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
		    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		    //config.setUsernameAndPassword("admin", "admin");
		    config.setServiceName("10.252.98.1");
		    config.setHost("10.252.98.1");
		    config.setPort(5222);
		    config.setDebuggerEnabled(true);
		    //config.setSocketFactory(SSLSocketFactory.getDefault());
	// | IOException | XMPPException
		    XMPPTCPConnection connection = new XMPPTCPConnection(config.build());
		    try {
		    	connection.connect();
		    } catch (SmackException e) {
		        e.printStackTrace();
		    } catch (XMPPException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		   
		    try {
	            connection.login("admin", "admin");
	        } catch (XMPPException e) {
	            e.printStackTrace();
	        } catch (SmackException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
				
		    AccountManager accountManager=AccountManager.getInstance(connection);
		    try {
		        accountManager.createAccount(username, password);
		        Intent i = new Intent("reg_success");
                mLocalBroadcastManager.sendBroadcast(i);
		    } catch (XMPPException.XMPPErrorException e) {
		    	
		    	
		    	if(e.getXMPPError().getCondition().equals(XMPPError.Condition.conflict)){
		    		// Send broadcast out with action filter and extras
	                Intent i = new Intent("conflict");
	                mLocalBroadcastManager.sendBroadcast(i);
		    	}
		    } catch (NoResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		    connection.disconnect();
		    
			    
		   
			return null;
	    }
	    protected void onPostExecute(XMPPTCPConnection connection) {
	    	
	    	//return null;
	      }
	
	}
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	public boolean isValidUser(final String username){
		  
		my_pattern = Pattern.compile(USERNAME_PATTERN);
		  my_matcher = my_pattern.matcher(username);
		  return my_matcher.matches();
	    	    
	  }
	
	public boolean isValidPass(final String password){
		  
		my_pattern = Pattern.compile(PASSWORD_PATTERN);
		  my_matcher = my_pattern.matcher(password);
		  return my_matcher.matches();
	    	    
	  }
	
	public void CreateNew(View view){
	
		EditText et1 = (EditText) findViewById(R.id.editText1);
		String user =  et1.getText().toString();
		EditText et2 = (EditText) findViewById(R.id.editText2);
		String email =  et2.getText().toString();
		EditText et3 = (EditText) findViewById(R.id.editText3);
		String pass1 =  et3.getText().toString();
		EditText et4 = (EditText) findViewById(R.id.editText4);
		String pass2 =  et4.getText().toString();
		
		if(!isValidEmail(email)){
			Toast.makeText(REGIN.this, "Invalid email",
	    			   Toast.LENGTH_SHORT).show();
		}
		
		if(!isValidUser(user)){
			Toast.makeText(REGIN.this, "Invalid username",
	    			   Toast.LENGTH_SHORT).show();
		}
		
		if(!isValidPass(pass1)){
			Toast.makeText(REGIN.this, "Invalid password",
	    			   Toast.LENGTH_SHORT).show();
		}
		
		
		if(!Objects.equals(pass1, pass2)){
			Toast.makeText(REGIN.this, "Passwords don't match!",
	    			   Toast.LENGTH_SHORT).show();
			
		}else if(isValidEmail(email)&&isValidUser(user)&&isValidPass(pass1)){
			//new ConnectToXmpp().execute();
			String[] myTaskParams = { user, pass1 };
			new ConnectToXmpp().execute(myTaskParams);
		}
		
		
	}
	
	
	@Override
	protected void onDestroy() {
	  // Unregister since the activity is about to be closed.
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver1);
	  super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.regin, menu);
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
