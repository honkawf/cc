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
import cn.edu.seu.gesturepassword.LockPatternView.DisplayMode;
import cn.edu.seu.gesturepassword.LockPatternView.OnPatternListener;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.login.ReloginActivity;
import cn.edu.seu.main.ExitActivity;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;

public class LockActivity extends Activity implements OnClickListener {

	// private OnPatternListener onPatternListener;

	private LockPatternView lockPatternView;

	private LockPatternUtils lockPatternUtils;

	private int wrongnum = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maingesture);
		Mapplication.getInstance().addActivity(this);

		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);

		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				int result = lockPatternUtils.checkPattern(pattern);
				
				if (result != 1) {
					if (result == 0) {
						lockPatternView.setDisplayMode(DisplayMode.Wrong);
						Toast.makeText(LockActivity.this, "密码错误",
								Toast.LENGTH_LONG).show();
						wrongnum++;
						if(wrongnum == 5){
							wrongnum = 0;
							Intent it = new Intent(LockActivity.this , ReloginActivity.class);
							startActivity(it);
							finish();
						}
					} else {
						lockPatternView.clearPattern();
						Toast.makeText(LockActivity.this, "请设置密码",
								Toast.LENGTH_LONG).show();
					}

				} else {
					Toast.makeText(LockActivity.this, "密码正确", Toast.LENGTH_LONG)
							.show();
					FlipActivity.s = true;
					finish();
					
				}

			}

			public void onPatternCleared() {

			}

			public void onPatternCellAdded(List<Cell> pattern) {

			}
		});
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
        		Intent intent = new Intent(LockActivity.this,ExitActivity.class);
            	startActivity(intent);
		}
		return false;
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
