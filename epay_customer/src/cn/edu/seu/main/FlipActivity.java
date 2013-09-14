package cn.edu.seu.main;
import java.io.ByteArrayInputStream;
import java.util.Properties;

import com.zxing.activity.CaptureActivity;

import cn.edu.seu.check.Check;
import cn.edu.seu.check.Checkdh;
import cn.edu.seu.check.ChequeActivity;
import cn.edu.seu.datadeal.PropertyInfo;
import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.datatransportation.LocalInfoIO;
import cn.edu.seu.financing.DepositActivity;
import cn.edu.seu.financing.ManageInfoActivity;
import cn.edu.seu.financing.ProhibitActivity;
import cn.edu.seu.gesturepassword.LockActivity;
import cn.edu.seu.gesturepassword.SetFirstActivity;
import cn.edu.seu.login.Mapplication;
import cn.edu.seu.pay.StoreInfoActivity;
import cn.edu.seu.personinfomodify.ModifyPhoActivity;
import cn.edu.seu.personinfomodify.ModifyPwdActivity;
import cn.edu.seu.record.GoodsActivity;
import cn.edu.seu.transfer.TransferActivity;
import cn.edu.seu.transfer.WaitingForTransferActivity;
import cn.edu.seu.xml.PersonInfo;
import cn.edu.seu.xml.Transfer;
import cn.edu.seu.xml.XML;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewFlipper;

