package cn.edu.seu.register;

import java.io.File;
import java.util.List;
import java.util.Properties;

import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.R;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;

import cn.edu.seu.gesturepassword.LockPatternUtils;
import cn.edu.seu.gesturepassword.LockPatternView;
import cn.edu.seu.gesturepassword.LockPatternView.Cell;
import cn.edu.seu.gesturepassword.LockPatternView.OnPatternListener;

public class SetGestureSecondActivity extends Activity implements OnClickListener {

	// private OnPatternListener onPatternListener;

	private LockPatternView lockPatternView;

	private LockPatternUtils lockPatternUtils;
	private String first_pattern,userName,password,customerName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_second);
		Mapplication.getInstance().addActivity(this);
		
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock_second);

		Intent intent = getIntent();
		first_pattern = intent.getStringExtra("firstPattern");
		userName = intent.getStringExtra("userName");
		password = intent.getStringExtra("password");
		customerName = intent.getStringExtra("customerName");

		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {

				if (first_pattern.equals(LockPatternUtils
						.patternToString(pattern))) {
					Properties property =PropertyInfo.getProperties();
					LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));
					LocalInfo li = new LocalInfo();
					li.setAvailableBalance("0");
					li.setBalance("0");
					li.setGesturePwd(first_pattern);
					li.setPassword(password);
					li.setPrivateKey("0");
					li.setPublicKeyn("0");
					li.setUserName(userName);
					li.setCardnum("0");
					lio.writefile(li);

					// lockPatternUtils.saveLockPattern(pattern);
					lockPatternView.clearPattern();

					Intent intentToCheck = new Intent();
					intentToCheck.setClass(SetGestureSecondActivity.this,
							LinkBankCardActivity.class);
					intentToCheck.putExtra("firstPattern", first_pattern);
					intentToCheck.putExtra("userName", userName);
					Log.i("second", userName);
					intentToCheck.putExtra("password", password);
					Log.i("second", password);
					intentToCheck.putExtra("customerName", customerName);
					
					startActivity(intentToCheck);
					SetGestureSecondActivity.this.finish();

				} else {
					Toast.makeText(SetGestureSecondActivity.this, "两次密码不一致",
							Toast.LENGTH_LONG).show();
					Intent intentToFirst = new Intent();
					intentToFirst.setClass(SetGestureSecondActivity.this,
							SetGestureFirstActivity.class);
					startActivity(intentToFirst);
					SetGestureSecondActivity.this.finish();
				}

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
