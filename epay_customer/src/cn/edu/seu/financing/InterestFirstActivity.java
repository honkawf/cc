package cn.edu.seu.financing;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.financing.DepositThirdActivity.MyHandler;
import cn.edu.seu.xml.XML;
import cn.edu.seu.main.R;

public class InterestFirstActivity extends Activity {

	private static final String[] choice ={"投资基金" , "投资股票" , "存入储蓄账户"};     
    private Spinner spinner;   
    private MyAdapter adapter;
	private Button button;
	private PersonInterestInfo interestInfo;
	
	private String produce;
	byte [] parse;
	
	private ProgressDialog pd;
	private Looper looper = Looper.myLooper();
	private MyHandler myHandler = new MyHandler(looper);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interest_first);
		
		spinner = (Spinner) findViewById(R.id.spinner1);
		button = (Button)findViewById(R.id.button1);
		adapter = new MyAdapter(this, choice);
		spinner.setAdapter(adapter);
		spinner.setSelection(0,true);
		
		LocalInfoIO lio = new LocalInfoIO("sdcard/data" , "local.dat");
		LocalInfo x = lio.readfile();
		interestInfo = new PersonInterestInfo();
		interestInfo.setUsername(x.getUserName());
		interestInfo.setFinancingway("0");
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				interestInfo.setFinancingway(""+arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
			 
		});
		
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				pd = new ProgressDialog(InterestFirstActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setCancelable(false);
				pd.setMessage("正在连接");  
				pd.show();
				produce = XML.produceInterestXML(interestInfo);
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
				Toast.makeText(InterestFirstActivity.this, xh_msg , Toast.LENGTH_LONG)
				.show(); 
				finish();
            }
        }  
    }
}
