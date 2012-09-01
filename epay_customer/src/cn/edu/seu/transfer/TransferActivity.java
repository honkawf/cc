package cn.edu.seu.transfer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.edu.seu.datatransportation.BluetoothDataTransportation;
import cn.edu.seu.datatransportation.ClsUtils;
import cn.edu.seu.xml.XML;

import cn.edu.seu.main.R;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
public class TransferActivity extends Activity {
    /** Called when the activity is first created. */
	private Button btnSearch; 
	private ListView lvBTDevices; 
	private String mac,name;
	private ArrayAdapter<String> adtDevices;
	private List<String> lstDevices = new ArrayList<String>(); 
	private BluetoothAdapter btAdapt; 
	private final static String TAG="TransferActivity";
	public static BluetoothDataTransportation bdt=new BluetoothDataTransportation();
     @Override 
     public void onCreate(Bundle savedInstanceState) { 
         super.onCreate(savedInstanceState); 
         setContentView(R.layout.transfer);
         
         
         btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能 
         /*打开蓝牙*/
         if(btAdapt==null)
			{
				Toast.makeText(TransferActivity.this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
			}
			else
			{
				if(!btAdapt.isEnabled())
				{
					btAdapt.enable();
				}
			}

         // ListView及其数据源 适配器 
         lvBTDevices = (ListView) this.findViewById(R.id.lvDevices); 
         adtDevices = new ArrayAdapter<String>(this, 
                 android.R.layout.simple_list_item_1, lstDevices); 
         lvBTDevices.setAdapter(adtDevices); 
         lvBTDevices.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				if(btAdapt.isDiscovering())
					btAdapt.cancelDiscovery(); 
                String str = lstDevices.get(position); 
                String[] values = str.split(";");
                name=values[0];
               	mac= values[1];
                bdt.connect(mac);
                Log.i(TAG, name);
                Intent intent= new Intent(TransferActivity.this,ReceiverInfoActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("mac",mac);
                startActivity(intent);
                Log.i(TAG, name);
                TransferActivity.this.finish();
			} 	
		});
   
         btnSearch=(Button) findViewById(R.id.btnSearch);
         btnSearch.setOnClickListener(new OnClickEvent());
         
 
         // ============================================================ 
         // 注册Receiver来获取蓝牙设备相关的结果 
         IntentFilter intent = new IntentFilter(); 
         intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果 
         intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); 
         intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED); 
         intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); 
         registerReceiver(searchDevices, intent);

         
     } 
  
     class OnClickEvent implements Button.OnClickListener{
     public void onClick(View v)
     {
    	 if (v==btnSearch)
    	 {
    		 if(btAdapt==null)
				{
					Toast.makeText(TransferActivity.this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(!btAdapt.isEnabled())
					{
						btAdapt.enable();
					}
				}
				btAdapt.startDiscovery();
    	 }
    	 
     }
     }
     
     private final BroadcastReceiver searchDevices = new BroadcastReceiver() {   
         @Override
         public void onReceive(Context context, Intent intent) {      

			Log.i("测试点","2");
             String action = intent.getAction(); 
             Bundle b = intent.getExtras(); 
             Object[] lstName = b.keySet().toArray(); 
				Log.i("测试点","4");

             // 显示所有收到的消息及其细节 
             for (int i = 0; i < lstName.length; i++) { 
                 String keyName = lstName.toString(); 
                 Log.e(keyName, String.valueOf(b.get(keyName))); 
             } 
				Log.i("测试点","5");

             BluetoothDevice device = null; 
             // 搜索设备时，取得设备的MAC地址 
             if (BluetoothDevice.ACTION_FOUND.equals(action)) { 
                 device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
 				Log.i("测试点","6");
                     String str =device.getName() + ";" + device.getAddress(); 
                     if (lstDevices.indexOf(str) == -1)// 防止重复添加 
                         lstDevices.add(str); // 获取设备名称和mac地址 
     				Log.i("测试点","3");
                    adtDevices.notifyDataSetChanged(); 
             }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){ 
                 device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
                 
                 switch (device.getBondState()) { 
                 case BluetoothDevice.BOND_BONDING: 
                     Log.d("BlueToothTestActivity", "正在配对......"); 
                     break; 
                 case BluetoothDevice.BOND_BONDED: 
                     Log.d("BlueToothTestActivity", "完成配对"); 
                     //connect(device);//连接设备 
                     break; 
                 case BluetoothDevice.BOND_NONE: 
                     Log.d("BlueToothTestActivity", "取消配对"); 
                 default: 
                     break; 
                 } 
             } 
              
         } 
     }; 
  
     @Override 
     protected void onDestroy() { 
         this.unregisterReceiver(searchDevices); 
         super.onDestroy(); 
     } 

  }