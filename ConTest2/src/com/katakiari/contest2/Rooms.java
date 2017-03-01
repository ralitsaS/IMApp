package com.katakiari.contest2;

public class Rooms {
    private String jid;
    private String subject;
    private String description;

    public Rooms(String jid, String subject, String description) {
        this.jid = jid;
        this.subject = subject;
        this.description = description;
    }
    public void setJID(String jid) {
        this.jid= jid;
    }
    public String getJID() {
        return jid;
    }
    public void setSUB(String subject) {
        this.subject= subject;
    }
    public String getSUB() {
        return subject;
    }
    public void setDES(String description) {
        this.description= description;
    }
    public String getDES() {
        return description;
    }
}