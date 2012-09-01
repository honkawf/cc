package cn.edu.seu.datatransportation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import cn.edu.seu.datadeal.DataDeal;



public class NetDataTransportation implements IDataTransportation{
	
	private Socket Cli_Soc;
	
	public Object connect(String address, int port){
		try{
			Cli_Soc = new Socket(address, port);	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Cli_Soc;
	}

	public boolean write(String xml){
		try{
			OutputStream out = Cli_Soc.getOutputStream();
			out.write(DataDeal.plusHead(xml.length()));
			out.write(xml.getBytes());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public byte[] read(){
		byte[] info = null;
		try{
			byte[] buffer = new byte[16];
			InputStream in = Cli_Soc.getInputStream();
			in.read(buffer);
			int XML_length = DataDeal.readHead(buffer);
			info = new byte[XML_length];
			in.read(info);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-gener ated catch block
			e.printStackTrace();
		}
		return info;
	}
	
	public boolean close(){
		try{
			Cli_Soc.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}