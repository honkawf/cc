package cn.edu.seu.check;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class Checkdh extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	
	public Checkdh(Context context, String name, CursorFactory factory,  
            int version) {  
        //必须通过super调用父类当中的构造函数  
        super(context, name, factory, version);  
    }  
      
    public Checkdh(Context context, String name, int version){  
        this(context,name,null,version);  
    }  
  
    public Checkdh(Context context, String name){  
        this(context,name,VERSION);  
    }  
  
    //该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法  
    @Override  
    public void onCreate(SQLiteDatabase db) { 
        //execSQL用于执行SQL语句  
        db.execSQL("create table if not exists checklist(checkid integer primary key autoincrement not null , payername varchar not null , payercardnum varchar not null ,payerimei varchar not null , totalprice varchar not null , transfertime varchar not null , xml blob not null)");  
    }
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("upgrade a database");  
    }
    
	public void insert(Check check){
    	getWritableDatabase().execSQL("create table if not exists checklist(checkid integer primary key autoincrement not null , payername varchar not null , payercardnum varchar not null ,payerimei varchar not null , totalprice varchar not null , transfertime varchar not null , xml blob not null)"); 
    	String sql = "insert into checklist(payername,payercardnum,payerimei,totalprice,transfertime,xml) values('" +check.getPayerName()+ "' , '" +check.getPayerCardnum()+ "' , '" +check.getPayerImei()+ "' , " +check.getTotalPrice() + " , '" +check.getTransferTime() + "','" +check.getXml().getBytes()+ "')";
    	getWritableDatabase().execSQL(sql);
    }
    
    public Check [] query(){
    	getWritableDatabase().execSQL("create table if not exists checklist(checkid integer primary key autoincrement not null , payername varchar not null , payercardnum varchar not null ,payerimei varchar not null , totalprice varchar not null , transfertime varchar not null , xml blob not null)"); 
    	Cursor c = getReadableDatabase().query("checklist", new String[] { "checkid" ,"payername",  
        "payercardnum", "payerimei", "totalprice" , "transfertime" , "xml" }, null , null , null, null, null);
    	Check [] list = new Check[c.getCount()];
    	if(c != null &&c.moveToFirst()){
    		for(int i = 0 ; i < c.getCount(); i++){
				list[i] = new Check(c.getInt(0) , c.getString(1) , c.getString(2) , c.getString(3) , c.getDouble(4) , c.getString(5) , new String(c.getBlob(6)));
    			c.moveToNext();
    		}
    		c.close();
    	}
    	else{
    		list = null;
    	}
    	return list;
    }
}
