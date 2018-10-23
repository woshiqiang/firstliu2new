package com.example.administrator.firstliu2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.firstliu2.bean.LoginResult;
import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private TextView tv_back, tv_register, tv_find_psw;//返回键,显示的注册，找回密码
    private Button btn_login;//登录按钮
    private String userName, psw, spPsw;//获取的用户名，密码，加密密码
    private EditText et_user_name, et_psw;//编辑框

    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

        bindReceiver();

        initData();
    }

    private void initData() {
        //判断是否登录过
        SharedPreferences sp = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(Constants.IS_LOGIN, false);
        if (isLogin) {
            String username = sp.getString(Constants.USERNAME, "");
            String password = sp.getString(Constants.PASSWORD, "");
            et_user_name.setText(username);
            et_psw.setText(password);
        }
    }

    private void bindReceiver() {
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_LOGIN);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }


    //获取界面控件
    private void init() {
        //从main_title_bar中获取的id
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back = findViewById(R.id.tv_back);
        //从activity_login.xml中获取的
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        //返回键的点击事件
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录界面销毁
                LoginActivity.this.finish();
            }
        });
        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到注册界面，并实现注册功能
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //找回密码控件的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到找回密码界面（此页面暂未创建）
            }
        });
        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录，获取用户名和密码 getText().toString().trim();
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //登录
                login(userName, psw);
            }
        });
    }

    /**
     * 登录方法
     *
     * @param account  用户名
     * @param password 密码
     */
    private void login(String account, String password) {
        HashMap<String, Object> cmdMap = new HashMap<>();
        cmdMap.put("appCmdCode1", 7);
        cmdMap.put("appCmdCode2", 7);
        cmdMap.put("account", account);
        cmdMap.put("password", password);
        Gson gson = new Gson();
        String json = gson.toJson(cmdMap);
        Log.d("LoginActivity", json);
        MyApplication.tcpClient.send(Constants.ACTION_LOGIN, json);
    }


    /**
     * 接收服务器消息
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction) {
                case Constants.ACTION_LOGIN:
                    try {
                        String msg = intent.getStringExtra(Constants.RECEIVE_MSG);
                        Log.d("MyBroadcastReceiver", "登录返回结果:" + msg);
                        Gson gson = new Gson();
                        LoginResult result = gson.fromJson(msg, LoginResult.class);
                        if ("OK".equalsIgnoreCase(result.loginReturn)) {
                            //登录成功
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Constants.USERNAME, userName);
                            editor.putString(Constants.PASSWORD, psw);
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            //登录失败
                            Toast.makeText(LoginActivity.this, result.loginReturn, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "数据解析异常"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }


}
