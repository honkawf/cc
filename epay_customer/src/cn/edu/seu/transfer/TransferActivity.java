package cn.edu.seu.transfer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.pay.TimeOutProgressDialog;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;
import cn.edu.seu.record.GoodsActivity;
import cn.edu.seu.xml.XML;
public class TransferActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageView btnSearch;
	private ListView lvBTDevices; 
	private String mac,name;
	private MyAdapter adtDevices;
	private boolean flag=false;
	private List<String> lstDevices = new ArrayList<String>(); 
	private BluetoothAdapter btAdapt; 
	private final static String TAG="TransferActivity";
	private TimeOutProgressDialog pd;
	private Thread sendAndReceiveThread,changeBackgroundThread;
	private String xml;
	private Button btn_back;
	public static BluetoothDataTransportation bdt=new BluetoothDataTransportation();
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
		         case 1:
		        	 pd=TimeOutProgressDialog.createProgressDialog(TransferActivity.this,50000,new OnTimeOutListener(){

							public void onTimeOut(TimeOutProgressDialog dialog) {
								// TODO Auto-generated method stub
								AlertDialog.Builder builder = new Builder(TransferActivity.this);
						    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener(){

									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										/*Intent intent=new Intent(TransferActivity.this,FlipActivity.class);
										startActivity(intent);*/
										TransferActivity.this.finish();
										changeBackgroundThread.interrupt();
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
		        	 AlertDialog.Builder alertDialog = new Builder(TransferActivity.this);
		        	 alertDialog.setTitle("转账结果").setMessage((String)msg.obj).setCancelable(false);
		        	 alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								TransferActivity.this.finish();
								/*Intent intent=new Intent(TransferActivity.this,FlipActivity.class);
								startActivity(intent);*/
								TransferActivity.bdt.close();
								changeBackgroundThread.interrupt();
								
							}
				    		
				    	});
						alertDialog.show();
						break;
		         case 3:
		            	pd.dismiss();
		            	AlertDialog.Builder builder1 = new Builder(TransferActivity.this);
				    	builder1.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								/*Intent intent=new Intent(TransferActivity.this,FlipActivity.class);
								startActivity(intent);*/
								TransferActivity.this.finish();
								changeBackgroundThread.interrupt();
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
		        	 	btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_left));
						break;
		         case 5:
		        	 	btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_top));
						break;
		         case 6:
		        	 	btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_right));
			    		break;
		         case 7:
		        	 	btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_pressed));
			    		break;
		     }
		     super.handleMessage(msg);
		  }
		};

     @Override 
     public void onCreate(Bundle savedInstanceState) { 
         super.onCreate(savedInstanceState); 
         setContentView(R.layout.transfer);
         Mapplication.getInstance().addActivity(this);
         
         btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能 
         /*打开蓝牙*/
         if(btAdapt==null)
			{
				Toast.makeText(TransferActivity.this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
			}
			else
			{
				if(!btAdapt.isEnabled())
				{
					btAdapt.enable();
				}
			}

         // ListView及其数据源 适配器 
         lvBTDevices = (ListView) this.findViewById(R.id.lvDevices); 
         adtDevices = new MyAdapter(); 
         lvBTDevices.setAdapter(adtDevices); 
         lvBTDevices.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				if(btAdapt.isDiscovering())
					btAdapt.cancelDiscovery(); 
                String str = lstDevices.get(position); 
                String[] values = str.split(";");
                name=values[0];
               	mac= values[1];
                bdt.connect(mac);
                Log.i(TAG, name);
                Intent flagIntent=getIntent();
                Intent intent;
                if(flagIntent.getStringExtra("flag").equals("transfer"))
                {
                	TransferActivity.bdt.connect(mac);
                	intent=new Intent(TransferActivity.this,ReceiverInfoActivity.class);
                	intent.putExtra("name",name);
                    intent.putExtra("mac",mac);
                    startActivity(intent);
                    Log.i(TAG, name);
					TransferActivity.this.finish();

                }
                else if(flagIntent.getStringExtra("flag").equals("transmit"))
                {
                	//转发电子支票
                     xml=flagIntent.getStringExtra("xml");
                     Log.i(TAG,xml);
                	 Message msg=handler.obtainMessage();
                	 msg.what=1;
                	 msg.sendToTarget();
                	 sendAndReceiveThread=new Thread(){
                		 public void run()
                		 {
                			
                			 try
                			 {
                				 TransferActivity.bdt.connect(mac);
                            	 TransferActivity.bdt.createSocket();
                            	 TransferActivity.bdt.write(xml);
                            	 byte []receive=TransferActivity.bdt.read();
                            	 if(receive!=null)
            						{
                            		 	XML info=new XML(); 
            							String sentence=info.parseSentenceXML(new ByteArrayInputStream(receive));
            							Message msg=handler.obtainMessage();
            							msg.what=2;
            							if(sentence.equals("转账成功"))
            							{
            								msg.obj="转账成功";
            								msg.sendToTarget();
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
                			 }catch(Exception e)
                			 {
                				 Message msg=handler.obtainMessage();
                				 msg.what=3;
                				 msg.sendToTarget();
                			 }
                			 
                		 }
                	 };
                	 sendAndReceiveThread.start();
                }
			} 	
		});
   
         btn_back = (Button)findViewById(R.id.btn_back_p);
         btn_back.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				TransferActivity.this.finish();
			}
        	 
         });
         
         btnSearch=(ImageView) findViewById(R.id.btnSearch);
         btnSearch.setOnClickListener(new OnClickEvent());
         changeBackgroundThread=new Thread(){
 	    	public void run()
 	    	{
 	    		try{
 	    			long start=System.currentTimeMillis();
 	    			while(true)
 	    			{
 	    				long end=System.currentTimeMillis();
 	    				long last=end-start;
 	    				if(last>60000)
 	    					break;
 	    				try
 	    				{
 	    					Message msg=handler.obtainMessage();
 	 	    				sleep(500);
 	 	    				msg.what=4;
 	 	    				msg.sendToTarget();
 	 	    				Log.i(TAG,"1");
 	 	    				sleep(500);
 	 	    				msg=handler.obtainMessage();
 	 	    				msg.what=5;
 	 	    				Log.i(TAG,"2");
 	 	    				msg.sendToTarget();
 	 	    				sleep(500);
 	 	    				msg=handler.obtainMessage();
 	 	    				msg.what=6;
 	 	    				Log.i(TAG,"3");
 	 	    				msg.sendToTarget();
 	 	    				sleep(500);
 	 	    				msg=handler.obtainMessage();
 	 	    				msg.what=7;
 	 	    				Log.i(TAG,"4");
 	 	    				msg.sendToTarget();
 	    				}
 	    				catch(InterruptedException e)
 	    				{
 	    					Log.i(TAG, e.getMessage());
 	    					break;
 	    				}
 	    			}
	    			TransferActivity.this.finish();
 	    			
 	    		}
 	    		catch(Exception e)
 	    		{
 	    			Log.i(TAG,e.getMessage());
 	    		}
 	    	}
 	    };
 	   
 
         // ============================================================ 
         // 注册Receiver来获取蓝牙设备相关的结果 
         IntentFilter intent = new IntentFilter(); 
         intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果 
         intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); 
         intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED); 
         intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); 
         registerReceiver(searchDevices, intent);

         
     } 
  
     private class MyAdapter extends BaseAdapter{        
         public int getCount() {
             // TODO Auto-generated method stub
             return lstDevices.size();
         }

         public Object getItem(int position) {
             // TODO Auto-generated method stub
             return position;
         }

         public long getItemId(int position) {
             // TODO Auto-generated method stub
             return position;
         }

         public View getView(int position, View convertView, ViewGroup parent) {
             // TODO Auto-generated method stub
             TextView mTextView = new TextView(getApplicationContext());
             mTextView.setText(lstDevices.get(position));
             mTextView.setTextSize(20);
             mTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.singleline_listview_bg));
             mTextView.setTextColor(Color.GRAY);
             
             return mTextView;
         }
         
     }


	class OnClickEvent implements Button.OnClickListener{
     public void onClick(View v)
     {
    	 try
    	 {

        	 if(!flag)
        	 {
        		 flag=true;
        		 if (v==btnSearch)
            	 {
            		 if(btAdapt==null)
        				{
        					Toast.makeText(TransferActivity.this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
        				}
        				else
        				{
        					if(!btAdapt.isEnabled())
        					{
        						btAdapt.enable();
        					}
        				}
        				btAdapt.startDiscovery();
        				changeBackgroundThread.start();
            	 }
        	 }
    	 }
    	 catch(Exception e)
    	 {
    		 Log.i(TAG,e.getMessage());
    	 }
     }
     }
     
     private final BroadcastReceiver searchDevices = new BroadcastReceiver() {   
         @Override
         public void onReceive(Context context, Intent intent) {      

			Log.i("测试点","2");
             String action = intent.getAction(); 
             Bundle b = intent.getExtras(); 
             Object[] lstName = b.keySet().toArray(); 
				Log.i("测试点","4");

             // 显示所有收到的消息及其细节 
             for (int i = 0; i < lstName.length; i++) { 
                 String keyName = lstName.toString(); 
                 Log.e(keyName, String.valueOf(b.get(keyName))); 
             } 
				Log.i("测试点","5");

             BluetoothDevice device = null; 
             // 搜索设备时，取得设备的MAC地址 
             if (BluetoothDevice.ACTION_FOUND.equals(action)) { 
                 device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
 				Log.i("测试点","6");
                     String str =device.getName() + ";" + device.getAddress(); 
                     if (lstDevices.indexOf(str) == -1)// 防止重复添加 
                         lstDevices.add(str); // 获取设备名称和mac地址 
     				Log.i("测试点","3");
                    adtDevices.notifyDataSetChanged(); 
             }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){ 
                 device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
                 
                 switch (device.getBondState()) { 
                 case BluetoothDevice.BOND_BONDING: 
                     Log.d("BlueToothTestActivity", "正在配对......"); 
                     break; 
                 case BluetoothDevice.BOND_BONDED: 
                     Log.d("BlueToothTestActivity", "完成配对"); 
                     //connect(device);//连接设备 
                     break; 
                 case BluetoothDevice.BOND_NONE: 
                     Log.d("BlueToothTestActivity", "取消配对"); 
                 default: 
                     break; 
                 } 
             } 
              
         } 
     }; 
  
     @Override 
     protected void onDestroy() { 
         this.unregisterReceiver(searchDevices); 
         super.onDestroy(); 
         try
 		{
 			changeBackgroundThread.interrupt();
 		}
 		catch(Exception e)
 		{
 			Log.i(TAG,"线程中断失败");
 		}
     } 
     public boolean onKeyDown(int keyCode, KeyEvent event) {
 	    if (keyCode == KeyEvent.KEYCODE_BACK) {
 	    	TransferActivity.this.finish();
 	    }
 		return false;
 	}
}