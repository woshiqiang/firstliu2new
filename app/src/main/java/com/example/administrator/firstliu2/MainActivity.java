package com.example.administrator.firstliu2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.firstliu2.bean.CommonResult;
import com.example.administrator.firstliu2.bean.ResultStatus;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * @Date 2018-10-15.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AppCompatButton btn_aware;
    private AppCompatButton btn_sleep;
    private AppCompatButton btn_add_pre_temp;
    private AppCompatButton btn_sub_temp;
    private AppCompatButton btn_state;
    private AppCompatButton btn_trouble;
    private TextView tv_pre_temp;
    private TextView tv_temp;
    private TextView tv_tds;
    private TextView tv_level;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        bindReceiver();

    }

    private void initView() {
        btn_aware = (AppCompatButton) findViewById(R.id.btn_aware);
        btn_sleep = (AppCompatButton) findViewById(R.id.btn_sleep);
        btn_add_pre_temp = (AppCompatButton) findViewById(R.id.btn_add_pre_temp);
        btn_sub_temp = (AppCompatButton) findViewById(R.id.btn_sub_pre_temp);
        btn_state = (AppCompatButton) findViewById(R.id.btn_state);
        btn_trouble = (AppCompatButton) findViewById(R.id.btn_trouble);
        tv_pre_temp = (TextView) findViewById(R.id.tv_pre_temp);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_tds = (TextView) findViewById(R.id.tv_tds);
        tv_level = (TextView) findViewById(R.id.tv_level);

        btn_aware.setOnClickListener(this);
        btn_sleep.setOnClickListener(this);
        btn_add_pre_temp.setOnClickListener(this);
        btn_sub_temp.setOnClickListener(this);
        btn_state.setOnClickListener(this);
        btn_trouble.setOnClickListener(this);
    }

    private void bindReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_AWARE);
        intentFilter.addAction(Constants.ACTION_SLEEP);
        intentFilter.addAction(Constants.ACTION_ADD_PRE_TEMP);
        intentFilter.addAction(Constants.ACTION_SUB_PRE_TEMP);
        intentFilter.addAction(Constants.ACTION_GET_INFO);
        intentFilter.addAction(Constants.ACTION_STATE);
        intentFilter.addAction(Constants.ACTION_REPORT_TROUBLE);

        registerReceiver(new MyBroadcastReceiver(), intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_aware:
                aware();
                break;
            case R.id.btn_sleep:
                sleep();
                break;
            case R.id.btn_add_pre_temp:
                addPreTemp();
                break;
            case R.id.btn_sub_pre_temp:
                subPreTemp();
                break;
            case R.id.btn_state:
                getState();
                break;
            case R.id.btn_trouble:
                reportTrouble();
                break;
        }
    }

    /**
     * 上报维修
     */
    private void reportTrouble() {
        final View view = View.inflate(this, R.layout.view_dialog, null);
        final EditText et_dialog_location = view.findViewById(R.id.et_dialog_location);
        final EditText et_dialog_trouble = view.findViewById(R.id.et_dialog_trouble);
        new AlertDialog.Builder(this)
                .setTitle("上报维修")//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String location = et_dialog_location.getText().toString();//位置
                                String content = et_dialog_trouble.getText().toString();//故障内容
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("appCmdCode1", 5);
                                map.put("appCmdCode2", 5);
                                map.put("errorReport", content);
                                map.put("location", location);
                                Gson gson = new Gson();
                                String json = gson.toJson(map);
                                MyApplication.tcpClient.send(Constants.ACTION_REPORT_TROUBLE, json);
                            }
                        })
                .setNegativeButton("取消", null)
                .create()
                .show();

    }

    /**
     * 获取热水器的最新状态
     */
    private void getState() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appCmdCode1", 2);
        map.put("appCmdCode2", 2);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        MyApplication.tcpClient.send(Constants.ACTION_STATE, json);
    }

    /**
     * 降低预设水温
     */
    private void subPreTemp() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appCmdCode1", 1);
        map.put("appCmdCode2", 1);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        MyApplication.tcpClient.send(Constants.ACTION_SUB_PRE_TEMP, json);
    }

    /**
     * 提高预设水温
     */
    private void addPreTemp() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appCmdCode1", 0);
        map.put("appCmdCode2", 0);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        MyApplication.tcpClient.send(Constants.ACTION_ADD_PRE_TEMP, json);
    }

    /**
     * 让热水器进入睡眠模式
     */
    private void sleep() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appCmdCode1", 3);
        map.put("appCmdCode2", 3);
        Gson gson = new Gson();
        String json = gson.toJson(map);

        MyApplication.tcpClient.send(Constants.ACTION_SLEEP, json);
    }

    /**
     * 5. 唤醒热水器
     */
    private void aware() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appCmdCode1", 4);
        map.put("appCmdCode2", 4);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        MyApplication.tcpClient.send(Constants.ACTION_AWARE, json);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(Constants.RECEIVE_MSG);
            Log.d("MyBroadcastReceiver", "onReceive:" + msg);
            String mAction = intent.getAction();
            switch (mAction) {
                case Constants.ACTION_AWARE:
                    handleCommonResult("唤醒", msg);
//                    Toast.makeText(MainActivity.this, "唤醒：" + msg, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.ACTION_SLEEP:
                    handleCommonResult("睡眠", msg);
//                    Toast.makeText(MainActivity.this, "睡眠：" + msg, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.ACTION_ADD_PRE_TEMP:
                    handleCommonResult("提高", msg);
//                    Toast.makeText(MainActivity.this, "提高预设温度：" + msg, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.ACTION_SUB_PRE_TEMP:
                    handleCommonResult("降低", msg);
//                    Toast.makeText(MainActivity.this, "降低预设温度：" + msg, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.ACTION_STATE:
                    setStatus(msg);
//                    Toast.makeText(MainActivity.this, "最新状态：" + msg, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.ACTION_REPORT_TROUBLE:
                    handleCommonResult("维修", msg);
//                    Toast.makeText(MainActivity.this, "上报维修：" + msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void handleCommonResult(String type, String msg) {
        Gson gson = new Gson();
        CommonResult commonResult = gson.fromJson(msg, CommonResult.class);
        switch (type) {
            case "提高":
                if (commonResult.appCmdReplayCode1 == 0) {
                    tv_pre_temp.setText(String.valueOf(commonResult.maxTemp));
                } else {
                    Toast.makeText(this, "降低预设温度失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case "降低":
                if (commonResult.appCmdReplayCode1 == 0) {
                    tv_pre_temp.setText(String.valueOf(commonResult.maxTemp));
                } else {
                    Toast.makeText(this, "降低预设温度失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case "睡眠":
                if (commonResult.appCmdReplayCode1 == 0) {
                    Toast.makeText(this, "进入睡眠成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "进入睡眠失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case "唤醒":
                if (commonResult.appCmdReplayCode1 == 0) {
                    Toast.makeText(this, "唤醒成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "唤醒失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case "维修":
                if (commonResult.appCmdReplayCode1 == 0) {
                    Toast.makeText(this, "上报维修成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "上报维修失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     * 最新状态
     *
     * @param msg
     */
    private void setStatus(String msg) {
        try {
            Gson gson = new Gson();
            ResultStatus status = gson.fromJson(msg, ResultStatus.class);
            tv_tds.setText(String.valueOf(status.waterTDS));
            tv_level.setText(status.waterLevel);
            tv_temp.setText(String.valueOf(status.waterTemp));
            tv_pre_temp.setText(String.valueOf(status.maxTemp));
        } catch (Exception e) {
            Toast.makeText(this, "数据解析异常", Toast.LENGTH_SHORT).show();
        }

    }


}
