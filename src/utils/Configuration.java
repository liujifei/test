package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件 优先加载同同路径的conf下面的文件 然后加载jar包内的文件
 * 
 * @author inspur
 *
 */
public class Configuration {
	private Properties prop = new Properties();

	public Configuration(String confFile) {
		File file = new File("resources/" + confFile);
		InputStream in = null;
		try {
			System.out.println(file.getAbsolutePath());
			if (file.exists()) {
				// System.out.println("存在"+file.getAbsolutePath());
				in = new FileInputStream(file);
			} else {
				in = Configuration.class.getResourceAsStream("/" + confFile);
			}
			if (in != null) {
				prop.load(in);
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get(String key) {
		return prop.getProperty(key);
	}

	public String get(String key, String defaultValue) {
		return prop.getProperty(key, defaultValue);
	}
}
