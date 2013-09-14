package cn.edu.seu.check;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.R;
import cn.edu.seu.datadeal.DataDeal;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.pay.ConfirmPriceActivity;
import cn.edu.seu.pay.TimeOutProgressDialog;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;
import cn.edu.seu.record.Record;
import cn.edu.seu.record.Recorddh;
import cn.edu.seu.transfer.TransferActivity;
import cn.edu.seu.transfer.TransferPriceActivity;
import cn.edu.seu.xml.Transfer;
import cn.edu.seu.xml.XML;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
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
	private Button cashbtn,transmitbtn,btn_back_c;
	private String xml;
	private static final String TAG="DetailActivity";
	private TimeOutProgressDialog pd;
	private Thread sendAndReceiveThread;
	private Properties property;
	String id;
	private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
	         case 1:
	        	 pd=TimeOutProgressDialog.createProgressDialog(DetailActivity.this,50000,new OnTimeOutListener(){

						public void onTimeOut(TimeOutProgressDialog dialog) {
							// TODO Auto-generated method stub
							AlertDialog.Builder builder = new Builder(DetailActivity.this);
					    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									/*Intent intent=new Intent(DetailActivity.this,FlipActivity.class);
									startActivity(intent);*/
									FlipActivity.id=2;
									DetailActivity.this.finish();
									try{
										TransferActivity.bdt.close();
									}
									catch(Exception e)
									{
										Log.i(TAG,"连接已经关闭");
									}
									
								}
					    		
					    	});
					    	builder.show();
						}
	            		
	            	});
					pd.setProgressStyle(TimeOutProgressDialog.STYLE_SPINNER);
					pd.setCancelable(false);
					pd.setMessage((String)msg.obj); 
					pd.show();
	                break;
	         case 2:
	        	 pd.dismiss();
	        	 AlertDialog.Builder alertDialog = new Builder(DetailActivity.this);
	        	 alertDialog.setTitle("兑现结果").setMessage((String)msg.obj).setCancelable(false);
	        	 alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							FlipActivity.id=2;
							DetailActivity.this.finish();
							/*Intent intent=new Intent(DetailActivity.this,FlipActivity.class);
							startActivity(intent);*/
							try{
								TransferActivity.bdt.close();
							}catch(Exception e){
								Log.i(TAG,"连接断开失败");
							}
						}
			    		
			    	});
					alertDialog.show();
					break;
	         case 3:
	            	pd.dismiss();
	            	AlertDialog.Builder builder1 = new Builder(DetailActivity.this);
			    	builder1.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							/*Intent intent=new Intent(DetailActivity.this,FlipActivity.class);
							startActivity(intent);*/
							FlipActivity.id=2;
							DetailActivity.this.finish();
							try{
								TransferActivity.bdt.close();
							}
							catch(Exception e)
							{
								Log.i(TAG,"连接已经关闭");
							}
							
						}
			    		
			    	});
			    	builder1.show();
			    	break;
	     }
	     super.handleMessage(msg);
	  }
	};
    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdetail);
		Mapplication.getInstance().addActivity(this);
		
		property =PropertyInfo.getProperties();
		/*name = (TextView)findViewById(R.id.name);
		price = (TextView)findViewById(R.id.price);
		time = (TextView)findViewById(R.id.time);
		cash = (TextView)findViewById(R.id.cash);
		cardnum = (TextView)findViewById(R.id.cardnum);*/
		cashbtn=(Button)findViewById(R.id.cashbtn);
		transmitbtn=(Button)findViewById(R.id.transmitbtn);
		
		/*name.setText(this.getIntent().getStringExtra("name"));
		price.setText(this.getIntent().getStringExtra("price"));
		time.setText(this.getIntent().getStringExtra("time"));
		cash.setText(this.getIntent().getStringExtra("cash"));
		cardnum.setText(this.getIntent().getStringExtra("cardnum"));*/
		id = this.getIntent().getStringExtra("id");
		xml=this.getIntent().getStringExtra("xml");
		cashbtn.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
        		 {
					Message msg=handler.obtainMessage();
					msg.what=1;
					msg.obj="正在兑现";
					msg.sendToTarget();
        			sendAndReceiveThread=new Thread()
        			{
        				public void run(){
        					Message msg=handler.obtainMessage();
        					XML cash=new XML();
                			Transfer transfer=cash.parseTransferXML(new ByteArrayInputStream(xml.getBytes()));
                			transfer.setReceiverDevice(FlipActivity.person.getBluetoothmac());
                			transfer.setReceiverName(FlipActivity.person.getUsername());
                			transfer.setReceiverCardNumber(FlipActivity.person.getCardnum());
                			cash.setTransfer(transfer);
                			String cashxml=cash.produceTransferXML("transfer");
                			NetDataTransportation ndt=new NetDataTransportation();
                			ndt.connect(property.getProperty("serverAdress","honka.xicp.net"), Integer.parseInt(property.getProperty("serverPort","30145")));
                			ndt.write(cashxml);
                   		 	Log.i("发送到银行长度",String.valueOf(cashxml.getBytes().length));
                   		 	Log.i("发送到银行",cashxml);
                   		 	byte [] result=ndt.read();
                   		 	Log.d("收到",new String(result));
                   		 	String parsedresult=cash.parseSentenceXML(new ByteArrayInputStream(result));
                   		 	if(parsedresult.equals(""))
                   		 	{
                   		 		parsedresult=cash.parseBalanceXML(new ByteArrayInputStream(result));
                   		 		msg.what=2;
                   		 		msg.obj="兑现成功";
                   		 		msg.sendToTarget();
	                   		 	LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));
								lio.modifyBalance(parsedresult);
								Checkdh cdh = new Checkdh(DetailActivity.this , "recorddb" , null , 1);
								cdh.UpdateIscashed(Integer.parseInt(id));
								//给交易记录赋值
								Record record = new Record( 0 ,transfer.getPayerName(),transfer.getPayerDevice(),transfer.getPayerIMEI(),transfer.getReceiverName(),transfer.getReceiverDevice(),transfer.getReceiverIMEI(),Double.parseDouble(transfer.getTotalPrice()),"收款", transfer.getTradeTime());
								Recorddh rdh = new Recorddh(DetailActivity.this , "recorddb" , null , 1);
								rdh.insert(record);
                   		 	}
                   		 	else
                   		 	{
                   		 		msg.what=2;
                   		 		msg.obj=parsedresult;
                   		 		msg.sendToTarget();
                   		 	}
                   		 	ndt.close();
        				}
        			};
        			sendAndReceiveThread.start();
        		 }
        		 catch(Exception e)
        		 {
        			 Message msg=handler.obtainMessage();
        			 msg=handler.obtainMessage();
        			 msg.what=3;
        			 msg.sendToTarget();
        		 }
			}
			
		});
		btn_back_c=(Button)findViewById(R.id.btn_back_c);
		btn_back_c.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FlipActivity.id=1;
				DetailActivity.this.finish();
			}
		});
		transmitbtn.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(DetailActivity.this,TransferActivity.class);
				intent.putExtra("flag", "transmit");
				intent.putExtra("xml", xml);
				startActivity(intent);
			}
			
		});
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	FlipActivity.id=2;
	    	DetailActivity.this.finish();
			
	    }
		return false;
	}
}
