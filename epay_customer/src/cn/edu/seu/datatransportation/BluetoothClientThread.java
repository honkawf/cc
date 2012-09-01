package cn.edu.seu.datatransportation;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothClientThread extends Thread{
	private String address=null;
	private boolean isConnected=false;
	private BluetoothSocket socket=null;
	public BluetoothClientThread(String address)
	{
		this.address=address;
	}
	public BluetoothSocket getSocket()
	{
		return socket;
	}
	public boolean isConnected()
	{
		return isConnected;
	}
	public void run()
	{
		try {
			BluetoothAdapter btAdapt=BluetoothAdapter.getDefaultAdapter();
			BluetoothDevice device=btAdapt.getRemoteDevice(address);
			socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			//连接
			Log.i("connect", "请稍候，正在连接服务器:"+address);
			socket.connect();
			isConnected=true;
			Log.i("connect", "已经连接上服务端！");
		} 
		catch (IOException e) 
		{
			Log.e("connect", "", e);
			
		} 
	}
}
