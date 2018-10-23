package com.example.administrator.firstliu2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private TextView tv_back;//返回按钮
    private Button btn_register;//注册按钮
    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name, et_psw, et_psw_again;
    //用户名，密码，再次输入的密码的控件的获取值
    private String userName, psw, pswAgain, name, serialNum;
    //标题布局
    private RelativeLayout rl_title_bar;
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    private EditText et_serial_num;//序列号
    private EditText et_name;//姓名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局 ,注册界面
        setContentView(R.layout.activity_regisiter);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

        bindReceiver();
    }

    private void bindReceiver() {
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_REGISTER);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back = findViewById(R.id.tv_back);
        //布局根元素
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        //从activity_register.xml 页面中获取对应的UI控件
        btn_register = findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_psw_again);
        et_serial_num = findViewById(R.id.et_serial_num);
        et_name = findViewById(R.id.et_name);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                RegisterActivity.this.finish();
            }
        });
        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                //判断输入框内容
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                } else if (!psw.equals(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "请输入姓名！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(serialNum)) {
                    Toast.makeText(RegisterActivity.this, "请输入热水器序列号！", Toast.LENGTH_SHORT).show();
                } else {
                    //发送注册指令
                    sendRegister();
                }


            }
        });
    }

    /**
     * 发送注册指令
     */
    private void sendRegister() {
        HashMap<String, Object> cmdMap = new HashMap<>();
        cmdMap.put("appCmdCode1", 8);
        cmdMap.put("appCmdCode2", 8);
        cmdMap.put("account", userName);
        cmdMap.put("password", psw);
        cmdMap.put("userName", name);//姓名
        cmdMap.put("serialNum", serialNum);//序列号

        Gson gson = new Gson();
        String json = gson.toJson(cmdMap);
        Log.d("RegisterActivity", json);
        MyApplication.tcpClient.send(Constants.ACTION_REGISTER, json);

    }


    /**
     * 获取控件中的字符串
     */
    private void getEditString() {
        userName = et_user_name.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
        pswAgain = et_psw_again.getText().toString().trim();
        name = et_name.getText().toString();
        serialNum = et_serial_num.getText().toString();
    }


    /**
     * 接收服务器消息
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction) {
                case Constants.ACTION_REGISTER:
                    String msg = intent.getStringExtra(Constants.RECEIVE_MSG);
                    Log.d("MyBroadcastReceiver", "注册返回结果:" + msg);
                    if (msg.contains("OK")) {
                        //注册成功,跳转
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, WiFiActivity.class));
                        RegisterActivity.this.finish();
                    } else {
                        //注册失败
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    }
}
