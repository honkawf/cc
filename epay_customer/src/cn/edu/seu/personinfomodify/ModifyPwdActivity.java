package cn.edu.seu.personinfomodify;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import cn.edu.seu.ciphertext.MD5;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.xml.XML;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyPwdActivity extends Activity {
	private Button btn_back_m1;
	private Button btn_check;
	private Button btn_confirm;
	private EditText password;
	private EditText newpassword1;
	private EditText newpassword2;
	private String username;
	private String produce;
	private byte [] parse;
	private ProgressDialog pd;
	private Looper looper = Looper.myLooper();
	private MyHandler myHandler = new MyHandler(looper);
	private boolean ischecked;
	private MD5 md5 = new MD5();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifypwd);
		
		ischecked = false;
		btn_check = (Button)findViewById(R.id.check);
		btn_check.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
				LocalInfo x = lio.readfile();
				username = x.getUserName();

				String p = password.getText().toString();
				if(md5.encrypt(p).equals(x.getPassword())){
					password.setFocusable(false);
					btn_check.setClickable(false);
					Toast.makeText(ModifyPwdActivity.this, "密码正确", Toast.LENGTH_LONG)
					.show();
					ischecked = true;
				}
				else{
					Toast.makeText(ModifyPwdActivity.this, "密码错误", Toast.LENGTH_LONG)
					.show();
				}
			}
		});
		btn_confirm = (Button)findViewById(R.id.confirm);
		btn_confirm.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				if(ischecked){
					if(newpassword1.getText().toString().equals(newpassword2.getText().toString())){
						pd = new ProgressDialog(ModifyPwdActivity.this);
						pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						pd.setCancelable(false);
						pd.setMessage("正在连接");  
						pd.show();
						Log.i("111111111111111" , "1");
						produce = XML.produceModifyPwdXML(username , md5.encrypt(newpassword1.getText().toString()));
						new Thread(){
							public void run(){
								Log.i("111111111111111" , produce);
								NetDataTransportation ndt = new NetDataTransportation();
								try{
									ndt.connect("honka.xicp.net",30145);
									ndt.write(produce);
									Log.i("111111111111111" , "3");
									parse = ndt.read();
									Log.i("111111111111111" , "4");
								}catch(Exception e){
									Message message = myHandler.obtainMessage();  
								    message.arg1 = 3;
								    message.sendToTarget();
								    return;
								}
								XML xml = new XML();
								String result = xml.parseSentenceXML(new ByteArrayInputStream(parse));
								if(result.equals("修改成功")){
									Properties property =PropertyInfo.getProperties();
									LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));									lio.modifyPassword(md5.encrypt(newpassword1.getText().toString()));
									Message message = myHandler.obtainMessage();
								    message.arg1 = 1;
								    message.sendToTarget();
								}
								else{
									Message message = myHandler.obtainMessage();  
								    message.arg1 = 2;
								    message.sendToTarget();
								}
							}
						}.start();
					}
					else{
						Toast.makeText(ModifyPwdActivity.this, "两次输入的密码不一致" , Toast.LENGTH_LONG)
						.show();
					}
				}
				else{
					Toast.makeText(ModifyPwdActivity.this, "当前密码未验证" , Toast.LENGTH_LONG)
					.show();
				}
			}
		});
		password = (EditText)findViewById(R.id.editText1);
		newpassword1 = (EditText)findViewById(R.id.editText2);
		newpassword2 = (EditText)findViewById(R.id.editText3);
		
		btn_back_m1=(Button) findViewById(R.id.btn_back_m1);
		btn_back_m1.setOnClickListener(new Button.OnClickListener(){


			public void onClick(View v) {
				// TODO Auto-generated method stub
				ModifyPwdActivity.this.finish();
				Intent intent=new Intent();
				intent.setClass(ModifyPwdActivity.this, FlipActivity.class);
				intent.putExtra("flag", 3);
				startActivity(intent);
			}
			
		});
	}
	
	class MyHandler extends Handler {  
        public MyHandler() {}  
        public MyHandler(Looper looper) {  
            super(looper);  
        }  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.arg1 == 1) {
                pd.dismiss();
				Toast.makeText(ModifyPwdActivity.this, "修改成功" , Toast.LENGTH_LONG)
				.show();
				ModifyPwdActivity.this.finish();
				Intent intent=new Intent();
				intent.setClass(ModifyPwdActivity.this, FlipActivity.class);
				intent.putExtra("flag", 3);
				startActivity(intent);
            } else if (msg.arg1 == 2) {
				Toast.makeText(ModifyPwdActivity.this, "修改失败", Toast.LENGTH_LONG)
				.show();
				pd.dismiss();
            } else if (msg.arg1 == 3) {
				Toast.makeText(ModifyPwdActivity.this, "网络不可用", Toast.LENGTH_LONG)
				.show();
				pd.dismiss();
            } 
        }  
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	ModifyPwdActivity.this.finish();
			Intent intent=new Intent();
			intent.setClass(ModifyPwdActivity.this, FlipActivity.class);
			intent.putExtra("flag", 3);
			startActivity(intent);
	    }
		return false;
	}
}
