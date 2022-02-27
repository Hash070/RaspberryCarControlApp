package top.hash070.rasp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private LayoutInflater layoutInflater;
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
        getComponent();
        initUDP();
        up.setOnTouchListener(upListener);
        down.setOnTouchListener(downListener);
        left.setOnTouchListener(leftListener);
        right.setOnTouchListener(rightListener);
    }

    private void getComponent() {
        left = (ImageButton) findViewById(R.id.left);
        right = (ImageButton) findViewById(R.id.right);
        up = (ImageButton) findViewById(R.id.up);
        down = (ImageButton) findViewById(R.id.down);
    }

    protected void setWebView(String ip, int port) {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView = (WebView) findViewById(R.id.webview);//获取view
        WebSettings WebSet = myWebView.getSettings();    //获取webview设置
        WebSet.setJavaScriptEnabled(true);              //设置JavaScript支持
        WebSet.setSupportZoom(true);            // 设置可以支持缩放
        WebSet.setBuiltInZoomControls(true);    // 设置出现缩放工具
        WebSet.setUseWideViewPort(true);        //扩大比例的缩放
        WebSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);   //自适应屏幕
        WebSet.setLoadWithOverviewMode(true);
        myWebView.loadUrl("http://" + ip + ":" + port + "/?action=stream");
    }

    @SuppressLint("HandlerLeak")
    protected void initUDP() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "192.168.1.1");//默认ip:192.168.1.1
        port = sharedPreferences.getInt("port", 7878);//默认端口:7878
        setWebView(ip, 8080);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initTCP(ip, port);
            }
        }).start();
//        myHandler=new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                switch (msg.what){
//                    case GOLEFT:
//                }
//            }
//        };
    }

    private void initTCP(String ip, int port) {
        try {
            s = new DatagramSocket();
            addr = new InetSocketAddress(ip, port);
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
                            sendData = ("1,50,0,0,0,1").getBytes();
                            datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
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
                            sendData = ("0,0,0,0,0,1").getBytes();
                            datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
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
                            sendData = ("2,50,0,0,0,1").getBytes();
                            datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
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
                            sendData = ("0,0,0,0,0,1").getBytes();
                            datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
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
                            sendData = ("3,50,0,0,0,1").getBytes();
                            datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
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
                            sendData = ("0,0,0,0,0,1").getBytes();
                            datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
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
                            sendData = ("4,50,0,0,0,1").getBytes();
                            datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
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
                            sendData = ("0,0,0,0,0,1").getBytes();
                            datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
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

    public void settings(View view) {
        layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.setting_layout, null);
        EditText ip_addr = (EditText) v.findViewById(R.id.ip_addr);
        EditText car_port = (EditText) v.findViewById(R.id.port);
        ip_addr.setText(ip);
        car_port.setText(String.valueOf(port));
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(v);
        builder.setPositiveButton("确定", null);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ip_addr.getText().toString().length() != 0 && car_port.getText().toString().length() != 0) {
                    Log.i(TAG, "onClick:" + ip_addr.getText().toString());
                    Log.i(TAG, "onClick:" + car_port.getText().toString());
                    SharedPreferences data = getSharedPreferences("data", Context.MODE_PRIVATE);
                    data.edit()
                            .putString("ip", ip_addr.getText().toString())
                            .putInt("port", Integer.parseInt(car_port.getText().toString()))
                            .apply();//写入
                    initUDP();//刷新UDP连接信息
                    alertDialog.cancel();
                } else {
                    Toast.makeText(MainActivity.this, "ip或端口不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void s_right(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendData = ("0,0,1,1,20,2").getBytes();
                    datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
                    s.send(datagramPacket);
                    Log.d(TAG, "右转20度");
                    Thread.sleep(100);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void s_left(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendData = ("0,0,0,1,20,2").getBytes();
                    datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
                    s.send(datagramPacket);
                    Log.d(TAG, "左转20度");
                    Thread.sleep(100);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void s_up(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendData = ("0,0,1,2,10,2").getBytes();
                    datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
                    s.send(datagramPacket);
                    Log.d(TAG, "抬头10度");
                    Thread.sleep(100);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void s_down(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendData = ("0,0,0,2,10,2").getBytes();
                    datagramPacket = new DatagramPacket(sendData, sendData.length, addr);
                    s.send(datagramPacket);
                    Log.d(TAG, "低头10度");
                    Thread.sleep(100);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}