package $packageName$;
%if servflg
%if http

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;
%endif
%endif

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
%if servflg
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
%if config
import org.osgi.service.cm.ConfigurationAdmin;
%endif
%if event
import org.osgi.service.event.EventAdmin;
%endif
%if http
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
%endif
%if log
import org.osgi.service.log.LogService;
%endif
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
%endif

/**
 * �A�N�e�B�x�[�^
 * 
 * @author $user$
 * @since $date$
 */
%if servflg
public class $activator$ implements BundleActivator, ServiceTrackerCustomizer {
%else
public class $activator$ implements BundleActivator {
%endif
	
	/** �o���h���R���e�L�X�g */
	private BundleContext bundleContext = null;
%if servflg
%if log

	/** �T�[�r�X�g���b�J�[�i���O�T�[�r�X�p�j */
	private ServiceTracker logServiceTracker = null;
	
	/** ���O�T�[�r�X */
	private LogService logService = null;
%endif
%if http

	/** �T�[�r�X�g���b�J�[�iHTTP�T�[�r�X�p�j */
	private ServiceTracker httpServiceTracker = null;
%endif
%if event

	/** �T�[�r�X�g���b�J�[�i�C�x���g�A�h�~���p�j */
	private ServiceTracker eventAdminTracker = null;

	/** �C�x���g�A�h�~�� */
	private EventAdmin eventAdmin = null;
%endif
%if config

	/** �T�[�r�X�g���b�J�[�i�R���t�B�O�A�h�~���p�j */
	private ServiceTracker configurationAdminTracker = null;
	
	/** �R���t�B�O�A�h�~�� */
	private ConfigurationAdmin configurationAdmin = null;
%endif
%endif
	
