package cn.edu.seu.pay;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.ciphertext.RSA;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;
import cn.edu.seu.record.Record;
import cn.edu.seu.record.Recorddh;
import cn.edu.seu.xml.Trade;
import cn.edu.seu.xml.XML;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmPriceActivity extends Activity{
	private TextView price,receivername;
	private Button confirm;
    private TimeOutProgressDialog pd;
    private byte[] receive;
    private Trade trade;
    private Thread sendAndReceiveThread;
    private final static String TAG="ConfirmPriceActivity";
	private Handler handler = new Handler() {
        
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
            	pd=TimeOutProgressDialog.createProgressDialog(ConfirmPriceActivity.this,50000,new OnTimeOutListener(){

					public void onTimeOut(TimeOutProgressDialog dialog) {
						// TODO Auto-generated method stub
						AlertDialog.Builder builder = new Builder(ConfirmPriceActivity.this);
				    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

							
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Intent intent=new Intent(ConfirmPriceActivity.this,FlipActivity.class);
								startActivity(intent);
								ConfirmPriceActivity.this.finish();
								FlipActivity.bdt.close();
								
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
            case 0:
                pd.dismiss();
                break;
            case 2:
            	pd.dismiss();
		    	AlertDialog.Builder builder = new Builder(ConfirmPriceActivity.this);
		    	builder.setTitle("付款结果").setMessage((String)msg.obj).setCancelable(false).setPositiveButton("确认", new OnClickListener(){

					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(ConfirmPriceActivity.this,FlipActivity.class);
						startActivity(intent);
						ConfirmPriceActivity.this.finish();
						FlipActivity.bdt.close();
						
					}
		    		
		    	});
		    	builder.show();
            	break;
            case 3:
            	pd.dismiss();
            	AlertDialog.Builder builder1 = new Builder(ConfirmPriceActivity.this);
		    	builder1.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(ConfirmPriceActivity.this,FlipActivity.class);
						startActivity(intent);
						ConfirmPriceActivity.this.finish();
						FlipActivity.bdt.close();
						
					}
		    		
		    	});
		    	builder1.show();
		    	break;
            }
            super.handleMessage(msg);
        }
    };
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.confirmprice);
        Mapplication.getInstance().addActivity(this);
        
        confirm=(Button) findViewById(R.id.confirm4);
        price=(TextView)findViewById(R.id.price);
        receivername=(TextView)findViewById(R.id.receivername);
        Intent intent=getIntent();
        receive=intent.getByteArrayExtra("receive");
        XML info =new XML();
        info.setTrade(trade);
        trade=info.parseIndividualTradeXML(new ByteArrayInputStream(receive));
        price.setText(trade.getTotalPrice()+"元");
        receivername.setText("收款人:"+trade.getReceiverName());
        confirm.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Date dt=new Date();
				String cardnumber=FlipActivity.person.getCardnum();
				String tradetime=String.valueOf(dt.getTime()/1000);
				String buyerimei=FlipActivity.person.getImei();
				String username=FlipActivity.person.getUsername();
				String buyerdevice=BluetoothDataTransportation.getLocalMac().replaceAll(":","");
				String salerdevice=FlipActivity.bdt.getRemoteMac().replaceAll(":","");
				int totalpricefill=(int)(Double.valueOf(trade.getTotalPrice())*100);
				String pricefill=String.format("%08d",totalpricefill);
				String buyerdevicesub=buyerdevice.substring(buyerdevice.length()-4,buyerdevice.length());
				String salerdevicesub=salerdevice.substring(salerdevice.length()-4,salerdevice.length());
				int buyerdevicefill=Integer.parseInt(buyerdevicesub,16);
				String buyerfill=String.format("%05d",buyerdevicefill);
				int salerdevicefill=Integer.parseInt(salerdevicesub,16);
				String salerfill=String.format("%05d",salerdevicefill);
				String words=tradetime+buyerfill+salerfill+pricefill;
				Log.d("words",words);
				RSA rsa=new RSA(FlipActivity.person.getPrivatekey(),"0");
				String cipher=rsa.setRSA(words);
				XML confirmTrade=new XML();
				trade.setPayerDevice(buyerdevice);
				trade.setPayerName(username);
				trade.setPayerIMEI(buyerimei);
				trade.setPayerCardNumber(cardnumber);
				trade.setTradeTime(tradetime);
				trade.setCipher(cipher);
				confirmTrade.setTrade(trade);
				String xml=confirmTrade.produceIndividualTradeXML("individualTrade");
				FlipActivity.bdt.write(xml);
				Message msg=handler.obtainMessage();
				msg.what=1;
				msg.obj="正在确认付款";
				msg.sendToTarget();
				sendAndReceiveThread=new Thread()
				{
					public void run()
					{
						byte receive[]=FlipActivity.bdt.read();
						XML payResult=new XML();
						String sentence=payResult.parseSentenceXML(new ByteArrayInputStream(receive));
						if(sentence=="")
						{
							//更新余额,交易记录
		 					//给余额赋值
							sentence=payResult.parseBalanceXML(new ByteArrayInputStream(receive));
							String balance = sentence;
							Properties property =PropertyInfo.getProperties();
							LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));							lio.modifyBalance(balance);
							//给交易记录赋值
							Record record = new Record( 0 ,trade.getPayerName(),trade.getPayerDevice(),trade.getPayerIMEI(),trade.getReceiverName(),trade.getReceiverDevice(),trade.getReceiverIMEI(),Double.parseDouble(trade.getTotalPrice()),"收款", trade.getTradeTime());
							Recorddh rdh = new Recorddh(ConfirmPriceActivity.this , "recorddb" , null , 1);
							rdh.insert(record);
							Message msg=handler.obtainMessage();
		            		msg.what=2;
		            		msg.obj="付款成功";
		            		msg.sendToTarget();
						}
						else if(sentence.equals("付款失败"))
						{
							Message msg=handler.obtainMessage();
		            		msg.what=2;
		            		msg.obj="付款失败";
		            		msg.sendToTarget();
						}
						else
						{
							Message msg=handler.obtainMessage();
		            		msg.what=3;
		            		msg.sendToTarget();
						}
					}
				};
				sendAndReceiveThread.start();
				
			}
        	
        });
	}
}
