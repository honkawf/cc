package cn.edu.seu.xml;


import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import cn.edu.seu.datatransportation.LocalInfo;
import cn.edu.seu.financing.PersonDepositInfo;
import cn.edu.seu.financing.PersonInterestInfo;


import android.util.Log;
import android.util.Xml;

public class XML {
    private ArrayList<Goods> goodslist=new ArrayList<Goods>();
    private Trade trade;
    private PersonInfo person;
    private Transfer transfer;
    
    
    
    public static String producePersonXML( String cardnum , String identificationnum ,String imei){  
        StringWriter stringWriter = new StringWriter();  
        try {  
            // 获取XmlSerializer对象  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlSerializer xmlSerializer = factory.newSerializer();  
            // 设置输出流对象  
            xmlSerializer.setOutput(stringWriter);  
         
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "information");
            xmlSerializer.attribute(null, "event" , "downloadPassword");
            xmlSerializer.startTag(null, "cardnumber");
            xmlSerializer.text(cardnum);
            xmlSerializer.endTag(null, "cardnumber");
            xmlSerializer.startTag(null, "identificationnumber");
            xmlSerializer.text(identificationnum);
            xmlSerializer.endTag(null, "identificationnumber");
            xmlSerializer.startTag(null, "imei");
            xmlSerializer.text(imei);
            xmlSerializer.endTag(null, "imei");
            xmlSerializer.endTag(null, "information");
            xmlSerializer.endDocument();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return stringWriter.toString();
    } 
    
    public static String produceGetInterestXML( String username ){  
        StringWriter stringWriter = new StringWriter();  
        try {  
            // 获取XmlSerializer对象  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlSerializer xmlSerializer = factory.newSerializer();  
            // 设置输出流对象  
            xmlSerializer.setOutput(stringWriter);  
         
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "information");
            xmlSerializer.attribute(null, "event" , "getInterest");
            xmlSerializer.startTag(null, "name");
            xmlSerializer.text(username);
            xmlSerializer.endTag(null, "name");
            xmlSerializer.endTag(null, "information");
            xmlSerializer.endDocument();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return stringWriter.toString();
    }
    
