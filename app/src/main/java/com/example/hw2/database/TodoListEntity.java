package com.example.hw2.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "douyin")
public class TodoListEntity {
    @PrimaryKey @NonNull
    @ColumnInfo(name = "id")
    private String mId;

    @ColumnInfo(name = "password")
    private String mPassword;

    @ColumnInfo(name = "isCurrent")
    private boolean isCurrent;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "intro")
    public String intro;

    @ColumnInfo( name = "xingbie")
    public String xingbie;

    @ColumnInfo(name = "douyinId")
    public String douyinId;

    @ColumnInfo(name = "birthday")
    public String birthday;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "school")
    public String school;

    public TodoListEntity(String id, String password) {
        this.mId=id;
        this.mPassword=password;
        this.isCurrent=false;
        this.name= this.douyinId=this.intro= this.xingbie= this.birthday= this.address= this.school="";
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setPassword(String password){this.mPassword=password;}

    public String getPassword(){ return this.mPassword; }

    public boolean getIsCurrent(){return this.isCurrent;}

    public void setIsCurrent(boolean is){ this.isCurrent=is;}

    public void setMName(String name)
    {
        this.name=name;
    }
    public void setIntro(String intro)
    {
        this.intro=intro;
    }
    public void setXingbie(String xingbie)
    {
        this.xingbie=xingbie;
    }
    public void setDouyinId(String douyinId)
    {
        this.douyinId=douyinId;
    }
    public void setBirthday(String Birthday){this.birthday=Birthday;}
    public void setAddress(String address){this.address=address;}
    public void setSchool(String school){this.school=school;}
    public String getname()
    {
        return this.name;
    }
    public String getintro()
    {
        return this.intro;
    }
    public String getxingbie(String xingbie)
    {
        return  this.xingbie;
    }
    public String getdouyinId(String douyinId)
    {
        return this.douyinId;
    }
    public String getbirthday(String Birthday){return this.birthday;}
    public String getaddress(String address){return this.address;}
    public String getschool(String school){return this.school;}

    public void setALot(List<String> l)
    {
        this.name=l.get(0);
        this.douyinId=l.get(1);
        this.intro=l.get(2);
        this.xingbie=l.get(3);
        this.birthday=l.get(4);
        this.address=l.get(5);
        this.school=l.get(6);
    }
    public List<String> getALot()
    {
        List<String> l=new ArrayList<>();
        l.add(this.name);
        l.add(this.douyinId);
        l.add(this.intro);
        l.add(this.xingbie);
        l.add(this.birthday);
        l.add(this.address);
        l.add(this.school);
        return  l;
    }

}
