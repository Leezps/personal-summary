package com.zulong.channels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @清空命令窗口的输出缓存区以及错误缓存区
 * 
 * @author Leezp
 *
 */
public class ProcessClearStream extends Thread {
	//缓存区的数据流种类
	public static String INFO_TYPE = "INFO";
	public static String ERROR_TYPE = "ERROR";
	//缓存区数据流
	private InputStream inputStream;
	//当前缓存区数据流的种类
	private String type;
	//回调接口
	private ProcessClearCallback callback;

	public ProcessClearStream(InputStream inputStream, String type, ProcessClearCallback callback) {
		this.inputStream = inputStream;
		this.type = type;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(inputStreamReader);
			// 读取缓存区中的数据
			String line = null;
			StringBuffer result = new StringBuffer();
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			line = result.toString();
			if (type.equals(ERROR_TYPE) && line!=null && !line.equals("")) {
				callback.onCallback("build.xml文件有误，请检查！");
			} else if (type.equals(INFO_TYPE) && line!=null && !line.equals("")) {
				callback.onCallback("执行完毕");
			}
			System.out.println(type+">"+result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static interface ProcessClearCallback {
		public void onCallback(String message);
	}
}
