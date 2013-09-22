package org.bistu.xgxykjcx.godshandandthief;

import java.util.ArrayList;

import org.bistu.xgxykjcx.godshandandthief.actor.Businessman;
import org.bistu.xgxykjcx.godshandandthief.actor.obstacle.Obstacle;
import org.bistu.xgxykjcx.godshandandthief.statesystem.GodHandPlayerState;
import org.bistu.xgxykjcx.godshandandthief.statesystem.IGameObject;
import org.bistu.xgxykjcx.godshandandthief.statesystem.ThiefPlayerState;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainActivity extends Activity {
	// Debugging
	private final String BluetoothTag = "BluetoothState";
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	
	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	
	public static final int THIEF = 0;
	public static final int GODSHAND = 1;
	public static final String THIEF_TAIL = "_Thief";
	//public static final String GODSHAND_TAIL = "_GodsHand";
	
	// Intent request codes
	//private static final int REQUEST_CONNECT_DEVICE = 1;
	//private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;
	
	public static Context CONTEXT;
	public static boolean CAN_SENDMESSAGE;
	
	private int mtype;
	private int [] bluetoothConnetedTime;
	private ThiefPlayerState thiefPlayerState;
	private GodHandPlayerState godHandPlayerState;
	// Name of the connected device
	private String mConnectedDeviceName = null;
	private ArrayList<String> devicesName;
	//private HashMap<String, String> devicesAddress;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().toString(), "- ON CREATR -");
		CONTEXT = this;
		CAN_SENDMESSAGE = false;
		
		BitmapStorage.setResources(this.getResources());
		
		// 启动游戏界面
		setContentView(new MainSurfaceView(this));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.e(this.getClass().toString(), "- ON START -");
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		Log.e(this.getClass().toString(), "- ON PAUSE -");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.e(this.getClass().toString(), "-- ON STOP --");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(this.getClass().toString(), "- ON DESTROY -");
		
		// Make sure we're not doing discovery anymore
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
		
		if(mtype == GODSHAND) {
			// Unregister broadcast listeners
			this.unregisterReceiver(mReceiver);
		}
	}

	public void startBluetooth(int type, IGameObject playerState) {
		mtype = type;
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(BluetoothTag, "BluetoothAdapter is null");
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			// 结束蓝牙连接尝试等后续事项
		}
		
		devicesName = new ArrayList<String>();
		
		if(type == THIEF) {
			thiefPlayerState = (ThiefPlayerState) playerState;
			// 小偷 直接请求能够被搜索
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivityForResult(discoverableIntent, REQUEST_ENABLE_BT);
		}
		
		if(type == GODSHAND) {
			godHandPlayerState = (GodHandPlayerState) playerState;
			// 上帝之手 启动蓝牙 注册广播接收器
			if (!mBluetoothAdapter.isEnabled()) {
	            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	        } else {		// Otherwise, setup the chat session
	            setupChat();
	        }
			
			// 注册BroadcastReceiver
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			this.registerReceiver(mReceiver, filter);		// 不要忘了之后解除绑定
		}
	}
	
	public void stopBluetooth() {
		if (mChatService != null) {
			Log.d(BluetoothTag, "stopBluetooth()");
			mChatService.stop();
			mChatService = null;
			devicesName.clear();
		}
	}
	
	private void setupChat() {
		
		if (mChatService == null) {
			Log.d(BluetoothTag, "setupChat()");
			
			// Initialize the BluetoothChatService to perform bluetooth connections
			mChatService = new BluetoothChatService(this, mHandler);
			
			// Initialize the buffer for outgoing messages
			mOutStringBuffer = new StringBuffer("");
		}
		
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't started already
			if(mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
		
		// 小偷要加上小偷的尾巴来让上帝之手找到
		if(mtype == THIEF) {
			// 要先确定蓝牙已经打开
			String name = mBluetoothAdapter.getName();
			if(!name.endsWith(THIEF_TAIL)) {
				name = name + THIEF_TAIL;
				mBluetoothAdapter.setName(name);
			}
		}
		
		// 上帝之手开始搜索小偷尾巴来连接小偷
		if(mtype == GODSHAND) {
			mBluetoothAdapter.startDiscovery();
		}
		
	}
	
	/**
     * Sends a message.
     * @param message  A string of text to send.
     */
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "not_connected", Toast.LENGTH_SHORT).show();
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
    
    public void sendOneByte(int oneByte) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "not_connected", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mChatService.writeOneByte(oneByte);
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (mChatService != null) {
			if(mtype == GODSHAND && mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
				mBluetoothAdapter.startDiscovery();
			}
			
			//sendMessage("touch");
		}
		
		return super.onTouchEvent(event);
	}
	
	public int getChatServiceState() {
		if(mChatService != null)
			return mChatService.getState();
		return -1;
	}
	
	// ACTION_DISCOVERY_START：开始搜索
	// ACTION_DISCOVERY_FINISHED：搜索结束
	// ACTION_FOUND：找到设备
	// 创建一个接收ACTION_FOUND广播的BroadcastReceiver
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 从Intent中获取设备对象
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String deviceName = device.getName();
				Log.i(BluetoothTag, "find a device :" + device.getName());
				// 如果不重复就并且是对应的玩家才加进去
				if(deviceName != null && !devicesName.contains(deviceName) && deviceName.endsWith(THIEF_TAIL)) {
					devicesName.add(deviceName);
					//devicesAddress.put(deviceName, device.getAddress());
					Log.i(BluetoothTag, "find a device :" + device.getName());
					mChatService.connect(device);
					Log.i(BluetoothTag, "maby find a bluetooth drive and i will connect it  --" + device.getName());
				}
			}
		}
	};
	
	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
            	CAN_SENDMESSAGE = false;
                switch (msg.arg1) {
                case BluetoothChatService.STATE_NONE:
                	mConnectedDeviceName = null;
                	Log.i(BluetoothTag, "STATE_NONE");
                    break;
                case BluetoothChatService.STATE_LISTEN:
                	mConnectedDeviceName = null;
                	Log.i(BluetoothTag, "STATE_LISTEN");
                	if(mtype == GODSHAND) {}
                	if(mtype == THIEF) {}
                	break;
                case BluetoothChatService.STATE_CONNECTING:
                	Log.i(BluetoothTag, "STATE_CONNECTING");
                	if(mtype == GODSHAND) {
                		godHandPlayerState.reset(ThiefPlayerState.MODEL_BRAND_NEW);
                	}
                	if(mtype == THIEF) {
                		thiefPlayerState.reset(ThiefPlayerState.MODEL_BRAND_NEW);
                	}
                	// 清空可连接设备名
                	devicesName.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTED:
                	Log.i(BluetoothTag, "STATE_CONNECTED");
                	// 取消扫描蓝牙
                	mBluetoothAdapter.cancelDiscovery();
                	CAN_SENDMESSAGE = true;
                	// 
                	bluetoothConnetedTime = new int[3];
                	bluetoothConnetedTime[0] = (int) (System.currentTimeMillis() % 10000000);
                	((MainActivity) CONTEXT).sendMessage(System.currentTimeMillis() % 10000000 + "BCT");
                	// 已经连接上 去掉小偷的尾巴
                	String name = mBluetoothAdapter.getName();
                	while(name.endsWith(THIEF_TAIL))
            			name = name.substring(0, name.lastIndexOf(THIEF_TAIL));
                	mBluetoothAdapter.setName(name);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                String writeMessage2 = "Me writeMessage：" + writeMessage;
                Log.i(BluetoothTag, writeMessage2);
                //Toast.makeText(CONTEXT, writeMessage2, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                String readMessage2 = "readMessage from " + mConnectedDeviceName + "：" + readMessage;
                if(readMessage.contains("BCT")) {
        			readMessage = readMessage.substring(0, readMessage.indexOf("BCT"));
        			bluetoothConnetedTime[1] = Integer.parseInt(readMessage);
        			bluetoothConnetedTime[2] = bluetoothConnetedTime[0] - bluetoothConnetedTime[1];
        		}
                if(mtype == GODSHAND) {
                	if(readMessage.contains(Businessman.UP_FLING_STRING)) {
            			godHandPlayerState.setFling(Businessman.UP_FLING);
            		}
                	if(readMessage.contains(Businessman.DOWN_FLING_STRING)) {
            			godHandPlayerState.setFling(Businessman.DOWN_FLING);
            		}
                	if(readMessage.contains(Businessman.IS_INJURED_STRING)) {
            			godHandPlayerState.businessmanbeInjured();
            		}
            	}
            	if(mtype == THIEF) {
            		int obstacleType = -1;
            		long distanceLong = 4000;
            		if(readMessage.contains(Obstacle.HOLE_STRING)) {
            			readMessage = readMessage.substring(0, readMessage.indexOf(Obstacle.HOLE_STRING));
            			obstacleType = Obstacle.HOLE;
            		}
            		if(readMessage.contains(Obstacle.PIT_STRING)) {
            			readMessage = readMessage.substring(0, readMessage.indexOf(Obstacle.PIT_STRING));
            			obstacleType = Obstacle.PIT;
            		}
            		if(readMessage.contains(Businessman.IS_INJURED_STRING)) {
            			thiefPlayerState.businessmanbeInjured();
            		} else {
            			distanceLong = 4000 - ((System.currentTimeMillis() % 10000000 - bluetoothConnetedTime[0]) - (Integer.parseInt(readMessage) - bluetoothConnetedTime[1]));
            		}
            		Log.i(BluetoothTag, "distanceLong = " + distanceLong);
            		thiefPlayerState.addObstacleByBluetooth(distanceLong, obstacleType);
            	}
                Log.i(BluetoothTag, readMessage2);
                //Toast.makeText(CONTEXT, readMessage2, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                while(mConnectedDeviceName.endsWith(THIEF_TAIL))
                	mConnectedDeviceName = mConnectedDeviceName.substring(0, mConnectedDeviceName.lastIndexOf(THIEF_TAIL));
                Toast.makeText(CONTEXT, "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(CONTEXT, msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d(BluetoothTag, "onActivityResult " + resultCode);
    	switch (requestCode) {
		case REQUEST_ENABLE_BT:
		    // When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK || resultCode == 120) {
			    // Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
			    // User did not enable Bluetooth or an error occured
			    Log.d(BluetoothTag, "BT not enabled");
			    Toast.makeText(this, /*R.string.bt_not_enabled_leaving*/"蓝牙 not enabled", Toast.LENGTH_SHORT).show();
			    //finish();
			}
			break;
    	}
    }
	
	public String getConnectedDeviceName() {
		if (mConnectedDeviceName != null)
			return mConnectedDeviceName;
		return "无设备，等待连接……";
	}
    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.secure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        case R.id.insecure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }
    */
    
}
