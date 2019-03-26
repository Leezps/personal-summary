package com.zulong.channels;

/**
 * @用于存储渠道配置的实体类
 * 
 * @author Leezp
 *
 */
public class Channels {
	//渠道名
	private String name;
	//渠道项目的文件名
	private String fileName;
	//渠道项目的包名
	private String packageName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
