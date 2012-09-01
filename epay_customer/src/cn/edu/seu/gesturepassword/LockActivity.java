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
import cn.edu.seu.login.FunctionActivity;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.login.ReloginActivity;
import cn.edu.seu.main.MainActivity;

import cn.edu.seu.main.R;

public class LockActivity extends Activity implements OnClickListener {

	// private OnPatternListener onPatternListener;

	private LockPatternView lockPatternView;

	private LockPatternUtils lockPatternUtils;

	private int wrongnum = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maingesture);

		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);

		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				int result = lockPatternUtils.checkPattern(pattern);
				Toast.makeText(LockActivity.this,
						LockPatternUtils.patternToString(pattern),
						Toast.LENGTH_LONG).show();

				if (result != 1) {
					if (result == 0) {
						lockPatternView.setDisplayMode(DisplayMode.Wrong);
						Toast.makeText(LockActivity.this, "密码错误",
								Toast.LENGTH_LONG).show();
						wrongnum++;
						if(wrongnum == 5){
							wrongnum = 0;
							MainActivity.s = true;
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
					MainActivity.s = true;
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
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        new AlertDialog.Builder(LockActivity.this)
	                .setTitle("真的要离开？")
	                .setMessage("你确定要离开")
	                .setPositiveButton("确定",
	                                new DialogInterface.OnClickListener() {
	                                        public void onClick(DialogInterface dialog,
	                                                        int which) {
	                                        	Mapplication.getInstance().exit();
	                                        }
	                                }).show();

	    }
		return false;
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
