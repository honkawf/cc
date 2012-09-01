package cn.edu.seu.financing;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;


import cn.edu.seu.main.R;

public class DepositFirstActivity extends Activity {

	private static final String[] choice ={"活期","定期3个月","定期6个月","定期1年","定期2年" , "定期3年" , "定期5年"};     
    private Spinner spinner;   
    private MyAdapter adapter;
	private Button button;
	private Intent it;
	public String result;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposit_first);
		spinner = (Spinner) findViewById(R.id.spinner1);
		button = (Button)findViewById(R.id.button1);
		it = new Intent(DepositFirstActivity.this , DepositSecondActivity.class);
		adapter = new MyAdapter(this, choice);
		spinner.setAdapter(adapter);
		spinner.setSelection(0,true);
		result = "0";
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				result = "" +arg2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
			 
		});
		
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				it.putExtra("method", result);
				startActivity(it);
				finish();
			}
		});
	}
}
