package cn.edu.seu.financing;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.pay.ConfirmListActivity;
import cn.edu.seu.pay.ConfirmPriceActivity;
import cn.edu.seu.pay.GoodsListActivity;
import cn.edu.seu.pay.TimeOutProgressDialog;
import cn.edu.seu.pay.TimeOutProgressDialog.OnTimeOutListener;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ManageInfoActivity extends Activity {
	private ListView lv_m;
	private Button btn_back_ma;
	private MyAdapter adapter;
	private Thread sendAndReceiveThread;
	private static final String TAG="ManageInfoActivity";
	private TimeOutProgressDialog pd;
	private ArrayList<HashMap<String,String>> tradelist=new ArrayList<HashMap<String,String>>();
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
             	pd=TimeOutProgressDialog.createProgressDialog(ManageInfoActivity.this,50000,new OnTimeOutListener(){
             		public void onTimeOut(TimeOutProgressDialog dialog) {
						// TODO Auto-generated method stub
						AlertDialog.Builder builder = new Builder(ManageInfoActivity.this);
				    	builder.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								/*Intent intent=new Intent(ConfirmListActivity.this,FlipActivity.class);
								startActivity(intent);*/
								FlipActivity.id=1;
								ManageInfoActivity.this.finish();
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
            	adapter.notifyDataSetChanged();
            	pd.dismiss();
            	break;
            case 3:
            	pd.dismiss();
            	AlertDialog.Builder builder1 = new Builder(ManageInfoActivity.this);
		    	builder1.setTitle("连接信息").setMessage("连接失败").setCancelable(false).setPositiveButton("确认", new OnClickListener(){

					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						/*Intent intent=new Intent(ManageInfoActivity.this,FlipActivity.class);
						startActivity(intent);*/
						ManageInfoActivity.this.finish();
						
					}
		    		
		    	});
		    	builder1.show();
		    	break;
            }
            super.handleMessage(msg);
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manageinfo);
		lv_m = (ListView) findViewById(R.id.lv_m);
		//Log.i("1","1");
		//随便初始化
		Mapplication.getInstance().addActivity(this);
		Log.i("size",String.valueOf(tradelist.size()));
		adapter=new MyAdapter();
		lv_m.setAdapter(adapter);
		btn_back_ma=(Button) findViewById(R.id.btn_back_ma);
		btn_back_ma.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
					FlipActivity.id=1;
					ManageInfoActivity.this.finish();
			}
			
		});
		
		Message msg=handler.obtainMessage();
		msg.what=1;
		msg.obj="正在查询";
		msg.sendToTarget();
		sendAndReceiveThread=new Thread()
		{
			public void run()
			{
				Properties property=PropertyInfo.getProperties();
				try
				{
					NetDataTransportation ndt=new NetDataTransportation();
					ndt.connect(property.getProperty("serverAddress", "honka.xicp.net"), Integer.parseInt(property.getProperty("serverPort", "30145")));
					String xml=XML.produceFinancingXML(FlipActivity.person.getUsername());
					ndt.write(xml);
					byte []receive=ndt.read();
					Log.i(TAG,new String(receive));
					tradelist=XML.parseFinancingXML(new ByteArrayInputStream(receive));
					Message msg=handler.obtainMessage();
					msg.what=2;
					msg.sendToTarget();
					
				}
				catch(Exception e)
				{
					Message msg=handler.obtainMessage();
					msg.what=3;
					msg.sendToTarget();
					
					Log.i(TAG,"网络连接失败");
				}
			}
		};
		sendAndReceiveThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cheque, menu);
		return true;
	}
	
	class MyAdapter extends BaseAdapter{
		LayoutInflater li = ManageInfoActivity.this.getLayoutInflater();
		public int getCount() {
			// TODO Auto-generated method stub
			return tradelist.size();
		}

		
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		
		public View getView(int position, View v, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Log.i("position",String.valueOf(position));
			
			ViewHolder vh = null;
			//if (v == null) {
			v = li.inflate(R.layout.cell1, null);
            vh=new ViewHolder();
            vh.tv1=(TextView) v.findViewById(R.id.tv1);
            vh.tv2=(TextView) v.findViewById(R.id.tv2);
            vh.tv3=(TextView) v.findViewById(R.id.tv3);
            vh.tv4=(TextView) v.findViewById(R.id.tv4);
            Log.i("test","1");
            vh.tv1.setText(String.valueOf(position));
            vh.tv2.setText(tradelist.get(position).get("type"));
            vh.tv3.setText(tradelist.get(position).get("amount"));
            vh.tv4.setText(tradelist.get(position).get("starttime"));
 
            
            
            v.setTag(vh);
            
			//}
			// else {
					//vh = (ViewHolder) v.getTag();
				//}
            return v;
		}
		
		class ViewHolder {
			TextView tv1;
			TextView tv2;
			TextView tv3;
			TextView tv4;
		}
		
	}
	  final class MyEntry<K, V> implements Map.Entry<K, V> {
  	 	private  K key;
  	    private V value;

  	    public MyEntry(K key, V value) {
  	        this.key = key;
  	        this.value = value;
  	    }

  	    public K getKey() {
  	        return key;
  	    }

  	    public V getValue() {
  	        return value;
  	    }

  	    public V setValue(V value) {
  	        V old = this.value;
  	        this.value = value;
  	        return old;
  	    }
  }
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if (keyCode == KeyEvent.KEYCODE_BACK) {
				FlipActivity.id=1;
				this.finish();
		    }
			return false;
		}

}
