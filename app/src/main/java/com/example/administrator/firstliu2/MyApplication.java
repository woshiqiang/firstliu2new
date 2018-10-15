package com.example.administrator.firstliu2;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Date 2018-10-15.
 */
public class MyApplication extends Application {
    public static TcpClient tcpClient;
    public static TcpClient tcpClient2;

    public static ExecutorService exec = Executors.newCachedThreadPool();

    @Override
    public void onCreate() {
        super.onCreate();
        tcpClient = new TcpClient(this, Constants.IP, Constants.PORT);
        tcpClient2 = new TcpClient(this, Constants.IP2, Constants.PORT2);
        exec.execute(tcpClient);
        exec.execute(tcpClient2);
    }


}
