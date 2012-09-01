package cn.edu.seu.xml;

public class Goods{  
	String name;  
	String price ; 
	String barcode;
	String quantity;
	public void setBarcode(String barcode)
	{
		this.barcode=barcode;
	}
	public String getBarcode()
	{
		return barcode;
	}
	public String getName() {  
		return name;  
	}  
	public void setName(String name) {  
		this.name = name;  
	}  
	public String getPrice() {  
		return price;  
	}  
	public void setPrice(String price) {  
		this.price = price;  
	}
	public String getQuantity() {  
		return quantity;  
	}  
	public void setQuantity(String quantity) {  
		this.quantity = quantity;  
	} 
	public Goods(String barcode,String name, String price,String quantity) {  
		this.barcode=barcode;
		this.name = name;  
		this.price = price;  
		this.quantity=quantity;
	}
	public Goods() {  
	}  
}  