package standard.osgi.target.platform;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * ���O���[�e�B���e�B
 * 
 * @author tnakagawa
 */
public class LogUtil {

	/** ���O���x���i�f�o�b�O�j */
	private static final int DEBUG = IStatus.OK;

	/** ���O���x���i�G���[�j */
	private static final int ERROR = IStatus.ERROR;

	/** ���O���x���i�C���t�H�j */
	private static final int INFO = IStatus.INFO;

	/** ���O���x���i���[�j���O�j */
	private static final int WARN = IStatus.WARNING;

	/** Eclipse�p���O */
	private ILog iLog = null;

	/** �f�o�b�O�o�̓t���O */
	private boolean debugFlg = false;

	/** �v���O�C���� */
	private String name = null;

	/**
	 * �R���X�g���N�^
	 */
	public LogUtil() {
		iLog = Activator.getDefault().getLog();
		name = Activator.PLUGIN_ID;
		debugFlg = "true".equalsIgnoreCase(Platform.getDebugOption(name + "/debug"));
	}

	/**
	 * �f�o�b�O
	 * 
	 * @param message ���b�Z�[�W
	 */
	public void debug(String message) {
		debug(message, null);
	}

	/**
	 * �f�o�b�O
	 * 
	 * @param message ���b�Z�[�W
	 * @param e ��O
	 */
	public void debug(String message, Throwable e) {
		if (debugFlg) {
			writeLog(DEBUG, message, e);
		}
	}

	/**
	 * �G���[
	 * 
	 * @param message ���b�Z�[�W
	 */
	public void error(String message) {
		error(message, null);
	}

	/**
	 * �G���[
	 * 
	 * @param message ���b�Z�[�W
	 * @param e ��O
	 */
	public void error(String message, Throwable e) {
		writeLog(ERROR, message, e);
	}

	/**
	 * �C���t�H
	 * 
	 * @param message ���b�Z�[�W
	 */
	public void info(String message) {
		info(message, null);
	}

	/**
	 * �C���t�H
	 * 
	 * @param message ���b�Z�[�W
	 * @param e ��O
	 */
	public void info(String message, Throwable e) {
		writeLog(INFO, message, e);
	}

	/**
	 * ���[�j���O
	 * 
	 * @param message ���b�Z�[�W
	 */
	public void warn(String message) {
		warn(message, null);
	}

	/**
	 * ���[�j���O
	 * 
	 * @param message ���b�Z�[�W
	 * @param e ��O
	 */
	public void warn(String message, Throwable e) {
		writeLog(WARN, message, e);
	}

	/**
	 * ���O�L�q
	 * 
	 * @param level ���O���x��
	 * @param message ���b�Z�[�W
	 * @param e ��O
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
