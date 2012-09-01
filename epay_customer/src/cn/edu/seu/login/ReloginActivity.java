package cn.edu.seu.login;

import java.io.File;

import cn.edu.seu.main.R;

import cn.edu.seu.ciphertext.MD5;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relogin);
		Mapplication.getInstance().addActivity(this);
		
		md5 = new MD5();
		b1 = (Button)findViewById(R.id.button1);
		username = (EditText)findViewById(R.id.editText1);
		password = (EditText)findViewById(R.id.EditText01);
		
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				File file = new File("sdcard/data" , "local.dat");
				if(!file.exists()){
					Toast.makeText(ReloginActivity.this, "请下载密码文件", Toast.LENGTH_LONG)
					.show();
				}
				else{
					LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
					LocalInfo x = lio.readfile();
					String u = username.getText().toString();
					String p = password.getText().toString();
					if(u.equals(x.getUserName()) && md5.encrypt(p).equals(x.getPassword())){
						finish();
					}
				}
			}
			
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        new AlertDialog.Builder(ReloginActivity.this)
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
