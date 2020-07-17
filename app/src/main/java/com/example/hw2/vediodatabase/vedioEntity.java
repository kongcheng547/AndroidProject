package com.example.hw2.vediodatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "video_data")
public class vedioEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long mId;

    //用户id
    @ColumnInfo(name = "userid")
    private String mUserid;

    //标题内容
    @ColumnInfo(name = "title")
    private String mTitle;

    //图片封面位置
    @ColumnInfo(name = "pic")
    private String mPic;

    //视频位置(因为一开始手残打错了，后来便没有修改)
    @ColumnInfo(name = "img")
    private String mImg;

    //创建时间
    @ColumnInfo(name = "time")
    private Date mTime;

    //获赞数量
    @ColumnInfo(name = "xinnumber")
    private int mXin;

    public vedioEntity(String mUserid, String mTitle, String mPic, String mImg, Date mTime, int mXin){
        this.mUserid = mUserid;
        this.mTitle = mTitle;
        this.mPic = mPic;
        this.mImg = mImg;
        this.mTime = mTime;
        this.mXin = mXin;
    }

    public Long getId() {
        return mId;
    }

    public String getUserid() {
        return mUserid;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getTime() {
        return mTime;
    }

    public int getXin() {
        return mXin;
    }

    public String getImg() {
        return mImg;
    }

    public String getPic() {
        return mPic;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }

    public void setImg(String mImg) {
        this.mImg = mImg;
    }

    public void setPic(String mPic) {
        this.mPic = mPic;
    }

    public void setTime(Date mTime) {
        this.mTime = mTime;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setUserid(String mUserid) {
        this.mUserid = mUserid;
    }

    public void setXin(int mXin) {
        this.mXin = mXin;
    }
}
