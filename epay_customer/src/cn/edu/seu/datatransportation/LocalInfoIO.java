package cn.edu.seu.datatransportation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.edu.seu.ciphertext.DES;
import cn.edu.seu.ciphertext.MD5;

public class LocalInfoIO {

	/**
	 * @param args
	 */
	private String dirPath;
	private String filename;
	private DES ed;
	
	public LocalInfoIO(String dir , String file){
		dirPath = dir;
		filename = file;
		ed = new DES();
		if(createDir()){
			createFile();
		}
	}
	private boolean createDir(){
		if(dirPath != null){
			File path = new File(dirPath);
			if(path.exists()){
				return true;
			}
			if(path.mkdirs() == true){
				return true;
			}
		}
		return false;
	}
	private void createFile(){
		if(filename != null){
			File file = new File(dirPath , filename);
			if(file.exists() == false){
				try{
					file.createNewFile();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void writefile(LocalInfo l){
		File file = new File(dirPath, filename);  
        if (false == file.isFile()) {  
            return ;  
        }  
        try {
			LocalInfo ldes = new LocalInfo(ed.encrypt(l.getUserName()),ed.encrypt(l.getPassword()),ed.encrypt(l.getGesturePwd()),ed.encrypt(l.getCardnum()),ed.encrypt(l.getBalance()),ed.encrypt(l.getAvailableBalance()),ed.encrypt(l.getPrivateKey()),ed.encrypt(l.getPublicKeyn()),ed.encrypt(l.getCustomerName()));
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file,false));  
            oos.writeObject(ldes);  
            oos.flush();
            oos.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
	}
	
	public LocalInfo readfile(){
		File file = new File(dirPath, filename);  
        if (false == file.isFile()) {  
            return null;  
        }
        LocalInfo ldes,l = new LocalInfo();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));  
            ldes = (LocalInfo)ois.readObject();
            l = new LocalInfo(ed.decrypt(ldes.getUserName()),ed.decrypt(ldes.getPassword()),ed.decrypt(ldes.getGesturePwd()),ed.decrypt(ldes.getCardnum()),ed.decrypt(ldes.getBalance()),ed.decrypt(ldes.getAvailableBalance()),ed.decrypt(ldes.getPrivateKey()),ed.decrypt(ldes.getPublicKeyn()),ed.decrypt(ldes.getCustomerName()));
            ois.close();
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return l;
	}
	
	public void modifyPassword(String newPwd){
		LocalInfo l = readfile();
		l.setPassword(newPwd);
		writefile(l);
	}
	
	public void modifyGesturePwd(String newGpwd){
		LocalInfo l = readfile();
		l.setGesturePwd(newGpwd);
		writefile(l);
	}
	
	public void modifyBalance(String newBalance){
		LocalInfo l = readfile();
		l.setBalance(newBalance);
		writefile(l);
	}
	
	public void modifyAvailableBalance(String newAvailableBalance){
		LocalInfo l = readfile();
		l.setAvailableBalance(newAvailableBalance);
		writefile(l);
	}
	
	public static void main(String[] args) {
		MD5 md5 = new MD5();
		LocalInfo l = new LocalInfo("kk" , md5.encrypt("9999997777999999999") , "shoushimima" , "yinhangkahao" , "yu e" , "keyongyu e" , "siyue" , "gongyue","momingqimiao");
		LocalInfoIO lio = new LocalInfoIO("f:\\shixun" , "local.data");
		//lio.writefile(l);
		LocalInfo x = lio.readfile();
		System.out.println(x.getUserName() + "\n" + x.getPassword() + "\n" + x.getGesturePwd() + "\n" );
		lio.modifyPassword(md5.encrypt("11111111111111111"));
		lio.modifyGesturePwd("11111111111");
		x = lio.readfile();
		System.out.println(x.getUserName() + "\n" + x.getPassword() + "\n" + x.getGesturePwd() + "\n" );
	}

}
