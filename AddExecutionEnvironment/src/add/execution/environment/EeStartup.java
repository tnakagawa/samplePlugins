package add.execution.environment;


import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.IStartup;

/**
 * JRE�̃f�t�H���g�l��ύX����ׂ̃X�^�[�g�A�b�v
 * 
 * @author tnakagawa
 */
public class EeStartup implements IStartup {
	
	/** �L�[�i����j */
	private static final String KEY_FIRST = "first";
	
	/** �f�t�H���g�ݒ�VM���� */
	private static final String TARGET_VM_NAME = "";
	
	/** ���O */
	private LogUtil log = new LogUtil();

	@Override
	public void earlyStartup() {
		log.debug("EeStartup#earlyStartup>BEGIN");
		try {
			// �v���t�@�����X�擾
			IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
			if (preferences != null) {
				boolean first = preferences.getBoolean(KEY_FIRST, true);
				log.debug("EeStartup#earlyStartup>FIRST:" + first);
				if (first) {
					IVMInstall install = null;
					// JavaRuntime����A�C���X�g�[���^�C�v�ꗗ�擾
					IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
					if (types != null) {
						// �C���X�g�[���^�C�v�ꗗ�����[�v
						for (int i = 0; i < types.length; i++) {
							if (types[i] != null) {
								log.debug("EeStartup#earlyStartup>types[" + i + "]:" + types[i].getName());
								// �C���X�g�[���^�C�v����A�C���X�g�[���ꗗ�擾
								IVMInstall[] installs = types[i].getVMInstalls();
								if (installs != null) {
									// �C���X�g�[���ꗗ�����[�v
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
					// �Ώ۔���
					if (install != null) {
						// �f�t�H���g�ɐݒ�
						JavaRuntime.setDefaultVMInstall(install, null);
						// �ۑ�
						JavaRuntime.saveVMConfiguration();
						// ������U�ɐݒ�
						preferences.putBoolean(KEY_FIRST, false);
						// �ۑ�
						preferences.flush();
					}
				}
			}
		} catch(Exception e) {
			// ��O
			log.error("EeStartup#earlyStartup>Exception:" + e.getMessage(), e);
		}
		log.debug("EeStartup#earlyStartup>END");
	}

}
