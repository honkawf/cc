package cn.edu.seu.pay;

import java.math.*;
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
	
 public String setRSA(String code) {
  // TODO Auto-generated method stub
	 /*
  Random rnd=new Random();
  BigInteger ref[]=new BigInteger[40];
  BigInteger ree[]=new BigInteger[40];
  BigInteger p=new BigInteger("819955293710851");
  BigInteger q=new BigInteger("673503352217029");
  BigInteger n=p.multiply(q);//552242638982356744711324281679
  BigInteger f=(p.add(new BigInteger("-1"))).multiply(q.add(new BigInteger("-1")));//552242638982355251252678353800
  BigInteger e=new BigInteger("2309916916024662023");
  BigInteger d=new BigInteger("1");
  BigInteger x=new BigInteger("3");
  int s=1;
  int t=0;
  ree[0]=e;
  ref[0]=f;
  System.out.println(e.toString());
  System.out.println(f.toString());
  while((!f.equals(BigInteger.ONE))&&(!e.equals(BigInteger.ONE)))
  {
   if(t==0)
   {
    f=f.remainder(e);
    t=1;
   }
   else if(t==1)
   {
    e=e.remainder(f);
    t=0;
   }
   ree[s]=e;
   ref[s]=f;
   s++;
  }
  System.out.println(s);
  System.out.println(e.toString());
  System.out.println(f.toString());
  t=0;
  for(int counter=34;counter>=0;counter--)
  {
   if(t==0)
   {
    System.out.println("d*"+ree[counter].toString()+"-"+ref[counter].toString()+"*"+x.toString()+"=1");
    d=((ref[counter].multiply(x)).add(BigInteger.ONE)).divide(ree[counter]);
    t=1;
   }
   else if(t==1)
   {
    System.out.println(d.toString()+"*"+ree[counter].toString()+"-"+ref[counter].toString()+"*x"+"=1");
    x=((ree[counter].multiply(d)).subtract(BigInteger.ONE)).divide(ref[counter]);
    t=0;
   }
   s++;
  }
  System.out.println(d.toString());
  System.out.println(d.multiply(ree[0]).remainder(ref[0]).toString());
  e=ree[0];//2309916916024662023
  f=ref[0];
  //d:435982084263274353850676993687
   * */
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
  BigInteger seco=sec.modPow(d, n);
  return seco.toString();
 }
 
 private BigInteger n;
 private BigInteger e;
 private BigInteger d;
}