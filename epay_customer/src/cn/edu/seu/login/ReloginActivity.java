package cn.edu.seu.login;

import java.io.File;
import java.util.Properties;

import cn.edu.seu.main.ExitActivity;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.ciphertext.MD5;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReloginActivity extends Activity {
	
	private Button b1;
	private EditText username;
	private EditText password;
	private MD5 md5;
	
	@SuppressLint("ShowToast")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relogin);
		Mapplication.getInstance().addActivity(this);
		
		md5 = new MD5();
		b1 = (Button)findViewById(R.id.login);
		username = (EditText)findViewById(R.id.account);
		password = (EditText)findViewById(R.id.pwd);
		username.getBackground().setAlpha(0);
		password.getBackground().setAlpha(0);
		Log.i("kjjjjjjjjjjjjjj","1");
		Toast.makeText(ReloginActivity.this, "请重新登录", Toast.LENGTH_LONG);
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Properties property =PropertyInfo.getProperties();
				File file = new File(property.getProperty("path") , property.getProperty("filename"));
				if(!file.exists()){
					Toast.makeText(ReloginActivity.this, "请下载密码文件", Toast.LENGTH_LONG)
					.show();
				}
				else{
					Log.i("ReloginActivity","else");
					LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));
					LocalInfo x = lio.readfile();
					String u = username.getText().toString();
					String p = password.getText().toString();
					Log.i("ReloginActivity","1");
					if(u.equals(x.getUserName()) && md5.encrypt(p).equals(x.getPassword())){
						FlipActivity.s = true;
						finish();
						Log.i("ReloginActivity","2");
					}
					else
					{
						Toast.makeText(ReloginActivity.this, "用户名或密码不正确", Toast.LENGTH_LONG).show();
						Log.i("ReloginActivity","3");
					}

				}
			}
			
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
        		Intent intent = new Intent();
            	intent.setClass(ReloginActivity.this,ExitActivity.class);
            	startActivityForResult(intent,100);    
		}
		return false;
	}
}