public class FlipActivity extends Activity implements OnGestureListener{
	private ViewFlipper flipper;
	private GestureDetector detector;
	private RadioButton home;
	private RadioButton manage;
	private RadioButton record;
	private RadioButton setting;
	private RadioButton more;
	private RadioGroup group;
	private PopupWindow menuWindow;
	private Button paybtn, shoukuanbtn,transferbtn,duixianbtn; 
	private View v1;
	private View v2;
	private View v3;
	private View v4;
	private View v5;
	private Button deposit;
	private Button prohibit;
	private Button manageinfo;
	private Button btn_s1;
	private Button btn_s2;
	private Button btn_s3;
	private Button exit,contact;
	private Button cheque;
	private Button goods;
	private BluetoothAdapter btAdapt; 
	private String scanResult;
	private boolean menu_display = false;
	public static PersonInfo person=new PersonInfo();;//这里写person的初始化
    public static boolean s = true;
    private static final String TAG="FlipActivity";
	public static BluetoothDataTransportation bdt=new BluetoothDataTransportation();
	private String mac;
	public static int id=0;
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(s){
				Intent it = new Intent(FlipActivity.this , LockActivity.class);
				startActivity(it);
				s = false;
			}
		}	
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flip);
		Mapplication.getInstance().addActivity(this);

		detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.viewFlipper1);
		v1 = LayoutInflater.from(this).inflate(R.layout.main, null);
		v2 = LayoutInflater.from(this).inflate(R.layout.manage, null);
		v3 = LayoutInflater.from(this).inflate(R.layout.record, null);
		v4 = LayoutInflater.from(this).inflate(R.layout.setting, null);
		v5 = LayoutInflater.from(this).inflate(R.layout.more, null);
		flipper.addView(v1);
		flipper.addView(v2);
		flipper.addView(v3);
		flipper.addView(v4);
		flipper.addView(v5);
		

		v1.setId(0);
		v2.setId(1);
		v3.setId(2);
		v4.setId(3);
		v5.setId(4);
		
		group=(RadioGroup)findViewById(R.id.radioGroup1);
		home=(RadioButton)findViewById(R.id.home);
		manage=(RadioButton)findViewById(R.id.manage);
		record=(RadioButton)findViewById(R.id.record);
		setting=(RadioButton)findViewById(R.id.setting);
		more=(RadioButton)findViewById(R.id.more);
		home.setShadowLayer(6,0,10, Color.BLACK);
		
        deposit=(Button)findViewById(R.id.deposit);
        prohibit=(Button)findViewById(R.id.prohibit);
        manageinfo=(Button)findViewById(R.id.manageinfo);
        manageinfo.setOnClickListener(new Button.OnClickListener()
        {


			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(FlipActivity.this, ManageInfoActivity.class);
				startActivity(intent);
				
			}
        	
        });
        
        deposit.setOnClickListener(new Button.OnClickListener()
        {


			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(FlipActivity.this, DepositActivity.class);
				startActivity(intent);
				
			}
        	
        });
        
        prohibit.setOnClickListener(new Button.OnClickListener(){


			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(FlipActivity.this, ProhibitActivity.class);
				startActivity(intent);
			}
        	
        });
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener(){


			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==home.getId())
				{
					setShadow(home);
					flipper.setDisplayedChild(0);	
				}
				if(checkedId==manage.getId())
				{
					setShadow(manage);
					flipper.setDisplayedChild(1);
				}
				if(checkedId==record.getId())
				{
					setShadow(record);
					flipper.setDisplayedChild(2);
				}
				if(checkedId==setting.getId())
				{
					setShadow(setting);
					flipper.setDisplayedChild(3);
				}
				if(checkedId==more.getId())
				{
					setShadow(more);
					flipper.setDisplayedChild(4);
				}
			}
			
			
		}
		);
	//main
		btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能 
		Properties property =PropertyInfo.getProperties();
		LocalInfoIO lio = new LocalInfoIO(property.getProperty("path") , property.getProperty("filename"));
        LocalInfo x = lio.readfile();
        {
       	try
       	{
       		if(!btAdapt.isEnabled())
           	 {
           		 btAdapt.enable();
           		 Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
              	 intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
              	 startActivity(intent);
           	 }
       		person.setBluetoothmac(BluetoothDataTransportation.getLocalMac().replace(":", ""));
       	}
       	catch(Exception e)
       	{
       		Log.i(TAG,"未打开蓝牙");
       	}
       	person.setPrivatekey(x.getPrivateKey());
       	person.setPublickeyn(x.getPublicKeyn());
    		person.setUsername(x.getUserName());
    		person.setCustomername(x.getCustomerName());
    		person.setCardnum(x.getCardnum());
    		person.setImei(((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
					.getDeviceId());
    		person.setBalance(x.getBalance());
    	}//载入person
        final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver,filter);
      
        // Button 设置 
	    paybtn=(Button)findViewById(R.id.pay);
	    shoukuanbtn=(Button)findViewById(R.id.shoukuan);
	    transferbtn=(Button)findViewById(R.id.transfer);
	    duixianbtn = (Button) this.findViewById(R.id.duixian); 
	    paybtn.setOnClickListener(new ClickEvent());
	    shoukuanbtn.setOnClickListener(new ClickEvent());
	    transferbtn.setOnClickListener(new ClickEvent());
	    duixianbtn.setOnClickListener(new ClickEvent());
       if(btAdapt==null)
        {
       	 Toast.makeText(FlipActivity.this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
        }
        else
        {
       	 if(!btAdapt.isEnabled())
       	 {
       		 btAdapt.enable();
           	 Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
           	 intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
           	 startActivity(intent);
       	 }
		 }
     
		
	//record界面
	cheque=(Button) findViewById(R.id.cheque);
	goods=(Button) findViewById(R.id.goods);
	cheque.setOnClickListener(new Button.OnClickListener(){


		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(FlipActivity.this, ChequeActivity.class);
			intent.putExtra("flag", 2);
			startActivity(intent);
		}
		
	});
	goods.setOnClickListener(new Button.OnClickListener(){


		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(FlipActivity.this, GoodsActivity.class);
			startActivity(intent);
		}
		
	});
		
			
			
	//setting界面
	btn_s1=(Button) findViewById(R.id.btn_s1);	
	btn_s2=(Button) findViewById(R.id.btn_s2);
	btn_s3=(Button) findViewById(R.id.btn_s3);
	btn_s1.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(FlipActivity.this, ModifyPwdActivity.class);
			startActivity(intent);
		}
		
	});
	btn_s2.setOnClickListener(new Button.OnClickListener(){


		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(FlipActivity.this, SetFirstActivity.class);
			intent.putExtra("flag", 3);
			startActivity(intent);
		}
		
	});
	btn_s3.setOnClickListener(new Button.OnClickListener(){


		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(FlipActivity.this, ModifyPhoActivity.class);
			startActivity(intent);
		}
		
	});
	
	//more界面
	exit=(Button) findViewById(R.id.exit);
	exit.setOnClickListener(new Button.OnClickListener(){


		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			id=flipper.getCurrentView().getId();     
    		Log.i(TAG,String.valueOf(id));
    		Intent intent = new Intent();
        	intent.setClass(FlipActivity.this,ExitFromSettings.class);
        	startActivityForResult(intent,100);   
		}
		
	});
	contact=(Button) findViewById(R.id.contact);
	contact.setOnClickListener(new Button.OnClickListener(){


		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Uri uri = Uri.parse("smsto:15659617567");
            Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
            intent.putExtra("sms_body", "您好");
            startActivity(intent);
			
		}
		
	});
	}
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  		super.onActivityResult(requestCode, resultCode, data);
  			if (resultCode == RESULT_OK) {
  			Bundle bundle = data.getExtras();
  			scanResult = bundle.getString("result");
  			mac=scanResult.split(";")[1];
  			mac=mac.substring(0,2)+":"+mac.substring(2,4)+":"+mac.substring(4,6)+":"+mac.substring(6,8)+":"+mac.substring(8,10)+":"+mac.substring(10,12);
  			Log.i(TAG,mac);
  			try {
  				bdt.connect(mac);
  				Toast.makeText(FlipActivity.this, scanResult, Toast.LENGTH_LONG).show();
  				Intent store=new Intent(FlipActivity.this,StoreInfoActivity.class);
  				store.putExtra("scanResult", scanResult);
  				startActivity(store);
  			} catch (Exception e) {
  				// TODO Auto-generated catch block
  				Toast.makeText(FlipActivity.this, "扫描失败，请重扫二维码", Toast.LENGTH_LONG).show();
  				Intent openCameraIntent = new Intent(FlipActivity.this,CaptureActivity.class);
  				startActivityForResult(openCameraIntent, 0);
  			}
  			}
  	}
      class ClickEvent implements View.OnClickListener { 
          public void onClick(View v) { 
         	/* if (v == btnClo)
              {
             	 
             		 Set<BluetoothDevice> btDev=btAdapt.getBondedDevices();
             		 Iterator it=btDev.iterator();
             		 BluetoothDevice dev;
             		 while(it.hasNext())
             		 {
             			 dev=(BluetoothDevice) it.next();
             			 try {
 							ClsUtils.removeBond(dev.getClass(), dev);
 						} catch (Exception e) {
 							// TODO Auto-generated catch block
 							e.printStackTrace();
 						}
             		 }
              }
              else if (v == btnExit) { 
             	 	if(bdt.isConnected())
                 	 bdt.close();
               
                  MainActivity.this.finish(); 
              }
              else*/
         	 if(v==paybtn)
              {
             	 if(btAdapt==null)
   				{
   					Toast.makeText(FlipActivity.this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
   				}
   				else
   				{
   					if(!btAdapt.isEnabled())
   					{
   						btAdapt.enable();
   					}
   					Intent openCameraIntent = new Intent(FlipActivity.this,CaptureActivity.class);
   					startActivityForResult(openCameraIntent, 0);
   				}
   			}
             else if(v==transferbtn)
             {
             	Intent intent = new Intent(FlipActivity.this,TransferActivity.class);
             	intent.putExtra("flag", "transfer");
 				startActivity(intent);
             }
             else if(v==shoukuanbtn)
             {
             	Intent intent = new Intent(FlipActivity.this,WaitingForTransferActivity.class);
 				startActivity(intent);
             }
             else if(v==duixianbtn)
             {
             	Intent intent = new Intent(FlipActivity.this,ChequeActivity.class);
             	intent.putExtra("flag", 0);
 				startActivity(intent);
             }
          }
      } 
	

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG+"onResume",String.valueOf(id));
		flipper.setDisplayedChild(id);
		switch(id)
		{
		case 0:
			home.setChecked(true);
			break;
		case 1:
			manage.setChecked(true);
			break;
		case 2:
			record.setChecked(true);
			break;
		case 3:
			setting.setChecked(true);
			break;
		case 4:
			more.setChecked(true);
			break;
			
		}
	}

	private void setShadow(Button button)
	{
		home.setShadowLayer(0, 0, 0, Color.BLUE);
		manage.setShadowLayer(0, 0, 0, Color.BLUE);
		record.setShadowLayer(0, 0, 0, Color.BLUE);
		setting.setShadowLayer(0, 0, 0, Color.BLUE);
		more.setShadowLayer(0, 0, 0, Color.BLUE);
		button.setShadowLayer((float) 6,0,10, Color.BLACK);
	}
	public boolean onTouchEvent(MotionEvent event) {
	    	// TODO Auto-generated method stub
	    	return this.detector.onTouchEvent(event);
	    }
	    


	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > 60) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			this.flipper.showNext();			
			if(flipper.getCurrentView()==v1)
			{
				home.setChecked(true);
			}
			if(flipper.getCurrentView()==v2)
			{
				manage.setChecked(true);
			}
			if(flipper.getCurrentView()==v3)
			{
				record.setChecked(true);

			}
			if(flipper.getCurrentView()==v4)
			{
				setting.setChecked(true);

			}
			if(flipper.getCurrentView()==v5)
			{
				more.setChecked(true);

			}
			return true;
		} else if (e1.getX() - e2.getX() < -60) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			this.flipper.showPrevious();			
			if(flipper.getCurrentView()==v1)
			{
				home.setChecked(true);

			}
			if(flipper.getCurrentView()==v2)
			{
				manage.setChecked(true);
	
			}
			if(flipper.getCurrentView()==v3)
			{
				record.setChecked(true);
	
			}
			if(flipper.getCurrentView()==v4)
			{
				setting.setChecked(true);

			}
			if(flipper.getCurrentView()==v5)
			{
				more.setChecked(true);

			}
			return true;
		}
		
		return false;
	}


	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		 System.out.println("onScroll" + " " + distanceX + "," + distanceY);
		   return false;
	}


	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(menu_display){         
				menuWindow.dismiss();
        		menu_display = false;
        	}
        	else {
        		id=flipper.getCurrentView().getId();     
        		Log.i(TAG,String.valueOf(id));
        		Intent intent = new Intent();
            	intent.setClass(FlipActivity.this,ExitActivity.class);
            	startActivityForResult(intent,100);            
        	}
		}
		return false;
	}

}
