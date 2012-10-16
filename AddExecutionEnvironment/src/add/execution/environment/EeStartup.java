package add.execution.environment;


import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.IStartup;

/**
 * JREのデフォルト値を変更する為のスタートアップ
 * 
 * @author tnakagawa
 */
public class EeStartup implements IStartup {
	
	/** キー（初回） */
	private static final String KEY_FIRST = "first";
	
	/** デフォルト設定VM名称 */
	private static final String TARGET_VM_NAME = "";
	
	/** ログ */
	private LogUtil log = new LogUtil();

	@Override
	public void earlyStartup() {
		log.debug("EeStartup#earlyStartup>BEGIN");
		try {
			// プレファレンス取得
			IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
			if (preferences != null) {
				boolean first = preferences.getBoolean(KEY_FIRST, true);
				log.debug("EeStartup#earlyStartup>FIRST:" + first);
				if (first) {
					IVMInstall install = null;
					// JavaRuntimeから、インストールタイプ一覧取得
					IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
					if (types != null) {
						// インストールタイプ一覧分ループ
						for (int i = 0; i < types.length; i++) {
							if (types[i] != null) {
								log.debug("EeStartup#earlyStartup>types[" + i + "]:" + types[i].getName());
								// インストールタイプから、インストール一覧取得
								IVMInstall[] installs = types[i].getVMInstalls();
								if (installs != null) {
									// インストール一覧分ループ
									for (int j = 0; j < installs.length; j++) {
										log.debug("EeStartup#earlyStartup>types[" + i + "," + j + "]:" + installs[j].getName());
										if (TARGET_VM_NAME.equals(installs[j].getName())) {
											log.debug("EeStartup#earlyStartup>Default");
											install = installs[j];
//											break;
										}
									}
//									if (install != null) {
//										break;
//									}
								}
							}
						}
					}
					// 対象判定
					if (install != null) {
						// デフォルトに設定
						JavaRuntime.setDefaultVMInstall(install, null);
						// 保存
						JavaRuntime.saveVMConfiguration();
						// 初回を偽に設定
						preferences.putBoolean(KEY_FIRST, false);
						// 保存
						preferences.flush();
					}
				}
			}
		} catch(Exception e) {
			// 例外
			log.error("EeStartup#earlyStartup>Exception:" + e.getMessage(), e);
		}
		log.debug("EeStartup#earlyStartup>END");
	}

}