    public static LocalInfo parsePersonXML(InputStream is)
    {
    	LocalInfo l = new LocalInfo();
        XmlPullParser xpp = Xml.newPullParser(); 
        try {
			xpp.setInput(is, "utf-8");
			for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
			{
			    if (i== XmlPullParser.START_TAG){
			    	if (xpp.getName().equals("username")){
			    		Log.i("11111111111111111" , "9");
			    		String username = xpp.nextText();
			    		l.setUserName(username);
			    	}
	                else if (xpp.getName().equals("password")) {
	                	Log.i("11111111111111111" , "9");
	                	String password = xpp.nextText();
	                	l.setPassword(password);
	                }
	                else if (xpp.getName().equals("cardnumber")){
	                	Log.i("11111111111111111" , "9");
	                	String cardnumber = xpp.nextText();
	                	l.setCardnum(cardnumber);
	                }
	                else if(xpp.getName().equals("balance")){
	                	Log.i("11111111111111111" , "9");
	                	String balance = xpp.nextText();
	                	l.setBalance(balance);
		                l.setAvailableBalance(balance);
	                } else if(xpp.getName().equals("privatekey")){
	                	Log.i("11111111111111111" , "9");
	                	String privatekey = xpp.nextText();
	                	l.setPrivateKey(privatekey);
	                }
	                else if(xpp.getName().equals("publickeyn")){
	                	Log.i("11111111111111111" , "9");
	                	String publickey = xpp.nextText();
	                	l.setPublicKeyn(publickey);
	                }
			    }
			    l.setGesturePwd("1");
			}
		} catch (XmlPullParserException e) {
				Log.e("错误","未成功接收xml");
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return l;
    }
    
    public static String produceModifyPwdXML( String username , String newpassword){  
        StringWriter stringWriter = new StringWriter();  
        try {  
            // 获取XmlSerializer对象  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlSerializer xmlSerializer = factory.newSerializer();  
            // 设置输出流对象  
            xmlSerializer.setOutput(stringWriter);  
         
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "information");
            xmlSerializer.attribute(null, "event" , "modifyPassword");
            xmlSerializer.startTag(null, "username");
            xmlSerializer.text(username);
            xmlSerializer.endTag(null, "username");
            xmlSerializer.startTag(null, "newpassword");
            xmlSerializer.text(newpassword);
            xmlSerializer.endTag(null, "newpassword");
            xmlSerializer.endTag(null, "information");
            xmlSerializer.endDocument();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return stringWriter.toString();
    }
    
    public static String produceModifyPhonenumXML( String username , String phonenum){  
        StringWriter stringWriter = new StringWriter();  
        try {  
            // 获取XmlSerializer对象  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlSerializer xmlSerializer = factory.newSerializer();  
            // 设置输出流对象  
            xmlSerializer.setOutput(stringWriter);  
         
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "information");
            xmlSerializer.attribute(null, "event" , "modifyPhonenum");
            xmlSerializer.startTag(null, "username");
            xmlSerializer.text(username);
            xmlSerializer.endTag(null, "username");
            xmlSerializer.startTag(null, "phonenum");
            xmlSerializer.text(phonenum);
            xmlSerializer.endTag(null, "phonenum");
            xmlSerializer.endTag(null, "information");
            xmlSerializer.endDocument();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return stringWriter.toString();
    }
    
    public static String produceInterestXML( PersonInterestInfo interestInfo){  
        StringWriter stringWriter = new StringWriter();  
        try {  
            // 获取XmlSerializer对象  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlSerializer xmlSerializer = factory.newSerializer();  
            // 设置输出流对象  
            xmlSerializer.setOutput(stringWriter);  
         
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "information");
            xmlSerializer.attribute(null, "event" , "interestFinancing");
            xmlSerializer.startTag(null, "username");
            xmlSerializer.text(interestInfo.getUsername());
            xmlSerializer.endTag(null, "username");
            xmlSerializer.startTag(null, "financingway");
            xmlSerializer.text(interestInfo.getFinancingway());
            xmlSerializer.endTag(null, "financingway");
            xmlSerializer.endTag(null, "information");
            xmlSerializer.endDocument();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return stringWriter.toString();
    }
    
    public static String produceDepositXML(PersonDepositInfo depositInfo){  
        StringWriter stringWriter = new StringWriter();  
        try {  
            // 获取XmlSerializer对象  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlSerializer xmlSerializer = factory.newSerializer();  
            // 设置输出流对象  
            xmlSerializer.setOutput(stringWriter);  
         
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "information");
            xmlSerializer.attribute(null, "event" , "depositFinancing");
            xmlSerializer.startTag(null, "username");
            xmlSerializer.text(depositInfo.getUsername());
            xmlSerializer.endTag(null, "username");
            xmlSerializer.startTag(null, "depositway");
            xmlSerializer.text(depositInfo.getDepositway());
            xmlSerializer.endTag(null, "depositway");
            xmlSerializer.startTag(null, "interestrateway");
            xmlSerializer.text(depositInfo.getInterestrateway());
            xmlSerializer.endTag(null, "interestrateway");
            xmlSerializer.startTag(null, "amount");
            xmlSerializer.text(depositInfo.getAmount());
            xmlSerializer.endTag(null, "amount");
            xmlSerializer.endTag(null, "information");
            xmlSerializer.endDocument();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return stringWriter.toString();
    }
    
    
    
    public static String producePicXML()
    {
    	 StringWriter stringWriter = new StringWriter();  
	        try {  
	            // 获取XmlSerializer对象  
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
	            XmlSerializer xmlSerializer = factory.newSerializer();  
	            // 设置输出流对象  
	            xmlSerializer.setOutput(stringWriter);  
	         
	            xmlSerializer.startDocument("utf-8", true);
	            xmlSerializer.startTag(null, "information");
	            xmlSerializer.attribute(null, "event", "guide");
	            xmlSerializer.endTag(null, "information");
	            xmlSerializer.endDocument();
    		} catch (Exception e) {  
    			e.printStackTrace();  
    		}  
    		return stringWriter.toString();  
    }
    
    public static String parsePic(InputStream is){
    	String sentence="";
        XmlPullParser xpp = Xml.newPullParser(); 
        try {
			xpp.setInput(is, "utf-8");
			for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
			{        	
			    switch (i) {  
	            case XmlPullParser.START_TAG: 
	            	if(xpp.getName().equals("information"))
	            	{

	            		if(!xpp.getAttributeValue(null,"event").equals("picture"))
	            			return sentence;
	            		else
	            		{	
	            			sentence=xpp.nextText();
	            		}

	            	}
	                
	            }
			}

		} catch (XmlPullParserException e) {
				Log.e("错误","未成功接收xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
        return sentence;
    }

    
    public static String parseSize(InputStream is)
    {
    	String sentence="";
        XmlPullParser xpp = Xml.newPullParser(); 
        try {
			xpp.setInput(is, "utf-8");
			for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
			{        	
			    switch (i) {  
	            case XmlPullParser.START_TAG: 
	            	if(xpp.getName().equals("information"))
	            	{

	            		if(!xpp.getAttributeValue(null,"event").equals("size"))
	            			return sentence;
	            		else
	            		{	
	            			sentence=xpp.nextText();
	            		}

	            	}
	                
	            }
			}

		} catch (XmlPullParserException e) {
				Log.e("错误","未成功接收xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
        return sentence;
    }

	    
		
	    public String parseBalanceXML(InputStream is)
	    {
	    	String sentence="";
	        XmlPullParser xpp = Xml.newPullParser(); 
	        try {
				xpp.setInput(is, "utf-8");
				for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
				{        	
				    if(i==XmlPullParser.START_TAG) 
				    {
				    	if(xpp.getName().equals("information"))
		            	{

		            		if(!xpp.getAttributeValue(null,"event").equals("strangebalance"))
		            			return sentence;
		            		else
		            		{	
		            			sentence=xpp.nextText();
		            		}

		            	}
		                
		            }
				}

			} catch (XmlPullParserException e) {
					Log.e("错误","未成功接收xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return sentence;
	    	
	    }
	    public Goods parsePriceXML(InputStream is)
	    {
	    	Goods goods=new Goods();
	        XmlPullParser xpp = Xml.newPullParser(); 
	        try {
				xpp.setInput(is, "utf-8");
				for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
				{
				    switch (i) {  
		            case XmlPullParser.START_TAG:  
		                if (xpp.getName().equals("barcode"))
		                	goods.setBarcode(xpp.nextText());
		                else if (xpp.getName().equals("name")) 
		                	goods.setName(xpp.nextText());
		                else if (xpp.getName().equals("price")) 
		                	goods.setPrice(xpp.nextText());
		                else if(xpp.getName().equals("quantity"))
		                	goods.setQuantity(xpp.nextText());
		                break;
		            }
				}
			} catch (XmlPullParserException e) {
					Log.e("错误","未成功接收xml");
			} catch (IOException e) {
				e.printStackTrace();
			} 
	        return goods;
	    }
		public Trade parseIndividualTradeXML(InputStream is)
		{
			Trade trade=new Trade();
	        XmlPullParser xpp = Xml.newPullParser(); 
	        try {
				xpp.setInput(is, "utf-8");
				for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
				{        	
				    if (i==XmlPullParser.START_TAG)
				    {
				    	if(xpp.getName().equals("information"))
				    	{

		            		if(!xpp.getAttributeValue(null,"event").equals("individualTrade"))
		            			return null;
		            		else
		            			continue;

		            	}
				    	if (xpp.getName().equals("payername"))
		                	trade.setPayerName(xpp.nextText());
				    	else if (xpp.getName().equals("payerdevice"))
		                	trade.setPayerDevice(xpp.nextText());
				    	else if (xpp.getName().equals("payerimei"))
		                	trade.setPayerIMEI(xpp.nextText());
				    	else if (xpp.getName().equals("payercardnumber")) 
		                	trade.setPayerCardNumber(xpp.nextText());
		                else if (xpp.getName().equals("receivername")) 
		                	trade.setReceiverName(xpp.nextText());
		                else if (xpp.getName().equals("receiverdevice")) 
		                	trade.setReceiverDevice(xpp.nextText());
		                else if (xpp.getName().equals("receiverimei")) 
		                	trade.setReceiverIMEI(xpp.nextText());
		                else if (xpp.getName().equals("receivercardnumber")) 
		                	trade.setReceiverCardNumber(xpp.nextText());
		                else if(xpp.getName().equals("tradetime"))
		                	trade.setTradeTime(xpp.nextText());
		                else if(xpp.getName().equals("totalprice"))
		                	trade.setTotalPrice(xpp.nextText());
		                else if(xpp.getName().equals("cipher"))
		                	trade.setCipher(xpp.nextText());
		                
				    }
				}

			} catch (XmlPullParserException e) {
					Log.e("错误","未成功接收xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return trade;
		}
	    public String parseTotalPriceXML(InputStream is)
	    {
	    	String totalprice="";
	        XmlPullParser xpp = Xml.newPullParser(); 
	        try {
				xpp.setInput(is, "utf-8");
				for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
				{

				    switch (i) {  
		            case XmlPullParser.START_TAG: 
		            	if(xpp.getName().equals("information"))
		            	{

		            		if(!xpp.getAttributeValue(null,"event").equals("sendTotalPrice"))
		            			return totalprice;
		            	}
		            	if (xpp.getName().equals("totalprice"))
		            	{
		            		totalprice=xpp.nextText();
		            	}
		            	break;
		                
		            }
				}

			} catch (XmlPullParserException e) {
					Log.e("错误","未成功接收xml");
			} catch (IOException e) {
				e.printStackTrace();
			} 
	        return totalprice;
	    }
	    public String parseSentenceXML(InputStream is)
	    {
	    	String sentence="";
	        XmlPullParser xpp = Xml.newPullParser(); 
	        try {
				xpp.setInput(is, "utf-8");
				for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
				{        	
				    switch (i) {  
		            case XmlPullParser.START_TAG: 
		            	if(xpp.getName().equals("information"))
		            	{

		            		if(!xpp.getAttributeValue(null,"event").equals("sentence"))
		            			return sentence;
		            		else
		            		{	
		            			sentence=xpp.nextText();
		            		}

		            	}
		                
		            }
				}

			} catch (XmlPullParserException e) {
					Log.e("错误","未成功接收xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return sentence;
	    }

	    public Transfer parseTransferXML(InputStream is){

	        transfer=new Transfer();
	        XmlPullParser xpp = Xml.newPullParser(); 
	        try{
	        	xpp.setInput(is, "utf-8");
	    		for (int i = xpp.getEventType(); i != XmlPullParser.END_DOCUMENT; i = xpp.next())
	    		{        	
	    		    if (i==XmlPullParser.START_TAG )
	    		    {
	    		    		if (xpp.getName().equals("payerdevice"))
	    	                	transfer.setPayerDevice(xpp.nextText());
	    	                else if (xpp.getName().equals("receiverdevice")) 
	    	                	transfer.setReceiverDevice(xpp.nextText());
	    	                else if (xpp.getName().equals("payerimei")) 
	    	                	transfer.setPayerIMEI(xpp.nextText());
	    	                else if (xpp.getName().equals("receiverimei")) 
	    	                	transfer.setReceiverIMEI(xpp.nextText());
	    	                else if (xpp.getName().equals("payername")) 
	    	                	transfer.setPayerName(xpp.nextText());
	    	                else if(xpp.getName().equals("receivername"))
	    	                	transfer.setReceiverName(xpp.nextText());
	    	                else if(xpp.getName().equals("payercardnumber"))
	    	                	transfer.setPayerCardNumber(xpp.nextText());
	    	                else if(xpp.getName().equals("receivercardnumber"))
	    	                	transfer.setReceiverCardNumber(xpp.nextText());
	    	                else if(xpp.getName().equals("transfertime"))
	    	                	transfer.setTradeTime(xpp.nextText());
	    	                else if(xpp.getName().equals("cipher"))
	    	                	transfer.setCipher(xpp.nextText());
	    	                else if(xpp.getName().equals("totalprice"))
	    	                	transfer.setTotalPrice(xpp.nextText());

	                    
	                }	
	    		}
	        }
	        catch (XmlPullParserException e) {
				Log.e("错误","未成功接收xml");
	        }	 
	        catch(Exception e)
	        {
				e.printStackTrace();  
	        }
	        return transfer;
	    }
	    public String productSentenceXML(String info)
	    {
	    	 StringWriter stringWriter = new StringWriter();  
		        try {  
		            // 获取XmlSerializer对象  
		            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
		            XmlSerializer xmlSerializer = factory.newSerializer();  
		            // 设置输出流对象  
		            xmlSerializer.setOutput(stringWriter);  
		         
		            xmlSerializer.startDocument("utf-8", true);
		            xmlSerializer.startTag(null, "information");
		            xmlSerializer.attribute(null, "event", "sentence");
		            xmlSerializer.text(info);
		            xmlSerializer.endTag(null, "information");
		            xmlSerializer.endDocument();
	    		} catch (Exception e) {  
	    			e.printStackTrace();  
	    		}  
	    		return stringWriter.toString();  
	    }
		public String producePriceXML(String event){  
		        StringWriter stringWriter = new StringWriter();  
		        try {  
		            // 获取XmlSerializer对象  
		            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
		            XmlSerializer xmlSerializer = factory.newSerializer();  
		            // 设置输出流对象  
		            xmlSerializer.setOutput(stringWriter);  
		         
		            xmlSerializer.startDocument("utf-8", true);
		            xmlSerializer.startTag(null, "information");
		            xmlSerializer.attribute(null, "event", event);
		            xmlSerializer.startTag(null, "goodslist");  
		            for(Goods Goods:goodslist){  
		               
		                xmlSerializer.startTag(null, "goods");  
		                xmlSerializer.startTag(null, "barcode");  
		                xmlSerializer.text(Goods.getBarcode());  
		                xmlSerializer.endTag(null, "barcode"); 
		                
		                xmlSerializer.startTag(null, "name");  
		                xmlSerializer.text(Goods.getName());  
		                xmlSerializer.endTag(null, "name");  
		                  
		                xmlSerializer.startTag(null, "price");  
		                xmlSerializer.text(Goods.getPrice());  
		                xmlSerializer.endTag(null, "price");  
		                
		                xmlSerializer.startTag(null, "quantity");  
		                xmlSerializer.text(Goods.getQuantity());  
		                xmlSerializer.endTag(null, "quantity");  
		                  
		                xmlSerializer.endTag(null, "goods");  
		            }  
		            xmlSerializer.endTag(null, "goodslist");
		            xmlSerializer.endTag(null, "information");
		            xmlSerializer.endDocument();  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		        return stringWriter.toString();  
		  
		    }  
		    
		    public String produceIndividualTradeXML(String event)
		    {
		    	 StringWriter stringWriter = new StringWriter();  
			        try {  
			            // 获取XmlSerializer对象  
			            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
			            XmlSerializer xmlSerializer = factory.newSerializer();  
			            // 设置输出流对象  
			            xmlSerializer.setOutput(stringWriter);  
			         
			            xmlSerializer.startDocument("utf-8", true);
			            xmlSerializer.startTag(null, "information");
			            xmlSerializer.attribute(null, "event", event);
			            xmlSerializer.startTag(null, "trade");
			            
			            xmlSerializer.startTag(null, "payername");  
		                xmlSerializer.text(trade.getPayerName());  
		                xmlSerializer.endTag(null, "payername");  
		                
			            xmlSerializer.startTag(null, "payerdevice");  
		                xmlSerializer.text(trade.getPayerDevice());  
		                xmlSerializer.endTag(null, "payerdevice");  
		                

			            xmlSerializer.startTag(null, "payerimei");  
		                xmlSerializer.text(trade.getPayerIMEI());  
		                xmlSerializer.endTag(null, "payerimei");  
		                
		                xmlSerializer.startTag(null, "payercardnumber");  
		                xmlSerializer.text(trade.getPayCardNumber());  
		                xmlSerializer.endTag(null, "payercardnumber");  
		               
		                
		                xmlSerializer.startTag(null, "receivername");  
		                xmlSerializer.text(trade.getReceiverName());  
		                xmlSerializer.endTag(null, "receivername");  
		                
			            xmlSerializer.startTag(null, "receiverdevice");  
		                xmlSerializer.text(trade.getReceiverDevice());  
		                xmlSerializer.endTag(null, "receiverdevice");  
		                

			            xmlSerializer.startTag(null, "receiverimei");  
		                xmlSerializer.text(trade.getReceiverIMEI());  
		                xmlSerializer.endTag(null, "receiverimei");  
		                
		                
			            xmlSerializer.startTag(null, "receivercardnumber");  
		                xmlSerializer.text(trade.getReceiverCardNumber());  
		                xmlSerializer.endTag(null, "receivercardnumber");  
		                
		                xmlSerializer.startTag(null, "tradetime");  
		                xmlSerializer.text(trade.getTradeTime());  
		                xmlSerializer.endTag(null, "tradetime");  
		                
		                xmlSerializer.startTag(null, "totalprice");  
		                xmlSerializer.text(trade.getTotalPrice());  
		                xmlSerializer.endTag(null, "totalprice");
		                
		                xmlSerializer.startTag(null, "cipher");  
		                xmlSerializer.text(trade.getCipher());  
		                xmlSerializer.endTag(null, "cipher");  
		                
		                
			            xmlSerializer.endTag(null, "trade");
			            xmlSerializer.endTag(null, "information");
			            xmlSerializer.endDocument();  
			        } catch (Exception e) {  
			            e.printStackTrace();  
			        }  
			        return stringWriter.toString();  
		    }
		    public String produceTradeXML(String event){  
		        StringWriter stringWriter = new StringWriter();  
		        try {  
		            // 获取XmlSerializer对象  
		            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
		            XmlSerializer xmlSerializer = factory.newSerializer();  
		            // 设置输出流对象  
		            xmlSerializer.setOutput(stringWriter);  
		         
		            xmlSerializer.startDocument("utf-8", true);
		            xmlSerializer.startTag(null, "information");
		            xmlSerializer.attribute(null, "event", event);
		            xmlSerializer.startTag(null, "goodslist");  
		            for(Goods Goods:goodslist){  
		               
		                xmlSerializer.startTag(null, "goods");  
		                xmlSerializer.startTag(null, "barcode");  
		                xmlSerializer.text(Goods.getBarcode());  
		                xmlSerializer.endTag(null, "barcode"); 
		                
		                xmlSerializer.startTag(null, "name");  
		                xmlSerializer.text(Goods.getName());  
		                xmlSerializer.endTag(null, "name");  
		                  
		                xmlSerializer.startTag(null, "price");  
		                xmlSerializer.text(Goods.getPrice());  
		                xmlSerializer.endTag(null, "price");  
		                
		                xmlSerializer.startTag(null, "quantity");  
		                xmlSerializer.text(Goods.getQuantity());  
		                xmlSerializer.endTag(null, "quantity");  
		                  
		                xmlSerializer.endTag(null, "goods");  
		            }  
		            xmlSerializer.endTag(null, "goodslist");
		            xmlSerializer.startTag(null, "trade");
		            xmlSerializer.startTag(null, "payername");  
	                xmlSerializer.text(trade.getPayerName());  
	                xmlSerializer.endTag(null, "payername");  
	                
		            xmlSerializer.startTag(null, "payerdevice");  
	                xmlSerializer.text(trade.getPayerDevice());  
	                xmlSerializer.endTag(null, "payerdevice");  
	                

		            xmlSerializer.startTag(null, "payerimei");  
	                xmlSerializer.text(trade.getPayerIMEI());  
	                xmlSerializer.endTag(null, "payerimei");  
	                
	                xmlSerializer.startTag(null, "payercardnumber");  
	                xmlSerializer.text(trade.getPayCardNumber());  
	                xmlSerializer.endTag(null, "payercardnumber");  
	               
	                
	                xmlSerializer.startTag(null, "receivername");  
	                xmlSerializer.text(trade.getReceiverName());  
	                xmlSerializer.endTag(null, "receivername");  
	                
		            xmlSerializer.startTag(null, "receiverdevice");  
	                xmlSerializer.text(trade.getReceiverDevice());  
	                xmlSerializer.endTag(null, "receiverdevice");  
	                

		            xmlSerializer.startTag(null, "receiverimei");  
	                xmlSerializer.text(trade.getReceiverIMEI());  
	                xmlSerializer.endTag(null, "receiverimei");  
	                
	                
		            xmlSerializer.startTag(null, "receivercardnumber");  
	                xmlSerializer.text(trade.getReceiverCardNumber());  
	                xmlSerializer.endTag(null, "receivercardnumber");  
	                
	                xmlSerializer.startTag(null, "tradetime");  
	                xmlSerializer.text(trade.getTradeTime());  
	                xmlSerializer.endTag(null, "tradetime");  
	                
	                xmlSerializer.startTag(null, "totalprice");  
	                xmlSerializer.text(trade.getTotalPrice());  
	                xmlSerializer.endTag(null, "totalprice");
	                
	                xmlSerializer.startTag(null, "cipher");  
	                xmlSerializer.text(trade.getCipher());  
	                xmlSerializer.endTag(null, "cipher");  
	                
		            xmlSerializer.endTag(null, "trade");
		            xmlSerializer.endTag(null, "information");
		            xmlSerializer.endDocument();  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		        return stringWriter.toString();  
		  
		    }
		    public String produceTransferXML(String event){  
		        StringWriter stringWriter = new StringWriter();  
		        try {  
		            // 获取XmlSerializer对象  
		            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
		            XmlSerializer xmlSerializer = factory.newSerializer();  
		            // 设置输出流对象  
		            xmlSerializer.setOutput(stringWriter);  
		         
		            xmlSerializer.startDocument("utf-8", true);
		            xmlSerializer.startTag(null, "information");
		            xmlSerializer.attribute(null, "event", event);
		            xmlSerializer.startTag(null, "transfer");
		            
		            xmlSerializer.startTag(null, "payerdevice");  
	                xmlSerializer.text(transfer.getPayerDevice());  
	                xmlSerializer.endTag(null, "payerdevice");  
	                
	                xmlSerializer.startTag(null, "receiverdevice");  
	                xmlSerializer.text(transfer.getReceiverDevice());  
	                xmlSerializer.endTag(null, "receiverdevice");  
	                
	                xmlSerializer.startTag(null, "payerimei");  
	                xmlSerializer.text(transfer.getPayerIMEI());  
	                xmlSerializer.endTag(null, "payerimei");  
	                
	                xmlSerializer.startTag(null, "receiverimei");  
	                xmlSerializer.text(transfer.getReceiverIMEI());  
	                xmlSerializer.endTag(null, "receiverimei");  
	                
	                xmlSerializer.startTag(null, "payername");  
	                xmlSerializer.text(transfer.getPayerName());  
	                xmlSerializer.endTag(null, "payername");
	                
	                xmlSerializer.startTag(null, "receivername");  
	                xmlSerializer.text(transfer.getReceiverName());  
	                xmlSerializer.endTag(null, "receivername");
	                
	                xmlSerializer.startTag(null, "transfertime");  
	                xmlSerializer.text(transfer.getTradeTime());  
	                xmlSerializer.endTag(null, "transfertime");  
	                
	                xmlSerializer.startTag(null, "totalprice");  
	                xmlSerializer.text(transfer.getTotalPrice());  
	                xmlSerializer.endTag(null, "totalprice");
	                
	                xmlSerializer.startTag(null, "cipher");  
	                xmlSerializer.text(transfer.getCipher());  
	                xmlSerializer.endTag(null, "cipher");  
	                
	                xmlSerializer.startTag(null, "payercardnumber");  
	                xmlSerializer.text(transfer.getPayCardNumber());  
	                xmlSerializer.endTag(null, "payercardnumber");  

	                xmlSerializer.startTag(null, "receivercardnumber");  
	                xmlSerializer.text(transfer.getReceiverCardNumber());  
	                xmlSerializer.endTag(null, "receivercardnumber");  
	                
		            xmlSerializer.endTag(null, "transfer");
		            xmlSerializer.endTag(null, "information");
		            xmlSerializer.endDocument();  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		        return stringWriter.toString();  
		  
		    }
		    public void addData(String barcode,String name, String price,String quantity){  
		        Goods newGoods =new Goods(barcode, name,price,quantity);
		        goodslist.add(newGoods); 
		    } 
		    public void setTrade(String payerdevice,String payername,String payerimei, String payercardnumber,String receiverdevice,String receivername,
					String receiverimei,String receivercardnumber,String tradetime,String totalprice,String cipher){ 
		    	trade=new Trade( payerdevice, payername, payerimei,  payercardnumber, receiverdevice, receivername,
		    			 receiverimei, receivercardnumber, tradetime, totalprice, cipher);
		    } 
		    public void setTrade(Trade trade){ 
		    	this.trade=trade;
		    }
		  
			public String producePersonXML(String event){  
		        StringWriter stringWriter = new StringWriter(); 
		        try {  
		            // 获取XmlSerializer对象  
		            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
		            XmlSerializer xmlSerializer = factory.newSerializer();  
		            // 设置输出流对象  
		            xmlSerializer.setOutput(stringWriter);  
		         
		            xmlSerializer.startDocument("utf-8", true);
		            xmlSerializer.startTag(null, "information");
		            xmlSerializer.attribute(null, "event", event);
		                xmlSerializer.startTag(null, "personInfo");  
		                xmlSerializer.startTag(null, "userName");  
		                xmlSerializer.text(person.getUsername());  
		                xmlSerializer.endTag(null, "userName"); 
		                
		                xmlSerializer.startTag(null, "customerName");  
		                xmlSerializer.text(person.getCustomername());  
		                xmlSerializer.endTag(null, "customerName");  
		                  
		                xmlSerializer.startTag(null, "cardNum");  
		                xmlSerializer.text(person.getCardnum());  
		                xmlSerializer.endTag(null, "cardNum");  
		                
		                xmlSerializer.startTag(null, "password");  
		                xmlSerializer.text(person.getPassword());  
		                xmlSerializer.endTag(null, "password");  
		                
		                xmlSerializer.startTag(null, "bluetoothMac");  
		                xmlSerializer.text(person.getBluetoothmac());  
		                xmlSerializer.endTag(null, "bluetoothMac"); 
		                
		                xmlSerializer.startTag(null, "dynamicPasswordNum");  
		                xmlSerializer.text(person.getPrivatekey());  
		                xmlSerializer.endTag(null, "dynamicPasswordNum"); 
		                
		                xmlSerializer.startTag(null, "identificationCardNum");  
		                xmlSerializer.text(person.getIdentificationcardnum());  
		                xmlSerializer.endTag(null, "identificationCardNum"); 
		                
		                xmlSerializer.startTag(null, "phoneNum");  
		                xmlSerializer.text(person.getPhonenum());  
		                xmlSerializer.endTag(null, "phoneNum"); 
		                
		                xmlSerializer.startTag(null, "balance");  
		                xmlSerializer.text(person.getBalance());  
		                xmlSerializer.endTag(null, "balance");  
		                  
		                xmlSerializer.endTag(null, "personInfo");
		            xmlSerializer.endTag(null, "information");
		            xmlSerializer.endDocument(); 
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		        return stringWriter.toString();  
		    }  

		    public void addPersonDatapublic (String username, String customername, String cardnum,
					String bluetoothmac, String privatekey,
					String identificationcardnum, String phonenum, String balance,
					String password, String publickeyn, String publickeyd, String imei) {  
		        PersonInfo newPerson =new PersonInfo(username, customername, cardnum, bluetoothmac, privatekey, identificationcardnum, phonenum,balance,password,publickeyn,publickeyd, imei);
		        person=newPerson;
		    } 
		    public void setTransfer(String payerdevice,String receiverdevice,String payername,String receivername,String transfertime,String totalprice,String cipher,String payercardnumber,String receivercardnumber)
		    {
		    	transfer=new Transfer();
		    	transfer.setPayerDevice(payerdevice);
		    	transfer.setReceiverDevice(receiverdevice);
		    	transfer.setPayerName(payername);
		    	transfer.setReceiverName(receivername);
		    	transfer.setTotalPrice(totalprice);
		    	transfer.setTradeTime(transfertime);
		    	transfer.setCipher(cipher);
		    	transfer.setPayerCardNumber(payercardnumber);
		    	transfer.setReceiverCardNumber(receivercardnumber);
		    }
		    public void setTransfer(Transfer transfer)
		    {
		    	this.transfer=transfer;
		    }

		    public Trade getTrade()
		    {
		    	return trade;
		    }

}
