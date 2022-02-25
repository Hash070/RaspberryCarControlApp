package top.hash070.rasp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "hash070";
    private ImageButton left;
    private ImageButton right;
    private ImageButton up;
    private ImageButton down;
//    private Socket s;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String ip;
    private int port;
    private DatagramSocket s;
    private InetSocketAddress addr;
    private byte[] sendData;
    private DatagramPacket datagramPacket;

    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setWebView(ip,8080);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initTCP(ip,port);
            }
        }).start();
        init();
        up.setOnTouchListener(upListener);
        down.setOnTouchListener(downListener);
        left.setOnTouchListener(leftListener);
        right.setOnTouchListener(rightListener);
    }
    protected void setWebView(String ip,int port){
        WebView myWebView =(WebView) findViewById(R.id.webview);
        myWebView = (WebView) findViewById(R.id.webview);//获取view
        WebSettings WebSet = myWebView.getSettings();    //获取webview设置
        WebSet.setJavaScriptEnabled(true);              //设置JavaScript支持
        WebSet.setSupportZoom(true);            // 设置可以支持缩放
        WebSet.setBuiltInZoomControls(true);    // 设置出现缩放工具
        WebSet.setUseWideViewPort(true);        //扩大比例的缩放
        WebSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);   //自适应屏幕
        WebSet.setLoadWithOverviewMode(true);
        myWebView.loadUrl("http://"+ip+":"+port+"/?action=stream");
    }
    @SuppressLint("HandlerLeak")
    protected void init(){
        left=(ImageButton) findViewById(R.id.left);
        right=(ImageButton) findViewById(R.id.right);
        up=(ImageButton) findViewById(R.id.up);
        down=(ImageButton) findViewById(R.id.down);
        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip","192.168.1.1");//默认ip:192.168.1.1
        port = sharedPreferences.getInt("port",7878);//默认端口:7878
//        myHandler=new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                switch (msg.what){
//                    case GOLEFT:
//                }
//            }
//        };
    }
    private void initTCP(String ip,int port){
        try {
            s = new DatagramSocket();
            addr = new InetSocketAddress(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private View.OnTouchListener upListener = new View.OnTouchListener() {//按钮监听
        @Override
        public boolean onTouch(View arg0, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // 按下 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendData=("1,50").getBytes();
                            datagramPacket=new DatagramPacket(sendData,sendData.length,addr);
                            s.send(datagramPacket);
                            Log.d(TAG, "run: 1,50");
                            Thread.sleep(100);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (action == MotionEvent.ACTION_UP) {
                // 松开 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendData=("0,0").getBytes();
                            datagramPacket=new DatagramPacket(sendData,sendData.length,addr);
                            s.send(datagramPacket);
                            Log.d(TAG, "run: 0,0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            return false;
        }
    };
    private View.OnTouchListener downListener = new View.OnTouchListener() {//按钮监听
        @Override
        public boolean onTouch(View arg0, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // 按下 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendData=("2,50").getBytes();
                            datagramPacket=new DatagramPacket(sendData,sendData.length,addr);
                            s.send(datagramPacket);
                            Log.d(TAG, "run: 2,50");
                            Thread.sleep(100);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (action == MotionEvent.ACTION_UP) {
                // 松开 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendData=("0,0").getBytes();
                            datagramPacket=new DatagramPacket(sendData,sendData.length,addr);
                            s.send(datagramPacket);
                            Log.d(TAG, "run: 0,0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            return false;
        }
    };
    private View.OnTouchListener leftListener = new View.OnTouchListener() {//按钮监听
        @Override
        public boolean onTouch(View arg0, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // 按下 todo 处理按钮按下逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendData=("3,50").getBytes();
                            datagramPacket=new DatagramPacket(sendData,sendData.length,addr);
                            s.send(datagramPacket);
                            Log.d(TAG, "run: 3,50");
                            Thread.sleep(100);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (action == MotionEvent.ACTION_UP) {
                // 松开 todo 处理按钮松开逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendData=("0,0").getBytes();
                            datagramPacket=new DatagramPacket(sendData,sendData.length,addr);
                            s.send(datagramPacket);
                            Log.d(TAG, "run: 0,0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            return false;
        }
    };
    private View.OnTouchListener rightListener = new View.OnTouchListener() {//按钮监听
        @Override
        public boolean onTouch(View arg0, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // 按下 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendData=("4,50").getBytes();
                            datagramPacket=new DatagramPacket(sendData,sendData.length,addr);
                            s.send(datagramPacket);
                            Log.d(TAG, "run: 4,50");
                            Thread.sleep(100);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (action == MotionEvent.ACTION_UP) {
                // 松开 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendData=("0,0").getBytes();
                            datagramPacket=new DatagramPacket(sendData,sendData.length,addr);
                            s.send(datagramPacket);
                            Log.d(TAG, "run: 0,0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            return false;
        }
    };
}