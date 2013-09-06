package cn.edu.seu.check;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
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

public class ChequeActivity extends Activity {
	private ListView lv_c;
	private TextView tv_c1;
	private TextView tv_c2;
	private TextView tv_c3;
	private TextView tv_c4;
	private TextView tv_c5;
	private Button btn_back_c;
	private Checkdh cdh;
	ArrayList<HashMap<String, Object>> listItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cheque);
		Mapplication.getInstance().addActivity(this);
		lv_c = (ListView) findViewById(R.id.lv_c);
		
		tv_c1=(TextView) findViewById(R.id.tv_c1);
		tv_c2=(TextView) findViewById(R.id.tv_c2);
		tv_c3=(TextView) findViewById(R.id.tv_c3);
		tv_c4=(TextView) findViewById(R.id.tv_c4);
		tv_c5=(TextView) findViewById(R.id.tv_c5);
		tv_c1.setText("编号");
		tv_c2.setText("付款人");
		tv_c3.setText("金额");
		tv_c4.setText("交易时间");
		tv_c5.setText("是否兑现");
		listItem = new ArrayList<HashMap<String, Object>>();
		lv_c.setAdapter(new MyAdapter());
		Log.i("1","1");
		//随便初始化
		cdh = new Checkdh(ChequeActivity.this, "recorddb" , null, 1);
        Check [] list = cdh.query();
		if(list != null) {
			for(int i = 0 ; i < list.length ; i++ ){
				HashMap<String, Object> map = new HashMap<String, Object>();
		        map.put("name", list[i].getPayerName());
		        map.put("cardnum", list[i].getPayerCardnum());
		        map.put("price", list[i].getTotalPrice().toString());
		        map.put("time", list[i].getTransferTime());
		        map.put("cash", list[i].getIsCashed());
		        map.put("xml",list[i].getXml());
		        listItem.add(map);
			}
		}
		lv_c.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(ChequeActivity.this, String.valueOf(arg2), Toast.LENGTH_LONG)
				.show();
				Intent it = new Intent(ChequeActivity.this , DetailActivity.class);
				HashMap<String, Object> tmap = (HashMap<String, Object>)listItem.get(arg2);
				it.putExtra("name", (String)tmap.get("name"));
				it.putExtra("cardnum", (String)tmap.get("cardnum"));
				it.putExtra("time", (String)tmap.get("time"));
				it.putExtra("price", (String)tmap.get("price"));
				it.putExtra("cash", (String)tmap.get("cash"));
				it.putExtra("xml", (String)tmap.get("xml"));
				startActivity(it);
			}
		});
		btn_back_c=(Button) findViewById(R.id.btn_back_c);
		btn_back_c.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChequeActivity.this.finish();
				Intent intent=new Intent();
				intent.setClass(ChequeActivity.this, FlipActivity.class);
				intent.putExtra("flag", getIntent().getIntExtra("flag", 0));
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cheque, menu);
		return true;
	}
	
	class MyAdapter extends BaseAdapter{
		LayoutInflater li = ChequeActivity.this.getLayoutInflater();

		public int getCount() {
			// TODO Auto-generated method stub
			return listItem.size();
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
            vh.text2.setText((String)listItem.get(position).get("name"));
            vh.text3.setText((String)listItem.get(position).get("price"));
            vh.text4.setText((String)listItem.get(position).get("time"));
            vh.text5.setText((String)listItem.get(position).get("cash"));
 
            
            
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	ChequeActivity.this.finish();
			Intent intent=new Intent();
			intent.setClass(ChequeActivity.this, FlipActivity.class);
			intent.putExtra("flag", getIntent().getIntExtra("flag", 0));
			startActivity(intent);
	    }
		return false;
	}
}
