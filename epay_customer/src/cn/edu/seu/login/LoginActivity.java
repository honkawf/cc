package cn.edu.seu.login;


import java.io.File;

import cn.edu.seu.main.R;
import cn.edu.seu.ciphertext.MD5;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.main.MainActivity;



import cn.edu.seu.register.RegisterActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button login;
	private TextView register;
	private TextView findback;
	private EditText username;
	private EditText password;
	private MD5 md5;
	private SharedPreferences preferences;
	private Editor editor;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
		 //判断是不是首次登录，
		if (preferences.getBoolean("firststart", true)) {
		    editor = preferences.edit();
		    //将登录标志位设置为false，下次登录时不在显示首次登录界面
		    editor.putBoolean("firststart", false);
		    editor.commit();
		    Intent intent = new Intent(LoginActivity.this , RecordActivity.class);
		    startActivity(intent);
		    finish();
		}

		
		md5 = new MD5();
		login = (Button)findViewById(R.id.login);
		register = (TextView)findViewById(R.id.textView1);
		findback = (TextView)findViewById(R.id.textView2);
		username = (EditText)findViewById(R.id.account);
		password = (EditText)findViewById(R.id.pwd);
		username.getBackground().setAlpha(0);
		password.getBackground().setAlpha(0);
		login.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				File file = new File("sdcard/data" , "local.dat");
				if(!file.exists()){
					Toast.makeText(LoginActivity.this, "请下载密码文件", Toast.LENGTH_LONG)
					.show();
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, ResetActivity.class);
					startActivity(intent);
				}
				else{
					LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
					LocalInfo x = lio.readfile();
					String u = username.getText().toString();
					String p = password.getText().toString();
					Toast.makeText(LoginActivity.this, u+md5.encrypt(p), Toast.LENGTH_LONG)
					.show();
					if(u.equals(x.getUserName()) && md5.encrypt(p).equals(x.getPassword())){
						Intent it = new Intent(LoginActivity.this , MainActivity.class);
						startActivity(it);
						finish();
					}
				}
			}
			
		});
		
		register.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent it = new Intent(LoginActivity.this , RegisterActivity.class);
				startActivity(it);
				finish();
			}
			
		});
		
		findback.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent it = new Intent(LoginActivity.this , FunctionActivity.class);
				startActivity(it);
				finish();
			}
			
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        new AlertDialog.Builder(LoginActivity.this)
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
}
