package com.example.administrator.firstliu2;

/**
 * @Date 2018-10-15.
 */
public class Constants {
    //app和服务端
    public static final String IP = "47.100.241.170";
    public static final int PORT = 2333;
    //app和热水器
    public static final String IP2 = "192.168.4.1";
    public static final int PORT2 = 8080;

    //SharedPreferences
    public static final String SP_NAME = "loginInfo";
    public static final String IS_LOGIN = "isLogin";
    public static final String USERNAME = "username";
    public static final String PASSWORD= "password";



    public static final String RECEIVE_MSG = "receiveMsg";
    //action
    public static final String ACTION_LOGIN = "action_login";// 用户或者管理员登陆
    public static final String ACTION_REGISTER = "action_register";// 用户注册
    public static final String ACTION_ADD_PRE_TEMP = "action_add_pre_temp";//提高预设水温
    public static final String ACTION_SUB_PRE_TEMP = "action_sub_pre_temp";//降低预设水温
    public static final String ACTION_STATE = "action_state";//获取热水器的最新状态
    public static final String ACTION_SLEEP = "action_sleep";//让热水器进入睡眠模式
    public static final String ACTION_AWARE = "action_aware";//唤醒热水器
    public static final String ACTION_REPORT_TROUBLE = "action_report_trouble";//上报维修
    public static final String ACTION_GET_INFO = "action_get_info";//用于管理员获得特定序列号的热水器的地址和使用者联系方式

    public static final String ACTION_WIFI = "action_wifi";//app和热水器通讯，连接wifi


}
