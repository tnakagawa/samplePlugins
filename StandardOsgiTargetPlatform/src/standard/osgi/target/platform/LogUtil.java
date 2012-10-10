package standard.osgi.target.platform;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * ログユーティリティ
 * 
 * @author tnakagawa
 */
public class LogUtil {

	/** ログレベル（デバッグ） */
	private static final int DEBUG = IStatus.OK;

	/** ログレベル（エラー） */
	private static final int ERROR = IStatus.ERROR;

	/** ログレベル（インフォ） */
	private static final int INFO = IStatus.INFO;

	/** ログレベル（ワーニング） */
	private static final int WARN = IStatus.WARNING;

	/** Eclipse用ログ */
	private ILog iLog = null;

	/** デバッグ出力フラグ */
	private boolean debugFlg = false;

	/** プラグイン名 */
	private String name = null;

	/**
	 * コンストラクタ
	 */
	public LogUtil() {
		iLog = Activator.getDefault().getLog();
		name = Activator.PLUGIN_ID;
		debugFlg = "true".equalsIgnoreCase(Platform.getDebugOption(name + "/debug"));
	}

	/**
	 * デバッグ
	 * 
	 * @param message メッセージ
	 */
	public void debug(String message) {
		debug(message, null);
	}

	/**
	 * デバッグ
	 * 
	 * @param message メッセージ
	 * @param e 例外
	 */
	public void debug(String message, Throwable e) {
		if (debugFlg) {
			writeLog(DEBUG, message, e);
		}
	}

	/**
	 * エラー
	 * 
	 * @param message メッセージ
	 */
	public void error(String message) {
		error(message, null);
	}

	/**
	 * エラー
	 * 
	 * @param message メッセージ
	 * @param e 例外
	 */
	public void error(String message, Throwable e) {
		writeLog(ERROR, message, e);
	}

	/**
	 * インフォ
	 * 
	 * @param message メッセージ
	 */
	public void info(String message) {
		info(message, null);
	}

	/**
	 * インフォ
	 * 
	 * @param message メッセージ
	 * @param e 例外
	 */
	public void info(String message, Throwable e) {
		writeLog(INFO, message, e);
	}

	/**
	 * ワーニング
	 * 
	 * @param message メッセージ
	 */
	public void warn(String message) {
		warn(message, null);
	}

	/**
	 * ワーニング
	 * 
	 * @param message メッセージ
	 * @param e 例外
	 */
	public void warn(String message, Throwable e) {
		writeLog(WARN, message, e);
	}

	/**
	 * ログ記述
	 * 
	 * @param level ログレベル
	 * @param message メッセージ
	 * @param e 例外
	 */
	private void writeLog(int level, String message, Throwable e) {
		String head = null;
		switch (level) {
		case DEBUG:
			head = "DEBUG";
			break;
		case INFO:
			head = "INFO ";
			break;
		case ERROR:
			head = "ERROR";
			break;
		default:
			head = "WARN ";
			break;
		}

		IStatus status = null;
		if (e != null) {
			status = new Status(level, Activator.PLUGIN_ID, message);
		} else {
			status = new Status(level, Activator.PLUGIN_ID, message, e);
		}
		iLog.log(status);

		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss ");
		String msg = sdf.format(new Date()) + "[" + head + "] " + "<" + Thread.currentThread().getName() + "> " + "(" + name + "):" + message;
		System.out.println(msg);
		if (e != null) {
			e.printStackTrace(System.out);
			System.out.println();
		}

	}
}
