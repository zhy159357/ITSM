package com.ruoyi.common.netty.ssh;

import ch.ethz.ssh2.*;
import com.ruoyi.common.exception.BusinessException;

import java.io.*;
import java.util.List;

/**
 * 远程shell脚本调用类
 * 
 * @author LiuPeng
 * 
 */
public class SshFactory {

	private String charset = "UTF-8"; // 设置编码格式
	private String username; // 用户名
	private File pemFile; // 登录密码
	private String hostname; // 主机IP
	private String password;
	private int port; // 端口
	Connection conn;
	private Session session;
	private BufferedReader brout;
	private BufferedReader brerr;

	public SshFactory(String hostname, int port, String username, File pemFile) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.pemFile = pemFile;
	}

	public SshFactory(String hostname, int port, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * 连接到指定的IP
	 * 
	 */
	public void connect() throws IOException {
		this.connect(0);
	}

	/**
	 * 连接到指定的IP
	 * 
	 */
	public void connect(int timeout) throws IOException {
		// 获取连接
		conn = new Connection(hostname, port);
		// 连接
		conn.connect(null, timeout, 0);
		// 输入账号密码登陆
		boolean isAuthenticated = conn.authenticateWithPublicKey(username, pemFile, "");
		// 登陆失败，返回错误
		if (isAuthenticated == false) {
			throw new IOException("authentication failed.");
		}
		session = conn.openSession();
	}

	public void connectUsePassword(int timeout) throws IOException {
		// 获取连接
		conn = new Connection(hostname, port);
		// 连接
		conn.connect(null, timeout, 0);
		// 输入账号密码登陆
		boolean isAuthenticated = conn.authenticateWithPassword(username, password);
		// 登陆失败，返回错误
		if (isAuthenticated == false) {
			throw new IOException("authentication failed.");
		}
		session = conn.openSession();
	}

	public void execCommand(String command) throws IOException {
		if (session == null) {
			throw new RuntimeException("session is null,please connect first ");
		}
		session.execCommand(command);
	}

	public void startShell(List<String> commands, List<String> outLine) {
		try {
			if (session == null) {
				throw new RuntimeException("session is null,please connect first ");
			}
			// 建立虚拟终端
			session.requestPTY("bash");
			// 打开一个Shell
			session.startShell();
			commands.add("exit");
			// 输入待执行命令
			// 打印信息
			InputStream stderr = new StreamGobbler(session.getStderr());
			InputStream stdout = new StreamGobbler(session.getStdout());
			brout = new BufferedReader(new InputStreamReader(stdout, charset));
			brerr = new BufferedReader(new InputStreamReader(stderr, charset));
			// 准备输入命令
			PrintWriter out = new PrintWriter(session.getStdin());
			for (String command : commands) {
				out.println(command);
			}
			// 关闭输入流
			out.flush();
			out.close();

			while (true) {
				String line = brout.readLine();
				if (line == null)
					break;
				if (!commands.contains(line) && !"".equals(line)) {
					if (outLine != null) {
						outLine.add(line);
					}
				}
			}
			while (true) {
				String line = brerr.readLine();
				if (line == null) {
					if (outLine != null) {
						outLine.add(line);
					}
					break;
				}
				if (!commands.contains(line) && !"".equals(line)) {
					if (outLine != null) {
						outLine.add(line);
					}
				}
			}

			// 等待，除非1.连接关闭；2.输出数据传送完毕；3.进程状态为退出；4.超时
			session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS,
					10000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getFile(String remoteFile, String localTargetDirectory) throws IOException {
		SCPClient client = new SCPClient(conn);
		client.get(remoteFile, localTargetDirectory);
	}

	public void putFile(String localFile, String remoteTargetDirectory) {
		try {
			SCPClient client = new SCPClient(conn);
			client.put(localFile, remoteTargetDirectory);
		} catch (IOException ex) {
			throw new BusinessException("传输文件失败:" + ex.getMessage(), ex);
		}
	}

	public void putFile(String localFile, String remoteFileName, String remoteTargetDirectory, String mode) {
		try {
			SCPClient client = new SCPClient(conn);
			if ((mode == null) || (mode.length() == 0)) {
				mode = "0600";
			}
			client.put(localFile, remoteFileName, remoteTargetDirectory, mode);
		} catch (IOException ex) {
			throw new BusinessException("传输文件失败:" + ex.getMessage(), ex);
		}
	}

	/**
	 * 在远端linux上创建文件夹
	 * 
	 * @param dirName
	 *            文件夹名称
	 * @param posixPermissions
	 *            目录或者文件夹的权限
	 * @throws IOException
	 */
	public void mkDir(String dirName, int posixPermissions) throws IOException {
		SFTPv3Client sftpClient = new SFTPv3Client(conn);
		sftpClient.mkdir(dirName, posixPermissions);
	}

	public void close() {
		try {
			session.close();
			conn.close();
		} catch (Exception e) {
		}
	}

}