package cn.edu.seu.pay;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.seu.guide.GuideActivity;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;
import cn.edu.seu.xml.XML;

import com.zxing.activity.CaptureActivity;

public class StoreInfoActivity extends Activity{
	private TextView tvstorename,tvaddress,tvtype;
	private Button btnConfirm,btnDiscount;
	private TimeOutProgressDialog pd;
	private String storeName,mac,type;
	private boolean loaded=false;
	private Thread sendThread;
	private static final String TAG="StoreInfoActivity";
	 private ArrayList<byte[]> pics = new ArrayList<byte[]>();
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
            	pd.dismiss();
       		 	Intent goodslist = new Intent(StoreInfoActivity.this,GoodsListActivity.class);
    			startActivity(goodslist);   
       		 	StoreInfoActivity.this.finish();
                break;
            case 1:
            	pd=TimeOutProgressDialog.createProgressDialog(StoreInfoActivity.this,50000,new OnTimeOutListener(){

					public void onTimeOut(TimeOutProgressDialog dialog) {
						// TODO Auto-generated method stub
						try{
							sendThread.interrupt();
						}
						catch(Exception e)
						{
							Log.i(TAG, "线程打断失败");
						}
						AlertDialog.Builder builder = new Builder(StoreInfoActivity.this);
				    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Intent intent=new Intent(StoreInfoActivity.this,FlipActivity.class);
								startActivity(intent);
								StoreInfoActivity.this.finish();
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
            case 2:
            	pd.dismiss();
            	AlertDialog.Builder builder = new Builder(StoreInfoActivity.this);
		    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(StoreInfoActivity.this,FlipActivity.class);
						startActivity(intent);
						StoreInfoActivity.this.finish();
						FlipActivity.bdt.close();
						
					}
		    		
		    	});
		    	builder.show();
            	break;
            case 3:
            	pd.dismiss();
            	Intent intent = new Intent(StoreInfoActivity.this,ConfirmPriceActivity.class);
            	intent.putExtra("receive", (byte[])msg.obj);
    			startActivity(intent);   
       		 	StoreInfoActivity.this.finish();
       		 	break;
            case 4:
            	  pd.dismiss();
                  Intent intent1 = new Intent(StoreInfoActivity.this,GuideActivity.class);
                  intent1.putExtra("size" , pics.size());
                  for(int i = 0 ; i < pics.size() ; i++){
                	  intent1.putExtra("guide"+i, pics.get(i));
                  }
                  startActivity(intent1);
                  break;
            }
            super.handleMessage(msg);
        }
    };
	
	public void onCreate(Bundle savedInstanceState)
	{
		 super.onCreate(savedInstanceState); 
         setContentView(R.layout.choose); 
         Mapplication.getInstance().addActivity(this);
         
         tvstorename=(TextView)findViewById(R.id.storename);
         tvaddress=(TextView)findViewById(R.id.address);
         tvtype=(TextView)findViewById(R.id.type);
         btnConfirm=(Button)findViewById(R.id.buy);
         btnDiscount=(Button)findViewById(R.id.discount);
         String Info[]=new String[3];
         try{
        	 Intent intent=getIntent();
        	 Info=intent.getStringExtra("scanResult").split(";");
        	 storeName=Info[0];
        	 mac=Info[1];
        	 type=Info[2];
        	 tvstorename.setText("店名："+storeName);
        	 tvaddress.setText("蓝牙地址："+mac);
        	 if(type.equals("supermarket"))
        		 type="超市";
        	 else if(type.equals("individual"))
        	 {
        		 type="个体商户";
        		 btnDiscount.setVisibility(View.GONE);
        	 }
        	 else
        		 type="信息有误";
        	 tvtype.setText("类型："+type);
        	
         }
         catch(Exception e)
         {
        	 Toast.makeText(StoreInfoActivity.this, "扫描失败，请重扫二维码", Toast.LENGTH_LONG).show();
        	 Intent openCameraIntent = new Intent(StoreInfoActivity.this,CaptureActivity.class);
        	 startActivityForResult(openCameraIntent, 0);   
         }
         btnConfirm.setOnClickListener(new Button.OnClickListener(){
        	 
        	 public void onClick(View v) {
        		 // TODO Auto-generated method stub
        		 if(type.equals("超市"))
        		 {
        			 Log.i("蓝牙地址",mac);
        			 Message msg=handler.obtainMessage();
        			 msg.what=1;
        			 msg.obj="正在连接服务器";
                     msg.sendToTarget();
             		Thread sendThread=new Thread()
             		{
             			public void run()
             			{

             				Message msg=handler.obtainMessage();
                    		msg=handler.obtainMessage();
             				try
             				{
             					FlipActivity.bdt.createSocket();
	             				Log.i("确认信息","顾客已确认");
	                   			 if( FlipActivity.bdt.isConnected())
	                   			 {
	                            		 msg.what=0;
	                            		 msg.sendToTarget();
	                   			 }
	                   			 else
	                   			 {
	                            		 msg.what=2;
	                            		 msg.sendToTarget();
	                   			 }
                   				 
             				}
             				catch(Exception e)
             				{
             					 msg.what=2;
                         		 msg.sendToTarget();
             				}
             			}
             		};
             		sendThread.start();
                 }
                 else if(type.equals("个体商户"))
                 {
                	 Message msg=handler.obtainMessage();
                     msg.what=1;
                     msg.obj="正在连接服务器";
                     msg.sendToTarget();
        		 	Thread sendThread=new Thread()
        			{
        				public void run()
        				{
        					Message msg=handler.obtainMessage();
                    		msg=handler.obtainMessage();
        					Log.i("确认信息","顾客已确认");
                			XML info=new XML();
                			String xml=info.productSentenceXML("我要付款");
							try 
							{
								 FlipActivity.bdt.createSocket();
								 if(FlipActivity.bdt.isConnected())
		                			{
		                				 FlipActivity.bdt.write(xml);
		                    			 byte[] receive=FlipActivity.bdt.read();
		                    			 if(receive!=null)
		                    			 {
		                             		 msg.what=3;
		                             		 msg.obj=receive;
		                             		 msg.sendToTarget();
		                    			 }
		                			}
		                			else
		                			{
		                				msg.what=2;
		                        		msg.sendToTarget();
		                			}
							} catch (Exception e) {
								msg.what=2;
                        		msg.sendToTarget();
							}
                			
            		
        				}
        			};
        			sendThread.start();
                 }
			}
         });
         btnDiscount.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View arg0) {
	            
				Thread sendThread=new Thread()
	            {
					public void run()
					{
						Message msg=handler.obtainMessage();
	                    msg.what=1;
	                    msg.obj="正在连接服务器";
	                    msg.sendToTarget();
						Log.i("确认信息","顾客已确认");
						String xml=XML.producePicXML();
						try 
						{
							FlipActivity.bdt.createSocket();
							if(FlipActivity.bdt.isConnected())
			                {
								FlipActivity.bdt.write(xml);
			                    byte[] receive=FlipActivity.bdt.read();
			                    String size = XML.parseSize(new ByteArrayInputStream(receive));
			                    pics.clear();
			                    for(int i = 0 ; i < Integer.parseInt(size) ; i++){
			                         receive = FlipActivity.bdt.read();
			                         pics.add(receive);
			                    }
			                    Log.i(TAG,String.valueOf(receive));
			                    if(receive!=null)
			                     {
			                    	msg=handler.obtainMessage();
			                    	msg.what=4;
			                        msg.sendToTarget();
	                                FlipActivity.bdt.close();
			                      }
			                      else
			                      {
			                    	 msg=handler.obtainMessage();
			                    	 msg.what=2;
			                         msg.sendToTarget();
			                      }
			                }
			            } catch (Exception e) {
			            	msg.what=2;
			            	msg.sendToTarget();
			            }
					}
	               
	             };
	            sendThread.start();
			}
			});
	}
}
