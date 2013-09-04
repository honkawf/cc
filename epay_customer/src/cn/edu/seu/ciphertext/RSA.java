package cn.edu.seu.ciphertext;
import java.math.*;
import java.sql.SQLException;
import java.util.*;

public class RSA{
 /**
  * @param args
 * @return 
  */
	public RSA()
	{
		 n=new BigInteger("552242638982356744711324281679");
		 e=new BigInteger("2309916916024662023");
		 d=new BigInteger("435982084263274353850676993687");
	}
	
	public RSA(String o_e,String o_d)
	{
		 n=new BigInteger("552242638982356744711324281679");
		 e=new BigInteger(o_e);
		 d=new BigInteger(o_d);
	}
	
	/*RSA(String message)
	{
		n=new BigInteger("552242638982356744711324281679");
		if(message.equals("create"))
		{
			String exist="";
			int counter=1;
			do{
				IRsaDbDAO rsa_db_dao=new RsaDbDAO();
				RsaDb rsadb=rsa_db_dao.findById(String.valueOf(counter));
				e=new BigInteger(rsadb.getKeye());
				d=new BigInteger(rsadb.getKeyd());
				exist=rsadb.getTheidu();
				counter++;
				if(exist.equals("nu"))
				{
					rsadb.setTheidu("u");
					rsa_db_dao.update(rsadb);
				}
			}while(exist.equals("u"));
		}
		else
		{
			e=BigInteger.ZERO;
			d=BigInteger.ZERO;
		}
	}*/
	
	public String getN()
	{
		return n.toString();
	}
	
	public String getE()
	{
		return e.toString();
	}
	
	public String getD()
	{
		return d.toString();
	}
	
 public String setRSA(String code) {
  // TODO Auto-generated method stub
	
  BigInteger mescode=new BigInteger(code);
  BigInteger sec=mescode.modPow(e, n);
  return sec.toString();
 }
 
 public Boolean checkRSA(String money,String cipher)
 {
	 BigInteger sec=new BigInteger(cipher);
	  BigInteger seco=sec.modPow(d, n);
	  String checkcode=seco.toString();
	  if(checkcode.endsWith(money))
	  	return true;
	  else
		 return false;
 }
 
 public String reRSA(String cipher)
 {
  BigInteger sec=new BigInteger(cipher);
//  System.out.println("cip="+cipher);
//  System.out.println("d="+d);
//  System.out.println("n="+n);
  BigInteger seco=sec.modPow(d, n);
  return seco.toString();
 }
 
 private BigInteger n;
 private BigInteger e;
 private BigInteger d;
}