package cn.edu.seu.main;
import cn.edu.seu.login.Mapplication;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ExitActivity extends Activity {
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_dialog);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	public void exitbutton1(View v) {  
 		Intent intent=new Intent();
		setResult(1,intent);	
    	this.finish();

      }  
	public void exitbutton0(View v) {
    	this.finish();
    	Mapplication.getInstance().exit();
      }  



}
