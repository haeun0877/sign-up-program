package com.example.project1;

import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

public class Mail implements Serializable {
    private String receiveid, sendid, title, content, time;

    public Mail(String receiveid, String sendid, String title, String content, String time){
        this.receiveid = receiveid;
        this.sendid = sendid;
        this.title=title;
        this.content= content;
        this.time=time;
    }



    public String getReceiveid() {
        return receiveid;
    }

    public String getSendid(){
        return sendid;
    }

    public String gettitle(){
        return title;
    }

    public String getContent() {return content;}

    public String getTime() {return time;}

}
