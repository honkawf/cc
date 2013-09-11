package cn.edu.seu.transfer;

import java.io.ByteArrayInputStream;

import cn.edu.seu.check.Check;
import cn.edu.seu.check.Checkdh;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.FlipActivity;
import cn.edu.seu.main.R;
import cn.edu.seu.xml.Transfer;
import cn.edu.seu.xml.XML;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WaitingForTransferActivity extends Activity{

	private ImageView  btnreceive;
	private Transfer transfer;
	private static final String TAG="WaitingForTransferActivity";
	private Thread changeBackgroundThread;
	private Button btn_back;
	private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
				 Toast.makeText(WaitingForTransferActivity.this, "转账失败", 2000).show();
				 WaitingForTransferActivity.this.finish();
				 try
				 {
					 changeBackgroundThread.interrupt();
				 }
				 catch(Exception e)
				 {
					 Log.i(TAG,"线程中断失败");
				 }
				 break;
            case 1:
				 Toast.makeText(WaitingForTransferActivity.this, "收到转账,您可以到我的支票页面进行兑现或转发", 5000).show();
				 try
			     {
					 byte[]receive=(byte[])msg.obj;
					 XML info=new XML();
					 transfer=info.parseTransferXML(new ByteArrayInputStream(receive));
					 Checkdh cdh = new Checkdh(WaitingForTransferActivity.this, "recorddb" , null, 1);
					 Check mycheck=new Check(0,transfer.getPayerName(), transfer.getPayCardNumber(),"imei", Double.parseDouble(transfer.getTotalPrice()), transfer.getTradeTime(),"否",new String(receive));
					 cdh.insert(mycheck); 
					 try
					 {
						 changeBackgroundThread.interrupt();
					 }
					 catch(Exception e)
					 {
						 Log.i(TAG,"线程中断失败");
					 }
					 WaitingForTransferActivity.this.finish();
			     }
			     catch(Exception e)
			     {
			     	Log.e("数据库操作","失败");
			     }	
				 break;
            case 2:
            	 Toast.makeText(WaitingForTransferActivity.this, "等待超时", 2000).show();
				 WaitingForTransferActivity.this.finish();
				 try
				 {
					 changeBackgroundThread.interrupt();
				 }
				 catch(Exception e)
				 {
					 Log.i(TAG,"线程中断失败");
				 }
				 break;
            case 3:
				btnreceive.setImageDrawable(getResources().getDrawable(R.drawable.receive_latter));
				break;
            case 4:
				btnreceive.setImageDrawable(getResources().getDrawable(R.drawable.receive_former));
				break;
            case 5:
	    		btnreceive.setImageDrawable(getResources().getDrawable(R.drawable.receive));
	    		break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
   		try
   		{
   			changeBackgroundThread.interrupt();
   		}
   		catch(Exception e)
   		{
   			Log.i(TAG,"线程中断失败");
   		}
   	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waitingtransfer);
		Mapplication.getInstance().addActivity(this);
		
		TransferWaitingThread twt=new TransferWaitingThread(handler);
	    twt.start();
	    btn_back = (Button)findViewById(R.id.btn_back_c);
	    btn_back.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				FlipActivity.id=0;
				WaitingForTransferActivity.this.finish();
			}
	    	
	    });
	    btnreceive=(ImageView)findViewById(R.id.receive);
	    changeBackgroundThread=new Thread(){
	    	public void run()
	    	{
	    		long start=System.currentTimeMillis();
	    		while(true)
    			{
	    			try{
		    			
	    				long end=System.currentTimeMillis();
	    				long last=end-start;
	    				Log.i(TAG,String.valueOf(last));
	    				if(last>60000)
	    					break;
	    				Message msg=handler.obtainMessage();
	    				sleep(500);
	    				msg.what=3;
	    				msg.sendToTarget();
	    				Log.i(TAG,"1");
	    				sleep(500);
	    				msg=handler.obtainMessage();
	    				msg.what=4;
	    				Log.i(TAG,"2");
	    				msg.sendToTarget();
	    				sleep(500);
	    				msg=handler.obtainMessage();
	    				msg.what=5;
	    				Log.i(TAG,"3");
	    				msg.sendToTarget();
		    		}
		    		catch(Exception e)
		    		{
		    			Log.i(TAG,"线程打断");
		    			break;
		    		}
    				
    			}
    			WaitingForTransferActivity.this.finish();
	    		
	    	}
	    };
	    changeBackgroundThread.start();
	}
	   public boolean onKeyDown(int keyCode, KeyEvent event) {
	 	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	 	    	FlipActivity.id=0;
	 	    	WaitingForTransferActivity.this.finish();
	 	    }
	 		return false;
	 	} 

}
