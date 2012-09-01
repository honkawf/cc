package cn.edu.seu.personinfomodify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.edu.seu.gesturepassword.SetFirstActivity;

import cn.edu.seu.main.R;

public class ModifyActivity extends Activity {

	private Button b1;
	private Button b2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify);
			
		b1 = (Button)findViewById(R.id.button1);
		b2 = (Button)findViewById(R.id.button2);
		
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent it = new Intent(ModifyActivity.this , ModifyPersonInfoActivity.class);
				startActivity(it);
			}
		});
		
		b2.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent it = new Intent(ModifyActivity.this , SetFirstActivity.class);
				startActivity(it);
			}
		});
	}
		
}
