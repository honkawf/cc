package cn.edu.seu.gesturepassword;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import cn.edu.seu.gesturepassword.LockPatternView.Cell;
import cn.edu.seu.gesturepassword.LockPatternView.OnPatternListener;
import cn.edu.seu.login.Mapplication;

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
				Intent intent=new Intent();
				intent.setClass(SetFirstActivity.this, FlipActivity.class);
				intent.putExtra("flag", source);
				startActivity(intent);
			}
			else{
				new AlertDialog.Builder(SetFirstActivity.this)
                .setTitle("真的要离开？")
                .setMessage("你确定要离开")
                .setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Mapplication.getInstance().exit();
                    }
                }).show();
			}
		}
		return false;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
