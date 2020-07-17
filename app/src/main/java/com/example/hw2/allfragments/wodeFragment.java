package com.example.hw2.allfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.hw2.R;
import com.example.hw2.database.TodoListDao;
import com.example.hw2.database.TodoListDatabase;
import com.example.hw2.database.TodoListEntity;
import com.example.hw2.edit_res;

import java.util.List;


public class wodeFragment extends Fragment {

    private static final String ARG_SHOW_TEXT = "text";
    private String mContentText;
    private Button edit_res,addFri,ChangeId;
    private ImageView headImg;
    private TextView userName,douyinId,selfIntro,selfBirthday,Country,school,xingbie;

    public wodeFragment() {
        // Required empty public constructor
    }

    public static wodeFragment newInstance(String param1) {
        wodeFragment fragment = new wodeFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_wode, container, false);
        edit_res=rootView.findViewById(R.id.edit_Res);
        addFri=rootView.findViewById(R.id.add_Friends);
        ChangeId=rootView.findViewById(R.id.changeId);
        userName=rootView.findViewById(R.id.user_Name);
        douyinId=rootView.findViewById(R.id.douyinId);
        selfIntro=rootView.findViewById(R.id.self_Intro);
        xingbie=rootView.findViewById(R.id.xingbie);
        selfBirthday=rootView.findViewById(R.id.self_Old);
        Country=rootView.findViewById(R.id.Country);
        school=rootView.findViewById(R.id.school);
        ChangeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),com.example.hw2.logIn.class));
                initPage();
            }
        });
        edit_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),edit_res.class));//修改资料界面
                initPage();
            }
        });
        initPage();
        return rootView;
    }
    public void initPage()
    {
        TodoListDao dao = TodoListDatabase.inst(getActivity()).todoListDao();
        TodoListEntity temp=dao.getCurrent(true);
        List<String> lst=temp.getALot();
        userName.setText(lst.get(0));
        douyinId.setText("抖音号"+lst.get(1));
        selfIntro.setText(lst.get(2));
        xingbie.setText(lst.get(3));
        selfBirthday.setText((lst.get(4)));
        Country.setText((lst.get(5)));
        school.setText((lst.get(6)));

    }
}
