package cn.edu.seu.transfer;

import java.io.ByteArrayInputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.edu.seu.check.Checkdh;
import cn.edu.seu.datatransportation.BluetoothReadThread;
import cn.edu.seu.datatransportation.BluetoothServerThread;
import cn.edu.seu.datatransportation.BluetoothWriteThread;
import cn.edu.seu.main.MainActivity;
import cn.edu.seu.xml.Transfer;
import cn.edu.seu.xml.XML;

public class TransferWaitingThread extends Thread{

	private Transfer transfer;
	private Handler handler;
	private static final String TAG="TransferWaitingThread";
	public TransferWaitingThread(Handler handler)
	{
		this.handler=handler;
	}
	public Transfer getTransfer()
	{
		return transfer;
	}
	public void run()
	{
		while(true)
		{
			try
    		{
				
				TransferActivity.bdt.createServer();
    			XML info=new XML();
                byte[] receive=TransferActivity.bdt.read();
                Log.i(TAG, "1");
             	String xml=info.productSentenceXML("转账成功");
                Log.i(TAG, "2");
             	TransferActivity.bdt.write(xml);
                Log.i(TAG, "3");
             	Message msg=handler.obtainMessage();
             	msg.what=1;
                Log.i(TAG, "4");
             	msg.obj=receive;
                Log.i(TAG, "5");
             	msg.sendToTarget();
             	try
             	{

             		TransferActivity.bdt.close();
             	}
             	catch(Exception e)
             	{
             		Log.i(TAG,"已关闭连接");
             	}
                Log.i(TAG, "7");
           
    		}
    		catch(Exception e)
    		{
    			Message msg=handler.obtainMessage();
    			msg.what=0;
    			msg.sendToTarget();
    			TransferActivity.bdt.close();
    		}
         }
             
	}
}
