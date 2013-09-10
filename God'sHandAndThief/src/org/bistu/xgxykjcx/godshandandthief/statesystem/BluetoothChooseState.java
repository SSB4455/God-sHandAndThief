package org.bistu.xgxykjcx.godshandandthief.statesystem;

import java.util.ArrayList;
import java.util.HashMap;

import org.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import org.bistu.xgxykjcx.godshandandthief.BluetoothChatService;
import org.bistu.xgxykjcx.godshandandthief.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

public class BluetoothChooseState implements IGameObject {
	// Debugging
	private final String Tag = "BluetoothChooseState";
	
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	
	// Intent request codes
	private static int REQUEST_CONNECT_DEVICE = 1;
	private static int REQUEST_ENABLE_BT = 2;
	
	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	
	public static final int THTIF = 0;
	public static final int GODSHAND = 1;
	public static final String THITF_TYPE = "_Thief";
	public static final String GODSHAND_TYPE = "_GodsHand";
	
	
	private long brushTime;
	
	private Context context;
	private StateSystem stateSystem;
	
	private boolean canConnect, brushFlag;
	public boolean connected;
	String mtype, findName, chooseName, chooseAddress;
	
	private AlertDialog.Builder dialog;
	private Bitmap waitMoment;
	
	private Paint paint;
	
	
	// 创建一个接收ACTION_FOUND广播的BroadcastReceiver
	private final BroadcastReceiver mReceiver;
	// Name of the connected device
	private String mConnectedDeviceName = null;
	
	private ArrayList<String> devicesName, oneMessages, twoMessages;
	private HashMap<String, String> devicesAddress;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	BluetoothAdapter mBluetoothAdapter;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	
	
	public BluetoothChooseState(StateSystem stateSystem, int type) {
		this.context = MainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		
		waitMoment = BitmapStorage.getWaitMoment();
		
		brushTime = 12000;
		brushFlag = true;
		
		devicesName = new ArrayList<String>();
		oneMessages = new ArrayList<String>();
		twoMessages = new ArrayList<String>();
		devicesAddress = new HashMap<String, String>();
		
		dialog = new AlertDialog.Builder(context);
		dialog.setTitle("请选择连接设备：");
		//dialog.show();
		
		/*Button button = new Button(context);
		button.setText("hello");
		((MainActivity) context).relativeLayout.addView(button);
		button.layout(100, 100, 150, 150);
		*/
		
		//LinearLayout mLinearLayout = new LinearLayout(context);
		//mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		//mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		//LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//((Activity) context).addContentView(button, params);
		
		
		
		
		
		
		
		
		if(mChatService == null) {
			Log.d(Tag, "setupChat()");
			
			// Initialize the BluetoothChatService to perform bluetooth connections
			mChatService = new BluetoothChatService(context, mHandler);
			
			// Initialize the buffer for outgoing messages
			mOutStringBuffer = new StringBuffer("");
		}
		
		if(mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't started already
			if(mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
		
		
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null) {
			Log.e(Tag, "BluetoothAdapter is null");
			canConnect = false;
		}
		
		mtype = type == 0 ? THITF_TYPE : GODSHAND_TYPE;
		findName = type != 0 ? THITF_TYPE : GODSHAND_TYPE;
		// 要先确定蓝牙已经打开
		String setName = mBluetoothAdapter.getName();
		while(setName.endsWith(THITF_TYPE))
			setName = setName.substring(0, setName.lastIndexOf(THITF_TYPE));
		while(setName.endsWith(GODSHAND_TYPE))
			setName = setName.substring(0, setName.lastIndexOf(GODSHAND_TYPE));
		setName = setName + mtype;
		//Log.i(Tag, "my orign name：" + mBluetoothAdapter.getName());
		//Log.i(Tag, "my set name：" + setName);
		//Log.i(Tag, "and i will find name like：" + findName);
		mBluetoothAdapter.setName(setName);
		
		
		
		
		
		
		
		// If BT is not on, request that it be enabled.
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
        	Log.v(Tag, "BluetoothAdapter isEnabled");
        }
        
		
        // ACTION_DISCOVERY_START：开始搜索
        // ACTION_DISCOVERY_FINISHED：搜索结束
        // ACTION_FOUND：找到设备
        // 初始化一个接收ACTION_FOUND广播的BroadcastReceiver
        mReceiver = new BroadcastReceiver() {
        	public void onReceive(Context context, Intent intent) {
        		String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // 从Intent中获取设备对象
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    // 如果不重复就并且是对应的玩家才加进去
                    if(!devicesName.contains(deviceName) && deviceName.endsWith(findName)) {
                		devicesName.add(deviceName);
                		devicesAddress.put(deviceName, device.getAddress());
                		brushFlag = false;
                	}
                }
            }
        };
		
