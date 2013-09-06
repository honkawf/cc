package cn.edu.seu.financing;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.edu.seu.check.ChequeActivity;
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
import android.util.Log;
import android.util.Property;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DepositActivity extends Activity implements OnClickListener, AbstractSpinnerAdapter.IOnItemSelectListener{
	private ImageButton bt_dropdown;
	private ImageButton bt_dropdown1;
	private SpinnerPopWindow mSpinerPopWindow;
	private SpinnerPopWindow pSpinerPopWindow;
	private TextView tv_value;
	private TextView tv_value1;
	private List<CustemObject> depositList = new ArrayList<CustemObject>();
	private List<CustemObject> prohibitList = new ArrayList<CustemObject>();
	private AbstractSpinnerAdapter<CustemObject> mAdapter;
	private AbstractSpinnerAdapter<CustemObject> pAdapter;
	private Button btn_back;
	private Button btn_confirm;
	private String sum = "0";
	private EditText edittext;
	private PersonDepositInfo depositInfo;
	private ProgressDialog pd;
	private String produce;
	private byte [] parse;
	private Looper looper = Looper.myLooper();
	private MyHandler myHandler = new MyHandler(looper);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposit);
		Mapplication.getInstance().addActivity(this);

		setupViews();
	}
    public void setupViews(){
    	tv_value = (TextView) findViewById(R.id.tv_value);
    	tv_value1= (TextView) findViewById(R.id.tv_value1);
    	btn_back=(Button) findViewById(R.id.btn_back);
    	btn_back.setOnClickListener(this);
    	btn_confirm = (Button) findViewById(R.id.confirm);
    	btn_confirm.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				if(edittext.getText().toString() != null)depositInfo.setAmount(edittext.getText().toString());
				pd = new ProgressDialog(DepositActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setCancelable(false);
				pd.setMessage("正在连接");
				pd.show();
				produce = XML.produceDepositXML(depositInfo);
				new Thread(){
					public void run(){
						Properties config =PropertyInfo.getProperties();
						String address = config.getProperty("serverAddress");
						int port = Integer.parseInt(config.getProperty("serverPort"));
						NetDataTransportation ndt = new NetDataTransportation();
						Log.i("11111111111111" , "1");
						String result = "连接不可用";
						try{
							ndt.connect(address , port);
							Log.i("11111111111111" , "2");
							ndt.write(produce);
							Log.i("11111111111111" , "3");
							parse = ndt.read();
							Log.i("11111111111111" , "4");
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
    	edittext = (EditText)findViewById(R.id.editText1);
		bt_dropdown = (ImageButton) findViewById(R.id.bt_dropdown);
		bt_dropdown1= (ImageButton) findViewById(R.id.bt_dropdown1);
		bt_dropdown.setOnClickListener(this);
		bt_dropdown1.setOnClickListener(this);
		String[] cunkuanway = getResources().getStringArray(R.array.cunkuan);
		String[] prohibitway=getResources().getStringArray(R.array.prohibit);
		for(int i = 0; i < cunkuanway.length; i++){
			CustemObject object = new CustemObject();
			object.data = cunkuanway[i];
			depositList.add(object);
			
		}
		
		for(int i = 0; i < prohibitway.length; i++){
			CustemObject object = new CustemObject();
			object.data = prohibitway[i];
			prohibitList.add(object);	
		}
		tv_value.setText(cunkuanway[0]);
		tv_value1.setText(prohibitway[0]);
		Properties property =PropertyInfo.getProperties();
		LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));
		LocalInfo x = lio.readfile();
		depositInfo = new PersonDepositInfo();
		depositInfo.setUsername(x.getUserName());
		depositInfo.setDepositway("0");
		depositInfo.setInterestrateway("0");
		depositInfo.setAmount("0");
		
		mAdapter = new CustemSpinnerAdapter(this);
		mAdapter.refreshData(depositList, 0);
		
		pAdapter = new CustemSpinnerAdapter(this);
		pAdapter.refreshData(prohibitList, 0);

		mSpinerPopWindow = new SpinnerPopWindow(this);
		mSpinerPopWindow.setAdatper(mAdapter);
		mSpinerPopWindow.setItemListener(tv_value,this);
		
		pSpinerPopWindow = new SpinnerPopWindow(this);
		pSpinerPopWindow.setAdatper(pAdapter);
		pSpinerPopWindow.setItemListener(tv_value1,this);
		
    }
	public void showSpinWindow(View v)
	{
		if(v==bt_dropdown)
		{
			mSpinerPopWindow.setWidth(tv_value.getWidth());
			mSpinerPopWindow.showAsDropDown(tv_value);
		}
		if(v==bt_dropdown1)
		{
			pSpinerPopWindow.setWidth(tv_value1.getWidth());
			pSpinerPopWindow.showAsDropDown(tv_value1);
		}
		
	}
	
	private void setDeposit(int pos){
		if (pos >= 0 && pos <= depositList.size()){
			CustemObject value = depositList.get(pos);
			depositInfo.setDepositway(""+pos);
			tv_value.setText(value.toString());
		}
	}
	
	private void setProhibit(int pos){
		if (pos >= 0 && pos <= prohibitList.size()){
			CustemObject value = prohibitList.get(pos);
			depositInfo.setInterestrateway(""+pos);
			tv_value1.setText(value.toString());
		}
	}

	public void onItemClick(View v,int pos) {
		// TODO Auto-generated method stub
		Log.i("1","1");
		if(v==tv_value)
		{
			Log.i("3","3");
			setDeposit(pos);
		}
		
		if(v==tv_value1)
		{
			setProhibit(pos);	
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("2","2");
		switch(v.getId()){
		case R.id.bt_dropdown:
			showSpinWindow(bt_dropdown);
			break;
		case R.id.bt_dropdown1:
			showSpinWindow(bt_dropdown1);
			break;
		case R.id.btn_back:
			this.finish();
			Intent intent=new Intent();
			intent.setClass(DepositActivity.this, FlipActivity.class);
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
				Toast.makeText(DepositActivity.this, xh_msg , Toast.LENGTH_LONG)
				.show(); 
				finish();
				Intent intent=new Intent();
				intent.setClass(DepositActivity.this, FlipActivity.class);
				intent.putExtra("flag", 1);
				startActivity(intent);
            }
        }  
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	DepositActivity.this.finish();
			Intent intent=new Intent();
			intent.setClass(DepositActivity.this, FlipActivity.class);
			intent.putExtra("flag", 1);
			startActivity(intent);
	    }
		return false;
	}
}
