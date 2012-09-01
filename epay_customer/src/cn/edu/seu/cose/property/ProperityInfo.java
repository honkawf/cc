package cn.edu.seu.cose.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProperityInfo {
	private static Properties config= null;;
	/**
	 * @param null
	 * @return Properties from the .properties
	 * @Discribe 
	 *          获取.Properties(配置文件) 信息,相当于一个数据对象存储静态数据
	 */
	public static Properties getProperties(){
		System.out.println(" Config load success!");
		if(config==null){
			System.out.println(" Config load success!");
			config= new Properties();
			System.out.println(" Config load success!");
			InputStream in=ProperityInfo.class.getClassLoader().getResourceAsStream("server.properties");
		    try {
		    	System.out.println(" Config load success!");
				config.load(in);
				System.out.println(" Config load success!");
				return config;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			return config;
		}
		return null;
	}

}
