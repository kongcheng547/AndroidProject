package com.example.hw2.shouye;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.hw2.R;
import com.example.hw2.model.Video;

import java.util.ArrayList;
import java.util.List;


public class shouye extends AppCompatActivity {
    private static final String TAG = "douyin";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    MyLayoutManager myLayoutManager;//暂时先写成这个
    static List<Video> finVideo;
    static String thisUrl;
    static List<String> finImgUrl = new ArrayList<>();
    static List<String> finVideoUrl = new ArrayList<>();

    public static void launch(Activity activity, String url, List<Video> mVideo) {
        finVideo = mVideo;
        Intent intent = new Intent(activity, shouye.class);
        intent.putExtra("url", url);
        thisUrl = url;
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shouye);//这地方要改
        initData();
        initView();
        initListener();
    }

    private void initData(){
        int i;
        for(i=0; i<finVideo.size(); i++){
            if(finVideo.get(i).videoUrl.equals(thisUrl)) break;
        }
        Log.i("iiiiiiii",i+"");
        finImgUrl.clear();
        finVideoUrl.clear();
        for(int j=i; j<finVideo.size() && j<i+7; j++){
            finImgUrl.add(finVideo.get(j).imageUrl);
            finVideoUrl.add(finVideo.get(j).videoUrl);
        }
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler);
        myLayoutManager = new MyLayoutManager(this, OrientationHelper.VERTICAL, false);

        mAdapter = new MyAdapter(this);
        mRecyclerView.setLayoutManager(myLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        myLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean bottom) {
                Log.e(TAG, "选择位置:" + position + " 下一页:" + bottom);

                playVideo(0);
            }
        });

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] imgs = finImgUrl.toArray(new String[finImgUrl.size()]);
        private String[] videos = finVideoUrl.toArray(new String[finVideoUrl.size()]);
        private int index = 0;
        private Context mContext;

        public MyAdapter(Context context) {
            this.mContext = context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Glide.with(holder.img_thumb.getContext()).load(imgs[index]).into(holder.img_thumb);
            holder.img_thumb.setImageURI(Uri.parse(imgs[index]));
            holder.videoView.setVideoURI(Uri.parse(videos[index]));
            holder.mHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable.ConstantState t1 = holder.mHeart.getDrawable().getCurrent().getConstantState();
                    Drawable.ConstantState t2 = getDrawable(R.drawable.icon_home_like_before).getConstantState();
                    Log.i("t1",t1.toString());
                    Log.i("t2",t2.toString());
                    if(!holder.redState){
                        Log.i("小红心","变红啦");
                        Drawable[] layers = new Drawable[2];
                        TransitionDrawable transitionDrawable;
                        layers[0] = getResources().getDrawable(R.drawable.icon_home_like_before);
                        layers[1] = getResources().getDrawable(R.drawable.icon_home_like_after);
                        transitionDrawable = new TransitionDrawable(layers);
                        holder.mHeart.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(600);
                        holder.redState = true;
                    }
                    else {
                        Log.i("小红心","变白啦");
                        Drawable[] layers = new Drawable[2];
                        TransitionDrawable transitionDrawable;
                        layers[0] = getResources().getDrawable(R.drawable.icon_home_like_after);
                        layers[1] = getResources().getDrawable(R.drawable.icon_home_like_before);
                        transitionDrawable = new TransitionDrawable(layers);
                        holder.mHeart.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(600);
                        holder.redState = false;
                    }
                }
            });
            index++;
            if (index >= 7) {
                index = 0;
            }
        }

        @Override
        public int getItemCount() {
            return 88;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            RelativeLayout rootView;
            ImageView mHeart;
            boolean redState = false;

            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
                mHeart = itemView.findViewById(R.id.light_heart);
            }
        }
    }

    private void releaseVideo(int index) {
        View itemView = mRecyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void playVideo(int position) {
        View itemView = mRecyclerView.getChildAt(position);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });

        videoView.start();

        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    imgPlay.animate().alpha(0.7f).start();
                    videoView.pause();
                    isPlaying = false;
                } else {
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }

}
