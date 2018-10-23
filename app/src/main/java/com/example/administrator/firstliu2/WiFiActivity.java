package com.example.administrator.firstliu2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class WiFiActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_pwd;
    private Button btn_send;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi);
        initView();

    }

    private void sendData() {
        String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("appCmdCode1", 8);
        map.put("appCmdCode2", 8);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        MyApplication.tcpClient2.send(Constants.ACTION_WIFI, json);
        Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_send.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendData();
                break;
            case R.id.btn_next:
                startActivity(new Intent(WiFiActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

}
