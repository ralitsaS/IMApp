package com.katakiari.contest2;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManager.MatchMode;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MUCNotJoinedException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smack.packet.Message;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class CTService extends Service{
	
	 private volatile HandlerThread mHandlerThread;
	    private ServiceHandler mServiceHandler;
	    private LocalBroadcastManager mLocalBroadcastManager;
	    
	    
	    
	@Override
    public IBinder onBind(Intent intent) {

        // TODO Auto-generated method stub
        return null;
    }
	
	
	public void onCreate() {
		  super.onCreate();
		  mHandlerThread = new HandlerThread("CTService.HandlerThread");
	        mHandlerThread.start();
	        // An Android service handler is a handler running on a specific background thread.
	        mServiceHandler = new ServiceHandler(mHandlerThread.getLooper());
	        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
	        
		 }
	
	
	
	// Define how the handler will process messages
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

     /* Define how to handle any incoming messages here
        public void handleMessage(android.os.Message message) {
            // ...
            // When needed, stop the service with
            // stopSelf();
        	
        	if(message.what==0) 
        	{
        	       
        	 } 
        }
        */
        
    }
	
    
    
    
	public int onStartCommand(final Intent intent, final int flags,
			   final int startId) {
		
		final String username=(String) intent.getExtras().get("user");
		final String password=(String) intent.getExtras().get("pass");
		
		XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
	    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
	   // config.setUsernameAndPassword("admin", "admin");
	    config.setServiceName("10.252.98.1");
	    config.setHost("10.252.98.1");
	    config.setPort(5222);
	    config.setDebuggerEnabled(true);


	    final XMPPTCPConnection connection = new XMPPTCPConnection(config.build());
		 mServiceHandler.post(new Runnable() {
	            @Override
	            public void run() {
	            	
	            	
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
	    	            connection.login(username, password);
	    	         // Send broadcast out with action filter and extras
		               
	    	        } catch (XMPPException e) {
	    	            e.printStackTrace();
	    	            Intent intent = new Intent("login_failure");
		                mLocalBroadcastManager.sendBroadcast(intent);
		                stopSelf();
	    	        } catch (SmackException e) {
	    	            e.printStackTrace();
	    	        } catch (IOException e) {
	    	            e.printStackTrace();
	    	        }
	            	
	    		    
	    		   // if(connection.isAuthenticated()){
	    		    	
	    		    	 Intent intent = new Intent("login_success");
			                mLocalBroadcastManager.sendBroadcast(intent);
	    		    Presence presence = new Presence(Presence.Type.available);
	    		    presence.setStatus("online!");
	    		    try {
						connection.sendPacket(presence);
					} catch (NotConnectedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    		    
	    		    
	    		    UpdateRoster();
	    		    //final Chat newChat;
	    		    final ChatManager chatmanager = ChatManager.getInstanceFor(connection);
	    		    final ChatMessageListener msg_l=new ChatMessageListener() {
	    		    	public void processMessage(Chat chat, Message message) {
	    		    		//System.out.println("Received message: " + message);
	    		    		String otherUser = chat.getParticipant();
	    		    		String user_jid= otherUser.split("\\/")[0];
	    		    		Log.d("sender", user_jid);
	    		    		String msg=message.getBody();
	    		    		Log.d("msg", msg);
	    		    		Intent int_recv=new Intent("recv_msg");
	    		    		int_recv.putExtra("sender", user_jid);
	    		    		int_recv.putExtra("message", msg);
	    		    		mLocalBroadcastManager.sendBroadcast(int_recv);
	    		    		
	    		    	}
	    		    	};
	    		    	
	    		    class ChatManagerListenerImpl implements ChatManagerListener {

	    		    	
	    		        /** {@inheritDoc} */
	    		        @Override
	    		        public void chatCreated(final Chat chat, final boolean createdLocally) {
	    		        	//newChat=chat;
	    		        	//chat.setThread("beh");
	    		            chat.addMessageListener(msg_l);
	    		            
	            			
	            			Log.d("threadid1", chat.getThreadID());
	    		        }

	    		    }
	    		    
	    		    
	    		   
	    		    chatmanager.addChatListener(new ChatManagerListenerImpl());
	    		    chatmanager.setMatchMode(MatchMode.BARE_JID);
	    		    
	    		    
	    		 // Get the MultiUserChatManager
	    		    final MultiUserChatManager muc_manager = MultiUserChatManager.getInstanceFor(connection);
	    		    
	    		    UpdateRooms();
	    		    
	    		   
	    		 
	                
	                BroadcastReceiver mMessageReceiverD = new BroadcastReceiver() {
	    				  @Override
	    				  public void onReceive(Context context, Intent intent) {
	    				    // Get extra data included in the Intent
	    				    String user_all = intent.getStringExtra("user");
	    				    String user_jid= user_all.split("\\(")[0];
	    				    Log.d("deluserjid", ""+user_jid);
	    				    //String nickname = intent.getStringExtra("nick");
	    				    Roster roster = Roster.getInstanceFor(connection);
	    	    		    try {

	    	    		    	roster.removeEntry(roster.getEntry(user_jid));
	    	    		    	UpdateRoster();
							} catch (NotLoggedInException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoResponseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (XMPPErrorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NotConnectedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				  }
	    				};
	    				
	    				mLocalBroadcastManager.registerReceiver(mMessageReceiverD,
	  	  				      new IntentFilter("del_entry"));
	    				
	    		   
	    		    BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	    				  @Override
	    				  public void onReceive(Context context, Intent intent) {
	    				    // Get extra data included in the Intent
	    				    String jid = intent.getStringExtra("jid");
	    				    String nickname = intent.getStringExtra("nick");
	    				    //String[] group = intent.getStringArrayExtra("group");
	    				    Roster roster = Roster.getInstanceFor(connection);
	    	    		    try {
	    	    		    	Presence presence = new Presence(Presence.Type.subscribed);
	    	    		        presence.setTo(jid);
	    	    		        connection.sendStanza(presence);
								roster.createEntry(jid, nickname, null);
								UpdateRoster();
								Log.d("add", "yes");
							} catch (NotLoggedInException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoResponseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (XMPPErrorException e) {
								// TODO Auto-generated catch block
								
								e.printStackTrace();
							} catch (NotConnectedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				  }
	    				};
	    				
	    				mLocalBroadcastManager.registerReceiver(mMessageReceiver,
	  	  				      new IntentFilter("add_entry"));
	    		
	    				
	    				BroadcastReceiver mMessageReceiver3 = new BroadcastReceiver() {
		    				  @Override
		    				  public void onReceive(Context context, Intent intent) {
		    				    // Get extra data included in the Intent
		    				    String mssg = intent.getStringExtra("message");
		    				    String send_to = intent.getStringExtra("to");
		    				    Chat chat=chatmanager.getThreadChat(send_to);
		    				    if(chat!=null){
		    				    	try {
										chat.sendMessage(mssg);
									} catch (NotConnectedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		    				    } else{
		    				    	chat=chatmanager.createChat(send_to, send_to, msg_l);
		    				    	try {
										chat.sendMessage(mssg);
									} catch (NotConnectedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		    				    }
		    				    
		    				  }
		    				};
		    				
		    				mLocalBroadcastManager.registerReceiver(mMessageReceiver3,
		  	  				      new IntentFilter("send_msg"));
		    				
		    			BroadcastReceiver mMessageReceiver4 = new BroadcastReceiver() {
			    				  @Override
			    				  public void onReceive(Context context, Intent intent) {
			    				    // Get extra data included in the Intent
			    					  String user_all = intent.getStringExtra("user");
			    					    String user_jid= user_all.split("\\(")[0];
			    				    
			    					    //Chat chat= chatmanager.getThreadChat(user_jid);
			    					    //Chat chat=chatmanager.getUserChat(user_jid);
			    					    chatmanager.createChat(user_jid);
			    				  }
			    				};
			    				
			    		mLocalBroadcastManager.registerReceiver(mMessageReceiver4,
			  	  				      new IntentFilter("create_chat"));
			    				
			    		BroadcastReceiver mMessageReceiver5 = new BroadcastReceiver() {
					    		  @Override
					    		 public void onReceive(Context context, Intent intent) {
					    				    // Get extra data included in the Intent
					    					  String room_name = intent.getStringExtra("roomname");
					    					  String subject = intent.getStringExtra("subject");
					    					  String description = intent.getStringExtra("description");
					    				   
					    					  MultiUserChat muc = muc_manager.getMultiUserChat(room_name+"@conference.gladosv2");
					    					  try {
												muc.create(username);
											} catch (NoResponseException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (XMPPErrorException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (SmackException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
	
												try {
													Form form = muc.getConfigurationForm();
													  Form submitForm = form.createAnswerForm();
														
							    				        for (Iterator<FormField> fields = form.getFields().iterator(); fields.hasNext();) {
							    				            FormField field = (FormField) fields.next();
							    				            if (!FormField.Type.hidden.equals(field.getType())
							    				                    && field.getVariable() != null) {
							    				                Log.d("field: ",field.getVariable());
							    				                // Sets the default value as the answer
							    				                submitForm.setDefaultAnswer(field.getVariable());
							    				            }
							    				        }
							    				        //List<String> owners = new ArrayList<String>();
							    				        //owners.add(username+"@gladosv2");
							    				        //submitForm.setAnswer("muc#roomconfig_roomowners", owners);
							    				        //submitForm.setAnswer("muc#roomconfig_roomadmins", "admin@gladosv2");
							    				        submitForm.setAnswer("muc#roomconfig_roomname", room_name);
							    				        submitForm.setAnswer("muc#roomconfig_publicroom", true);
							    				        submitForm.setAnswer("muc#roomconfig_persistentroom", true);
							    				        submitForm.setAnswer("muc#roomconfig_roomdesc", description);

							    				        muc.sendConfigurationForm(submitForm);
							    				        muc.changeSubject(subject);
							    				       
							    				        muc.sendMessage("Hello!");
												} catch (NoResponseException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (XMPPErrorException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (NotConnectedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
												
					    				      
					    		 }
					    	};
				    				
				    		mLocalBroadcastManager.registerReceiver(mMessageReceiver5,
				  	  				      new IntentFilter("create_room"));
				    		
				    		BroadcastReceiver mMessageReceiver6 = new BroadcastReceiver() {
					    		  @Override
					    		 public void onReceive(Context context, Intent intent) {
					    				    // Get extra data included in the Intent
					    					  String room_jid = intent.getStringExtra("proom_jid");
					    					  final MultiUserChat muc_room= muc_manager.getMultiUserChat(room_jid);					    					  
					    					  ArrayList<String> nick= new ArrayList<String>();
					    		    			ArrayList<String> mes= new ArrayList<String>();
					    		    			
					    				  try {
					    					  if(!muc_room.isJoined()){
					    						  DiscussionHistory history = new DiscussionHistory();
						    		    			long timeout=SmackConfiguration.getDefaultPacketReplyTimeout();
						    		    			history.setMaxStanzas(5);
						    		    			
						    		    			muc_room.join(username, null, history, timeout);
						    		    			
						    		    			
						    		    			
					    					  }
					    					
					    					  for(int i=0; i<5; i++){
					    		    				Message oldMsg = muc_room.nextMessage(SmackConfiguration.getDefaultPacketReplyTimeout());
					    		    				
						    		    			if(oldMsg!=null){
						    		    				nick.add(oldMsg.getFrom().replaceAll(".*/", ""));
						    		    				mes.add(oldMsg.getBody());
						    		    				
						    		    			
						    		    			Log.d("oldnick", oldMsg.getFrom().replaceAll(".*/", ""));
						    		    			Log.d("oldmes", ""+oldMsg.getBody());
						    		    			}
					    		    				
					    		    			}
					    					  
					    					  boolean priv_del=false;
					    					  String priv="no";
					    					  List<Affiliate> owners=muc_room.getOwners();
					    					  for (Affiliate owner : owners) 
					    		    		    {
					    		    		    	if(owner.getJid().equals(username+"@gladosv2")){
					    		    		    		priv="yes";
					    		    		    		//priv_del=true;
					    		    		    	}
					    		    		    		
					    		    		    }
					    					  
					    					  Log.d("priv", priv);
					    					  Intent i = new Intent();
					    					  i.setClass(CTService.this, PublicChat.class);
					    					  i.putExtra("room_jid", room_jid);
					    					  i.putExtra("priv", priv);
					    					  i.putStringArrayListExtra("his_nick", nick);
					    					  i.putStringArrayListExtra("his_mes", mes);
					    					  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    					  startActivity(i);
					    		    			

										} catch (NoResponseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (XMPPErrorException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (NotConnectedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (MUCNotJoinedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										 
					    				  muc_room.addMessageListener(new MessageListener(){

											@Override
											public void processMessage(Message arg0) {
												// TODO Auto-generated method stub
												String sender=arg0.getFrom().replaceAll(".*/", "");
												String msg_body=arg0.getBody();
											 if(!Objects.equals(sender, username)){
												Intent i=new Intent("recv_msg_room");
												i.putExtra("sender", sender);
												i.putExtra("msg", msg_body);
												mLocalBroadcastManager.sendBroadcast(i);
												 }
											}
					    					  
					    				  
					    				  });
					    				  
					    				  
					    				  BroadcastReceiver mr_smr = new BroadcastReceiver() {
						    				  @Override
						    				  public void onReceive(Context context, Intent intent) {
						    				    // Get extra data included in the Intent
						    				    String mssg = intent.getStringExtra("message");
						    				    //String send_to = intent.getStringExtra("to");
						    				   try {
												muc_room.sendMessage(mssg);
											} catch (NotConnectedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
						    				    
						    				  }
						    				};
						    				
						    				mLocalBroadcastManager.registerReceiver(mr_smr,
						  	  				      new IntentFilter("send_msg_room"));
						    				
						    				BroadcastReceiver mr_dr = new BroadcastReceiver() {
							    				  @Override
							    				  public void onReceive(Context context, Intent intent) {
							    				    try {
														muc_room.destroy("", "");
													} catch (NoResponseException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													} catch (XMPPErrorException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													} catch (NotConnectedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
							    				    
							    				  }
							    				};
							    				
							    				mLocalBroadcastManager.registerReceiver(mr_dr,
							  	  				      new IntentFilter("del_room"));
					    				 
					    		 }
					    	};
				    				
				    		mLocalBroadcastManager.registerReceiver(mMessageReceiver6,
				  	  				      new IntentFilter("enter_room"));
	           
				    		
				    		
				    		
				    		BroadcastReceiver mMessageReceiver7 = new BroadcastReceiver() {
					    		  @Override
					    		 public void onReceive(Context context, Intent intent) {
					    				    // Get extra data included in the Intent
					    					  String group_name = intent.getStringExtra("roomname");
					    					  String subject = intent.getStringExtra("subject");
					    					  String description = intent.getStringExtra("description");
					    				   
					    					  MultiUserChat muc = muc_manager.getMultiUserChat(group_name+"@conference.gladosv2");
					    					  try {
												muc.create(username);
											} catch (NoResponseException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (XMPPErrorException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (SmackException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
	
												try {
													Form form = muc.getConfigurationForm();
													  Form submitForm = form.createAnswerForm();
														
							    				        for (Iterator<FormField> fields = form.getFields().iterator(); fields.hasNext();) {
							    				            FormField field = (FormField) fields.next();
							    				            if (!FormField.Type.hidden.equals(field.getType())
							    				                    && field.getVariable() != null) {
							    				                Log.d("field: ",field.getVariable());
							    				                // Sets the default value as the answer
							    				                submitForm.setDefaultAnswer(field.getVariable());
							    				            }
							    				        }
							    				        //List<String> owners = new ArrayList<String>();
							    				        //owners.add(username+"@gladosv2");
							    				        //submitForm.setAnswer("muc#roomconfig_roomowners", owners);
							    				        //submitForm.setAnswer("muc#roomconfig_roomadmins", "admin@gladosv2");
							    				        submitForm.setAnswer("muc#roomconfig_roomname", group_name);
							    				        //submitForm.setAnswer("muc#roomconfig_publicroom", false);
							    				        submitForm.setAnswer("muc#roomconfig_persistentroom", true);
							    				        submitForm.setAnswer("muc#roomconfig_allowinvites", true);
							    				        submitForm.setAnswer("muc#roomconfig_membersonly", true);
							    				        submitForm.setAnswer("muc#roomconfig_roomdesc", description);

							    				        muc.sendConfigurationForm(submitForm);
							    				        muc.changeSubject(subject);
							    				       
							    				        muc.sendMessage("Hello!");
												} catch (NoResponseException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (XMPPErrorException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (NotConnectedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
												
					    				      
					    		 }
					    	};
				    				
				    		mLocalBroadcastManager.registerReceiver(mMessageReceiver7,
				  	  				      new IntentFilter("create_group"));
				    		
				    		BroadcastReceiver mMessageReceiver9 = new BroadcastReceiver() {
					    		  @Override
					    		 public void onReceive(Context context, Intent intent) {
					    				    // Get extra data included in the Intent
					    					  String room_jid = intent.getStringExtra("proom_jid");
					    					  final MultiUserChat muc_room= muc_manager.getMultiUserChat(room_jid);					    					  
					    					  ArrayList<String> nick= new ArrayList<String>();
					    		    			ArrayList<String> mes= new ArrayList<String>();
					    		    			DiscussionHistory history = new DiscussionHistory();
					    		    			long timeout=SmackConfiguration.getDefaultPacketReplyTimeout();
					    		    			history.setMaxStanzas(5);
					    		    			
					    		    			
					    				  try {
					    						  
						    		    			
						    		    			muc_room.join(username, null, history, timeout);
						    		    			
						    		    			
						    		    			
					    					 
					    					
					    					  for(int i=0; i<5; i++){
					    		    				Message oldMsg = muc_room.nextMessage(SmackConfiguration.getDefaultPacketReplyTimeout());
					    		    				
						    		    			if(oldMsg!=null){
						    		    				nick.add(oldMsg.getFrom().replaceAll(".*/", ""));
						    		    				mes.add(oldMsg.getBody());
						    		    				
						    		    			
						    		    			Log.d("oldnick", oldMsg.getFrom().replaceAll(".*/", ""));
						    		    			Log.d("oldmes", ""+oldMsg.getBody());
						    		    			}
					    		    				
					    		    			}
					    					  
					    					  List<Affiliate> members= muc_room.getMembers();
												ArrayList<String> mem_jids= new ArrayList<String>();
												for (Affiliate member : members) 
								    		    {
								    		    	mem_jids.add(member.getJid());
								    		    }
												
												
					    					  String priv="no";
					    					  List<Affiliate> owners=muc_room.getOwners();
					    					  for (Affiliate owner : owners) 
					    		    		    {
					    		    		    	if(owner.getJid().equals(username+"@gladosv2"))
					    		    		    		priv="yes";
					    		    		    }
					    					  
					    					  
					    					  Intent i = new Intent();
					    					  i.setClass(CTService.this, GroupChat.class);
					    					  i.putExtra("room_jid", room_jid);
					    					  i.putExtra("priv", priv);
					    					  i.putStringArrayListExtra("his_nick", nick);
					    					  i.putStringArrayListExtra("his_mes", mes);
					    					  i.putStringArrayListExtra("mem_jids", mem_jids);
					    					  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    					  startActivity(i);
					    		    		

										} catch (NoResponseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (XMPPErrorException e) {
											// TODO Auto-generated catch block
											Intent intent_er=new Intent("cant_enter");
											mLocalBroadcastManager.sendBroadcast(intent_er);
											e.printStackTrace();
										} catch (NotConnectedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (MUCNotJoinedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
					    				  
					    			
										 
					    				  muc_room.addMessageListener(new MessageListener(){

											@Override
											public void processMessage(Message arg0) {
												// TODO Auto-generated method stub
												String sender=arg0.getFrom().replaceAll(".*/", "");
												String msg_body=arg0.getBody();
												
												Intent i=new Intent("recv_msg_group");
												i.putExtra("sender", sender);
												i.putExtra("msg", msg_body);
												mLocalBroadcastManager.sendBroadcast(i);
											}
					    					  
					    				  
					    				  });
					    				  
					    				  BroadcastReceiver ban = new BroadcastReceiver() {
						    				  @Override
						    				  public void onReceive(Context context, Intent intent) {
						    				    // Get extra data included in the Intent
						    				    String jid = intent.getStringExtra("ban_jid");
						    				  
						    				    try {
													muc_room.revokeMembership(jid);
												} catch (XMPPErrorException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (NoResponseException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (NotConnectedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						    				    
						    				  }
						    				};
						    				
						    				mLocalBroadcastManager.registerReceiver(ban,
						  	  				      new IntentFilter("ban_member"));
						    				
						    				
					    				  BroadcastReceiver mr_smr = new BroadcastReceiver() {
						    				  @Override
						    				  public void onReceive(Context context, Intent intent) {
						    				    // Get extra data included in the Intent
						    				    String mssg = intent.getStringExtra("message");
						    				    //String send_to = intent.getStringExtra("to");
						    				   try {
												muc_room.sendMessage(mssg);
											} catch (NotConnectedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
						    				    
						    				  }
						    				};
						    				
						    				mLocalBroadcastManager.registerReceiver(mr_smr,
						  	  				      new IntentFilter("send_msg_group"));
						    				
						    				BroadcastReceiver mr_dr = new BroadcastReceiver() {
							    				  @Override
							    				  public void onReceive(Context context, Intent intent) {
							    				    try {
														muc_room.destroy("", "");
													} catch (NoResponseException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													} catch (XMPPErrorException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													} catch (NotConnectedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
							    				    
							    				  }
							    				};
							    				
							    				mLocalBroadcastManager.registerReceiver(mr_dr,
							  	  				      new IntentFilter("del_group"));
					    				 
					    		 }
					    	};
				    				
				    		mLocalBroadcastManager.registerReceiver(mMessageReceiver9,
				  	  				      new IntentFilter("enter_group"));
	            
				    		
				    		BroadcastReceiver mMessageReceiver8 = new BroadcastReceiver() {
					    		  @Override
					    		 public void onReceive(Context context, Intent intent) {
					    				    // Get extra data included in the Intent
					    					  
												
												
					    				      
					    		 }
					    	};
				    				
				    		mLocalBroadcastManager.registerReceiver(mMessageReceiver8,
				  	  				      new IntentFilter("reset_password"));
	            
	            
	    		    	
	            };
	        
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            public void UpdateRoster(){
    		    	Roster roster = Roster.getInstanceFor(connection);
    		    	roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

    		    if (!roster.isLoaded())
					try {
						roster.reloadAndWait();
					} catch (NotLoggedInException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotConnectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				Collection <RosterEntry> entries = roster.getEntries();
    		    Presence presence;
    		    ArrayList<String> user_jids = new ArrayList<String>();
    		    ArrayList<String> user_pres = new ArrayList<String>();
    		    ArrayList<String> user_names = new ArrayList<String>();
    		    
    		    for (RosterEntry entry : entries) 
    		    {
    		    	presence = roster.getPresence(entry.getUser());

    		    	user_jids.add(entry.getUser());
    		    	user_pres.add(presence.getType().name());
    		    	user_names.add(entry.getName());
    		    	
                    Log.d("user", ""+entry.getUser());
                    Log.d("type", ""+presence.getType().name());
                    Log.d("status", ""+presence.getStatus());
    		    }
    		    
    		    Intent intent_roster = new Intent("user_roster");
    		    intent_roster.putStringArrayListExtra("user_j", user_jids);
    		    intent_roster.putStringArrayListExtra("user_p", user_pres);
    		    intent_roster.putStringArrayListExtra("user_n", user_names);
                mLocalBroadcastManager.sendBroadcast(intent_roster);
    		    	
             /*
                PendingIntent broadcast = PendingIntent.getBroadcast(CTService.this, 0, intent_roster, PendingIntent.FLAG_UPDATE_CURRENT);

                PendingIntent update_roster=PendingIntent.
                long first = System.currentTimeMillis(); // now
                long interval = 5 * 1000; // every 5 seconds
                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                am.setRepeating(AlarmManager.RTC, first, interval, broadcast);
                
                */
    		    };
    		    
    		    
    		    public void UpdateRooms(){
    		    	
    		    	final MultiUserChatManager muc_manager = MultiUserChatManager.getInstanceFor(connection);
    		    	
    		    	try {
						List<HostedRoom> rooms = muc_manager.getHostedRooms("conference.gladosv2");
						ArrayList<String> r_jids=new ArrayList<String>();
						ArrayList<String> r_descs=new ArrayList<String>();
						ArrayList<String> r_subjects=new ArrayList<String>();
						ArrayList<String> g_jids=new ArrayList<String>();
						ArrayList<String> g_descs=new ArrayList<String>();
						ArrayList<String> g_subjects=new ArrayList<String>();
						//List<String> joined_rooms= muc_manager.getJoinedRooms(username+"@gladosv2");
						 //Log.d("joined", ""+joined_rooms);
						
						
						for (HostedRoom room : rooms) 
		    		    {
		    		    	RoomInfo r_info= muc_manager.getRoomInfo(room.getJid());
		    		    	//MultiUserChat muc =muc_manager.getMultiUserChat(room.getJid());
		    		    	if(!r_info.isMembersOnly()){
		    		    		r_descs.add(r_info.getDescription());
		    		    		r_subjects.add(r_info.getSubject());
		    		    		r_jids.add(room.getJid());
		    		    	} else{
		    		    		g_descs.add(r_info.getDescription());
		    		    		g_subjects.add(r_info.getSubject());
		    		    		g_jids.add(room.getJid());
		    		    	}
		    		    	
		                    Log.d("rjid", ""+room.getJid());
		    		    }
		    		    
		    		    Intent intent_rooms = new Intent("public_rooms");
		    		    intent_rooms.putStringArrayListExtra("rooms_j", r_jids);
		    		    intent_rooms.putStringArrayListExtra("rooms_s", r_subjects);
		    		    intent_rooms.putStringArrayListExtra("rooms_d", r_descs);
		                mLocalBroadcastManager.sendBroadcast(intent_rooms);
		                
		                Intent intent_groups = new Intent("groups");
		                intent_groups.putStringArrayListExtra("rooms_j", g_jids);
		                intent_groups.putStringArrayListExtra("rooms_s", g_subjects);
		                intent_groups.putStringArrayListExtra("rooms_d", g_descs);
		                mLocalBroadcastManager.sendBroadcast(intent_groups);
					} catch (NoResponseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (XMPPErrorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotConnectedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    		    
	    		    
    		    }
		 
		 });
		 
		 
		 
		 
		 
			  return START_NOT_STICKY;
			 }
	
	
	
	
	@Override
    public void onDestroy() {
        // Cleanup service before destruction
        mHandlerThread.quit();
    }
}