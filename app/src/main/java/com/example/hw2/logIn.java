package com.example.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw2.database.TodoListDao;
import com.example.hw2.database.TodoListDatabase;
import com.example.hw2.database.TodoListEntity;


public class logIn extends AppCompatActivity {

    private Button logIn,signIn,leftBold;
    private EditText tele,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logIn=findViewById(R.id.logInBtn);
        signIn=findViewById(R.id.signIn);
        tele=findViewById(R.id.editTextTele);
        password=findViewById(R.id.password);
        leftBold=findViewById(R.id.leftBold);
        ManageLog();

    }
    public void ManageLog()
    {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tele.getText().toString()=="请输入手机号"||password.getText().toString()=="请输入密码")
                {
                    Toast.makeText(getApplication(), "请输入完整信息", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    TodoListDao dao = TodoListDatabase.inst(com.example.hw2.logIn.this).todoListDao();
                    TodoListEntity temp=dao.getIfTheIdIsUsed(tele.getText().toString());
                    if(temp==null)//为空可能判断错误
                    {
                        TodoListEntity temp2=new TodoListEntity(tele.getText().toString(),password.getText().toString());
                        dao.addTodo(temp2);
                        Toast.makeText(getApplication(),"注册成功，完善资料",Toast.LENGTH_LONG).show();
                        //登陆成功需要进行修改，让之前的isCurrent为false，现在的是true
                        TodoListEntity temp1=dao.getCurrent(true);
                        if(temp1!=null){
                            temp1.setIsCurrent(false);
                            dao.updateEntity(temp1);
                        }
                        temp2.setIsCurrent(true);
                        dao.updateEntity(temp2);

                        startActivity(new Intent(getApplication(), edit_res.class));
                        Intent intent=new Intent();
                        intent.putExtra("isLoged",1);
                        setResult(102,intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplication(),"本手机号已经被注册",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tele.getText().toString()=="请输入手机号"||password.getText().toString()=="请输入密码")
                {
                    Toast.makeText(getApplication(), "请输入完整信息", Toast.LENGTH_LONG).show();

                }
                else
                {
                    TodoListDao dao = TodoListDatabase.inst(com.example.hw2.logIn.this).todoListDao();
                    TodoListEntity temp=dao.getIfThePasswordIsRight(tele.getText().toString());
                    //Toast.makeText(getApplication(),temp.getPassword()+"kk"+password.getText().toString(),Toast.LENGTH_SHORT).show();
                    if(temp==null)
                    {
                        Toast.makeText(getApplication(),"账号不存在，请点击注册进行注册",Toast.LENGTH_LONG).show();
                    }
                    else if(temp.getPassword().equals(password.getText().toString()))
                    {
                        Toast.makeText(getApplication(),"登录成功,返回界面",Toast.LENGTH_LONG).show();
                        //登陆成功需要进行修改，让之前的isCurrent为false，现在的是true
                        TodoListEntity temp1=dao.getCurrent(true);
                        if(temp1!=null)
                            temp1.setIsCurrent(false);
                        temp.setIsCurrent(true);//将当前的设为true
                        dao.updateEntity(temp,temp1);
                        //TodoListEntity temp3=dao.getIfTheIdIsUsed(tele.getText().toString());
                        //Toast.makeText(getApplication(),temp3.getIsCurrent()+"lll",Toast.LENGTH_LONG).show();
                        //这里跳转界面,登录成功了
                        Intent intent=new Intent();
                        intent.putExtra("isLoged",1);
                        setResult(102,intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplication(),"密码错误，请重新输入",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        leftBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
