package com.ruoyi.common.netty.ssh;

import com.ruoyi.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 
 * @author LiuPeng
 *
 */
public class SftpUtils {

	private static final Logger log = LoggerFactory.getLogger(SftpUtils.class);

	/**
	 * sftp获取远程文件（使用互信文件）
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param pemFile
	 * @param filepath
	 * @param localDir
	 * @return
	 */
	public static File getFile(String ip, int port, String username, File pemFile, String filepath, String localDir) {
		File tmpFile = new File(filepath);
		String fileName = tmpFile.getName();
		File file = new File(localDir, fileName);
		if (file.exists()) {
			file.delete();
		}
		SshFactory sshFactory = new SshFactory(ip, port, username, pemFile);
		try {
			log.info("开始建立SSH连接................");
			sshFactory.connect(5000);
			log.info("建立SSH连接成功................");
			sshFactory.getFile(filepath, localDir);
			log.info("getFile成功................");
		} catch (Exception e) {
			throw new BusinessException("sftp获取文件失败," + e.getMessage(), e);
		} finally {
			sshFactory.close();
		}
		if (!file.exists()) {
			throw new BusinessException("sftp获取文件完成，本地文件不存在:" + file.getPath());
		}
		return file;
	}

	/**
	 * sftp获取远程文件（使用密码）
	 *
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param filepath
	 * @param localDir
	 * @return
	 */
	public static File getFile(String ip, int port, String username, String password, String filepath, String localDir) {
		File tmpFile = new File(filepath);
		String fileName = tmpFile.getName();
		File file = new File(localDir, fileName);
		if (file.exists()) {
			file.delete();
		}
		SshFactory sshFactory = new SshFactory(ip, port, username, password);
		try {
			log.info("开始建立SSH连接................");
			sshFactory.connectUsePassword(5000);
			log.info("建立SSH连接成功................");
			sshFactory.getFile(filepath, localDir);
			log.info("getFile成功................");
		} catch (Exception e) {
			throw new BusinessException("sftp获取文件失败," + e.getMessage(), e);
		} finally {
			sshFactory.close();
		}
		if (!file.exists()) {
			throw new BusinessException("sftp获取文件完成，本地文件不存在:" + file.getPath());
		}
		return file;
	}

}
