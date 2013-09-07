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

import cn.edu.seu.main.R;


import cn.edu.seu.ciphertext.MD5;
import cn.edu.seu.datadeal.DataDeal;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.datatransportation.IDataTransportation;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.login.LoginActivity;
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

public class RegisterActivity extends Activity implements IDataTransportation{

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
	
	@SuppressLint("HandlerLeak")
	private int XML_length1,XML_length2;
	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{6,15}$";
	private static final String REALNAME_PATTERN = "^[a-zA-Z]{6,15}$";

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
				
		    	LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
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
				account_label.setText("注册失败，请重试");
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	public Object connect(String address, int port){
		Socket socket = null;
		try{
			socket = new Socket(address, port);	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socket;
	}
	
	public boolean write(String xml){
		try{
			OutputStream out = cli_Soc.getOutputStream();
			out.write(DataDeal.plusHead(xml.length()));
			out.write(xml.getBytes());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public byte[] read(){
		byte[] info = null;
		try{
			byte[] buffer = new byte[16];
			InputStream in = cli_Soc.getInputStream();
			in.read(buffer);
			int XML_length = DataDeal.readHead(buffer);
			info = new byte[XML_length];
			in.read(info);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
	
	public boolean close(){
		try{
			cli_Soc.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean checkForm(String name) {
		Pattern pattern = Pattern.compile(USERNAME_PATTERN);

		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}


	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
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
		

		File file = new File("sdcard/data" , "local.dat");
    	if(file.exists()){
    		LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
    		Log.i("tiaozhuan", "1");
	    	LocalInfo l = lio.readfile();
	    	Log.i("tiaozhuan", "1:"+l.getUserName()+" 2:"+l.getPassword()+" 3:"+l.getGesturePwd()+" 4:"+l.getCardnum()+" 5:"+l.getBalance()+" 6:"+l.getAvailableBalance()+" 7:"+l.getPrivateKey()+" 8:"+l.getPublicKeyn());
	    	String checkGesture = l.getGesturePwd();
	    	Log.i("tiaozhuan", checkGesture);
	    	String checkBank = l.getCardnum();
	    	Log.i("tiaozhuan", checkBank);
	    	if(!checkGesture.equals("0")){
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
						if (checkForm(account_content)) {
							pb.setVisibility(View.VISIBLE);
							new Thread() {
								public void run() {
									String event = "checkAccount";
									XML_Person xmlp = new XML_Person();
									xmlp.addPersonUserName(account_content);
									String resultXML = xmlp.produceUserNameXML(event);
									
									Properties config =PropertyInfo.getProperties();
									String serverAddress=config.getProperty("serverAddress");
									String serverPort=config.getProperty("serverPort" );
									cli_Soc = (Socket)connect(serverAddress, Integer.parseInt(serverPort));
									
									write(resultXML);
									byte[] info = read();
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
									close();	
									
	/*								try {
										cli_Soc = new Socket("honka.xicp.net",30145);
										OutputStream out = cli_Soc.getOutputStream();
										out.write(plusHead(resultXML.length()));
										out.write(resultXML.getBytes());
										
										byte[] buffer = new byte[16];
										InputStream in = cli_Soc.getInputStream();
										in.read(buffer);
										XML_length1 = readHead(buffer);
										byte[] info = new byte[XML_length1];
										in.read(info);
										String checkResult = new String(info);
										checkResult = XML_Person.parseSentenceXML(new ByteArrayInputStream(info));
										
										if (checkResult.equals("可以使用")) {
											Log.i("chris", "keyi");
											handler.sendEmptyMessage(0);
										} else {
											Log.i("chris", "bukeyi");
											handler.sendEmptyMessage(1);
										}
										Log.i("chris", checkResult);

										cli_Soc.close();
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
*/																
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
						if (checkForm(pwd1_content)) {
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
						if (checkForm(pwd2_content)) {
							if (pwd2_content.equals(pwd1_content)) {
								pwd_correct = true;
								pwd1_label.setText("");
								pwd2_label.setText("");
							} else {
								pwd_correct = false;
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
						realName_label.setText("身份证号码不能为空");
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
					if (checkForm(realName_content)) {
						realName_label.setText("");
						if (account_correct && pwd_correct) {
							bluetoothMac = BluetoothDataTransportation.getLocalMac();
							if(bluetoothMac.equals(""))
								Log.e("bluetoothError", "empty");
							else
							bluetoothMac = bluetoothMac.replaceAll(":","");
							

							
							new Thread() {
								public void run() {
									
									String event = "register";
									XML_Person xmlp = new XML_Person();
									MD5 md5 = new MD5();
									pwd1_content = md5.encrypt(pwd1_content);
									xmlp.addPersonRegister(account_content,pwd1_content, realName_content,bluetoothMac);
									String resultXML = xmlp.produceRegisterXML(event);
									
									Properties config =PropertyInfo.getProperties();
									String serverAddress=config.getProperty("serverAddress");
									String serverPort=config.getProperty("serverPort" );
									cli_Soc = (Socket)connect(serverAddress, Integer.parseInt(serverPort));
									
									write(resultXML);
									byte[] info = read();
									String checkResult = new String(info);
									checkResult = XML_Person.parseSentenceXML(new ByteArrayInputStream(info));
								
									if (checkResult.equals("注册成功")) {
										Log.i("chris", "注册成功");
										handler.sendEmptyMessage(2);
									} else {
										Log.i("chris", "注册失败");
										handler.sendEmptyMessage(3);
									}
									close();
/*									try {
										String event = "register";
										XML_Person xmlp = new XML_Person();
										pwd1_content = MD5T.encodeStr(pwd1_content);
										xmlp.addPersonRegister(account_content,pwd1_content, realName_content,bluetoothMac);
										String resultXML = xmlp.produceRegisterXML(event);
										Socket cli_Soc = new Socket("honka.xicp.net", 30145);
										OutputStream out = cli_Soc.getOutputStream();
										out.write(plusHead(resultXML.length()));
										out.write(resultXML.getBytes());
										
										byte[] buffer = new byte[16];
										InputStream in = cli_Soc.getInputStream();
										in.read(buffer);
										XML_length2 = readHead(buffer);
										byte[] info = new byte[XML_length2];
										in.read(info);
										String checkResult = new String(info);
										checkResult = XML_Person.parseSentenceXML(new ByteArrayInputStream(info));
										
										
										if (checkResult.equals("注册成功")) {
											Log.i("chris", "注册成功");
											handler.sendEmptyMessage(2);
										} else {
											Log.i("chris", "注册失败");
											handler.sendEmptyMessage(3);
										}
										cli_Soc.close();
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
*/									
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
