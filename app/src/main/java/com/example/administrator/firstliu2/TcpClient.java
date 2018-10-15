package com.example.administrator.firstliu2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient implements Runnable {
    private String TAG = "TcpClient";
    private PrintWriter pw;
    private InputStream is;
    private DataInputStream dis;
    private boolean isRun = true;
    private Socket socket = null;
    byte buff[] = new byte[4096];
    private String rcvMsg;
    private int rcvLen;
    private Context mContext;
    private String action;
    private String serverIP;
    private int port;

    public TcpClient(Context mContext, String ip, int port) {
        this.mContext = mContext;
        this.serverIP = ip;
        this.port = port;
    }

    public void closeSelf() {
        isRun = false;
    }

    public void send(String action, String msg) {
        this.action = action;
        pw.println(msg);
        pw.flush();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverIP, port);
            socket.setSoTimeout(15000);
            pw = new PrintWriter(socket.getOutputStream(), true);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRun) {
            try {
                rcvLen = dis.read(buff);
                rcvMsg = new String(buff, 0, rcvLen, "GBK");
                Log.i(TAG, "run: 收到消息:" + rcvMsg);
                Intent intent = new Intent();
                intent.setAction(action);
                Log.i(TAG, "run: action:" + action);
                intent.putExtra(Constants.RECEIVE_MSG, rcvMsg);
                mContext.sendBroadcast(intent);//将消息发送给主界面
                if (rcvMsg.equals("QuitClient")) {   //服务器要求客户端结束
                    isRun = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pw.close();
            is.close();
            dis.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
