package cn.edu.seu.transfer;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import cn.edu.seu.main.R;
import cn.edu.seu.check.Check;
import cn.edu.seu.check.CheckActivity;
import cn.edu.seu.check.Checkdh;
import cn.edu.seu.ciphertext.RSA;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.main.MainActivity;
import cn.edu.seu.pay.ConfirmPriceActivity;
import cn.edu.seu.pay.GoodsListActivity;
import cn.edu.seu.pay.TimeOutProgressDialog;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;
import cn.edu.seu.record.Record;
import cn.edu.seu.record.Recorddh;
import cn.edu.seu.xml.PersonInfo;
import cn.edu.seu.xml.Trade;
import cn.edu.seu.xml.XML;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.seu.xml.Transfer;
public class TransferPriceActivity extends Activity {
	private TextView textView1;
	private EditText editText1;
	private Button btnConfirm;
	private TimeOutProgressDialog pd;
	private boolean loaded=false;
	private PersonInfo receiver;
	private Thread sendAndReceiveThread;
	private final static String TAG="TransferPriceActivity";
	private Transfer transfer;
	private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
	         case 1:
	        	 pd=TimeOutProgressDialog.createProgressDialog(TransferPriceActivity.this,50000,new OnTimeOutListener(){

						public void onTimeOut(TimeOutProgressDialog dialog) {
							// TODO Auto-generated method stub
							AlertDialog.Builder builder = new Builder(TransferPriceActivity.this);
					    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									Intent intent=new Intent(TransferPriceActivity.this,MainActivity.class);
									startActivity(intent);
									TransferPriceActivity.this.finish();
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
	        	 AlertDialog.Builder alertDialog = new Builder(TransferPriceActivity.this);
	        	 alertDialog.setTitle("转账结果").setMessage((String)msg.obj).setCancelable(false);
	        	 alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							TransferPriceActivity.this.finish();
							Intent intent=new Intent(TransferPriceActivity.this,MainActivity.class);
							startActivity(intent);
							TransferActivity.bdt.close();
							
						}
			    		
			    	});
					alertDialog.show();
					break;
	         case 3:
	            	pd.dismiss();
	            	AlertDialog.Builder builder1 = new Builder(TransferPriceActivity.this);
			    	builder1.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(TransferPriceActivity.this,MainActivity.class);
							startActivity(intent);
							TransferPriceActivity.this.finish();
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
	         case 4:
	            	AlertDialog.Builder builder2 = new Builder(TransferPriceActivity.this);
			    	builder2.setTitle("连接信息").setMessage("转账金额超限，请重试").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							
							
						}
			    		
			    	});
			    	builder2.show();
			    	break;
	     }
	     super.handleMessage(msg);
	  }
	};

    
	 @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try
		{
			pd.dismiss();
		}
		catch(Exception e)
		{
			Log.i(TAG,"界面已经关闭，无需再次关闭");
		}
	}


	public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.transferprice);
        btnConfirm=(Button) findViewById(R.id.btnConfirm);
        editText1=(EditText) findViewById(R.id.editText1);
        btnConfirm.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnConfirm.setVisibility(View.GONE);
				PropertyInfo pi=new PropertyInfo();
				Properties properties=pi.getProperties();
				double limitpertime=Double.parseDouble(properties.getProperty("limitpertime","2000"));
				double limitperday=Double.parseDouble(properties.getProperty("limitperday","10000"));
				boolean condition1=(Double.parseDouble(editText1.getText().toString())<limitpertime);//单笔限额
				double sum=0;
				try
				{
					Recorddh rdh= new Recorddh(TransferPriceActivity.this, "recorddb" , null, 1);
					Record [] list = rdh.query();
					long currenttime = System.currentTimeMillis();
					long date = currenttime/3600/24/1000;
					long begin = date*3600*24*1000;
					long end = (date+1)*3600*24*1000;
					sum = 0;
					for(int i = 0 ; i < list.length ; i++ ){
				        if(Long.parseLong(list[i].getTradeTime()) >= begin && Long.parseLong(list[i].getTradeTime()) <= end && list[i].getTradeType().equals("转出")){
				        	sum += list[i].getPrice();
				        }
				    }
				}
				catch(Exception e)
				{
					Log.i(TAG,"数据不存在");
				}
				boolean condition2=(Double.parseDouble(editText1.getText().toString())+sum<limitperday);
				/*!!!!!此处加入计算当前交易额*/
				LocalInfoIO localinfoio=new LocalInfoIO(properties.getProperty("path","sdcard/data") , properties.getProperty("filename","local.dat"));
				LocalInfo local=localinfoio.readfile();
				boolean condition3=Double.parseDouble(editText1.getText().toString())<Double.parseDouble(local.getAvailableBalance());
				if(condition1&&condition2&&condition3)
				{
					Message msg=handler.obtainMessage();
					msg.what=1;
					msg.obj="正在发送电子支票";
					msg.sendToTarget();
					sendAndReceiveThread=new Thread()
					{
						public void run()
						{

							XML info=new XML();
							// 点击确认按钮后，获取用户输入金额，完成转账交易
							String totalprice=editText1.getText().toString();
							Date dt=new Date();
							String cardnumber=MainActivity.person.getCardnum();
							String username=MainActivity.person.getUsername();
							String imei=MainActivity.person.getImei();
							String transfertime=String.valueOf(dt.getTime()/1000);
							String payerdevice=BluetoothDataTransportation.getLocalMac().replaceAll(":","");
							String receiverdevice=TransferActivity.bdt.getRemoteMac().replaceAll(":","");
							int totalpricefill=(int)(Double.valueOf(totalprice)*100);
							String pricefill=String.format("%08d",totalpricefill);
							String payerdevicesub=payerdevice.substring(payerdevice.length()-4,payerdevice.length());
							int payerdevicefill=Integer.parseInt(payerdevicesub,16);
							String payerfill=String.format("%05d",payerdevicefill);
							String words=transfertime+payerfill+pricefill;
							RSA rsa=new RSA(MainActivity.person.getPrivatekey(),"0");
							String cipher=rsa.setRSA(words);
							transfer=new Transfer();
							transfer.setPayerName(username);
							transfer.setPayerCardNumber(cardnumber);
							transfer.setPayerIMEI(imei);
							transfer.setPayerDevice(payerdevice);
							transfer.setTotalPrice(totalprice);
							transfer.setTradeTime(transfertime);
							transfer.setCipher(cipher);
							Log.d("words",words);
							info.setTransfer(transfer);
							String xml=info.produceTransferXML("transfer");
							byte[] receive=null;
							try
							{
								TransferActivity.bdt.write(xml);
								Log.d("发送",xml);
								receive=TransferActivity.bdt.read();
							}
							catch(Exception e)
							{
								Message msg=handler.obtainMessage();
								msg.what=3;
								msg.sendToTarget();
							}
							if(receive!=null)
							{
								String sentence=info.parseSentenceXML(new ByteArrayInputStream(receive));
								Message msg=handler.obtainMessage();
								msg.what=2;
								if(sentence.equals("转账成功"))
								{

									msg.obj="转账成功";
									msg.sendToTarget();
									//扣除余额
									try
									{
										
										LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
										LocalInfo local=lio.readfile();
										double avaliblebalance=Double.parseDouble(local.getBalance())-Double.parseDouble(totalprice);
										lio.modifyAvailableBalance(String.valueOf(avaliblebalance));
										
										//生成转账记录
										Record record = new Record( 0 ,transfer.getPayerName(),transfer.getPayerDevice(),transfer.getPayerIMEI(),transfer.getReceiverName(),transfer.getReceiverDevice(),transfer.getReceiverIMEI(),Double.parseDouble(transfer.getTotalPrice()),"转出", transfer.getTradeTime());
										Recorddh rdh = new Recorddh(TransferPriceActivity.this , "recorddb" , null , 1);
										rdh.insert(record);
										
									}
									catch(Exception e)
									{
										Log.i(TAG,"文件写入失败");
									}
								}
								else
								{

									msg.obj="转账失败";
									msg.sendToTarget();
								}
							}
							try{
								TransferActivity.bdt.close();
							}
							catch(Exception e)
							{
								Log.i(TAG,"关闭socket失败");
							}
						}
					};
					sendAndReceiveThread.start();
				}
				else
				{
					Log.i(TAG,"转账金额超限");
					Message msg=handler.obtainMessage();
					msg.what=4;
					msg.sendToTarget();
				}
			}
        	
        });
        }
	     
 

}
