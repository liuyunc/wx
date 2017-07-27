package com.wx.android.fragment;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.android.EcgActivity;
import com.wx.android.HeartActivity;
import com.wx.android.R;
import com.wx.android.adapter.CommonFrameFragmentAdapter;
import com.wx.android.base.BaseFragment;
import com.wx.android.oxygenActivity;
import com.wx.android.pressureActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

/**
 *
 * 作用：蓝牙模块
 */
public class CommonFrameFragment extends BaseFragment implements View.OnClickListener{

    private ListView mListView;

    private String[] datas;

    private CommonFrameFragmentAdapter adapter;
//蓝牙

    private static final String TAG = CommonFrameFragment.class.getSimpleName();//"CommonFrameFragment"
    public static ArrayAdapter<String> mArrayAdapter;
    public static BluetoothAdapter mBluetoothAdapter;    //本地蓝牙适配器
    public static BluetoothDevice device;                //代表连接的蓝牙设备对象
    public String DeviceMAC = null;            //连接的蓝牙设备MAC地址 ;
    public String DeviceName = null;        //连接的蓝牙设备名称 ;

    /*Handler和其使用的状态识别常量*/
    public static Handler mHandler;    //信息处理机

    public static int CONNECT_STATE = 0;        //标示当前连接状态
    public static final int CONNECTING = 1;        //正在连接
    public static final int CONNECT_SUCCESS = 2;//连接成功
    public static final int CONNECT_BREAK = 3;    //连接断开
    public static final int CONNECT_FAIL = 4;    //连接设备时失败
    public static final int MESSAGE_READ = 7;    //接收数据
    public static final int MESSAGE_WRITE = 8;    //发送数据


    private boolean D = false;        //调试Toast信息 if条件
    //private boolean D=true;


    private Context context;
    public ImageView bluetoothOpen, bluetoothClose, connection, disConnection;
    public TextView Connect_State_TextView;
    Button btn_one, btn_two, btn_three, btn_four;

    private RelativeLayout back;
    private TextView title;

    private BluetoothGatt mBluetoothGatt;
    int dataInt_ECG, dataInt_Heart, dataInt_Oxygen, dataInt_Pre_high, dataInt_Pre_low;
    private byte EE = (byte) 0xEE; // 开始位
    private byte FF = (byte) 0xFF; // 结束位
    private byte[] data = new byte[4];
    private byte[] sendByte = {EE};


    Queue<Byte> queue = new LinkedList<>();
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt.discoverServices();//连接成功
                mHandler.obtainMessage(CONNECT_SUCCESS).sendToTarget();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mHandler.obtainMessage(CONNECT_BREAK).sendToTarget();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                Log.d(TAG, "发现Services");
                List<BluetoothGattService> mCustomService = mBluetoothGatt.getServices();

                if (mCustomService != null)
                    Log.d(TAG, "gattServiceList不为空");
                BluetoothGattService mBluetoothGattService = mCustomService.get(0);

                List<BluetoothGattCharacteristic> gattCharacteristicsList = mBluetoothGattService.getCharacteristics();
                BluetoothGattCharacteristic gattCharacteristic = gattCharacteristicsList.get(0);



                if (gattCharacteristic != null)
                    Log.d(TAG, "Characteristic不为空");


                final int charaProp = gattCharacteristic.getProperties();
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    setCharacteristicNotification(gattCharacteristic, true);
                }

                boolean Rssi = mBluetoothGatt.readRemoteRssi();
                if (Rssi)
                    Log.d(TAG, "Rssi可读为true");

            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //  broadcastUpdate(ACTION_DATA_AVAILABLE, ch
                Log.d(TAG, "Read有数据");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            // broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);

            final UUID MY_UUID = characteristic.getUuid();
            final BluetoothSocket mmSocket;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "创建设备RfcommSocket失败");
            }
            mmSocket = tmp;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "创建sockets不成功");
            }

            InputStream mmInStream = tmpIn;
            OutputStream mmOutStream = tmpOut;

            try {
                byte[] buffer = new byte[12];
                int num = mmInStream.read(buffer, 0, 1);
                if (num != 1) {
                    Log.d(TAG, "socket方法读不到");
                }

                // 接收另外3byte
                int bytes = 0;
                while (bytes < 11) {
                    num = mmInStream.read(buffer, bytes + 1, 11 - bytes);
                    if (num == -1)
                        break;
                    else
                        bytes += num;
                }
                if (bytes != 11 || buffer[11] != FF) {
                    // MyLog.v("结尾错", bytes + "" + buffer[3]);
                    //continue; // 接收的数据不完整或结尾错误
                }
                Log.d(TAG, "sockte方法数据：" + buffer[0]);
            } catch (Exception e) {
                mHandler.obtainMessage(CONNECT_BREAK).sendToTarget();
//				Log.e(MainActivity.TAG, "读取数据流断开disconnected", e);
            }


            Log.d(TAG, "the given characteristic has changed");

            if (characteristic.getValue() != null)
                Log.d(TAG, "可以得到数据");
            final byte[] datar = characteristic.getValue();
            if (datar != null && datar.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(datar.length);
                for (byte byteChar : datar) {
                    stringBuilder.append(String.format("%02X ", byteChar));
                    queue.offer(byteChar);
                }
                Log.d(TAG, "数据转字符串为：" + stringBuilder.toString());
                Log.d(TAG, "长度：" + queue.size());
            }
            while (queue.size() >= 4d) {

                for (int i = 0; i < 4; i++) {
                    data[i] = queue.poll();
                }
                final StringBuilder stringBuilder1 = new StringBuilder(data.length);
                for (byte byteChar : data) {
                    stringBuilder1.append(String.format("%02X ", byteChar));
                }
                Log.d(TAG, "待传数据为：" + stringBuilder1.toString());
                if (data[0] == EE && data[3] == FF) {
                    // 转换为整数 ECG
                    dataInt_ECG = ((0x0FF & data[1]) << 8)
                            + (0x0FF & (data[2] << 1));
                    dataInt_ECG >>= 1;


                    SendBroadCastOfData();
                    data[0] = (byte) 0xFE;
                    data[3] = (byte) 0xFE;
                }

            }
