package cn.edu.seu.register;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.edu.seu.datatransportation.NetDataTransportation;

import cn.edu.seu.main.R;


import cn.edu.seu.ciphertext.MD5;
import cn.edu.seu.datadeal.DataDeal;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.datatransportation.IDataTransportation;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.login.LoginActivity;
import cn.edu.seu.login.Mapplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity{

	private EditText account, pwd1, pwd2, realName;
	private TextView pwd1_label, pwd2_label, account_label, realName_label, button_label;
	private Button button;
	private String pwd1_content = "", pwd2_content = "", account_content = "", realName_content = "";
	private boolean account_correct = false;
	private boolean pwd_correct = false;
	private Socket cli_Soc = null;
	private ProgressDialog pd;
	private String bluetoothMac;
	private ProgressBar pb;
	private Button btn_back;
	
	@SuppressLint("HandlerLeak")
	private int XML_length1,XML_length2;
	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{5,15}$";
	private static final String REALNAME_PATTERN = "^[\u4e00-\u9fa5]{1,10}$";
	private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9_~]{6,15}$";

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				account_label.setText("用户名可用");
				pb.setVisibility(View.INVISIBLE);
				break;
			case 1:
				account_label.setText("用户名不可用");
				pb.setVisibility(View.INVISIBLE);
				break;
			case 2:
				//存储用户信息，供之后绑定时使用。
