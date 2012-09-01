package cn.edu.seu.login;

import cn.edu.seu.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends Activity{

	private TextView name;
	private TextView type;
	private TextView time;
	private TextView price;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdetail);
		Mapplication.getInstance().addActivity(this);
		
		name = (TextView)findViewById(R.id.name);
		type = (TextView)findViewById(R.id.type);
		time = (TextView)findViewById(R.id.time);
		price = (TextView)findViewById(R.id.price);
		
		name.setText(this.getIntent().getStringExtra("name"));
		type.setText(this.getIntent().getStringExtra("type"));
		time.setText(this.getIntent().getStringExtra("time"));
		price.setText(this.getIntent().getStringExtra("price"));
	}
}
