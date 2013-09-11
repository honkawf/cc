package cn.edu.seu.personinfomodify;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import cn.edu.seu.ciphertext.MD5;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.financing.ProhibitActivity;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.personinfomodify.ModifyPwdActivity.MyHandler;
import cn.edu.seu.xml.XML;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyPhoActivity extends Activity {
	private Button btn_back_m2;
	private Button btn_check;
	private Button btn_confirm;
	private EditText password;
	private EditText newphonenum;
	private String username;
	private String produce;
	private byte [] parse;
	private ProgressDialog pd;
	private Looper looper = Looper.myLooper();
	private MyHandler myHandler = new MyHandler(looper);
	private boolean ischecked;
	private MD5 md5 = new MD5();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifyphone);
		
		password = (EditText)findViewById(R.id.editText1);
		newphonenum = (EditText)findViewById(R.id.editText2);
		btn_back_m2=(Button) findViewById(R.id.btn_back_m2);
		btn_back_m2.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ModifyPhoActivity.this.finish();
				FlipActivity.id = 3;
			}
		});
		
		ischecked = false;
		newphonenum.setFocusable(false);
		btn_check = (Button)findViewById(R.id.check);
		btn_check.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				Properties property =PropertyInfo.getProperties();
				LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));				LocalInfo x = lio.readfile();
				username = x.getUserName();

				String p = password.getText().toString();
				if(md5.encrypt(p).equals(x.getPassword())){
					password.setFocusable(false);
					btn_check.setClickable(false);
					newphonenum.setFocusable(true);
					Toast.makeText(ModifyPhoActivity.this, "密码正确", Toast.LENGTH_LONG)
					.show();
					ischecked = true;
				}
				else{
					Toast.makeText(ModifyPhoActivity.this, "密码错误", Toast.LENGTH_LONG)
					.show();
				}
			}
		});
		btn_confirm = (Button)findViewById(R.id.confirm);
		btn_confirm.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View arg0) {
				pd = new ProgressDialog(ModifyPhoActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setCancelable(false);
				pd.setMessage("正在连接");  
				pd.show();
				produce = XML.produceModifyPhonenumXML(username , newphonenum.getText().toString());
				new Thread(){
					public void run(){
						Properties config =PropertyInfo.getProperties();
						String address = config.getProperty("serverAddress");
						int port = Integer.parseInt(config.getProperty("serverPort"));
						NetDataTransportation ndt = new NetDataTransportation();
						try{
							ndt.connect(address , port);
							ndt.write(produce);
							parse = ndt.read();
						}catch(Exception e){
							Message message = myHandler.obtainMessage();  
						    message.arg1 = 3;
						    message.sendToTarget();
						    return;
						}
						XML xml = new XML();
						String result = xml.parseSentenceXML(new ByteArrayInputStream(parse));
						if(result.equals("修改成功")){
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
				Toast.makeText(ModifyPhoActivity.this, "修改成功" , Toast.LENGTH_LONG)
				.show();  
            } else if (msg.arg1 == 2) {
				Toast.makeText(ModifyPhoActivity.this, "修改失败", Toast.LENGTH_LONG)
				.show();
				pd.dismiss();
            } else if (msg.arg1 == 3) {
				Toast.makeText(ModifyPhoActivity.this, "网络不可用", Toast.LENGTH_LONG)
				.show();
				pd.dismiss();
            } 
        }  
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	ModifyPhoActivity.this.finish();
	    	FlipActivity.id = 3;
	    }
		return false;
	}
}
