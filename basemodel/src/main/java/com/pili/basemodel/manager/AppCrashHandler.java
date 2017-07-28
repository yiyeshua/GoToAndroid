package com.pili.basemodel.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.pili.basemodel.utils.AppUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppCrashHandler implements UncaughtExceptionHandler {

	// 保存奔溃日志开关
	private boolean canSaveLog = false;
	private boolean automaticExit = true;
	private String logPath = "sdcard/crashLog/"
			+ AppUtils.getAppContext().getPackageName() + "/Log/";
	private UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
	private static AppCrashHandler INSTANCE = new AppCrashHandler();// CrashHandler实例
	private Context mContext;// 程序的Context对象
	private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");// 用于格式化日期,作为日志文件名的一部分

	private CrashListener crashListener;

	public interface CrashListener {
		public void onCrashAction();
	}

	/** 保证只有一个AppCrashHandler实例 */
	private AppCrashHandler() {

	}

	/** 获取AppCrashHandler实例 ,单例模式 */
	public static AppCrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化AppCrashHandler
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		newFolder(logPath);
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该AppCrashHandler为程序的默认处理器
	}

	/**
	 * 设置crash日志的文件夹地址
	 * 
	 * @Title:setLogPath
	 * @param logPath
	 * 
	 * @author yudapei
	 */
	public void setLogPath(String logPath) {
		this.logPath = logPath;
		newFolder(logPath);
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			Log.i("test", "uncaughtException");
			// 退出程序
			if (automaticExit) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				exitApp();
			}
		}
	}

	/**
	 * 退出app
	 * 
	 * TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @Title:exitApp
	 * 
	 * @author yudapei
	 */
	public void exitApp() {
		AppManager.instance.finishActivity();
		// 这样就可以从操作系统中结束掉当前程序的进程。当你Kill掉当前程序的进程时也就是说整个程序的所有线程都会结束，Service也会停止，整个程序完全退出
		android.os.Process.killProcess(android.os.Process.myPid());
		// 退出JVM（java虚拟机）。参数0和1代表退出的状态，0表示正常退出，1表示异常退出(只要是非0的都为异常退出)

		System.exit(1);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 *            异常信息 true 如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable ex) {
		Log.i("test", "handleException");
		if (ex == null)
			return false;
		if (canSaveLog) {
			// 收集设备参数信息
			collectDeviceInfo(mContext);
			// 保存日志文件
			saveCrashInfo2File(ex);
		}
		// 把action放到调用的地方定义
		if (crashListener != null) {
			crashListener.onCrashAction();
		}
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fields = Build.class.getDeclaredFields();// 反射机制
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存设备和crash的信息岛文件 TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @Title:saveCrashInfo2File
	 * @param ex
	 * @return
	 * 
	 * @author yudapei
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key.equals("FINGERPRINT") || key.equals("BOARD")
					|| key.equals("HOST") || key.equals("versionCode")
					|| key.equals("versionName") || key.equals("MODEL")
					|| key.equals("CPU_ABI") || key.equals("IS_DEBUGGABLE")
					|| key.equals("SERIAL")) {
				sb.append(key + "=" + value + "\r\n");
			}
			// Log.i("test", "key=" + key + ",value=" + value);
		}
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		ex.printStackTrace(pw);
		Throwable cause = ex.getCause();
		// 循环着把所有的异常信息写入writer中
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();// 记得关闭
		String result = writer.toString();
		sb.append(result);
		// 保存文件
		long timetamp = System.currentTimeMillis();
		String time = format.format(new Date());
		String fileName = "crash-" + time + "-" + timetamp + ".log";
		Log.e("error", "本地保存路径--" + logPath + "        " + "程序出现奔溃:" + fileName
				+ "\n奔溃详细信息:\n" + sb.toString());
		writeLog(logPath,
				"程序出现奔溃:" + fileName + "\n奔溃详细信息:\n" + sb.toString());

		return null;
	}

	/**
	 * 新建目录
	 *
	 * @param folderPath
	 *            String 如 c:/fqf
	 * @return boolean
	 */
	public static void newFolder(String folderPath) {
		try {
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.mkdirs();
			}
		} catch (Exception e) {
			System.err.println("新建目录操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 写程序日志 TODO(这里用一句话描述这个方法的作用)
	 *
	 * @Title:writeLog
	 * @param fileName
	 * @param info
	 *
	 * @author yudapei
	 */
	public static void writeLog(String fileName, String info) {
		Date dt = new Date();
		fileName = fileName + "log_" + new SimpleDateFormat( "yyyyMMddHHmmss", Locale.CHINA).format(dt)
				+ ".txt";
		info = new SimpleDateFormat( "HH:mm:ss.", Locale.CHINA).format(dt)
				+ String.format("%03d ", dt.getTime() % 1000) + info + "\r\n";

		writeFile(fileName, info, true, "GB2312");
	}

	/**
	 * 写文件
	 *
	 * @param fileName
	 *            文件名
	 * @param info
	 *            写的内容
	 * @param append
	 *            是否添加
	 * @param encode
	 *            编码
	 * @return 是否成功
	 */
	public static boolean writeFile(String fileName, String info,
									boolean append, String encode) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName, append);
			byte[] bytes = info.getBytes(encode);
			fout.write(bytes);
			fout.close();
			return true;
		} catch (Exception err) {
		}
		return false;
	}


	public boolean isCanSaveLog() {
		return canSaveLog;
	}

	public void setCanSaveLog(boolean canSaveLog) {
		this.canSaveLog = canSaveLog;
	}

	public boolean isAutomaticExit() {
		return automaticExit;
	}

	public void setAutomaticExit(boolean automaticExit) {
		this.automaticExit = automaticExit;
	}

	public CrashListener getCrashListener() {
		return crashListener;
	}

	public void setCrashListener(CrashListener crashListener) {
		this.crashListener = crashListener;
	}

}
