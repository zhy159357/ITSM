/*
 * Gsoft开发框架
 * Copyright 2015-2020 the original author or authors.
 */

package com.ruoyi.common.netty.vo;

import java.io.Serializable;

/**
 * 文件上传返回对象
 * 
 * @author LiuPeng
 * @date 2019年7月4日
 * 
 */
public class FileUploadResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String hostname;

	private String path;

	private String fileName;

	private String uploadTime;

	private String md5;

	private String fileType;

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the uploadTime
	 */
	public String getUploadTime() {
		return uploadTime;
	}

	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
