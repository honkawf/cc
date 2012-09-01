package cn.edu.seu.financing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.edu.seu.main.R;

public class FinancingActivity extends Activity {

	private Button b1;
	private Button b2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.financing);
		b1 = (Button)findViewById(R.id.button1);
		b2 = (Button)findViewById(R.id.button2);
		
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent it = new Intent(FinancingActivity.this , DepositFirstActivity.class);
				startActivity(it);
				finish();
			}
		});
		
		b2.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent it = new Intent(FinancingActivity.this , InterestFirstActivity.class);
				startActivity(it);
			}
		});
	}
}
