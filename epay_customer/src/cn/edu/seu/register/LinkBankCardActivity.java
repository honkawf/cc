package cn.edu.seu.register;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.seu.main.R;
import cn.edu.seu.datadeal.DataDeal;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.IDataTransportation;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.PersonInfo;
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
import android.widget.TextView;


@SuppressLint("HandlerLeak")
public class LinkBankCardActivity extends Activity implements IDataTransportation{
	private EditText cardNum, cardPwd, phoneNum, idCardNum;
	private TextView cardNum_label, cardPwd_label, phoneNum_label, idCardNum_label, btn_link_label;
	private Button btn_link_submit;
	private Socket cli_Soc = null;
	private ProgressDialog linkBankCard_pd;
	private String cardNum_content = "", cardPwd_content = "", phoneNum_content = "", idCardNum_content= "";
	private boolean cardNum_correct = false, cardPwd_correct = false, phoneNum_correct = false, idCardNum_correct = false;
	private String first_pattern;
	private String userName;
	private String password;
	private String customerName, publickeyn, privatekey, localBalance;
	
	private static final String CARDNUM_PATTERN = "^[0-9]{19,19}$";
	private static final String CARDPWD_PATTERN = "^[0-9]{6,6}$";
	private static final String PHONENUM_PATTERN = "^[0-9]{11,11}$";
	private static final String IDCARDNUM_PATTERN = "^[0-9]{18,18}$";

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
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
		    	LocalInfo li = new LocalInfo();
		    	li.setAvailableBalance(localBalance);
		    	Log.i("linkBankCard", "0");
		    	li.setBalance(localBalance);
		    	Log.i("linkBankCard", "0");
		    	li.setCardnum(cardNum_content);
		    	Log.i("linkBankCard", cardNum_content);
		    	li.setGesturePwd(first_pattern);
		    	Log.i("linkBankCard", first_pattern);
		    	li.setPassword(password);
		    	Log.i("linkBankCard", password);
		    	li.setPrivateKey(privatekey);
		    	Log.i("linkBankCard", "0");
		    	li.setPublicKeyn(publickeyn);
		    	Log.i("linkBankCard", "0");
		    	li.setUserName(userName);
		    	Log.i("linkBankCard", userName);
		    	lio.writefile(li);
				
				linkBankCard_pd.dismiss();
				Intent intent = new Intent();
				intent.setClass(LinkBankCardActivity.this, LoginActivity.class);
				startActivity(intent);
				LinkBankCardActivity.this.finish();
				break;
			case 1:
				linkBankCard_pd.dismiss();
				btn_link_label.setText("绑定失败，请重新绑定");
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
		setContentView(R.layout.linkbankcard);
		cardNum = (EditText) findViewById(R.id.cardNum);
		cardPwd = (EditText) findViewById(R.id.cardPwd);
		phoneNum = (EditText) findViewById(R.id.phoneNum);
		idCardNum = (EditText) findViewById(R.id.idCardNum);
		btn_link_submit = (Button) findViewById(R.id.btn_link_submit);

		cardNum_label = (TextView) findViewById(R.id.cardNum_label);
		cardPwd_label = (TextView) findViewById(R.id.cardPwd_label);
		phoneNum_label = (TextView) findViewById(R.id.phoneNum_label);
		idCardNum_label = (TextView) findViewById(R.id.idCardNum_label);
		btn_link_label = (TextView) findViewById(R.id.btn_link_label);
		
		Intent intent = getIntent();
		first_pattern = intent.getStringExtra("firstPattern");
		userName = intent.getStringExtra("userName");
		password = intent.getStringExtra("password");
		customerName = intent.getStringExtra("customerName");
		
