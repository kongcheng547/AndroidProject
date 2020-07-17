package com.example.hw2.cameraactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hw2.MainActivity;
import com.example.hw2.R;
import com.example.hw2.api.IMiniDouyinService;
import com.example.hw2.model.PostVideoResponse;
import com.example.hw2.vediodatabase.vedioDatabase;
import com.example.hw2.vediodatabase.vedioEntity;
import com.example.hw2.vediodatabase.videoDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyCameraSubmit extends AppCompatActivity {

    private ImageView finalReturn;
    private String mp4Path;
    private String jpgPath;
    private ImageView finShowImg;
    private Bitmap previewBitmap;

    private Button clickToNet;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera_submit);

        //获取数据
        getData();
        //初始化按钮
        initButton();
        //绑定按钮
        bindButtonEvent();
        //获取视频关键帧
        getThePic();
    }



    private void getData(){
        final Intent intent = getIntent();
        mp4Path = intent.getStringExtra("mp4Path");
        Log.i("the Video Path is",mp4Path);
    }


    private void initButton(){
        finalReturn = findViewById(R.id.finalReturn);
        finShowImg = findViewById(R.id.finContain);
        clickToNet = findViewById(R.id.clicktonet);
    }



    private void bindButtonEvent(){
        finalReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clickToNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileOutputStream fos = null;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                jpgPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+ File.separator+str+".jpg";
                File file = new File(jpgPath);
                try {
                    fos = new FileOutputStream(file);
                    previewBitmap.compress(Bitmap.CompressFormat.JPEG,0,fos);
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.i("the path is:",jpgPath);

                Toast.makeText(MyCameraSubmit.this, "上传中...",Toast.LENGTH_SHORT).show();
                clickToNet.setEnabled(false);
                MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", jpgPath);
                MultipartBody.Part videoPart = getMultipartFromUri("video", mp4Path);
                miniDouyinService.postVideo("318466", "llfhyc", coverImagePart, videoPart).enqueue(
                        new Callback<PostVideoResponse>() {
                            @Override
                            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                                if (response.body() != null) {
                                    Toast.makeText(MyCameraSubmit.this, "提交成功", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                                Toast.makeText(MyCameraSubmit.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                new Thread(){
                    @Override
                    public void run() {
                        videoDao dao = vedioDatabase.inst(MyCameraSubmit.this).tovedioDao();
                        dao.addVideo(new vedioEntity("318466","llfhyc",jpgPath,mp4Path,new Date(System.currentTimeMillis()),0));
                        //数据存储成功后跳转回到原来的界面
                        findViewById(R.id.btn_refresh).callOnClick();
                    }
                };
                Intent intent = new Intent();
                intent.setClass(MyCameraSubmit.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private MultipartBody.Part getMultipartFromUri(String name, String path) {
        File f = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }


    private void getThePic(){

        try {
            // 获取预览图
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mp4Path);
            previewBitmap = mmr.getFrameAtTime();

            finShowImg.setImageBitmap(previewBitmap);
            // 获取时长
            String strDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int duration = Integer.parseInt(strDuration) / 1000;
            Log.i("time is:",String.format("%d:%02d", duration / 60, duration % 60));
            mmr.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