		// 注册BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		((Activity) context).registerReceiver(mReceiver, filter);		// 不要忘了之后解除绑定
		
		
		
		// 使本机可以被发现（默认打开120秒，可以将时间最多延长至300秒）
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);  
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);//设置持续时间（最多300秒）
		((Activity) context).startActivity(discoverableIntent);
		
		
		
		
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	
	
	public void update(long elapsedTime) {
		
		
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			((Activity) context).startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			canConnect = true;
		}
		
		
		if(brushFlag) {
			if(brushTime < 12000)
				brushTime += elapsedTime;
			else {
				mBluetoothAdapter.startDiscovery();
				brushTime = 0;
				Log.i(Tag, "brush again");
			}
		} else {
			// 如果蓝牙没有连接
			if(canConnect && !connected && (mChatService.getState() != BluetoothChatService.STATE_CONNECTING || 
					mChatService.getState() != BluetoothChatService.STATE_CONNECTED)) {
				
				connected = true;
				mBluetoothAdapter.cancelDiscovery();
				// 当前只有一个可连接设备
				if(devicesName.size() == 1) {
					
					BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(devicesAddress.get(devicesName.get(0)));
					mChatService.connect(device);
					Log.i(Tag, "maby find a bluetooth drive and i will connect it  --" + devicesName.get(0));
				}
				
			}
			
		}
		
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		//canvas.drawBitmap(waitMoment, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H /5, paint);
		
		// 
		canvas.drawText(mConnectedDeviceName, 50, 70, paint);
		
		// 对应玩家的可用设备列表
		canvas.drawText("devicesName", 50, 90, paint);
		for(int i = 0; i < devicesName.size(); i++)
			canvas.drawText(devicesName.get(i), 50, 100 + 12 * i, paint);
		
		// my message
		for(int i = 0; i < oneMessages.size(); i++)
			canvas.drawText(oneMessages.get(i), 200, 100 + 12 * i, paint);
		
		// their message
		for(int i = 0; i < twoMessages.size(); i++)
			canvas.drawText(twoMessages.get(i), 300, 100 + 12 * i, paint);
	}
	
	/**
     * Sends a message.
     * @param message  A string of text to send.
     */
    void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(context, "not_connected", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
            
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }
    
    // The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothChatService.STATE_NONE:
                    //mTitle.setText(R.string.title_not_connected);
                	Log.i(Tag, "STATE_NONE");
                    //Toast.makeText(context, "not_connected", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatService.STATE_LISTEN:
                	Log.i(Tag, "STATE_LISTEN");
                	break;
                case BluetoothChatService.STATE_CONNECTING:
                    //mTitle.setText(R.string.title_connecting);
                	Log.i(Tag, "STATE_CONNECTING");
                    //Toast.makeText(context, "connecting", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatService.STATE_CONNECTED:
                    //mTitle.setText(R.string.title_connected_to);
                    //mTitle.append(mConnectedDeviceName);
                	mConnectedDeviceName = null;
                	Log.i(Tag, "STATE_CONNECTED");
                    //Toast.makeText(context, "connected ->" + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    //mConversationArrayAdapter.clear();
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                Log.i(Tag, "writeMessage：" + writeMessage);
                oneMessages.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                Log.i(Tag, "readMessage：" + readMessage);
                twoMessages.add(mConnectedDeviceName + ":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(context, "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(context, msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
	public boolean onTouchEvent(MotionEvent event) {
		sendMessage("touch");
		return false;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(Tag, "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//不让别人做了
	}
	
	public String getChooseName() {
		return chooseName;
	}
	
	public String getChooseAddress() {
		return chooseAddress;
	}
	
	
}
