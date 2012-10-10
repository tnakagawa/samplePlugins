package standard.osgi.target.platform;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;

import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.LoadTargetDefinitionJob;

import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * �^�[�Q�b�g�E�v���b�g�t�H�[���ǉ��X�^�[�g�A�b�v
 * 
 * @author tnakagawa
 */
public class TargetPlatformStartup implements IStartup, ServiceTrackerCustomizer<ITargetPlatformService	, ITargetPlatformService> {
	
	private static final String TARGET_PLATFORM_NAME = "osgi4.2";

	/** ���O */
	private LogUtil log = new LogUtil();

	/** �o���h���R���e�L�X�g */
	private BundleContext bundleContext = null;
	
	private ServiceTracker<ITargetPlatformService, ITargetPlatformService> serviceTracker = null;
	
	@Override
	public void earlyStartup() {
		log.debug("TargetPlatformStartup#earlyStartup>BEGIN");
		// �o���h���R���e�L�X�g�̗L������
		if (bundleContext == null) {
			bundleContext = Activator.getDefault().getBundle().getBundleContext();
		}

		// �T�[�r�X�g���b�J�[����
		serviceTracker = new ServiceTracker<ITargetPlatformService, ITargetPlatformService>(bundleContext, ITargetPlatformService.class, this);
		
		// �T�[�r�X�g���b�J�[�J�n
		serviceTracker.open();
		log.debug("TargetPlatformStartup#earlyStartup>END");
	}

	@Override
	public ITargetPlatformService addingService(ServiceReference<ITargetPlatformService> reference) {
		log.debug("TargetPlatformStartup#addingService>BEGIN");
		ITargetPlatformService service = null;
		if (bundleContext != null) {
			service = bundleContext.getService(reference);
			if (service != null) {
				try {
					// �ǉ��t���O���^
					boolean add = true;
					// �^�[�Q�b�g�ꗗ�擾
					ITargetHandle[] handles = service.getTargets(null);
					// �^�[�Q�b�g�ꗗ�����[�v
					for (int i = 0; i < handles.length; i++) {
						// ���O�擾
						String name = handles[i].getTargetDefinition().getName();
						log.debug("TargetPlatformStartup#addingService>handles[" + i + "]:" + name);
						// ���O���ǉ�����^�[�Q�b�g�E�v���b�g�t�H�[���ƈ�v���邩�m�F
						if (TARGET_PLATFORM_NAME.equals(name)) {
							// ��v�����̂ŁA�ǉ��t���O���U
							add = false;
							log.debug("TargetPlatformStartup#addingService>already installed:" + name);
						}
					}
					// �ǉ�����
					if (add) {
						// �V�^�[�Q�b�g�E�v���b�g�t�H�[������
						ITargetDefinition targetDefinition = service.newTarget();
						// �^�[�Q�b�g�E�v���b�g�t�H�[���̖��O�ݒ�
						targetDefinition.setName(TARGET_PLATFORM_NAME);
						// �t�H���_URL�擾
						URL url = Activator.getDefault().getBundle().getEntry("/targetPlatform/osgi_4_2");
						log.debug("TargetPlatformStartup#addingService>URL:" + url);
						// �t�H���_�p�X�擾
						String path = FileLocator.resolve(url).getPath();
						log.debug("TargetPlatformStartup#addingService>path:" + path);
						// �^�[�Q�b�g�E�v���b�g�t�H�[���̃��P�[�V��������
						ITargetLocation location = service.newDirectoryLocation(path);
						// �^�[�Q�b�g�E�v���b�g�t�H�[���̃��P�[�V�����ݒ�
						targetDefinition.setTargetLocations(new ITargetLocation[] { location });
						// �^�[�Q�b�g�E�v���b�g�t�H�[���ۑ�
						service.saveTargetDefinition(targetDefinition);
						// �^�[�Q�b�g�E�v���b�g�t�H�[�����f�t�H���g�ɐݒ�
						LoadTargetDefinitionJob.load(targetDefinition);
					}
					
					// �T�[�r�X�g���b�J�[��~
					serviceTracker.close();
				} catch (Exception e) {
					log.error("TargetPlatformStartup#addingService>Exception:" + e.getMessage(), e);
				}

			}
		}
		
		log.debug("TargetPlatformStartup#addingService>END");
		return service;
	}

	@Override
	public void modifiedService(ServiceReference<ITargetPlatformService> reference, ITargetPlatformService service) {
		log.debug("TargetPlatformStartup#modifiedService>BEGIN");
		log.debug("TargetPlatformStartup#modifiedService>END");
	}

	@Override
	public void removedService(ServiceReference<ITargetPlatformService> reference, ITargetPlatformService service) {
		log.debug("TargetPlatformStartup#removedService>BEGIN");
		log.debug("TargetPlatformStartup#removedService>END");
	}

}
