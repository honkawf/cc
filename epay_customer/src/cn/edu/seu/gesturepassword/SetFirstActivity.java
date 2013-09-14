package cn.edu.seu.gesturepassword;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import cn.edu.seu.gesturepassword.LockPatternView.Cell;
import cn.edu.seu.gesturepassword.LockPatternView.OnPatternListener;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.ExitActivity;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;

public class SetFirstActivity extends Activity implements OnClickListener {

	// private OnPatternListener onPatternListener;

	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_first);
		Mapplication.getInstance().addActivity(this);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock_first);


		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				String first_pattern = LockPatternUtils.patternToString(pattern);
			    lockPatternView.clearPattern();
			    
				Intent intent = new Intent();
				intent.putExtra("firstPattern", first_pattern);
				intent.putExtra("flag", getIntent().getIntExtra("flag", -1));
		        intent.setClass(SetFirstActivity.this, SetSecondActivity.class);
		        startActivity(intent);
		        SetFirstActivity.this.finish();
									
	
							
			}

			public void onPatternCleared() {

			}

			public void onPatternCellAdded(List<Cell> pattern) {

			}
		});
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	int source = getIntent().getIntExtra("flag", -1);
			if (source > -1){
				FlipActivity.id =  source;
				finish();
			}
			else{

        		Intent intent = new Intent(SetFirstActivity.this,ExitActivity.class);
        		startActivity(intent);
			}
		}
		return false;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
