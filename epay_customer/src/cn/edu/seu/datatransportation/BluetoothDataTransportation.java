package cn.edu.seu.datatransportation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothDataTransportation implements IDataTransportation{

	private  BluetoothSocket socket=null;
	private  boolean isConnected=false;
	private  String address="";
	private  byte [] receive=null;
	private  int receiveConnection=0;
	private  String msg=null;
	private InputStream is;
	private  BluetoothServerSocket mserverSocket;
	private  BluetoothAdapter btAdapt=BluetoothAdapter.getDefaultAdapter();
	public void createSocket()
	{
		BluetoothClientThread bct=new BluetoothClientThread(address);
		bct.start();
		try {
			bct.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket=bct.getSocket();
		isConnected=true;
	}
	public void createServer()
	{
		BluetoothServerThread bst=new BluetoothServerThread();
		bst.start();
		try {
			bst.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket=bst.getSocket();
	}
	public boolean isAlive()
	{
		return socket!=null;
	}
    public void connect(String address)
    {
    	if(btAdapt.isDiscovering())
            btAdapt.cancelDiscovery();            
        BluetoothDevice btDev = btAdapt.getRemoteDevice(address); 
        this.address=address;
        try { 
        	if (btDev.getBondState() == BluetoothDevice.BOND_NONE) { 
        		Log.i("pair蓝牙状态","远程设备发送蓝牙配对请求"); 
                Log.i("蓝牙地址",address);
                //这里只需要createBond就行了
                //ClsUtils.setPin(btDev.getClass(), btDev, "0000"); // 手机和蓝牙采集器配对
				ClsUtils.createBond(btDev.getClass(), btDev);   
				//ClsUtils.cancelPairingUserInput(btDev.getClass(),btDev);
            }else if(btDev.getBondState() == BluetoothDevice.BOND_BONDED){
            	Log.i("pair蓝牙状态","已经配对"); 
            } 
        } catch (Exception e) { 
                Log.e("pair错误","配对出错");
        } 
    }
       
    public static String getLocalMac()
    {
    	BluetoothAdapter btAdapt=BluetoothAdapter.getDefaultAdapter();
    	String localmac=btAdapt.getAddress();
    	return localmac;
    }
    public String getRemoteMac()
    {
    	return address;
    }
	public Object connect(String address, int port) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean write(String xml) {
		// TODO Auto-generated method stub
		if(xml.equals(""))
			return false;
		if(socket==null)
		{
			createSocket();
		}
		BluetoothWriteThread bst=new BluetoothWriteThread(socket,xml);
		bst.start();
		try {
			bst.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public byte[] read() {
		// TODO Auto-generated method stub
		BluetoothReadThread brt=new BluetoothReadThread(socket);
		brt.start();
		try {
			brt.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return brt.getReceive();
	}
	
	public boolean close() {
		// TODO Auto-generated method stub
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean isConnected()
	{
		return isConnected;
	}
}
