package top.hash070.rasp;

import android.annotation.SuppressLint;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "hash070";
    private ImageButton left;
    private ImageButton right;
    private ImageButton up;
    private ImageButton down;
    private Socket s;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String ip;
    private int port;

//    public static final int GOUP=1;
//    public static final int GODOWN=1;
//    public static final int GOLEFT=1;
//    public static final int GORIGHT=1;
    private Handler myHandler;
    private InputStream ins;
    private OutputStream ous;
    private BufferedWriter bw;
    private BufferedReader bufferedReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setWebView("192.168.1.180",8080);
        ip="192.168.1.180";
        port=7878;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initTCP(ip,port);
            }
        }).start();
        getWidget();
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
    protected void getWidget(){
        left=(ImageButton) findViewById(R.id.left);
        right=(ImageButton) findViewById(R.id.right);
        up=(ImageButton) findViewById(R.id.up);
        down=(ImageButton) findViewById(R.id.down);

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
            s = new Socket("192.168.1.180", 7878);
            ins = s.getInputStream();
            ous = s.getOutputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(ins));
            bw = new BufferedWriter(new OutputStreamWriter(ous));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private View.OnTouchListener upListener = new View.OnTouchListener() {//按钮监听
        public boolean onTouch(View arg0, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // 按下 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bw.write("1,50");
                            bw.flush();
                            Log.d(TAG, "run: 1,50");
                        } catch (IOException e) {
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
                            bw.write("0,0");
                            bw.flush();
                            Thread.sleep(100);
                            Log.d(TAG, "run: 0,0");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            return false;
        }
    };    private View.OnTouchListener downListener = new View.OnTouchListener() {//按钮监听
        public boolean onTouch(View arg0, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // 按下 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bw.write("2,50");
                            bw.flush();
                            Thread.sleep(100);
                            Log.d(TAG, "run: 2,50");
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
                            bw.write("0,0");
                            bw.flush();
                            Log.d(TAG, "run: 0,0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            return false;
        }
    };    private View.OnTouchListener leftListener = new View.OnTouchListener() {//按钮监听
        public boolean onTouch(View arg0, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // 按下 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bw.write("3,50");
                            bw.flush();
                            Thread.sleep(100);
                            Log.d(TAG, "run: 3,50");
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
                            bw.write("0,0");
                            bw.flush();
                            Log.d(TAG, "run: 0,0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            return false;
        }
    };    private View.OnTouchListener rightListener = new View.OnTouchListener() {//按钮监听
        public boolean onTouch(View arg0, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // 按下 todo 处理相关逻辑
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bw.write("4,50");
                            bw.flush();
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
                            bw.write("0,0");
                            bw.flush();
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