	/**
	 * @see BundleActivator#start(BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		// �o���h���R���e�L�X�g�ݒ�
		this.bundleContext = bundleContext;
%if servflg
%if log

		// ���O�T�[�r�X�g���b�J�[�J�n
		logServiceTracker = new ServiceTracker(bundleContext, LogService.class.getName(), this);
		logServiceTracker.open();
%endif
%if http

		// HTTP�T�[�r�X�g���b�J�[�J�n
		httpServiceTracker = new ServiceTracker(bundleContext, HttpService.class.getName(), this);
		httpServiceTracker.open();
%endif
%if event
		
		// �C�x���g�A�h�~���g���b�J�[�J�n
		eventAdminTracker = new ServiceTracker(bundleContext, EventAdmin.class.getName(), this);
		eventAdminTracker.open();
%endif
%if config

		// �R���t�B�O�A�h�~���g���b�J�[�J�n
		configurationAdminTracker = new ServiceTracker(bundleContext, ConfigurationAdmin.class.getName(), this);
		configurationAdminTracker.open();
%endif
%endif

		// ��������J�n�������L�q���Ă��������B
	}

	/**
	 * @see BundleActivator#stop(BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
%if servflg
%if config

		// �R���t�B�O�A�h�~���g���b�J�[��~
		configurationAdminTracker.close();
%endif
%if event
		
		// �C�x���g�A�h�~���g���b�J�[��~
		eventAdminTracker.close();
%endif
%if http
		
		// HTTP�T�[�r�X�g���b�J�[��~
		httpServiceTracker.close();
%endif
%if log

		// ���O�T�[�r�X�g���b�J�[��~
		logServiceTracker.close();
%endif
%endif

		// ���������~�������L�q���Ă��������B
	}
%if servflg

	/**
	 * @see ServiceTrackerCustomizer#addingService(ServiceReference)
	 */
	public Object addingService(ServiceReference reference) {
		// �Ԃ�l�p
		Object service = null;
		// ���t�@�����X�ɑ�����N���X���𒊏o
		String[] objectClass = (String[]) reference.getProperty(Constants.OBJECTCLASS);
		// �N���X�����`�F�b�N
		for (int i = 0; i < objectClass.length; i++) {
			// �N���X���ɊY������T�[�r�X�����邩����
%if log
			$logIf$ (LogService.class.getName().equals(objectClass[i])) {
				// ���O�T�[�r�X�̏ꍇ
				if (logService == null) {
					// ���O�T�[�r�X���擾���Ă��Ȃ������ꍇ�A�擾���ݒ�
					logService = (LogService) bundleContext.getService(reference);
					service = logService;
				}
				break;
%endif
%if http
			$httpIf$ (HttpService.class.getName().equals(objectClass[i])) {
				// HTTP�T�[�r�X�̏ꍇ
				HttpService httpService = (HttpService) bundleContext.getService(reference);
				service = httpService;
//				// HTTP�T�[�r�X�ɒǉ�����T�[�u���b�g�A���\�[�X��o�^
//				try {
//					// �T�[�u���b�g�o�^
//					String alias = null;// �G�C���A�X
//					Servlet servlet = null;// �T�[�u���b�g����
//					Dictionary initparams = new Hashtable();//�@�����p�����[�^
//					HttpContext context = httpService.createDefaultHttpContext();// HTTP�R���e�L�X�g
//					httpService.registerServlet(alias, servlet, initparams, context);
//					// ���\�[�X�o�^
//					String name = null;
//					httpService.registerResources(alias, name, context);
//				} catch (Exception e) {
//					// ���O�̓v���W�F�N�g�ɂ����ďo�͂��鎖
//					e.printStackTrace();
//				}
				break;
%endif
%if event
			$eventIf$ (EventAdmin.class.getName().equals(objectClass[i])) {
				// �C�x���g�A�h�~���̏ꍇ
				if (eventAdmin == null) {
					// �C�x���g�A�h�~�����擾���Ă��Ȃ������ꍇ�A�擾���ݒ�
					eventAdmin = (EventAdmin) bundleContext.getService(reference);
					service = eventAdmin;
				}
				break;
%endif
%if config
			$configIf$ (ConfigurationAdmin.class.getName().equals(objectClass[i])) {
				// �R���t�B�O�A�h�~���̏ꍇ
				if (configurationAdmin == null) {
					// �R���t�B�O�A�h�~�����擾���Ă��Ȃ������ꍇ�A�擾���ݒ�
					configurationAdmin = (ConfigurationAdmin) bundleContext.getService(reference);
					service = configurationAdmin;
				}
				break;
%endif
			}
		}
		return service;
	}

	/**
	 * @see ServiceTrackerCustomizer#addingService(ServiceReference)
	 */
	public void modifiedService(ServiceReference reference, Object service) {
		// �����͍l�����Ȃ�
	}

	/**
	 * @see ServiceTrackerCustomizer#addingService(ServiceReference)
	 */
	public void removedService(ServiceReference reference, Object service) {
		// ���t�@�����X�ɑ�����N���X���𒊏o
		String[] objectClass = (String[]) reference.getProperty(Constants.OBJECTCLASS);
		// �N���X����S�ă`�F�b�N
		for (int i = 0; i < objectClass.length; i++) {
			// �N���X���ɊY������T�[�r�X�����邩����
%if log
			$logIf$ (LogService.class.getName().equals(objectClass[i])) {
				// ���O�T�[�r�X�̏ꍇ
				if (logService != null) {
					// ���O�T�[�r�X���ݒ肳��Ă��ꍇ�Anull��ݒ�
					logService = null;
				}
				break;
%endif
%if http
			$httpIf$ (HttpService.class.getName().equals(objectClass[i])) {
				// HTTP�T�[�r�X�̏ꍇ
				// ��{�I�ɓo�^�݂̂Ȃ̂ŏ����Ȃ�
				break;
%endif
%if event
			$eventIf$ (EventAdmin.class.getName().equals(objectClass[i])) {
				// �C�x���g�A�h�~���̏ꍇ
				if (eventAdmin != null) {
					// �C�x���g�A�h�~�����ݒ肳��Ă��ꍇ�Anull��ݒ�
					eventAdmin = null;
				}
				break;
%endif
%if config
			$configIf$ (ConfigurationAdmin.class.getName().equals(objectClass[i])) {
				// �R���t�B�O�A�h�~���̏ꍇ
				if (configurationAdmin != null) {
					// �R���t�B�O�A�h�~�����ݒ肳��Ă��ꍇ�Anull��ݒ�
					configurationAdmin = null;
				}
				break;
%endif
			}
		}
	}
%endif

	/**
	 * �o���h���R���e�L�X�g�擾
	 * 
	 * @return �o���h���R���e�L�X�g
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}
%if servflg
%if log

	/**
	 * ���O�T�[�r�X�擾
	 * 
	 * @return ���O�T�[�r�X
	 */
	public LogService getLogService() {
		return logService;
	}
%endif
%if event

	/**
	 * �C�x���g�A�h�~���擾
	 * 
	 * @return �C�x���g�A�h�~��
	 */
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}
%endif
%if config

	/**
	 * �R���t�B�O�A�h�~���擾
	 * 
	 * @return �R���t�B�O�A�h�~��
	 */
	public ConfigurationAdmin getConfigurationAdmin() {
		return configurationAdmin;
	}
%endif
%endif
}
