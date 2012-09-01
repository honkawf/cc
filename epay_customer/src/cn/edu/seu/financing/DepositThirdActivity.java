package cn.edu.seu.financing;

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
import android.widget.Toast;
import cn.edu.seu.cose.property.ProperityInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.xml.XML;

import cn.edu.seu.main.R;

public class DepositThirdActivity extends Activity {

	private Button button;
	private EditText edittext;
	private PersonDepositInfo depositInfo;
	
	private String produce;
	byte [] parse;
	
	private ProgressDialog pd;
	private Looper looper = Looper.myLooper();
	private MyHandler myHandler = new MyHandler(looper);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposite_third);
		button = (Button)findViewById(R.id.button1);
		edittext = (EditText)findViewById(R.id.editText1);
		
		LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
		LocalInfo x = lio.readfile();
		
		depositInfo = new PersonDepositInfo();
		depositInfo.setUsername(x.getUserName());
		depositInfo.setDepositway(getIntent().getStringExtra("method"));
		depositInfo.setInterestrateway(getIntent().getStringExtra("rate"));
		depositInfo.setAmount("0");
		
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				if(edittext.getText().toString() != null)depositInfo.setAmount(edittext.getText().toString());
				pd = new ProgressDialog(DepositThirdActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setCancelable(false);
				pd.setMessage("正在连接");
				pd.show();
				produce = XML.produceDepositXML(depositInfo);
				new Thread(){
					public void run(){
						Properties config =ProperityInfo.getProperties();
						String address = config.getProperty("serverAddress");
						int port = Integer.parseInt(config.getProperty("serverPort"));
						NetDataTransportation ndt = new NetDataTransportation();
						Log.i("11111111111111" , "1");
						ndt.connect(address , port);
						Log.i("11111111111111" , "2");
						ndt.write(produce);
						Log.i("11111111111111" , "3");
						parse = ndt.read();
						Log.i("11111111111111" , "4");
						XML xml = new XML();
						String result = xml.parseSentenceXML(new ByteArrayInputStream(parse));
						Message message = myHandler.obtainMessage();  
						message.arg1 = 1;
						message.obj = result;
						message.sendToTarget();
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
            	CharSequence xh_msg = (CharSequence) msg.obj;
                pd.dismiss();
				Toast.makeText(DepositThirdActivity.this, xh_msg , Toast.LENGTH_LONG)
				.show(); 
				finish();
            }
        }  
    }
}
