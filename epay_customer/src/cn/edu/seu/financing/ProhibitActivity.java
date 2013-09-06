package cn.edu.seu.financing;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.datatransportation.NetDataTransportation;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.spinner.AbstractSpinnerAdapter;
import cn.edu.seu.spinner.CustemObject;
import cn.edu.seu.spinner.CustemSpinnerAdapter;
import cn.edu.seu.spinner.SpinnerPopWindow;
import cn.edu.seu.xml.XML;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProhibitActivity extends Activity implements OnClickListener, AbstractSpinnerAdapter.IOnItemSelectListener{
	private ImageButton bt_dropdown3;
	private SpinnerPopWindow prSpinerPopWindow;
	private TextView tv;
	private TextView interest;
	private List<CustemObject> proList = new ArrayList<CustemObject>();
	private AbstractSpinnerAdapter<CustemObject> prAdapter;
	private Button btn_back_p;
	private Button btn_confirm;
	private PersonInterestInfo interestInfo;
	private String produce;
	private byte [] parse;	
	private ProgressDialog pd;
	private Looper looper = Looper.myLooper();
	private MyHandler myHandler = new MyHandler(looper);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prohibit);
		Mapplication.getInstance().addActivity(this);
		setupViews();
	}
    public void setupViews(){
    	tv = (TextView) findViewById(R.id.tv);
    	interest = (TextView) findViewById(R.id.interest);
        btn_back_p= (Button) findViewById(R.id.btn_back_p);
        btn_back_p.setOnClickListener(this);
        btn_confirm = (Button) findViewById(R.id.confirm_p);
        btn_confirm.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				pd = new ProgressDialog(ProhibitActivity.this);
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
						String result = "网络不可用";
						try{
							ndt.connect(address , port);
							ndt.write(produce);
							parse = ndt.read();
							XML xml = new XML();
							result = xml.parseSentenceXML(new ByteArrayInputStream(parse));
						}catch(Exception e){
							
						}
						Message message = myHandler.obtainMessage();  
						message.arg1 = 1;
						message.obj = result;
						message.sendToTarget();
					}
				}.start();
			}
        	
        });
		bt_dropdown3 = (ImageButton) findViewById(R.id.bt_dropdown3);
		bt_dropdown3.setOnClickListener(this);
		String[] pro = getResources().getStringArray(R.array.pro);
		for(int i = 0; i < pro.length; i++){
			CustemObject object = new CustemObject();
			object.data = pro[i];
			proList.add(object);
			
		}
		Properties property =PropertyInfo.getProperties();
		LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));
		LocalInfo x = lio.readfile();
		interestInfo = new PersonInterestInfo();
		interestInfo.setUsername(x.getUserName());
		interestInfo.setFinancingway("0");
		
		
		tv.setText(proList.get(0).toString());
		prAdapter = new CustemSpinnerAdapter(this);
		prAdapter.refreshData(proList, 0);

		prSpinerPopWindow = new SpinnerPopWindow(this);
		prSpinerPopWindow.setAdatper(prAdapter);
		prSpinerPopWindow.setItemListener(tv,this);
		
		produce = XML.produceGetInterestXML(interestInfo.getUsername());
		Log.i("11111111111111111111" , produce);
		new Thread(){
			public void run(){
				Properties config =PropertyInfo.getProperties();
				String address = config.getProperty("serverAddress");
				int port = Integer.parseInt(config.getProperty("serverPort"));
				NetDataTransportation ndt = new NetDataTransportation();
				String result;
				try{
					ndt.connect(address , port);
					ndt.write(produce);
					parse = ndt.read();
					Log.i("解析之前" , new String(parse));
					XML xml = new XML();
					result = xml.parseSentenceXML(new ByteArrayInputStream(parse));
				}catch(Exception e){
					result = "查询失败";
				}
				Log.i("1111111111111111111" , "11");
				if(!result.equals("查询失败")){
					Message message = myHandler.obtainMessage();  
				    message.arg1 = 2;
				    message.obj = result;
				    message.sendToTarget();
				}
				else{
					Message message = myHandler.obtainMessage();  
				    message.arg1 = 3;
				    message.sendToTarget();
				}
			}
		}.start();
		
    }
	public void showSpinWindow(View v)
	{
		if(v==bt_dropdown3)
		{
			prSpinerPopWindow.setWidth(tv.getWidth());
			prSpinerPopWindow.showAsDropDown(tv);
		}	
	}
	
	
	private void setProhibit(int pos){
		if (pos >= 0 && pos <= proList.size()){
			CustemObject value = proList.get(pos);
			interestInfo.setFinancingway(""+pos);
			tv.setText(value.toString());
		}
	}

	public void onItemClick(View v,int pos) {
		// TODO Auto-generated method stub
			setProhibit(pos);	
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("2","2");
		switch(v.getId()){
		case R.id.bt_dropdown3:
			showSpinWindow(bt_dropdown3);
			break;
		case R.id.btn_back_p:
			this.finish();
			Intent intent=new Intent();
			intent.setClass(ProhibitActivity.this, FlipActivity.class);
			intent.putExtra("flag", 1);
			startActivity(intent);
			break;
			
		}
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
				Toast.makeText(ProhibitActivity.this, xh_msg , Toast.LENGTH_LONG)
				.show(); 
				finish();
				Intent intent=new Intent();
				intent.setClass(ProhibitActivity.this, FlipActivity.class);
				intent.putExtra("flag", 1);
				startActivity(intent);
            }else if(msg.arg1 == 2){
            	CharSequence xh_msg = (CharSequence) msg.obj;
            	interest.setText(xh_msg);
            }else if(msg.arg1 == 3){
            	interest.setText("查询失败");
            }
        }  
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	ProhibitActivity.this.finish();
			Intent intent=new Intent();
			intent.setClass(ProhibitActivity.this, FlipActivity.class);
			intent.putExtra("flag", 1);
			startActivity(intent);
	    }
		return false;
	}
}
