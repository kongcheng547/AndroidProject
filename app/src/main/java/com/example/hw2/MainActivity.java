package com.example.hw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.hw2.allfragments.guanzhuFragment;
import com.example.hw2.allfragments.shouyeFragment;
import com.example.hw2.allfragments.wodeFragment;
import com.example.hw2.allfragments.xiaoxiFragment;
import com.example.hw2.cameraactivity.MyCamera;
import com.example.hw2.database.TodoListDao;
import com.example.hw2.database.TodoListDatabase;
import com.example.hw2.database.TodoListEntity;

public class MainActivity extends AppCompatActivity{

    private RadioGroup mTabRadioGroup;
    private SparseArray<Fragment> mFragmentSparseArray;
    private boolean isLogIn=false;
    private static final String TAG = "TAG";


    //这边是摄像头拍摄的一些参数
    ImageView mCameraBtn;
    private final static int REQUEST_PERMISSION = 123;
    private String[] mPermissionsArrays = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化ratioGroup
        initRatioGroup();
        //初始化按钮
        initMyCameraButton();
        //最后刷新整个页面
    }

    private void initRatioGroup(){
        mTabRadioGroup = findViewById(R.id.rg);
        mFragmentSparseArray = new SparseArray<>();
        mFragmentSparseArray.append(R.id.shouye_tab, shouyeFragment.newInstance("首页"));
        mFragmentSparseArray.append(R.id.guanzhu_tab, guanzhuFragment.newInstance("搜索"));
        mFragmentSparseArray.append(R.id.xiaoxi_tab, xiaoxiFragment.newInstance("消息"));
        mFragmentSparseArray.append(R.id.wode_tab, wodeFragment.newInstance("我的"));
        mTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                TodoListDao dao = TodoListDatabase.inst(MainActivity.this).todoListDao();
                TodoListEntity temp=dao.getCurrent(true);
                if(temp==null)
                {
                    startActivity(new Intent(MainActivity.this, logIn.class));
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mFragmentSparseArray.get(checkedId)).commit();
            }
        });
        //默认显示第一个
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragmentSparseArray.get(R.id.shouye_tab)).commit();
    }




    //获取相关的权限
    private void initMyCameraButton(){
        mCameraBtn = findViewById(R.id.mCamera);
        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkPermissionAllGranted(mPermissionsArrays)){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        requestPermissions(mPermissionsArrays, REQUEST_PERMISSION);
                    }
                }
                else{
                    Log.i("lfy_tips","已经获取了所有所需权限");
                    startActivity(new Intent(MainActivity.this, MyCamera.class));
                }
            }
        });
    }

    //检查权限
    private boolean checkPermissionAllGranted(String[] permissions){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        for(String permission : permissions){
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }





    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "MainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "MainActivity onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "MainActivity onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "MainActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "MainActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MainActivity onDestroy");
    }
}
