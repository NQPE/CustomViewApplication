package com.hx100.levi.customviewapplication.controldemo.autotest.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ShellUtils {

	public static Process shell(String command) {
		return process(command);
	}
	
	public static BufferedReader shellOut(Process ps) {
		BufferedInputStream in = new BufferedInputStream(ps.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		return br;
	}

	public static String getShellOut(Process ps) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = shellOut(ps);
		String line;

		try {
			while ((line = br.readLine()) != null) {
//				sb.append(line);
			sb.append(line + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	private static Process process(String command) {
		Process ps = null;
		try {
			ps = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ps;
	}
	
	//需要root权限执行命令时使用该方法
	public static void suShell(String cmd) {
//		Process ps = null;
//		DataOutputStream os;
//
//		try {
//			ps = Runtime.getRuntime().exec("su");
//			os = new DataOutputStream(ps.getOutputStream());
//
//			os.writeBytes(cmd + "\n");
//			os.writeBytes("exit\n");
//			os.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		execCommand(cmd,true,false);
	}
	
	public static InputStream StringTOInputStream(String in) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
		return is;
	}

	public static final String COMMAND_SU = "su";
	public static final String COMMAND_SH = "sh";
	public static final String COMMAND_EXIT = "exit\n";
	public static final String COMMAND_LINE_END = "\n";


//	private ShellUtils() {
//		throw new AssertionError();
//	}


	/**
	 * check whether has root permission
	 *
	 * @return
	 */
	public static boolean checkRootPermission() {
		return execCommand("echo root", true, false).result == 0;
	}


	/**
	 * execute shell command, default return result msg
	 *
	 * @param command command
	 * @param isRoot whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(Object[], boolean, boolean)
	 */
	public static CommandResult execCommand(String command, boolean isRoot) {
		return execCommand(new String[] {command}, isRoot, true);
	}


	/**
	 * execute shell commands, default return result msg
	 *
	 * @param commands command list
	 * @param isRoot whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(Object[], boolean, boolean)
	 */
	public static CommandResult execCommand(List commands, boolean isRoot) {
		return execCommand(commands == null ? null : commands.toArray(new String[] {}),isRoot, true);
	}


	/**
	 * execute shell commands, default return result msg
	 *
	 * @param commands command array
	 * @param isRoot whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(Object[], boolean, boolean)
	 */
	public static CommandResult execCommand(String[] commands, boolean isRoot) {
		return execCommand(commands, isRoot, true);
	}


	/**
	 * execute shell command
	 *
	 * @param command command
	 * @param isRoot whether need to run with root
	 * @param isNeedResultMsg whether need result msg
	 * @return
	 * @see ShellUtils#execCommand(Object[], boolean, boolean)
	 */
	public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
		return execCommand(new String[] {command}, isRoot, isNeedResultMsg);
	}


	/**
	 * execute shell commands
	 *
	 * @param commands command list
	 * @param isRoot whether need to run with root
	 * @param isNeedResultMsg whether need result msg
	 * @return
	 * @see ShellUtils#execCommand(Object[], boolean, boolean)
	 */
	public static CommandResult execCommand(List commands, boolean isRoot, boolean isNeedResultMsg) {
		return execCommand(commands==null?null:commands.toArray(new String[] {}), isRoot, isNeedResultMsg);
	}


	/**
	 * execute shell commands
	 *
	 * @param commands command array
	 * @param isRoot whether need to run with root
	 * @param isNeedResultMsg whether need result msg
	 * @return
	 *
	if isNeedResultMsg is false, {@link CommandResult#successMsg} is null and
	 * {@link CommandResult#errorMsg} is null.
	 *
	if {@link CommandResult#result} is -1, there maybe some excepiton.
	 *
	 */
	public static CommandResult execCommand(Object[] commands, boolean isRoot, boolean isNeedResultMsg) {
		int result = -1;
		if (commands == null || commands.length == 0) {
			return new CommandResult(result, null, null);
		}


		Process process = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = null;
		StringBuilder errorMsg = null;


		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
			os = new DataOutputStream(process.getOutputStream());
			for (Object command : commands) {
				String strCommand= String.valueOf(command);

				if (strCommand == null) {
					continue;
				}
// donnot use os.writeBytes(strCommand), avoid chinese charset error
				os.write(strCommand.getBytes());
				os.writeBytes(COMMAND_LINE_END);
				os.flush();
			}
			os.writeBytes(COMMAND_EXIT);
			os.flush();


			result = process.waitFor();
// get command result
			if (isNeedResultMsg) {
				successMsg = new StringBuilder();
				errorMsg = new StringBuilder();
				successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
				errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String s;
				while ((s = successResult.readLine()) != null) {
					successMsg.append(s);
				}
				while ((s = errorResult.readLine()) != null) {
					errorMsg.append(s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (successResult != null) {
					successResult.close();
				}
				if (errorResult != null) {
					errorResult.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


			if (process != null) {
				process.destroy();
			}
		}
		return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
				: errorMsg.toString());
	}

}
