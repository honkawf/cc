package cn.edu.seu.check;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.edu.seu.main.R;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CheckActivity extends Activity{
	private Button b1;
	private CornerListView listView;
	private CheckAdapter ca;
	private Checkdh cdh;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check);	
		b1 = (Button)findViewById(R.id.button1);
		ca = new CheckAdapter(this);
		listView = (CornerListView) findViewById(R.id.list1);  
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		cdh = new Checkdh(CheckActivity.this, "recorddb" , null, 1);
        Check [] list = cdh.query();
		if(list != null) {
			for(int i = 0 ; i < list.length ; i++ ){
		        HashMap<String, Object> map = new HashMap<String, Object>();
		        map.put("name", list[i].getPayerName());
		        map.put("cardnum", list[i].getPayerCardnum());
		        map.put("price", list[i].getTotalPrice().toString());
		        map.put("time", "2011");
		        map.put("cash", "是");
		        ca.addData(map);  
		    }
			listView.setAdapter(ca);
			listView.setCacheColorHint(0);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(CheckActivity.this, String.valueOf(arg2), Toast.LENGTH_LONG)
				.show();
				Intent it = new Intent(CheckActivity.this , DetailActivity.class);
				HashMap<String, Object> tmap = (HashMap<String, Object>)ca.getItem(arg2);
				it.putExtra("name", (String)tmap.get("name"));
				it.putExtra("cardnum", (String)tmap.get("cardnum"));
				it.putExtra("time", (String)tmap.get("time"));
				it.putExtra("price", (String)tmap.get("price"));
				it.putExtra("cash", (String)tmap.get("cash"));
				startActivity(it);
			}
		});
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				DateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
		        String time = f.format(new Date());
		        Check mycheck=new Check(1,"test","test","test",0.0,"test","test");
				cdh.insert(mycheck);
			}
		});
	}
}
