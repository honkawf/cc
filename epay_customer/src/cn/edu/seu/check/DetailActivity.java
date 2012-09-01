package cn.edu.seu.check;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import cn.edu.seu.main.R;

import cn.edu.seu.datadeal.DataDeal;
import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.main.MainActivity;
import cn.edu.seu.xml.Transfer;
import cn.edu.seu.xml.XML;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity{

	private TextView name;
	private TextView price;
	private TextView time;
	private TextView cash;
	private TextView cardnum;
	private Button cashbtn;
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
				 Toast.makeText(DetailActivity.this, "兑现失败", 2000).show();
				 break;
            case 1:
				 Toast.makeText(DetailActivity.this, "兑现成功", 5000).show();
				 break;
            case 2:
            	 Toast.makeText(DetailActivity.this, "连接服务器失败", 2000).show();
				 break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdetail);
		
		name = (TextView)findViewById(R.id.name);
		price = (TextView)findViewById(R.id.price);
		time = (TextView)findViewById(R.id.time);
		cash = (TextView)findViewById(R.id.cash);
		cardnum = (TextView)findViewById(R.id.cardnum);
		cashbtn=(Button)findViewById(R.id.cashbtn);
		
		name.setText(this.getIntent().getStringExtra("name"));
		price.setText(this.getIntent().getStringExtra("price"));
		time.setText(this.getIntent().getStringExtra("time"));
		cash.setText(this.getIntent().getStringExtra("cash"));
		cardnum.setText(this.getIntent().getStringExtra("cardnum"));
		cashbtn.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg=handler.obtainMessage();
				try
        		 {
					Transfer transfer=new Transfer();
        			XML cash=new XML();
        			MainActivity.person.setCardnum("1234567890123456789");
        			transfer.setReceiverDevice( MainActivity.person.getBluetoothmac());
        			transfer.setReceiverName(MainActivity.person.getUsername());
        			transfer.setReceiverCardNumber(MainActivity.person.getCardnum());
        			cash.setTransfer(transfer);
        			String cashxml=cash.produceTransferXML("transfer");
        			Socket clientsocket=new Socket("honka.xicp.net",30145);
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
           		 	if(parsedresult.equals("兑现成功"))
           		 	{
           		 		msg.what=1;
           		 	}
           		 	else
           		 	{
           		 		msg.what=0;
           		 	}
           		 	msg.sendToTarget();
        		 }
        		 catch(Exception e)
        		 {
        			 msg=handler.obtainMessage();
        			 msg.what=2;
        			 msg.sendToTarget();
        		 }
			}
			
		});
	}
}
