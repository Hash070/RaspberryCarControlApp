package top.hash070.rasp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

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
    private WebView myWebView;
    private int running_speed;//运行速度
    private int camera_angle;//转向角度
    private TextView speed_text;

    private static String sshName ="root";
    private static String sshPw ="";
    private static int sshPort =22;

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
        setWebView(ip, 8080);
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
        speed_text = (TextView) findViewById(R.id.speed_textview);
    }

    protected void setWebView(String ip, int port) {
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

    protected void initUDP() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "192.168.1.1");//默认ip:192.168.1.1
        port = sharedPreferences.getInt("port", 7878);//默认端口:7878
        running_speed = sharedPreferences.getInt("speed", 50);//默认速度50
        sshName= sharedPreferences.getString("sshname","root");//默认root
        sshPw=sharedPreferences.getString("sshpw","0");
        new Thread(new Runnable() {
            @Override
            public void run() {
                createUDP(ip, port);
            }
        }).start();
        myWebView.loadUrl("http://" + ip + ":" + 8080 + "/?action=stream");
        speed_text.setTextColor(Color.GREEN);
        speed_text.setText("当前速度为："+running_speed);
    }

    private void createUDP(String ip, int port) {
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
                            sendData = ("1,"+running_speed+",0,0,0,1").getBytes();
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
                            sendData = ("2,"+running_speed+",0,0,0,1").getBytes();
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
                            sendData = ("3,"+running_speed+",0,0,0,1").getBytes();
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
                            sendData = ("4,"+running_speed+",0,0,0,1").getBytes();
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
                } catch (IOException e) {
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
                } catch (IOException e) {
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
                } catch (IOException e) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void inc_speed(View view) {//速度增加
        SharedPreferences data = getSharedPreferences("data", Context.MODE_PRIVATE);
        if (running_speed>0&&running_speed<=100){
            if (running_speed<100) running_speed+=10;//最高速度为100
            data.edit()
                    .putInt("speed", running_speed)
                    .apply();//写入
        }
        speed_text.setText("当前速度为："+running_speed);
    }

    public void desc_speed(View view) {//速度减少
        SharedPreferences data = getSharedPreferences("data", Context.MODE_PRIVATE);
        if (running_speed>0&&running_speed<=100){
            if (running_speed>20) running_speed-=10;//最低速度为20
            data.edit()
                    .putInt("speed", running_speed)
                    .apply();//写入
        }
        speed_text.setText("当前速度为："+running_speed);
    }

    public ArrayList<String> execSSH(String command){
        ArrayList<String> output = new ArrayList<>();
        try{
            JSch jsch=new JSch();
            Session session = jsch.getSession(sshName,ip, sshPort);
            session.setPassword(sshPw);
            // username and password will be given via UserInfo interface.
            session.setUserInfo(new MyUserInfo());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

//            String command="ifconfig";

            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);

            // X Forwarding
            // channel.setXForwarding(true);

            //channel.setInputStream(System.in);
            channel.setInputStream(null);

            //channel.setOutputStream(System.out);

            //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            //((ChannelExec)channel).setErrStream(fos);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream in=channel.getInputStream();

            channel.connect();


            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);//返回的是读取的长度
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                    output.add(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    if(in.available()>0) continue;
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
            session.disconnect();
        }
        catch(Exception e){
            System.out.println(e);
        }
        return output;
    }

    public void fix_error(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                execSSH("sudo systemctl restart carcil.service");
            }
        }).start();
    }

    public void fix_settings(View view) {//ssh连接设置
        layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.fix_setting_layout, null);
        EditText fix_name = (EditText) v.findViewById(R.id.fix_name);
        EditText fix_pw = (EditText) v.findViewById(R.id.fix_pw);
        fix_name.setText(sshName);
        fix_pw.setText(sshPw);
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
                if (fix_name.getText().toString().length() != 0 && fix_pw.getText().toString().length() != 0) {
                    Log.i(TAG, "onClick:" + fix_name.getText().toString());
                    Log.i(TAG, "onClick:" + fix_pw.getText().toString());
                    SharedPreferences data = getSharedPreferences("data", Context.MODE_PRIVATE);
                    data.edit()
                            .putString("sshname", fix_name.getText().toString())
                            .putString("sshpw", fix_pw.getText().toString())
                            .apply();//写入
                    initUDP();//刷新UDP连接信息
                    alertDialog.cancel();
                } else {
                    Toast.makeText(MainActivity.this, "登录名和密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class MyUserInfo implements UserInfo {
        @Override
        public String getPassphrase() {
            System.out.println("getPassphrase");
            return null;
        }
        @Override
        public String getPassword() {
            System.out.println("getPassword");
            return null;
        }
        @Override
        public boolean promptPassword(String s) {
            System.out.println("promptPassword:"+s);
            return false;
        }
        @Override
        public boolean promptPassphrase(String s) {
            System.out.println("promptPassphrase:"+s);
            return false;
        }
        @Override
        public boolean promptYesNo(String s) {
            System.out.println("promptYesNo:"+s);
            return true;//notice here!
        }
        @Override
        public void showMessage(String s) {
            System.out.println("showMessage:"+s);
        }
    }
}