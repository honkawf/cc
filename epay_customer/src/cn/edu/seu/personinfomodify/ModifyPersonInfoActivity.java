package cn.edu.seu.personinfomodify;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.seu.ciphertext.MD5;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.xml.XML;
import cn.edu.seu.main.R;

public class ModifyPersonInfoActivity extends Activity {

	private Button b1;
	private Button b2;
	private Button b3;
	
	private EditText password;
	private EditText newpassword;
	private EditText newphonenum;
	
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;

	private String username;
	private String produce;
	byte [] parse;
	
	private ProgressDialog pd;
	private Looper looper = Looper.myLooper();
	private MyHandler myHandler = new MyHandler(looper);
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifypersoninfo);
			
		b1 = (Button)findViewById(R.id.button1);
		b2 = (Button)findViewById(R.id.button2);
		b3 = (Button)findViewById(R.id.button3);
		
		password = (EditText)findViewById(R.id.editText1);
		newpassword = (EditText)findViewById(R.id.editText2);
		newphonenum = (EditText)findViewById(R.id.editText3);
		
		textview1 = (TextView)findViewById(R.id.textView1);
		textview2 = (TextView)findViewById(R.id.textView2);
		textview3 = (TextView)findViewById(R.id.textView3);
		
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
				LocalInfo x = lio.readfile();
				username = x.getUserName();
				MD5 md5 = new MD5();
				String p = password.getText().toString();
				if(md5.encrypt(p).equals(x.getPassword())){
					textview2.setVisibility(View.VISIBLE);
					textview3.setVisibility(View.VISIBLE);
					
					newpassword.setVisibility(View.VISIBLE);
					newphonenum.setVisibility(View.VISIBLE);
					password.setFocusable(false);
					
					b2.setVisibility(View.VISIBLE);
					b3.setVisibility(View.VISIBLE);
					b1.setClickable(false);
				}
				else{
					Toast.makeText(ModifyPersonInfoActivity.this, "密码错误", Toast.LENGTH_LONG)
					.show();
				}
			}
		});
		
		b2.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				pd = new ProgressDialog(ModifyPersonInfoActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setCancelable(false);
				pd.setMessage("正在连接");  
				pd.show();
				Log.i("111111111111111" , "1");
				produce = XML.produceModifyPwdXML(username , newpassword.getText().toString());
				new Thread(){
					public void run(){
						Log.i("111111111111111" , produce);
						NetDataTransportation ndt = new NetDataTransportation();
						ndt.connect("honka.xicp.net",30145);
						ndt.write(produce);
						Log.i("111111111111111" , "3");
						parse = ndt.read();
						Log.i("111111111111111" , "4");
						XML xml = new XML();
						String result = xml.parseSentenceXML(new ByteArrayInputStream(parse));
						if(result.equals("修改成功")){
							LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
							lio.modifyPassword(newpassword.getText().toString());
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
		b3.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				pd = new ProgressDialog(ModifyPersonInfoActivity.this);
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
						ndt.connect(address , port);
						ndt.write(produce);
						parse = ndt.read();
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
				Toast.makeText(ModifyPersonInfoActivity.this, "修改成功" , Toast.LENGTH_LONG)
				.show();  
            } else if (msg.arg1 == 2) {
				Toast.makeText(ModifyPersonInfoActivity.this, "修改失败", Toast.LENGTH_LONG)
				.show();
				pd.dismiss();
            }  
        }  
    }
}