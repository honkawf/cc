package cn.edu.seu.datatransportation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.edu.seu.datadeal.DataDeal;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothWriteThread extends Thread{
	private BluetoothSocket socket;
	private String msg;
	public BluetoothWriteThread(BluetoothSocket socket,String msg)
	{
		this.socket=socket;
		this.msg=msg;
	}
	public void run()
	{
		if (socket == null) 
		{
			Log.e("警告","没有连接");
			return;
		}
		try {			
			OutputStream os = socket.getOutputStream();
			byte [] send=DataDeal.plusHead(msg.getBytes().length);
			os.write(send);
			Log.i("发送长度",String.valueOf(send));
			os.write(msg.getBytes());
			Log.i("发送",msg);
			os.flush();
		} catch (IOException e) {
		// TODO Auto-generated catch block
			Log.e("警告","连接失败");
			e.printStackTrace();
		}	
	}
}
