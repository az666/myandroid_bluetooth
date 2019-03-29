//https://github.com/akexorcist
//https://github.com/GayratRakhimov/LabBluetooth/blob/master/app/src/main/res/values/dimens.xml
//https://www.youtube.com/watch?v=DtiAzGnIwS0&t=58s

package bluetooth.tv.example.com.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
public class MainActivity extends AppCompatActivity {
    //备注：接收消息为带换行符的字符串
    final String ON = "F";
    final String OFF = "G";
    BluetoothSPP bluetooth;
    Button connect;
    ImageButton on, ledon;
    ImageButton off, ledoff;
    private TextView  time;
    private TextView yi_val;
    private TextView jia_val;
    private  ImageButton jia_add;
    private ImageButton jia_sub;
    private ImageButton yi_add;
    private ImageButton yi_sub;
    private ImageButton changeall;
    private String TAG = "MainActivity Configuration SCREEN";
    private String TAG1 = "Main Bluetooth Send";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();  //icon on toolbar
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        bluetooth = new BluetoothSPP(this);
        connect = findViewById(R.id.connect);
        on =  findViewById(R.id.on);
        off = findViewById(R.id.off);
        ledon =  findViewById(R.id.ledon);
        ledoff =  findViewById(R.id.ledoff);
        time = findViewById(R.id.time);
        jia_val = findViewById(R.id.jia_val);
        yi_val = findViewById(R.id.yi_val);
        jia_add = findViewById(R.id.jia_add);
        jia_sub = findViewById(R.id.jia_sub);
        yi_add = findViewById(R.id.yi_add);
        yi_sub = findViewById(R.id.yi_sub);
        changeall = findViewById(R.id.changeall);
        if (!bluetooth.isBluetoothAvailable()) {   //检查蓝牙是否可用
            Toast.makeText(getApplicationContext(), "蓝牙不可用！", Toast.LENGTH_SHORT).show();
            finish();
        }
        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {  //链接监听器
            public void onDeviceConnected(String name, String address) {
                connect.setText("Connected to " + name);
            }
            public void onDeviceDisconnected() {
                connect.setText("Connection lost");
            }
            public void onDeviceConnectionFailed() {
                connect.setText("Unable to connect");
            }
        });
        jia_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send("A", true);
            }
        });
        jia_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send("B", true);
            }
        });
        yi_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send("C", true);
            }
        });
        yi_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send("D", true);
            }
        });
        changeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send("E", true);
            }
        });



        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetooth.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);   //•意图选择设备活动
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String msg = "Application send a '1' to target via Bluetooth";
              //  bluetoothMsg(msg);
                bluetooth.send(ON, true);
                Log.e(TAG1, "SEND_ON");
                on.setVisibility(View.INVISIBLE); //ON and OFF button
                off.setVisibility(View.VISIBLE); //ON and OFF button
                ledon.setVisibility(View.VISIBLE);
                ledoff.setVisibility(View.INVISIBLE);
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String msg = "Application send a '0' to target via Bluetooth";
               // if (bluetoothMsg(msg))
               //     return;
                bluetooth.send(OFF, true);
                Log.e(TAG1, "SEND_OFF");
                off.setVisibility(View.INVISIBLE);
                on.setVisibility(View.VISIBLE);
                ledoff.setVisibility(View.VISIBLE);
                ledon.setVisibility(View.INVISIBLE);
            }
        });
        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {   //数据接收监听器
            @Override
            public void onDataReceived(byte[] data, String message) {
                Log.e(TAG1, String.valueOf(data));
                //Toast.makeText(getApplicationContext(), String.valueOf(data) , Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(),message , Toast.LENGTH_SHORT).show();
                if (message.length()== 10) {
                    time.setText(message.substring(0, 2)+":"+message.substring(2, 4));
                    jia_val.setText(message.substring(4, 7));
                    yi_val.setText(message.substring(7, 10));
                }
            }
        });
    }
    public boolean bluetoothMsg(String msg) {
        if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            return false;
        } else {
            Toast.makeText(MainActivity.this, "还没有蓝牙连接，请先连接设备...", Toast.LENGTH_LONG).show();
            return true;
        }
    }
    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);  //用于与任何与蓝牙串口配置文件模块通信的微控制器连接
            }
        }
    }
    public void onDestroy() {
        super.onDestroy();   //调用父类同名方法
        bluetooth.stopService();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "蓝牙不可用，请打开蓝牙！"
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    public void onConfigurationChanged(Configuration newConfig) {       //链接状态
        super.onConfigurationChanged(newConfig);
        Toast.makeText(MainActivity.this, "Configuration state changes", Toast.LENGTH_LONG).show();
        LinearLayout ly = (LinearLayout) findViewById(R.id.activity_main);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e(TAG, "ORIENTATION_LANDSCAPE");
            //setContentView(R.layout.main);
//            setContentView(R.drawable.bg3);
            ly.setBackgroundResource(R.drawable.bg3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e(TAG, "ORIENTATION_PORTRAIT");
            ly.setBackgroundResource(R.drawable.bg);
        }
    }
}