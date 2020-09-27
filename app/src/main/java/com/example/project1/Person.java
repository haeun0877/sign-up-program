package com.example.project1;

import android.net.Uri;

import java.io.Serializable;

public class Person implements Serializable {
    private String name, id, password, major;
    Uri picture;

    public Person(String name, String id, String password, Uri picture){
        this.name=name;
        this.id=id;
        this.password=password;
        this.picture=picture;
        this.major=major;
    }

    public Person(String name, String id, String password, Uri picture, String major){
        this.name=name;
        this.id=id;
        this.password=password;
        this.picture=picture;
        this.major=major;
    }

    public String getId() {
        return id;
    }

    public String getPassword(){
        return password;
    }

    public String getName(){
        return name;
    }
}
