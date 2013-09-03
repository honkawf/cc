package cn.edu.seu.cash;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;


import cn.edu.seu.datadeal.DataDeal;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.main.MainActivity;
import cn.edu.seu.xml.Transfer;
import cn.edu.seu.xml.XML;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class CashActivity extends Activity{
	private Button cash;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
  		try
		 {
			 XML cash=new XML();
			 Transfer transfer=new Transfer();
			 transfer.setReceiverDevice( MainActivity.person.getBluetoothmac());
			 transfer.setReceiverName(MainActivity.person.getUsername());
			 transfer.setReceiverCardNumber(MainActivity.person.getCardnum());
			 cash.setTransfer(transfer);
			 String cashxml=cash.produceTransferXML("transfer");
			 Properties server=PropertyInfo.getProperties();
			 Socket clientsocket=new Socket(server.getProperty("serverAddress"),Integer.parseInt(server.getProperty("serverPort")));
	   		 InputStream in=clientsocket.getInputStream();
	   		 OutputStream out=clientsocket.getOutputStream();
	   		 out.write(DataDeal.plusHead(cashxml.getBytes().length));
	   		 Log.i("发送到银行长度",String.valueOf(cashxml.getBytes().length));
	   		 out.write(cashxml.getBytes());
	   		 Log.i("发送到银行",cashxml);
	   		 byte [] buffer=new byte[16];
	   		 in.read(buffer);
	   		 int length=DataDeal.readHead(buffer);
	   		 byte [] result=new byte [length];
	   		 in.read(result);
	   		 Log.d("收到",new String(result));
	   		 String parsedresult=cash.parseSentenceXML(new ByteArrayInputStream(result));
	   		 clientsocket.close();
			 }
			 catch(Exception e)
			 {
				 e.printStackTrace();
			 }
	}

}
