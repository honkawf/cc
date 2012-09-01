package cn.edu.seu.financing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import cn.edu.seu.main.R;

public class DepositSecondActivity extends Activity {

	private static final String[] choice ={"CPI同浮动","按基金利率" , "按固定利率"};     
    private Spinner spinner;   
    private MyAdapter adapter;
	private Button button;
	private Intent it;
	private String result;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposit_second);
		
		spinner = (Spinner) findViewById(R.id.spinner1);
		button = (Button)findViewById(R.id.button1);
		adapter = new MyAdapter(this, choice);
		spinner.setAdapter(adapter);
		spinner.setSelection(0,true);
		result = "0";
		it = new Intent(DepositSecondActivity.this , DepositThirdActivity.class);
		it.putExtra("method", getIntent().getStringExtra("method"));
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				result = ""+arg2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
			 
		});
		
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				it.putExtra("rate", result);
				startActivity(it);
				finish();
			}
		});
	}
}
