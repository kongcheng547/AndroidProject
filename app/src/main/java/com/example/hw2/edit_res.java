package com.example.hw2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw2.database.TodoListDao;
import com.example.hw2.database.TodoListDatabase;
import com.example.hw2.database.TodoListEntity;

import java.util.ArrayList;
import java.util.List;

public class edit_res extends AppCompatActivity {
    EditText name,id,intro,xingbie,birthday,address,school;
    ImageView headImg;
    Button commit_Edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_res);
        name=findViewById(R.id.edit_name);
        id=findViewById(R.id.edit_douyinID);
        intro=findViewById(R.id.edit_Intro);
        xingbie=findViewById(R.id.edit_xingbie);
        birthday=findViewById(R.id.edit_birthday);
        address=findViewById(R.id.edit_address);
        school=findViewById(R.id.edit_School);
        headImg=findViewById(R.id.head_Img1);
        commit_Edit=findViewById(R.id.commit_edit);
        commit_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString()!=""&&id.getText().toString()!=""&&intro.getText().toString()!=""&&
                        xingbie.getText().toString()!=""&&birthday.getText().toString()!=""&&address.getText().toString()!=""&&school.getText().toString()!="")
                {
                    List<String> lst=new ArrayList<>();
                    lst.add(name.getText().toString());
                    lst.add(id.getText().toString());
                    lst.add(intro.getText().toString());
                    lst.add(xingbie.getText().toString());
                    lst.add(birthday.getText().toString());
                    lst.add(address.getText().toString());
                    lst.add(school.getText().toString());
                    TodoListDao dao = TodoListDatabase.inst(com.example.hw2.edit_res.this).todoListDao();
                    TodoListEntity temp=dao.getCurrent(true);
                    temp.setALot(lst);
                    dao.updateEntity(temp);
                    finish();
                }
                else
                {
                    Toast.makeText(edit_res.this,"请输入全部内容",Toast.LENGTH_LONG);
                }
            }
        });

    }

}