		cardNum.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					cardNum_label.setText("");
					cardNum_correct = false;
				}else{
					cardNum_content = cardNum.getText().toString();
					if(cardNum_content.equals(""))
						cardNum_label.setText("银行卡卡号不能为空");
					else{
						if(checkForm(cardNum_content,CARDNUM_PATTERN)){
							cardNum_correct = true;
//							cardNum_label.setText(cardNum_content);
							cardNum_label.setText("格式正确");
							}
						else
							cardNum_label.setText("银行卡卡号格式不正确");
//							cardNum_label.setText("ddd" + PersonInfo.localPersonInfo.getUserName());
					}
				}
			}
		});
		
		cardPwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					cardPwd_label.setText("");
					cardPwd_correct = false;
				}else{
					cardPwd_content = cardPwd.getText().toString();
					if(cardPwd_content.equals(""))
						cardPwd_label.setText("银行卡密码不能为空");
					else{
						if(checkForm(cardPwd_content,CARDPWD_PATTERN)){
							cardPwd_correct = true;
//							cardPwd_label.setText(cardPwd_content);
							cardPwd_label.setText(customerName);
						}
						else
							cardPwd_label.setText("银行卡密码格式不正确");
					}
				}
			}
		});
		
		phoneNum.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					phoneNum_label.setText("");
					phoneNum_correct = false;
				}else{
					phoneNum_content = phoneNum.getText().toString();
					if(phoneNum_content.equals(""))
						phoneNum_label.setText("手机号码不能为空");
					else{
						if(checkForm(phoneNum_content,PHONENUM_PATTERN)){
							phoneNum_correct = true;
//							phoneNum_label.setText(phoneNum_content);
							phoneNum_label.setText("格式正确");
						}
						else
							phoneNum_label.setText("手机号码格式不正确");
					}
				}
			}
		});
		
		idCardNum.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					idCardNum_label.setText("");
					idCardNum_correct = false;
				}else{
					idCardNum_content = idCardNum.getText().toString();
					if(idCardNum_content.equals(""))
						idCardNum_label.setText("身份证号码不能为空");
					else{
						if(checkForm(idCardNum_content,IDCARDNUM_PATTERN)){
							idCardNum_correct = true;
//							idCardNum_label.setText(idCardNum_content);
							idCardNum_label.setText("格式正确");
						}
						else
							idCardNum_label.setText("身份证号码格式不正确");
					}
				}
			}
		});
		
		btn_link_submit.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				idCardNum_content = idCardNum.getText().toString();
				cardNum_content = cardNum.getText().toString();
				cardPwd_content = cardPwd.getText().toString();
				phoneNum_content = phoneNum.getText().toString();
				
				if(checkForm(idCardNum_content,IDCARDNUM_PATTERN)){
					if(checkForm(phoneNum_content,PHONENUM_PATTERN)){
						if(checkForm(cardPwd_content,CARDPWD_PATTERN)){
							if(checkForm(cardNum_content,CARDNUM_PATTERN)){
								
								new Thread() {
									public void run() {
										Log.i("linkcardrun","12");
										String event = "linkBankCard";
										Log.i("linkcardrun","13");
										XML_Person xmlp = new XML_Person();
										Log.i("linkcardrun","14");
										xmlp.addPersonLinkBankCard(userName, cardNum_content, cardPwd_content, phoneNum_content, idCardNum_content, customerName);
										Log.i("linkcardrun","15");
										String resultXML = xmlp.produceLinkBankCardXML(event);
										Log.i("linkcardrun","16");
										
										Properties config =PropertyInfo.getProperties();
										Log.i("linkcardrun","17");
										String serverAddress=config.getProperty("serverAddress");
										Log.i("linkcardrun","18");
										String serverPort=config.getProperty("serverPort" );
										Log.i("linkcardrun","19");
										cli_Soc = (Socket)connect(serverAddress, Integer.parseInt(serverPort));
										Log.i("linkcardrun","20");
										write(resultXML);
										Log.i("linkcardrun","21");
										byte[] info = read();
										Log.i("linkcardrun","22");
										String checkResult = new String(info);
										Log.i("linkcardrun","23");
										checkResult = XML_Person.parseSentenceXML(new ByteArrayInputStream(info));
										Log.i("linkBankCard!!!!!!!", new String(info));
										Log.i("linkBankCard!!!!!!!", checkResult);
										Log.i("linkBankCard!!!!!!!", checkResult);
										if(checkResult.equals("")){
											Log.i("linkcardrun","24");
											XML_Person infoReceived = new XML_Person();
											Log.i("linkcardrun","25");
											PersonInfo personReceived = infoReceived.parsePersonXML(new ByteArrayInputStream(info));
											Log.i("linkCardBankprivate", personReceived.getPrivatekey());
											Log.i("linkbankCardpublic", personReceived.getPublickeyn());
											privatekey = personReceived.getPrivatekey();
											publickeyn = personReceived.getPublickeyn();
											localBalance = personReceived.getBalance();
									
											Log.i("chris", "绑定成功");
											handler.sendEmptyMessage(0);
										} else {
											Log.i("chris", "绑定失败");
											handler.sendEmptyMessage(1);
										}
										close();
									}
								}.start();
								
								linkBankCard_pd = ProgressDialog.show(LinkBankCardActivity.this,"注册", "注册中，请稍后……");
								linkBankCard_pd.setCancelable(true);// 设置进度条是否可以按退回键取消
								linkBankCard_pd.setCanceledOnTouchOutside(false);
								
							}else{
								cardNum_label.setText("银行卡密码格式不正确");
							}
						}else{
							cardPwd_label.setText("银行卡密码格式不正确");
						}
					}else{
						phoneNum_label.setText("手机号码格式不正确");
					}
				}else{
					idCardNum_label.setText("身份证号码格式不正确");
				}
			}
		});
		
	}	
	
}
