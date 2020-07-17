package com.example.hw2.cameraactivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hw2.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCamera extends AppCompatActivity {

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private String mp4Path="";

    //这些是屏幕上的一些按钮
    private LottieAnimationView playVideo;
    private ImageView stopVideo;
    private ImageView deleteVideo;
    private ImageView finishVideo;
    private ImageView paisheDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);

        //初始化基本的按钮
        initButton();
        //初始化摄像头
        initCamera();
        //初始化点击操作
        initClick();
    }


    //初始化基本的按钮
    private void initButton(){
        mSurfaceView = findViewById(R.id.mCameraSurf);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(new PlayerCallBack());

        playVideo = findViewById(R.id.animation_takePhoto);
        stopVideo = findViewById(R.id.animation_stopPhoto);

        deleteVideo = findViewById(R.id.videoDelete);
        finishVideo = findViewById(R.id.videoFinish);
        paisheDelete = findViewById(R.id.paisheDelete);
    }

    //初始化摄像头
    private void initCamera(){
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("orientation","portrait");
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
    }

    //初始化点击
    private void initClick(){
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mp4Path.equals("")){
                    //如果现在还有拍摄了但是没有上传的一段视频
                    Toast toast = Toast.makeText(getApplicationContext(),"还有一段未上传的视频，请先上传或删除",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    return;
                }
                if(prepareVideoRecorder()){
                    isRecording = true;
                    playVideo.setVisibility(View.GONE);
                    stopVideo.setVisibility(View.VISIBLE);
                    mMediaRecorder.start();
                }
            }
        });

        stopVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecording = false;
                deleteVideo.setVisibility(View.INVISIBLE);
                finishVideo.setVisibility(View.INVISIBLE);
                paisheDelete.setVisibility(View.INVISIBLE);
                stopVideo.setVisibility(View.GONE);
                playVideo.setVisibility(View.VISIBLE);
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                mCamera.lock();

                Intent intent = new Intent(MyCamera.this, MyCameraBrowse.class);
                intent.putExtra("mp4Path", mp4Path);
                startActivity(intent);
            }
        });

        finishVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteVideo.setVisibility(View.INVISIBLE);
                finishVideo.setVisibility(View.INVISIBLE);
                paisheDelete.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MyCamera.this, MyCameraBrowse.class);
                intent.putExtra("mp4Path", mp4Path);
                startActivity(intent);
            }
        });

        deleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder normalDiaog = new AlertDialog.Builder(MyCamera.this);
                normalDiaog.setTitle("提示");
                normalDiaog.setMessage("您确定要删除这条视频吗?");
                normalDiaog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteVideo.setVisibility(View.INVISIBLE);
                        finishVideo.setVisibility(View.INVISIBLE);
                        paisheDelete.setVisibility(View.INVISIBLE);
                        File file = new File(mp4Path);
                        if(file.exists()){
                            file.delete();
                        }
                        mp4Path = "";
                        Toast toast = Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                normalDiaog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                normalDiaog.show();
            }
        });

        paisheDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder normalDiaog = new AlertDialog.Builder(MyCamera.this);
                normalDiaog.setTitle("提示");
                normalDiaog.setMessage("您确定要删除这条视频吗?");
                normalDiaog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteVideo.setVisibility(View.INVISIBLE);
                        finishVideo.setVisibility(View.INVISIBLE);
                        paisheDelete.setVisibility(View.INVISIBLE);
                        File file = new File(mp4Path);
                        if(file.exists()){
                            file.delete();
                        }
                        mp4Path = "";
                        Toast toast = Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                });
                normalDiaog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                normalDiaog.show();
            }
        });
    }










    //录制操作
    private boolean prepareVideoRecorder(){
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        //获得相应文件路径
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "VIDEO_"+timeStamp+".mp4");
        if(!mediaFile.exists()){
            mediaFile.getParentFile().mkdir();
        }
        mp4Path = mediaFile.getAbsolutePath();
        Log.i("the path is: ",mp4Path);
        mMediaRecorder.setOutputFile(mp4Path);
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
        mMediaRecorder.setOrientationHint(90);
        try {
            mMediaRecorder.prepare();
        }catch (Exception e){
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            return false;
        }
        return true;
    }








    //回调类
    private class PlayerCallBack implements SurfaceHolder.Callback{
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            if(surfaceHolder.getSurface() == null){
                return;
            }
            mCamera.stopPreview();
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }





    //生命周期
    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null){
            initCamera();
        }
        mCamera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    //重新启动页面的时候要检查是否存在视频
    @Override
    protected void onRestart() {
        super.onRestart();
        if(mp4Path.equals("")){
            return;
        }
        Log.i("myInfo","这个页面又出现了");
        deleteVideo.setVisibility(View.VISIBLE);
        finishVideo.setVisibility(View.VISIBLE);
        paisheDelete.setVisibility(View.VISIBLE);
    }
}
