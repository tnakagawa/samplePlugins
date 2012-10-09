package standard.osgi.template;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;
import org.eclipse.pde.ui.templates.OptionTemplateSection;
import org.osgi.framework.Constants;

public class PluginContentWizard extends NewPluginTemplateWizard {

	/** Import Packages */
	private static final String[] IMPORT_PACKAGES = { "org.osgi.framework;version=\"1.3.0\"", };
	
	/** �Ώۃv���W�F�N�g */
	private IProject project = null;

	/** ���O */
	private LogUtil log = new LogUtil();

	@Override
	public ITemplateSection[] createTemplateSections() {
		return new ITemplateSection[] { new PluginContentSection() };
	}

	@Override
	public String[] getImportPackages() {
		return IMPORT_PACKAGES;
	}

	@Override
	public void dispose() {
		log.debug("PluginContentWizard#dispose>BEGIN:" + project);
		try {
			if (project != null) {
				// �v���W�F�N�g�X�V
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
				// �}�j�t�F�X�g�t�@�C���擾
				IFile file = null;
				file = project.getFile("META-INF/MANIFEST.MF");
				// �}�j�t�F�X�g�t�@�C���X�V
				file.refreshLocal(IResource.DEPTH_ZERO, null);
				log.debug("PluginContentWizard#dispose>IFile:" + file);

				// �}�j�t�F�X�g�擾
				Manifest manifest = new Manifest(file.getContents());
				log.debug("PluginContentWizard#dispose>Manifest:" + manifest);

				// ���C���A�g���r���[�g�擾
				Attributes attr = manifest.getMainAttributes();
				String importPackage = "org.osgi.framework";
				
				OptionTemplateSection section = (OptionTemplateSection) getTemplateSections()[0];
				
				// �T�[�r�X�擾����
				log.debug("PluginContentWizard#dispose>section(servflg):" + section.getBooleanOption("servflg"));
				if (section.getBooleanOption("servflg")) {
					// �T�[�r�X�擾����Ȃ̂ŁAImportPackage�C��
					importPackage += ",org.osgi.util.tracker";
					
					// ���O�T�[�r�X�擾����
					if (section.getBooleanOption("log")) {
						log.debug("PluginContentWizard#dispose>section(log):" + section.getBooleanOption("log"));
						importPackage += ",org.osgi.service.log";
					}

					// HTTP�T�[�r�X�擾����
					if (section.getBooleanOption("http")) {
						log.debug("PluginContentWizard#dispose>section(http):" + section.getBooleanOption("http"));
						importPackage += ",javax.servlet";
						importPackage += ",javax.servlet.http";
						importPackage += ",org.osgi.service.http";
					}

					// �C�x���g�A�h�~���T�[�r�X�擾����
					if (section.getBooleanOption("event")) {
						log.debug("PluginContentWizard#dispose>section(event):" + section.getBooleanOption("event"));
						importPackage += ",org.osgi.service.event";
					}

					// �R���t�B�O�A�h�~���T�[�r�X�擾����
					if (section.getBooleanOption("config")) {
						log.debug("PluginContentWizard#dispose>section(config):" + section.getBooleanOption("config"));
						importPackage += ",org.osgi.service.cm";
					}
				}

				log.debug("PluginContentWizard#dispose>ImportPackage:" + importPackage);
				attr.putValue(Constants.IMPORT_PACKAGE, importPackage);

				// �}�j�t�F�X�g�t�@�C���o��
				OutputStream os = new FileOutputStream(file.getRawLocation().toFile());
				manifest.write(os);
				os.flush();
				os.close();
				// �}�j�t�F�X�g�t�@�C���X�V
				file.refreshLocal(IResource.DEPTH_ZERO, null);
				// �v���W�F�N�g�X�V
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
			
		} catch (Exception e) {
			// ��O�̏ꍇ
			log.error("PluginContentWizard#dispose>Exception:" + e.getMessage(), e);
		}
		project = null;
		super.dispose();
		log.debug("PluginContentWizard#dispose>END");
	}

	@Override
	public boolean performFinish(IProject project, IPluginModelBase model, IProgressMonitor monitor) {
		log.debug("PluginContentWizard#performFinish>BEGIN:" + project);
		this.project = project;
		boolean ret = super.performFinish(project, model, monitor);
		log.debug("PluginContentWizard#performFinish>END:" + ret);
		return ret;
	}
}
