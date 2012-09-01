package cn.edu.seu.datatransportation;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothServerThread extends Thread{
	private BluetoothServerSocket mserverSocket=null;
	private BluetoothSocket socket=null;
	private boolean isConnected=false;
	public boolean isConnected()
	{
		return isConnected;
	}
	public BluetoothSocket getSocket()
	{
		return socket;
	}
	public void close()
	{
		try
		{
			socket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void run() {		
		try {
			/* 创建一个蓝牙服务器 
			* 参数分别：服务器名称、UUID	 */	
			BluetoothAdapter btAdapt=BluetoothAdapter.getDefaultAdapter();
			mserverSocket = btAdapt.listenUsingRfcommWithServiceRecord("btspp",
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));		
			Log.d("server", "wait cilent connect...");
			socket = mserverSocket.accept();
			isConnected=true;
			Log.d("server", "accept success !");
			//启动接受数据
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
   
}
