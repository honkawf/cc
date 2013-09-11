package cn.edu.seu.record;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.seu.check.ChequeActivity;
import cn.edu.seu.check.DetailActivity;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.personinfomodify.ModifyPwdActivity;
import cn.edu.seu.xml.Trade;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GoodsActivity extends Activity {
	private ListView lv_g;
	private TextView tv_g1;
	private TextView tv_g2;
	private TextView tv_g3;
	private TextView tv_g4;
	private TextView tv_g5;
	private Button btn_back_g;
	private Recorddh dh;
	private ArrayList<MyEntry<String,Trade>> tradelist=new ArrayList<MyEntry<String,Trade>>();
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods);
		Mapplication.getInstance().addActivity(this);
		
		lv_g = (ListView) findViewById(R.id.lv_g);
		lv_g.setAdapter(new MyAdapter());
		tv_g1=(TextView) findViewById(R.id.tv_g1);
		tv_g2=(TextView) findViewById(R.id.tv_g2);
		tv_g3=(TextView) findViewById(R.id.tv_g3);
		tv_g4=(TextView) findViewById(R.id.tv_g4);
		tv_g5=(TextView) findViewById(R.id.tv_g5);
		tv_g1.setText("编号");
		tv_g2.setText("付款人");
		tv_g3.setText("收款人");
		tv_g4.setText("金额");
		tv_g5.setText("交易时间");
		//随便初始化
		dh = new Recorddh(GoodsActivity.this, "recorddb" , null, 1);
        Record [] list = dh.query();
        
		if(list != null) {
			for(int i = 0 ; i < list.length ; i++ ){
				Trade trade=new Trade();
				trade.setPayerName(list[i].getPayerName());
				trade.setReceiverName(list[i].getReceiverName());
				trade.setTotalPrice(list[i].getPrice().toString());
				Date date = new Date(Long.parseLong(list[i].getTradeTime())*1000);
				SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd");
				trade.setTradeTime(myFmt.format(date));
				MyEntry<String,Trade> entry=new MyEntry<String,Trade>(String.valueOf(list[i].getTradenum()),trade);
				tradelist.add(entry);
		    }
		}
		
		btn_back_g=(Button) findViewById(R.id.btn_back_g);
		btn_back_g.setOnClickListener(new Button.OnClickListener(){


			public void onClick(View v) {
				// TODO Auto-generated method stub
				FlipActivity.id=2;
				GoodsActivity.this.finish();
			}
			
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	FlipActivity.id=2;
			GoodsActivity.this.finish();
	    }
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cheque, menu);
		return true;
	}
	
	class MyAdapter extends BaseAdapter{
		LayoutInflater li = GoodsActivity.this.getLayoutInflater();

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
			Log.i("1","1");
			ViewHolder vh = null;
			//if (v == null) {
			v = li.inflate(R.layout.cell, null);
            vh=new ViewHolder();
            vh.text1=(TextView) v.findViewById(R.id.text1);
            vh.text2=(TextView) v.findViewById(R.id.text2);
            vh.text3=(TextView) v.findViewById(R.id.text3);
            vh.text4=(TextView) v.findViewById(R.id.text4);
            vh.text5=(TextView) v.findViewById(R.id.text5);
            Log.i("test","1");
            vh.text1.setText(String.valueOf(position));
            vh.text2.setText(tradelist.get(position).getValue().getPayerName());
            vh.text3.setText(tradelist.get(position).getValue().getReceiverName());
            vh.text4.setText(tradelist.get(position).getValue().getTotalPrice());
            vh.text5.setText(tradelist.get(position).getValue().getTradeTime());
 
            
            
            v.setTag(vh);
            
			//}
			// else {
					//vh = (ViewHolder) v.getTag();
				//}
            return v;
		}
		
		class ViewHolder {
			TextView text1;
			TextView text2;
			TextView text3;
			TextView text4;
			TextView text5;
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

}
