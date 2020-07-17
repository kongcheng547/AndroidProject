package com.example.hw2.cameraactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hw2.R;

import java.io.IOException;

public class MyCameraBrowse extends AppCompatActivity {

    private String mp4Path;


    ImageView mStartStop;
    ImageView mLeftQuick;
    ImageView mRightQuick;
    TextView mStart;
    TextView mEnd;
    SeekBar mSeekBar;
    Thread theThread;

    Button nextStepBtn;

    //快进时间
    private final int oneStep = 3000;

    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private int AllTime;
    private boolean isChanging = false;
    private ImageView backBtn;

    //判断现在的进度条是显示的还是隐藏的
    //横屏状态每次单击surfaceView之后，进度条会隐藏
    boolean isShowing = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera_browse);

        initPage();
        initAll();
    }

    private void initPage(){
        final Intent intent = getIntent();
        mp4Path = intent.getStringExtra("mp4Path");
        Log.i("the Video Path is",mp4Path);
    }

    private void initAll(){
        surfaceView = findViewById(R.id.surfaceView);
        mStartStop = findViewById(R.id.bofang);
        mLeftQuick = findViewById(R.id.zuokuaijin);
        mRightQuick = findViewById(R.id.youkuaijin);
        mStart = findViewById(R.id.startTime);
        mEnd = findViewById(R.id.endTime);
        mSeekBar = findViewById(R.id.theSeekBar);
        backBtn = findViewById(R.id.backBtn);
        nextStepBtn = findViewById(R.id.finalStep);
        player = new MediaPlayer();
        try {
            player.setDataSource(mp4Path);
            holder = surfaceView.getHolder();
            holder.addCallback(new PlayerCallBack());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mStartStop.callOnClick();
                    player.setLooping(true);
                }
            });
            player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    changeVideoSize(mp);
                }
            });
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    System.out.println(percent);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

        //设置播放暂停按钮
        mStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable.ConstantState t1=  mStartStop.getDrawable().getCurrent().getConstantState();
                Drawable.ConstantState t2=  getDrawable(R.drawable.bofang).getConstantState();
                if(t1.equals(t2)){
                    //代表现在显示的是开始
                    mStartStop.setImageResource(R.drawable.zanting);
                    player.start();
                    theThread = new Thread(new SeekBarThread());
                    theThread.start();
                }
                else {
                    //代表现在是暂停
                    mStartStop.setImageResource(R.drawable.bofang);
                    player.pause();
                }
            }
        });

        //获取音乐的时长
        AllTime = player.getDuration();
        setmEnd(AllTime);
        mSeekBar.setMax(AllTime);

        //设置seekBar拖动响应函数
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setmStart(player.getCurrentPosition());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isChanging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
                isChanging = false;
                theThread = new Thread(new SeekBarThread());
                theThread.start();
            }
        });

        //设置回退快进按钮
        mLeftQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanging = true;
                int nowPosition = player.getCurrentPosition();
                nowPosition = nowPosition - oneStep;
                if(nowPosition<0) nowPosition=0;
                player.seekTo(nowPosition);
                mSeekBar.setProgress(nowPosition);
                isChanging = false;
            }
        });

        //设置前进快进按钮
        mRightQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanging = true;
                int nowPosition = player.getCurrentPosition();
                nowPosition = nowPosition + oneStep;
                if(nowPosition>AllTime) nowPosition= AllTime;
                player.seekTo(nowPosition);
                mSeekBar.setProgress(nowPosition);
                isChanging = false;
            }
        });


        //设置横屏时候单击surfaceView隐藏进度条
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowing){
                    isShowing = false;
                    findViewById(R.id.downItem).setVisibility(View.INVISIBLE);
                }
                else{
                    isShowing = true;
                    findViewById(R.id.downItem).setVisibility(View.VISIBLE);
                }
            }
        });

        //回退按钮
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //进入到下一个页面的按钮
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable.ConstantState t1=  mStartStop.getDrawable().getCurrent().getConstantState();
                Drawable.ConstantState t2=  getDrawable(R.drawable.bofang).getConstantState();
                if(!t1.equals(t2)){
                    mStartStop.setImageResource(R.drawable.bofang);
                    player.pause();
                }
                Intent intent = new Intent(MyCameraBrowse.this, MyCameraSubmit.class);
                intent.putExtra("mp4Path", mp4Path);
                startActivity(intent);
            }
        });
    }

    //设置开始
    private void setmStart(int time){
        time = time/1000;
        int mins = time / 60;
        int secs = time - mins*60;
        String finTime;
        if(mins<10) finTime = "0"+mins+":";
        else finTime = ""+mins+":";
        if(secs<10) finTime = finTime + "0" + secs;
        else finTime = finTime + secs;
        mStart.setText(finTime);
    }

    //设置结束
    private void setmEnd(int time){
        time = time/1000;
        int mins = time / 60;
        int secs = time - mins*60;
        String finTime;
        if(mins<10) finTime = "0"+mins+":";
        else finTime = ""+mins+":";
        if(secs<10) finTime = finTime + "0" + secs;
        else finTime = finTime + secs;
        mEnd.setText(finTime);
    }

    public void changeVideoSize(MediaPlayer mediaPlayer) {
//        int surfaceWidth = surfaceView.getWidth();
//        int surfaceHeight = surfaceView.getHeight();
//
//        int videoWidth = mediaPlayer.getVideoWidth();
//        int videoHeight = mediaPlayer.getVideoHeight();
//
//        float max;
//        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
//        } else {
//            max = Math.max(((float) videoWidth / (float) surfaceWidth), (float) videoHeight / (float) surfaceHeight);
//        }
//
//        videoWidth = (int) Math.ceil((float) videoWidth / max);
//        videoHeight = (int) Math.ceil((float) videoHeight / max);
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        surfaceView.setLayoutParams(layoutParams);
    }


    private class PlayerCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    //更新进度条
    class SeekBarThread extends Thread{
        @Override
        public void run() {
            while (!isChanging && player.isPlaying()){
                mSeekBar.setProgress(player.getCurrentPosition());
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Drawable.ConstantState t1=  mStartStop.getDrawable().getCurrent().getConstantState();
        Drawable.ConstantState t2=  getDrawable(R.drawable.bofang).getConstantState();
        if(t1.equals(t2)){
            mStartStop.setImageResource(R.drawable.zanting);
            player.start();
            theThread = new Thread(new SeekBarThread());
            theThread.start();
        }
    }
}