/*          //调用writeCharacteristic可以将自定义的byte数组sendByte从手机端发给蓝牙
            characteristic.setValue(sendByte);
            writeCharacteristic(characteristic);


                // 转换为整数 ECG
                dataInt_ECG = (  (0x0FF &  data[1]  ) << 8   )
                        + (  0x0FF & (data[2]<<1) );
                dataInt_ECG >>=1;

                // 转换为整数 Heart
                dataInt_Heart = (  (0x0FF &  data[3]  ) << 8   )
                        + (  0x0FF & (data[4]<<1) );
                dataInt_Heart >>=1;

                // 转换为整数 Oxygen
                dataInt_Oxygen = (  (0x0FF &  data[5]  ) << 8   )
                        + (  0x0FF & (data[6]<<1) );
                dataInt_Oxygen >>=1;
               // 转换为整数 Pre_high
                dataInt_Pre_high = (  (0x0FF &  data[7]  ) << 8   )
                        + (  0x0FF & (data[8]<<1) );
                dataInt_Pre_high >>=1;

                // 转换为整数 Pre_low
                dataInt_Pre_low = (  (0x0FF &  data[9]  ) << 8   )
                        + (  0x0FF & (data[10]<<1) );
                dataInt_Pre_low >>=1;

                SendBroadCastOfData() ;
            */

        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Write有数据------>");
            }
        }
    };

    void SendBroadCastOfData() {

        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra("DataSend", dataInt_ECG);                // ECG
        context.sendBroadcast(intent);
/*
        intent.setAction("android.intent.action.EDIT_Heart");
        intent.putExtra("HeartData",dataInt_Heart);             //心率
        context.sendBroadcast(intent);

        intent.setAction("android.intent.action.EDIT_Oxygen");
        intent.putExtra("OxygenData",dataInt_Oxygen);         //血氧
        context.sendBroadcast(intent);

        intent.setAction("android.intent.action.EDIT_Pre_high");
        intent.putExtra("Pre_highData",dataInt_Pre_high);       //收缩压
        intent.putExtra("Pre_lowData",dataInt_Pre_low);
        context.sendBroadcast(intent);*/
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        Log.d(TAG, "calling readCharacteristic ");
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

    }

    @Override
    protected View initView() {

        View view = View.inflate(mContext, R.layout.fragment_common_frame, null);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = datas[position];
                Toast.makeText(mContext, "data==" + data, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        super.initData();

        //准备数据
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main1, container, false);
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bluetoothOpen= (ImageView) view.findViewById(R.id.iv_open);
        bluetoothClose= (ImageView) view.findViewById(R.id.iv_close);
        connection= (ImageView) view.findViewById(R.id.imageView4);
        disConnection= (ImageView) view.findViewById(R.id.imageView5);
        Connect_State_TextView= (TextView) view.findViewById(R.id.connectSitId);


        btn_one = (Button) view.findViewById(R.id.btn_one);
        btn_two = (Button) view.findViewById(R.id.btn_two);
        btn_three = (Button) view.findViewById(R.id.btn_three);
        btn_four=(Button)view.findViewById(R.id.btn_four);


        context = getContext();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity().getApplicationContext(), "未找到蓝牙适配器", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.i(TAG, "获取本地蓝牙适配器");
            if(D)
                Toast.makeText(getActivity().getApplicationContext(), "蓝牙适配器已找到", Toast.LENGTH_SHORT).show();
        }
        //handle处理器，处理不同的连接状态
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECTING:

                        Connect_State_TextView.setText("正在连接……");
                        CONNECT_STATE = CONNECTING;
                        break;
                    case MESSAGE_READ:

                        break;
                    case MESSAGE_WRITE:

                        break;
                    case CONNECT_SUCCESS:
                        Connect_State_TextView.setText("已连接"+DeviceName);
                        CONNECT_STATE = CONNECT_SUCCESS;
                        //startPaintTimer();
                        break;
                    case CONNECT_FAIL:
                        Connect_State_TextView.setText("连接失败,请重试");
                        CONNECT_STATE = CONNECT_FAIL;

                        break;
                    case CONNECT_BREAK:
                        CONNECT_STATE =CONNECT_BREAK;

                        //ReceiveData_TextView.setText(ReceiveDataCount+"");
                        Connect_State_TextView.setText("连接断开");
                        //stopPaintTimer();

                        break;

                }
            }
        };

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);

        bluetoothOpen.setOnClickListener(this);
        bluetoothClose.setOnClickListener(this);
        connection.setOnClickListener(this);
        disConnection.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_one:
                intent = new Intent(getActivity(), EcgActivity.class);
                startActivity(intent);
                break;
              case R.id.btn_two:
                intent = new Intent(getActivity(), HeartActivity.class);
                startActivity(intent);
                break;
               case R.id.btn_three:
                intent = new Intent(getActivity(), oxygenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_four:
                intent = new Intent(getActivity(), pressureActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_open:
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    startActivity(enableBtIntent);
                    Log.i(TAG, "按键：打开蓝牙");
                }
                else
                    Toast.makeText(getActivity(), "蓝牙已经打开", Toast.LENGTH_SHORT).show();
                if(D)
                    Toast.makeText(getActivity(), "按键：打开蓝牙", Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_close:
                if (!mBluetoothAdapter.isEnabled())
                    Toast.makeText(getActivity(), "蓝牙未开启关闭", Toast.LENGTH_SHORT).show();
                else{
                    mBluetoothAdapter.disable();
                    Toast.makeText(getActivity(), "关闭蓝牙", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "关闭蓝牙");
                }
                if(D)
                    Toast.makeText(getActivity(), "按键：关闭蓝牙", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageView4:
                //设置适配器，存储已配对的设备
                mArrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }
                //弹出对话框，显示已配对设备
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()) ;
                builder.setAdapter(mArrayAdapter,new DialogInterface.OnClickListener() {
                    @Override
                    //点击项目，获得设备名称和物理地址
                    public void onClick(DialogInterface dialog, int which) {
                        if(CONNECT_STATE==CONNECT_SUCCESS){
                            Toast.makeText(getActivity(), "已连接设备，请先断开", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = mArrayAdapter.getItem(which).toString();// 获取选择项的值
                        //获取选择的蓝牙设备的 名称和地址
                        DeviceName=result.substring(0,result.length()-18);
                        DeviceMAC=result.substring(result.length()-17,result.length());

                        //在未打开蓝牙的情况下点击按键，因为Adapter列表为空，所以获得的MAC也为空
                        if(DeviceMAC==null)
                            Toast.makeText(getActivity(), "未选择设备", Toast.LENGTH_SHORT).show();
                        else{
                            device = mBluetoothAdapter.getRemoteDevice(DeviceMAC);
                            Log.i(TAG, "获取远程设备成功");
                            //开启连接线程
                            if(CONNECT_STATE!=CONNECT_SUCCESS){
                                mBluetoothGatt = device.connectGatt(context,false,mGattCallback);
                                mHandler.obtainMessage(CONNECTING).sendToTarget();

                            }
                        }

                        dialog.dismiss();  //关闭对话框
                        if(D)
                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                    }
                });
                if (!mBluetoothAdapter.isEnabled()){
                    //若未打开蓝牙，此时适配器内容为空，并提示未打开蓝牙
                    builder.setTitle("蓝牙未打开，请打开蓝牙");
                    builder.show();

                }else{
                    builder.setTitle("已配对的设备");
                    builder.show();
                }

                if(D)
                    Toast.makeText(getActivity(), "按键：连接", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageView5:
                Log.i(TAG, "点击“断开”按键");
                mHandler.obtainMessage(CONNECT_BREAK).sendToTarget();
                //检查当前状态，只有连接设备成功后，点击按键才会断开设备
                if(CONNECT_STATE==CONNECT_SUCCESS){
                    mBluetoothGatt.disconnect();

                }else
                    Toast.makeText(getActivity(), "未连接设备", Toast.LENGTH_SHORT).show();
                if(D)
                    Toast.makeText(getActivity(), "按键：断开", Toast.LENGTH_SHORT).show();

                break;
        }


    }
}