//				PersonInfo.localPersonInfo.setUserName(account_content);
				
				Properties property =PropertyInfo.getProperties();
				LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));
		    	LocalInfo li = new LocalInfo();
		    	li.setAvailableBalance("0");
		    	li.setBalance("0");
		    	li.setCardnum("0");
		    	li.setGesturePwd("0");
		    	li.setPassword(pwd1_content);
		    	li.setPrivateKey("0");
		    	li.setPublicKeyn("0");
		    	li.setUserName(account_content);
		    	lio.writefile(li);
				
				pd.dismiss();
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, SetGestureFirstActivity.class);
				intent.putExtra("userName", account_content);
				intent.putExtra("password", pwd1_content);
				intent.putExtra("customerName", realName_content);
				startActivity(intent);
				RegisterActivity.this.finish();
				break;
			case 3:
				button_label.setText("注册失败，请重试");
				pd.dismiss();
				break;
			case 4:
				account_label.setText("网络不可用");
				pb.setVisibility(View.INVISIBLE);
				break;
			case 5:
				button_label.setText("网络不可用");
				pd.dismiss();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	public boolean checkForm(String name, String re) {
		Pattern pattern = Pattern.compile(re);

		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}



	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		Mapplication.getInstance().addActivity(this);
		
		account = (EditText) findViewById(R.id.editText1);
		account.getBackground().setAlpha(0);
		pwd1 = (EditText) findViewById(R.id.editText2);
		pwd1.getBackground().setAlpha(0);
		pwd2 = (EditText) findViewById(R.id.editText3);
		pwd2.getBackground().setAlpha(0);
		realName = (EditText) findViewById(R.id.editText4);
		realName.getBackground().setAlpha(0);
		button = (Button) findViewById(R.id.btn_register);
		pb = (ProgressBar) findViewById(R.id.progressBar_Account);

		pwd1_label = (TextView) findViewById(R.id.pwd1_label);
		pwd2_label = (TextView) findViewById(R.id.pwd2_label);
		account_label = (TextView) findViewById(R.id.account_label);
		realName_label = (TextView) findViewById(R.id.realName_label);
		button_label = (TextView) findViewById(R.id.button_label);
		
		btn_back = (Button)findViewById(R.id.btn_back_c);
		btn_back.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				RegisterActivity.this.finish();
			}
			
		});
		
		Properties property =PropertyInfo.getProperties();
		File file = new File(property.getProperty("path") , property.getProperty("filename"));
    	if(file.exists()){
    		LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));
    		Log.i("tiaozhuan", "1");
	    	LocalInfo l = lio.readfile();
	    	Log.i("tiaozhuan", "1:"+l.getUserName()+" 2:"+l.getPassword()+" 3:"+l.getGesturePwd()+" 4:"+l.getCardnum()+" 5:"+l.getBalance()+" 6:"+l.getAvailableBalance()+" 7:"+l.getPrivateKey()+" 8:"+l.getPublicKeyn());
	    	String checkGesture = l.getGesturePwd();
	    	Log.i("tiaozhuan", checkGesture);
	    	String checkBank = l.getCardnum();
	    	Log.i("tiaozhuan", checkBank);
	    	if(!checkBank.equals("0")){
	    		Toast.makeText(RegisterActivity.this, "该手机已注册，不用注册", Toast.LENGTH_LONG).show();
	    		Intent intentToAlready = new Intent();
	    		Log.i("tiaozhuan", "6");
	    		intentToAlready.setClass(RegisterActivity.this, LoginActivity.class);
	    		Log.i("tiaozhuan", "7");
	    		intentToAlready.putExtra("userName", l.getUserName());
	    		intentToAlready.putExtra("password", l.getPassword());
	    		startActivity(intentToAlready);
	    		Log.i("tiaozhuan", "8");
	    		RegisterActivity.this.finish();
	    	}
	    		
	    	else if(checkGesture.equals("0")){
	    		Log.i("tiaozhuan", "5");
	    		Intent intentToAlready = new Intent();
	    		Log.i("tiaozhuan", "6");
	    		intentToAlready.setClass(RegisterActivity.this, SetGestureFirstActivity.class);
	    		Log.i("tiaozhuan", "7");
	    		intentToAlready.putExtra("userName", l.getUserName());
	    		intentToAlready.putExtra("password", l.getPassword());
	    		startActivity(intentToAlready);
	    		Log.i("tiaozhuan", "8");
	    		RegisterActivity.this.finish();
	    	}
	    	else if(checkBank.equals("0")){
	    		Log.i("tiaozhuan", "15");
	    		Intent intentToAlready = new Intent();
	    		Log.i("tiaozhuan", "16");
	    		intentToAlready.setClass(RegisterActivity.this, LinkBankCardActivity.class);
	    		Log.i("tiaozhuan", "17");
	    		intentToAlready.putExtra("userName", l.getUserName());
	    		intentToAlready.putExtra("password", l.getPassword());
	    		intentToAlready.putExtra("firstPattern", l.getGesturePwd());
	    		startActivity(intentToAlready);
	    		Log.i("tiaozhuan", "18");
	    		RegisterActivity.this.finish();
	    	}
	    	Log.i("meicuo","youxialaile");
    	}else{
		
		

		account.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					account_label.setText("");
					account_correct = false;				
				} else {
					account_content = account.getText().toString();
					if (account_content.equals("")) {
						account_label.setText("用户名不能为空");
						account_correct = false;
					} else {
						if (checkForm(account_content,USERNAME_PATTERN)) {
							pb.setVisibility(View.VISIBLE);
							new Thread() {
								public void run() {
									String event = "checkAccount";
									XML_Person xmlp = new XML_Person();
									xmlp.addPersonUserName(account_content);
									String resultXML = xmlp.produceUserNameXML(event);
									NetDataTransportation ndt=new NetDataTransportation();
									Properties config =PropertyInfo.getProperties();
									String serverAddress=config.getProperty("serverAddress");
									String serverPort=config.getProperty("serverPort" );
									byte[] info = {};
									try{
										cli_Soc = (Socket) ndt.connect(serverAddress, Integer.parseInt(serverPort));
										ndt.write(resultXML);
										info = ndt.read();
									}catch(Exception e){
										handler.sendEmptyMessage(4);
										return;
									}
									String checkResult = new String(info);
									checkResult = XML_Person.parseSentenceXML(new ByteArrayInputStream(info));
									
									if (checkResult.equals("可以使用")) {
										Log.i("chris", "keyi");
										account_correct = true;
										handler.sendEmptyMessage(0);
									} else {
										Log.i("chris", "bukeyi");
										account_correct = false;
										handler.sendEmptyMessage(1);
									}
									ndt.close();																
								}
							}.start();
						} else{
							account_label.setText("用户名只能包含大小写字母、数字和下划线");
							account_correct = false;
						}
					}
				}
			}

		});

		pwd1.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					pwd1_label.setText("");
					pwd_correct =false;
				} else {
					pwd1_content = pwd1.getText().toString();
					if (pwd1_content.equals("")) {
						pwd1_label.setText("密码不能为空");
						pwd_correct = false;
					} else {
						if (checkForm(pwd1_content,PASSWORD_PATTERN)) {
							if (pwd1_content.equals(pwd2_content)) {
								pwd_correct = true;
								pwd1_label.setText("");
								pwd2_label.setText("");
							} else {
								pwd_correct = false;
							}
						} else {
							pwd1_label.setText("密码只能包含大小写字母、数字和下划线");
							pwd_correct = false;
						}
					}
				}
			}

		});

		pwd2.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					pwd2_label.setText("");
					pwd_correct =false;
				} else {
					pwd2_content = pwd2.getText().toString();
					if (pwd2_content.equals("")) {
						pwd2_label.setText("密码不能为空");
						pwd_correct =false;
					} else {
						if (checkForm(pwd2_content,PASSWORD_PATTERN)) {
							if (pwd2_content.equals(pwd1_content)) {
								pwd_correct = true;
								pwd1_label.setText("");
								pwd2_label.setText("");
							} else {
								pwd_correct = false;
								pwd2_label.setText("两次密码不一致");
							}
						} else {
							pwd2_label.setText("密码只能包含大小写字母、数字和下划线");
							pwd_correct = false;
						}
					}
				}
			}

		});
		
		realName.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					realName_label.setText("");
