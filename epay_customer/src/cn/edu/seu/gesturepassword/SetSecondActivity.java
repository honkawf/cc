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
import android.widget.Toast;

import cn.edu.seu.gesturepassword.LockPatternView.Cell;
import cn.edu.seu.gesturepassword.LockPatternView.OnPatternListener;
import cn.edu.seu.login.Mapplication;

import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;

public class SetSecondActivity extends Activity implements OnClickListener {

	// private OnPatternListener onPatternListener;

	private LockPatternView lockPatternView;

	private LockPatternUtils lockPatternUtils;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_second);
		Mapplication.getInstance().addActivity(this);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock_second);

		Intent intent = getIntent();
        final String first_pattern = intent.getStringExtra("firstPattern");
        
		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				
				if( first_pattern.equals(LockPatternUtils.patternToString(pattern))){
					lockPatternUtils.saveLockPattern(pattern);
					Toast.makeText(SetSecondActivity.this, "密码已经设置", Toast.LENGTH_LONG)
					.show();
					lockPatternView.clearPattern();
					int source = getIntent().getIntExtra("flag", -1);
					if (source > -1){
						Intent intent=new Intent();
						intent.setClass(SetSecondActivity.this, FlipActivity.class);
						intent.putExtra("flag", source);
						startActivity(intent);
						SetSecondActivity.this.finish();
					}
					else{
						
					}
		            SetSecondActivity.this.finish();
				} else{
					Toast.makeText(SetSecondActivity.this, "输入不一致，请重新输入", Toast.LENGTH_LONG)
					.show();
					Intent intentToFirst = new Intent();
					intentToFirst.setClass(SetSecondActivity.this, SetFirstActivity.class);
		            startActivity(intentToFirst);
		            SetSecondActivity.this.finish();
				}
				
				
			
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
				intent.setClass(SetSecondActivity.this, FlipActivity.class);
				intent.putExtra("flag", source);
				startActivity(intent);
			}
			else{
				new AlertDialog.Builder(SetSecondActivity.this)
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
