package cn.edu.seu.transfer;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import cn.edu.seu.main.R;

import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.main.MainActivity;
import cn.edu.seu.pay.ConfirmPriceActivity;
import cn.edu.seu.pay.GoodsListActivity;
import cn.edu.seu.pay.RSA;
import cn.edu.seu.pay.TimeOutProgressDialog;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;
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
									MainActivity.bdt.close();
									
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
	        	 AlertDialog.Builder alertDialog = new Builder(TransferPriceActivity.this);
	        	 alertDialog.setTitle("转账结果").setMessage((String)msg.obj).setCancelable(false);
	        	 alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							TransferPriceActivity.this.finish();
							Intent intent=new Intent(TransferPriceActivity.this,MainActivity.class);
							startActivity(intent);
							GoodsListActivity.flag=1;
							TransferActivity.bdt.close();
							
						}
			    		
			    	});
					alertDialog.show();
					break;
	         case 3:
	        	 pd.dismiss();
				 Toast.makeText(TransferPriceActivity.this, "连接失败", 2000).show();
	             break;
	     }
	     super.handleMessage(msg);
	  }
	};

    
	 public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.transferprice);
        btnConfirm=(Button) findViewById(R.id.btnConfirm);
        editText1=(EditText) findViewById(R.id.editText1);
        btnConfirm.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
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
						String username=MainActivity.person.getCustomername();
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
						RSA rsa=new RSA();
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
						TransferActivity.bdt.write(xml);
						Log.d("发送",xml);
						byte[] receive=TransferActivity.bdt.read();
						String sentence=info.parseSentenceXML(new ByteArrayInputStream(receive));
						Message msg=handler.obtainMessage();
						msg.what=2;
						msg.obj="转账失败";
						if(sentence.equals("转账成功"))
						{

							msg.obj="转账成功";
							//生成转账记录
							//扣除余额
						}
						msg.sendToTarget();
						TransferActivity.bdt.close();
								
					}
				};
				sendAndReceiveThread.start();
			}
        	
        });
        }
	     
 

}