//					realName_correct = false;
				}else{
					realName_content = realName.getText().toString();
					if(realName_content.equals(""))
						realName_label.setText("真实姓名不能为空");
					else{
//						if(checkForm(realName_content)){
//							realName_correct = true;
//							realName_label.setText(realName_content);
//						}
//						else
//							realName_label.setText("身份证号码格式不正确");
					}
				}
			}
		});

		// button
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				button.setFocusable(true);
				button.setFocusableInTouchMode(true);
				
				realName_content = realName.getText().toString();
				if (realName_content.equals("")) {
					realName_label.setText("真实姓名不能为空");
				} else {	
					Log.i("register", "000");
					//checkForm(realName_content,REALNAME_PATTERN
					if (checkForm(realName_content,REALNAME_PATTERN)) {
						Log.i("register", "001");
						realName_label.setText("");
						Log.i("register", "002");
						if (account_correct && pwd_correct) {
							Log.i("register", "003");
							bluetoothMac = BluetoothDataTransportation.getLocalMac();
							Log.i("register", bluetoothMac);
							if(bluetoothMac.equals(""))
								Log.e("bluetoothError", "empty");
							else
							bluetoothMac = bluetoothMac.replaceAll(":","");
							

							
							new Thread() {
								public void run() {
									
									String event = "register";
									Log.i("register", "1");
									XML_Person xmlp = new XML_Person();
									Log.i("register", "2");
									MD5 md5 = new MD5();
									Log.i("register", "3");
									pwd1_content = md5.encrypt(pwd1_content);
									Log.i("register", "4");
									xmlp.addPersonRegister(account_content,pwd1_content, realName_content,bluetoothMac);
									Log.i("register", "5");
									String resultXML = xmlp.produceRegisterXML(event);
									Log.i("register", resultXML);
									
									Properties config =PropertyInfo.getProperties();
									Log.i("register", "7");
									String serverAddress=config.getProperty("serverAddress");
									Log.i("register", "8");
									String serverPort=config.getProperty("serverPort" );
									Log.i("register", "9");
									NetDataTransportation ndt=new NetDataTransportation();
									byte[] info = {};
									try{
										cli_Soc = (Socket)ndt.connect(serverAddress, Integer.parseInt(serverPort));
										Log.i("register", "10");
										
										ndt.write(resultXML);
										Log.i("register", "11");
										info = ndt.read();
									}catch(Exception e){
										handler.sendEmptyMessage(5);
										return;
									}
									Log.i("register", "12");
									String checkResult = new String(info);
									Log.i("register", "13");
									checkResult = XML_Person.parseSentenceXML(new ByteArrayInputStream(info));
									Log.i("register", "14");
									
									if (checkResult.equals("注册成功")) {
										Log.i("chris", "注册成功");
										handler.sendEmptyMessage(2);
									} else {
										Log.i("chris", "注册失败");
										handler.sendEmptyMessage(3);
									}
									ndt.close();								
								}
							}.start();

							pd = ProgressDialog.show(RegisterActivity.this,
									"注册", "注册中，请稍后……");
							pd.setCancelable(true);// 设置进度条是否可以按退回键取消
							pd.setCanceledOnTouchOutside(false);

						} else {
							button_label.setText("请确认输入是否正确");
						}
					} else {
						realName_label.setText("真实姓名格式不正确");
					}

				}

			}

		});
	}
	}
}
