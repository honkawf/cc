package cn.edu.seu.datatransportation;

import java.io.IOException;
import java.io.InputStream;

import cn.edu.seu.datadeal.DataDeal;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothReadThread extends Thread{
	private BluetoothSocket socket;
	private InputStream is=null;
	private byte[]receive=null;
	public BluetoothReadThread(BluetoothSocket socket)
	{
		this.socket=socket;
	}
	public boolean isReceive()
	{
		return receive!=null;
	}
	public byte[] getReceive()
	{
		return receive;
	}
	public void run() {
		if (socket == null) 
		{
			Log.e("警告","没有连接");
			return;
		}
	        	
		byte[] buffer = new byte[16];
		try {
			is = socket.getInputStream();
		} catch (IOException e1) {
		// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		try {
			int total=0;
			is.read(buffer);
			total=DataDeal.readHead(buffer);
			byte [] receiveTemp=new byte[total];
			is.read(receiveTemp);
			receive=receiveTemp;
			String s = new String(receive);
			Log.i("收到",s);
		} catch (IOException e) {
				e.printStackTrace();
		}
	 }
}
