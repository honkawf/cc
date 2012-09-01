package cn.edu.seu.login;

import cn.edu.seu.financing.FinancingActivity;
import cn.edu.seu.gesturepassword.LockActivity;
import cn.edu.seu.personinfomodify.ModifyActivity;

import cn.edu.seu.main.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class FunctionActivity extends Activity{
	public static boolean s = true;
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(s){
				Intent it = new Intent(FunctionActivity.this , LockActivity.class);
				startActivity(it);
				s = false;
			}
		}	
	};
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function);
		Mapplication.getInstance().addActivity(this); 

		Button b1 = (Button)findViewById(R.id.button3);
		b1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent it = new Intent(FunctionActivity.this , ModifyActivity.class);
				startActivity(it);
			}
			
		});
		
		Button b2 = (Button)findViewById(R.id.button4);
		b2.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent it = new Intent(FunctionActivity.this , FinancingActivity.class);
				startActivity(it);
			}
			
		});
		
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver,filter);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        new AlertDialog.Builder(FunctionActivity.this)
	                .setTitle("真的要离开？")
	                .setMessage("你确定要离开")
	                .setPositiveButton("确定",
	                                new DialogInterface.OnClickListener() {
	                                        public void onClick(DialogInterface dialog,
	                                                        int which) {
	                                        	Mapplication.getInstance().exit();
	                                        }
	                                }).show();

	    }
		return false;
	}
}
