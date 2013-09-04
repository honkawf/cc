package cn.edu.seu.login;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.edu.seu.main.R;

import cn.edu.seu.dboperation.CornerListView;
import cn.edu.seu.dboperation.ListViewAdapter;
import cn.edu.seu.record.Record;
import cn.edu.seu.record.Recorddh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RecordActivity extends Activity {

	private Button b1;
	private CornerListView listView;
	private Recorddh dh;
	private ListViewAdapter mListViewAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordlist);
		Mapplication.getInstance().addActivity(this);
		
		b1 = (Button)findViewById(R.id.button1);
		listView = (CornerListView) findViewById(R.id.list1);  
		mListViewAdapter = new ListViewAdapter(this);
		dh = new Recorddh(RecordActivity.this, "recorddb" , null, 1);
        Record [] list = dh.query();
		if(list != null) {
			for(int i = 0 ; i < list.length ; i++ ){
		        HashMap<String, Object> map = new HashMap<String, Object>();
		        map.put("tradenum", String.valueOf(list[i].getTradenum()));
		        map.put("payername", list[i].getPayerName());
		        map.put("payermac", list[i].getPayerMac());
		        map.put("payerimei", list[i].getPayerImei());
		        map.put("receivername", list[i].getReceiverName());
		        map.put("receivermac", list[i].getReceiverMac());
		        map.put("receiveriemi", list[i].getReceiverImei());
		        map.put("price", list[i].getPrice().toString());
		        map.put("tradetype", list[i].getTradeType());
		        map.put("tradetime", list[i].getTradeTime());
		        mListViewAdapter.addData(map);  
		    }
			listView.setAdapter(mListViewAdapter);
			listView.setCacheColorHint(0);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(RecordActivity.this, String.valueOf(arg2), Toast.LENGTH_LONG)
				.show();
				Log.d("1111111111","xxx");
				Intent it = new Intent(RecordActivity.this , DetailActivity.class);
				HashMap<String, Object> tmap = (HashMap<String, Object>)mListViewAdapter.getItem(arg2);
				it.putExtra("tradenum", (String)tmap.get("tradenum"));
				it.putExtra("payername", (String)tmap.get("payername"));
				it.putExtra("payermac", (String)tmap.get("payermac"));
				it.putExtra("payerimei", (String)tmap.get("payerimei"));
				it.putExtra("receivername", (String)tmap.get("receivername"));
				it.putExtra("receivermac", (String)tmap.get("receivermac"));
				it.putExtra("receiverimei", (String)tmap.get("receiverimei"));
				it.putExtra("tradetype", (String)tmap.get("tradetype"));
				it.putExtra("tradetime", (String)tmap.get("tradetime"));
				it.putExtra("price", (String)tmap.get("price"));
				startActivity(it);
			}
		});
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				DateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
		        String time = f.format(new Date());
		        Record r = new Record(0,"huangk" , "999" ,"111","xiaomaok" ,"11111","9999", 10.1 ,"转入" , time );
				dh.insert(r);
				Log.i("11111111111111111" , "1");
			}
		});
	}
}
