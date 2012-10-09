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
	
	/** 対象プロジェクト */
	private IProject project = null;

	/** ログ */
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
				// プロジェクト更新
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
				// マニフェストファイル取得
				IFile file = null;
				file = project.getFile("META-INF/MANIFEST.MF");
				// マニフェストファイル更新
				file.refreshLocal(IResource.DEPTH_ZERO, null);
				log.debug("PluginContentWizard#dispose>IFile:" + file);

				// マニフェスト取得
				Manifest manifest = new Manifest(file.getContents());
				log.debug("PluginContentWizard#dispose>Manifest:" + manifest);

				// メインアトリビュート取得
				Attributes attr = manifest.getMainAttributes();
				String importPackage = "org.osgi.framework";
				
				OptionTemplateSection section = (OptionTemplateSection) getTemplateSections()[0];
				
				// サービス取得判定
				log.debug("PluginContentWizard#dispose>section(servflg):" + section.getBooleanOption("servflg"));
				if (section.getBooleanOption("servflg")) {
					// サービス取得ありなので、ImportPackage修正
					importPackage += ",org.osgi.util.tracker";
					
					// ログサービス取得判定
					if (section.getBooleanOption("log")) {
						log.debug("PluginContentWizard#dispose>section(log):" + section.getBooleanOption("log"));
						importPackage += ",org.osgi.service.log";
					}

					// HTTPサービス取得判定
					if (section.getBooleanOption("http")) {
						log.debug("PluginContentWizard#dispose>section(http):" + section.getBooleanOption("http"));
						importPackage += ",javax.servlet";
						importPackage += ",javax.servlet.http";
						importPackage += ",org.osgi.service.http";
					}

					// イベントアドミンサービス取得判定
					if (section.getBooleanOption("event")) {
						log.debug("PluginContentWizard#dispose>section(event):" + section.getBooleanOption("event"));
						importPackage += ",org.osgi.service.event";
					}

					// コンフィグアドミンサービス取得判定
					if (section.getBooleanOption("config")) {
						log.debug("PluginContentWizard#dispose>section(config):" + section.getBooleanOption("config"));
						importPackage += ",org.osgi.service.cm";
					}
				}

				log.debug("PluginContentWizard#dispose>ImportPackage:" + importPackage);
				attr.putValue(Constants.IMPORT_PACKAGE, importPackage);

				// マニフェストファイル出力
				OutputStream os = new FileOutputStream(file.getRawLocation().toFile());
				manifest.write(os);
				os.flush();
				os.close();
				// マニフェストファイル更新
				file.refreshLocal(IResource.DEPTH_ZERO, null);
				// プロジェクト更新
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
			
		} catch (Exception e) {
			// 例外の場合
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
