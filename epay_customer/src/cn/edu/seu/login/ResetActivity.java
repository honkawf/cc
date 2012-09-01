package cn.edu.seu.login;

import java.io.ByteArrayInputStream;
import java.util.Properties;


import cn.edu.seu.cose.property.ProperityInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.gesturepassword.SetFirstActivity;
import cn.edu.seu.xml.XML;

import cn.edu.seu.main.R;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetActivity extends Activity {

	private Button b1;
	private EditText cardnumber;
	private EditText identificationnumber;
	private String produce;
	private byte[] parse;
	private ProgressDialog pd;
	private Looper looper = Looper.myLooper();
	private MyHandler myHandler = new MyHandler(looper);
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset);
        Mapplication.getInstance().addActivity(this);
        
        b1=(Button)findViewById(R.id.button3);
        cardnumber = (EditText)findViewById(R.id.editText2);
		identificationnumber = (EditText)findViewById(R.id.editText1);
		
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				String cardnum = cardnumber.getText().toString();
				String identificationcardnum = identificationnumber.getText().toString();
				if(cardnum.equals("") || identificationcardnum.equals("")){
					b1.setClickable(true);
					Toast.makeText(ResetActivity.this, "请输入有效证件号" , Toast.LENGTH_LONG)
					.show();
				}
				else{
					pd=new ProgressDialog(ResetActivity.this);
					pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pd.setCancelable(false);
					pd.setMessage("正在获取");  
					pd.show();
					produce = XML.producePersonXML(cardnum, identificationcardnum , ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
							.getDeviceId());
					Log.i("11111111111111111111" , produce);
					new Thread(){
						public void run(){
							Properties config =ProperityInfo.getProperties();
							String address = config.getProperty("serverAddress");
							int port = Integer.parseInt(config.getProperty("serverPort"));
							NetDataTransportation ndt = new NetDataTransportation();
							ndt.connect(address , port);
							ndt.write(produce);
							parse = ndt.read();
							Log.i("解析之前" , new String(parse));
							XML xml = new XML();
							String result = xml.parseSentenceXML(new ByteArrayInputStream(parse));
							Log.i("1111111111111111111" , "11");
							if(!result.equals("")){
								Message message = myHandler.obtainMessage();  
							    message.arg1 = 1;
							    message.obj = result;
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
                CharSequence xh_msg = (CharSequence) msg.obj;
                pd.dismiss();
				Toast.makeText(ResetActivity.this, xh_msg , Toast.LENGTH_LONG)
				.show();  
            } else if (msg.arg1 == 2) {
                LocalInfo l = XML.parsePersonXML(new ByteArrayInputStream(parse));
				LocalInfoIO llio = new LocalInfoIO("sdcard/data" , "local.dat");
				llio.writefile(l);
				Toast.makeText(ResetActivity.this, "下载完成", Toast.LENGTH_LONG)
				.show();
				pd.dismiss();
				Intent it = new Intent(ResetActivity.this , SetFirstActivity.class);
				startActivity(it);
				finish();
            }  
        }  
    }
}
