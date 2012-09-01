package cn.edu.seu.datatransportation;

public interface IDataTransportation {

	public Object connect(String address,int port);
	public boolean write(String xml);
	public byte[] read();
	public boolean close();
}
