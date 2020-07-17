package com.example.hw2.allfragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw2.R;
import com.example.hw2.api.IMiniDouyinService;
import com.example.hw2.model.GetVideosResponse;
import com.example.hw2.model.Video;
import com.example.hw2.shouye.shouye;
import com.example.hw2.shouyevideo.ImageHelper;
import com.example.hw2.shouyevideo.VideoActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class shouyeFragment extends Fragment {

    private RecyclerView mRv;
    static private List<Video> mVideos = new ArrayList<>();
    private Button mBtnRefresh;

    private static final String ARG_SHOW_TEXT = "text";
    private String mContentText;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

    public shouyeFragment() {
        // Required empty public constructor
    }

    public static shouyeFragment newInstance(String param1) {
        shouyeFragment fragment = new shouyeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TEXT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContentText = getArguments().getString(ARG_SHOW_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shouye, container, false);
        initRecyclerView(rootView);
        mBtnRefresh = rootView.findViewById(R.id.btn_refresh);
        initBtns();
        //然后调用一下获取数据
        rootView.findViewById(R.id.btn_refresh).callOnClick();
        return rootView;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgssss);
        }

        public void bind(final Activity activity, final Video video) {
            ImageHelper.displayWebImage(video.imageUrl, img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shouye.launch(activity, video.videoUrl, mVideos);
                }
            });
        }
    }

    private void initRecyclerView(View rootView){
        mRv = (RecyclerView)rootView.findViewById(R.id.recycler);
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRv.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new MyViewHolder(
                        LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.video_item_view, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
                final Video video = mVideos.get(i);
                viewHolder.bind(getActivity(), video);
            }

            @Override
            public int getItemCount() {
                return mVideos.size();
            }
        });
    }


    public void fetchFeed(View view) {
        mBtnRefresh.setText("requesting...");
        mBtnRefresh.setEnabled(false);
        miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
            @Override
            public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
                if (response.body() != null && response.body().videos != null) {
                    mVideos = response.body().videos;
                    mRv.getAdapter().notifyDataSetChanged();
                }
                mBtnRefresh.setText("首页");
                mBtnRefresh.setEnabled(true);
            }

            @Override
            public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
                mBtnRefresh.setText("失败");
                mBtnRefresh.setEnabled(true);
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBtns(){
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchFeed(view);
            }
        });
    }
}
