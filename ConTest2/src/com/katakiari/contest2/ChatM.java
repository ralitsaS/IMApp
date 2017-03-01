package com.katakiari.contest2;

public class ChatM {
    private String chat_sender;
    //private String chat_time;
    private String chat_message;

    public ChatM(String chat_sender, String chat_message) {
        this.chat_sender = chat_sender;
        //this.chat_time = chat_time;
        this.chat_message = chat_message;
    }
    public void setSENDER(String chat_sender) {
        this.chat_sender= chat_sender;
    }
    public String getSENDER() {
        return chat_sender;
    }
    
    public void setMES(String chat_message) {
        this.chat_message= chat_message;
    }
    public String getMES() {
        return chat_message;
    }
}