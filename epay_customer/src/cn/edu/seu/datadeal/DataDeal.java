package cn.edu.seu.datadeal;



public class DataDeal {
    public static int readHead(byte [] buffer)
    {
		int total=0;
		int counter=0;
        for(counter=0;counter<16;counter++)
        {
        	if(buffer[counter]!='\0')
        		break;
        }
        byte []tmp=new byte[16-counter];
        System.arraycopy(buffer, counter, tmp, 0, 16-counter);
        try
        {
        	total=Integer.parseInt(new String(tmp));
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        return total;
        
    }
	public static byte[] plusHead(int length)
    {
    	String head=Integer.toString(length);
		byte [] temp=head.getBytes();
		byte [] send=new byte[16];
		for(int i=16-temp.length,j=0;j<temp.length;i++,j++)
		{
			send[i]=temp[j];
		}
		return send;
    } 
}
