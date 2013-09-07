package cn.edu.seu.register;

import java.util.List;

import cn.edu.seu.gesturepassword.LockPatternUtils;
import cn.edu.seu.gesturepassword.LockPatternView;
import cn.edu.seu.gesturepassword.LockPatternView.Cell;
import cn.edu.seu.gesturepassword.LockPatternView.OnPatternListener;

import cn.edu.seu.main.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class SetGestureFirstActivity extends Activity implements OnClickListener {

	// private OnPatternListener onPatternListener;

	private LockPatternView lockPatternView;

	private LockPatternUtils lockPatternUtils;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("firstSet","1");
		setContentView(R.layout.set_first);
		Log.i("firstSet","2");
		
		Intent intent = getIntent();
		Log.i("firstSet","3");
		final String userName = intent.getStringExtra("userName");
		Log.i("firstSet","4");
		final String password = intent.getStringExtra("password");
		Log.i("firstSet","5");
		final String customerName = intent.getStringExtra("customerName");
		Log.i("firstSet","6");
		
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock_first);
		Log.i("firstSet","7");
		lockPatternUtils = new LockPatternUtils(this);
		Log.i("firstSet","8");
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				Log.i("firstSet","9");
				String first_pattern = LockPatternUtils.patternToString(pattern);
				Log.i("firstSet","10");
			    lockPatternView.clearPattern();
			    Log.i("firstSet","11");
			    
				Intent intentToSecond = new Intent();
				Log.i("firstSet","12");
				intentToSecond.putExtra("firstPattern", first_pattern);
				Log.i("firstSet","13");
				intentToSecond.putExtra("userName", userName);
				Log.i("firstSet","14");
				intentToSecond.putExtra("password", password);
				Log.i("firstSet","15");
				intentToSecond.putExtra("customerName", customerName);
				Log.i("firstSet","16");
				intentToSecond.setClass(SetGestureFirstActivity.this, SetGestureSecondActivity.class);
				Log.i("firstSet","17");
		        startActivity(intentToSecond);
		        Log.i("firstSet","18");
		        SetGestureFirstActivity.this.finish();	
		        Log.i("firstSet","19");
			}

			public void onPatternCleared() {

			}

			public void onPatternCellAdded(List<Cell> pattern) {

			}
		});
	}

	public void onClick(View v) {

			
	}
	

}
