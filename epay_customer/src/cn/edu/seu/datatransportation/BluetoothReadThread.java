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
	private static final String TAG="BluetoothReadThread";
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
			Log.i(TAG,"长度"+total);
			byte [] receiveTemp=new byte[total];
			buffer=new byte[8];
			for(int i=0;i<total;i++)
			{
				receiveTemp[i]=127;
			}
			int times=total/8;
			int rest=total%8;
			for(int i=0;i<times;i++)
			{
				int length=is.read(buffer);
				Log.i(TAG,String.valueOf(length));
				System.arraycopy(buffer, 0, receiveTemp, i*length, length);
			}
			if(rest!=0)
			{
				buffer=new byte[rest];
				Log.i(TAG,String.valueOf(rest));
				int newlength=is.read(buffer);
				Log.i(TAG,String.valueOf(newlength));
				System.arraycopy(buffer, 0, receiveTemp, times*8, rest);
				
			}
			receive=receiveTemp;
			String s = new String(receive);
			Log.i("收到",s);
		} catch (IOException e) {
				e.printStackTrace();
		}
	 }
}
