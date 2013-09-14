package cn.edu.seu.transfer;

import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.pay.TimeOutProgressDialog;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;


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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReceiverInfoActivity extends Activity{
	private TextView receivername,address;
	private Button btnConfirm,btn_back_c;
	private TimeOutProgressDialog pd;
	private Thread sendAndReceiveThread;
	private final static String TAG="ReceiverInfoActivity";
		
	private String name,mac;
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
                pd.dismiss();
                Intent intent=new Intent(ReceiverInfoActivity.this,TransferPriceActivity.class);
                startActivity(intent);
                ReceiverInfoActivity.this.finish();
                break;
            case 1:
	        	 pd=TimeOutProgressDialog.createProgressDialog(ReceiverInfoActivity.this,20000,new OnTimeOutListener(){

						public void onTimeOut(TimeOutProgressDialog dialog) {
							// TODO Auto-generated method stub
							AlertDialog.Builder builder = new Builder(ReceiverInfoActivity.this);
					    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									/*Intent intent=new Intent(ReceiverInfoActivity.this,FlipActivity.class);
									startActivity(intent);*/
									FlipActivity.id=0;
									ReceiverInfoActivity.this.finish();
									try
									{
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
            	AlertDialog.Builder builder = new Builder(ReceiverInfoActivity.this);
		    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						/*Intent intent=new Intent(ReceiverInfoActivity.this,FlipActivity.class);
						startActivity(intent);*/
						FlipActivity.id=0;
						ReceiverInfoActivity.this.finish();
						try
						{
							TransferActivity.bdt.close();
						}
						catch(Exception e)
						{
							Log.i(TAG,"连接已经关闭");
						}
						
					}
		    		
		    	});
		    	builder.show();
             	break;
            }
            super.handleMessage(msg);
        }
    };
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		 super.onCreate(savedInstanceState); 
         setContentView(R.layout.receiver); 
         Mapplication.getInstance().addActivity(this);
         
         receivername=(TextView)findViewById(R.id.receivername);
         address=(TextView)findViewById(R.id.address);
         btnConfirm=(Button)findViewById(R.id.confirm1);
         btn_back_c=(Button)findViewById(R.id.btn_back_c);
         Intent intent=getIntent();
         name=intent.getStringExtra("name");
         mac=intent.getStringExtra("mac");
         receivername.setText("收款方："+name);
         address.setText("\n蓝牙地址："+mac);
         btn_back_c.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				FlipActivity.id=0;
				ReceiverInfoActivity.this.finish();
			}
        	 
         });
         btnConfirm.setOnClickListener(new Button.OnClickListener(){
        	 public void onClick(View v) {
        		 // TODO Auto-generated method stub
        		 Message msg=handler.obtainMessage();
        		 msg.what=1;
        		 msg.obj="正在连接";
        		 msg.sendToTarget();
        		 Log.i(TAG,"正在连接");
        		 sendAndReceiveThread=new Thread()
        		 {
        			 public void run()
        			 {
        				 try
                		 {
        	        		 Log.i(TAG,"创建socket");
        					 TransferActivity.bdt.createSocket();
        					 Message msg=handler.obtainMessage();
        					 msg=handler.obtainMessage();
        					 msg.what=0;
        					 msg.sendToTarget();
                		 }
        				 catch(Exception e)
        				 {
        					 Message msg=handler.obtainMessage();
        					 Log.i(TAG,"连接失败");
        					 msg=handler.obtainMessage();
        					 msg.what=2;
        					 msg.sendToTarget();
        				 }

        			 }
        		 };
        		 sendAndReceiveThread.start();
			}
         });
	}
    public boolean onKeyDown(int keyCode, KeyEvent event) {
 	    if (keyCode == KeyEvent.KEYCODE_BACK) {
 	    	FlipActivity.id=0;
 	    	ReceiverInfoActivity.this.finish();
 	    }
 		return false;
 	} 
 
}
