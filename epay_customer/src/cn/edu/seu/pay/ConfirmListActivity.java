package cn.edu.seu.pay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.main.MainActivity;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;
import cn.edu.seu.record.Record;
import cn.edu.seu.record.Recorddh;
import cn.edu.seu.xml.Goods;
import cn.edu.seu.xml.Trade;
import cn.edu.seu.xml.XML;

import cn.edu.seu.main.R;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmListActivity extends Activity{
	private ListView listview;
	private Button confirm;
	private TextView totalView;
	private ConfirmAdapter adapter;
	private String totalprice;
    private TimeOutProgressDialog pd;
	private ArrayList<Map<String,Object>> goodslist;
	private Thread sendAndReceiveThread;
	private final static String TAG="ConfirmListActivity";
	private Trade trade;
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
             	pd=TimeOutProgressDialog.createProgressDialog(ConfirmListActivity.this,50000,new OnTimeOutListener(){

				
					public void onTimeOut(TimeOutProgressDialog dialog) {
						// TODO Auto-generated method stub
						AlertDialog.Builder builder = new Builder(ConfirmListActivity.this);
				    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Intent intent=new Intent(ConfirmListActivity.this,MainActivity.class);
								startActivity(intent);
								ConfirmListActivity.this.finish();
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
            case 0:
                pd.dismiss();
                break;
            case 2:
		    	AlertDialog.Builder builder = new Builder(ConfirmListActivity.this);
		    	builder.setTitle("付款结果").setMessage((String)msg.obj).setCancelable(false).setPositiveButton("确认", new OnClickListener(){

					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(ConfirmListActivity.this,MainActivity.class);
						startActivity(intent);
						GoodsListActivity.flag=1;
						ConfirmListActivity.this.finish();
						MainActivity.bdt.close();
						
					}
		    		
		    	});
		    	builder.show();
            	break;
            case 3:
            	AlertDialog.Builder builder1 = new Builder(ConfirmListActivity.this);
		    	builder1.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(ConfirmListActivity.this,MainActivity.class);
						startActivity(intent);
						ConfirmListActivity.this.finish();
						MainActivity.bdt.close();
						
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
        setContentView(R.layout.confirmlist);
        Intent intent=getIntent();
        totalprice=intent.getStringExtra("totalprice");
        totalView=(TextView)findViewById(R.id.totalprice);
        totalView.setText(totalprice);
        goodslist=GoodsListActivity.goodslist;
        adapter = new ConfirmAdapter(this);
        listview=(ListView)findViewById(R.id.listView2); 
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        confirm=(Button)findViewById(R.id.confirm3);
        confirm.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//从本地文件获取随机码
				//假设随机码是2309916916024662023
				confirm.setVisibility(View.GONE);
				Message msg=handler.obtainMessage();
				msg.what=1;
				msg.obj="正在确认付款";
				msg.sendToTarget();
				sendAndReceiveThread=new Thread()
				{
					public void run()
					{
						try
						{
							Date dt=new Date();
							String cardnumber=MainActivity.person.getCardnum();
							String tradetime=String.valueOf(dt.getTime()/1000);
							String buyerimei=MainActivity.person.getImei();
							Log.i(TAG, buyerimei);
							String username=MainActivity.person.getUsername();
							String buyerdevice=BluetoothDataTransportation.getLocalMac().replaceAll(":","");
							String salerdevice=MainActivity.bdt.getRemoteMac().replaceAll(":","");
							int totalpricefill=(int)(Double.valueOf(totalprice)*100);
							String pricefill=String.format("%08d",totalpricefill);
							String buyerdevicesub=buyerdevice.substring(buyerdevice.length()-4,buyerdevice.length());
							String salerdevicesub=salerdevice.substring(salerdevice.length()-4,salerdevice.length());
							int buyerdevicefill=Integer.parseInt(buyerdevicesub,16);
							String buyerfill=String.format("%05d",buyerdevicefill);
							int salerdevicefill=Integer.parseInt(salerdevicesub,16);
							String salerfill=String.format("%05d",salerdevicefill);
							String words=tradetime+buyerfill+salerfill+pricefill;
							Log.d("words",words);
							RSA rsa=new RSA();
							String cipher=rsa.setRSA(words);
							XML confirmTrade=new XML();
							for(Map<String, Object> map :goodslist)
							{
								confirmTrade.addData(map.get("barcode").toString(), map.get("name").toString(), map.get("price").toString(), map.get("quantity").toString());
							}
							confirmTrade.setTrade(buyerdevice, username, buyerimei, cardnumber, salerdevice, "receivername", "receiverimei", "receivercardnumber", tradetime, totalprice, cipher);
							String xml=confirmTrade.produceTradeXML("confirmTrade");
							Log.d("",xml);
							if(MainActivity.bdt.write(xml))
							{
								byte[] receive=MainActivity.bdt.read();
								Message msg=handler.obtainMessage();
								msg.what=0;
								msg.sendToTarget();
						    	XML confirmXML=new XML();
						    	String sentence=confirmXML.parseSentenceXML(new ByteArrayInputStream(receive));
						    	if(sentence.equals(""))
						    	{
						    		sentence=confirmXML.parseBalanceXML(new ByteArrayInputStream(receive));
						    		msg=handler.obtainMessage();
							    	msg.what=2;
							    	msg.obj="付款成功";
							    	msg.sendToTarget();
				 					Log.d("",new String(receive));
				 					//更新余额,交易记录
				 					//给余额赋值
				 					String balance=sentence;
				 					LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
									lio.modifyBalance(balance);
									//给交易记录赋值
									trade=confirmTrade.getTrade();
									Record record = new Record( 0 ,trade.getPayerName(),trade.getPayerDevice(),trade.getPayerIMEI(),trade.getReceiverName(),trade.getReceiverDevice(),trade.getReceiverIMEI(),Double.parseDouble(trade.getTotalPrice()),"收款", trade.getTradeTime());
									Recorddh rdh = new Recorddh(ConfirmListActivity.this , "recorddb" , null , 1);
									rdh.insert(record);
						    	}
						    	else
						    	{
						    		msg=handler.obtainMessage();
									msg.what=2;
									msg.obj="付款失败";
									msg.sendToTarget();
						    	}
							}
							else
								return;
						}
						catch(Exception e)
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
	public final class ViewHolder{
		public TextView name;
		public TextView barcode;
		public TextView price;
		public TextView count;
	    }
	public class ConfirmAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		public ConfirmAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
			}
	        
	        public int getCount() {
	            // TODO Auto-generated method stub
	             return goodslist.size();
	        }
	 
	        
	        public Object getItem(int arg0) {
	            // TODO Auto-generated method stub
	            return null;
	        }
	 
	        
	        public long getItemId(int arg0) {
	            // TODO Auto-generated method stub
	            return 0;
	        }
	     
	        public View getView(final int position, View convertView, ViewGroup parent) {
	 			ViewHolder holder;
	            if (convertView == null) {
	                holder=new ViewHolder();  
	                convertView = mInflater.inflate(R.layout.confirmitem, null);
	                holder.name=(TextView)convertView.findViewById(R.id.goodsinfo1);
	                holder.barcode=(TextView)convertView.findViewById(R.id.barcode1);
	                holder.price=(TextView)convertView.findViewById(R.id.price1);
	                holder.count=(TextView)convertView.findViewById(R.id.count);
	                convertView.setTag(holder);
	                 
	            }else {
	                 
	                holder = (ViewHolder)convertView.getTag();
	            }
	    		holder.name.setText((String)goodslist.get(position).get("name"));
	            holder.barcode.setText((String)goodslist.get(position).get("barcode"));
	            holder.price.setText((String)goodslist.get(position).get("price"));
	            holder.count.setText("X"+(String)goodslist.get(position).get("quantity"));
	            return convertView;
	        }
	         
	    }
}
