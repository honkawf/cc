package cn.edu.seu.record;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Recorddh extends SQLiteOpenHelper {  
    private static final int VERSION = 1;  
  
    /** 
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数 
     * @param context   上下文对象 
     * @param name      数据库名称 
     * @param factory 
     * @param version   当前数据库的版本，值必须是整数并且是递增的状态 
     */  
    public Recorddh(Context context, String name, CursorFactory factory,  
            int version) {  
        //必须通过super调用父类当中的构造函数  
        super(context, name, factory, version);  
    }  
      
    public Recorddh(Context context, String name, int version){  
        this(context,name,null,version);  
    }  
  
    public Recorddh(Context context, String name){  
        this(context,name,VERSION);  
    }  
  
    //该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法  
    @Override  
    public void onCreate(SQLiteDatabase db) { 
        //execSQL用于执行SQL语句  
        db.execSQL("create table if not exists record(tradenum integer primary key autoincrement not null , payername varchar not null ,payermac varchar not null ,payerimei varchar not null ,receivername varchar not null ,receivermac varchar not null ,receiverimei varchar not null , price varchar not null , tradetype varchar not null , tradetime varchar not null)");  
    }
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        // TODO Auto-generated method stub  
        System.out.println("upgrade a database");  
    }
    
	public void insert(Record record){
    	String sql = "insert into record(payername,payermac,payerimei,receivername,receivermac,receiverimei,price,tradetype,tradetime) values('" +record.getPayerName()+ "' ,'" +record.getPayerMac()+ "' ,'" +record.getPayerImei()+ "' ,'" +record.getReceiverName()+ "' ,'" +record.getReceiverMac()+ "' , '" +record.getReceiverImei()+ "' ," +record.getPrice()+ " , '" +record.getTradeType()+ "' , " +record.getTradeTime()+ ")";
    	getWritableDatabase().execSQL(sql);
    }
    
    public Record [] query(){
    	//getWritableDatabase().execSQL("drop table record");
    	getWritableDatabase().execSQL("create table if not exists record(tradenum integer primary key autoincrement not null , payername varchar not null ,payermac varchar not null ,payerimei varchar not null ,receivername varchar not null ,receivermac varchar not null ,receiverimei varchar not null , price varchar not null , tradetype varchar not null , tradetime varchar not null)");
    	Cursor c = getReadableDatabase().query("record", new String[] {"tradenum", "payername", "payermac","payerimei","receivername","receivermac","receiverimei",
        "price" , "tradetype" , "tradetime" }, null , null , null, null, null);
    	Record [] list = new Record[c.getCount()];
    	if(c != null &&c.moveToFirst()){
    		for(int i = 0 ; i < c.getCount(); i++){
				list[i] = new Record(c.getInt(0) , c.getString(1) , c.getString(2) , c.getString(3),c.getString(4),c.getString(5) ,c.getString(6) ,c.getDouble(7),c.getString(8) ,c.getString(9));
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