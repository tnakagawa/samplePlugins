package standard.osgi.template;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.osgi.framework.Bundle;
import org.eclipse.pde.ui.templates.OptionTemplateSection;

public class PluginContentSection extends OptionTemplateSection {

	/** パッケージオプション */
	private static final String[][] PKG_OPTIONS = {
		{ "log", "org.osgi.service.log.LogService" },
		{ "http", "org.osgi.service.http.HttpService" },
		{ "event", "org.osgi.service.event.EventAdmin" },
		{ "config", "org.osgi.service.cm.ConfigurationAdmin" },
	};

	/** ログ */
	private LogUtil log = new LogUtil();

	/**
	 * コンストラクタ
	 */
	public PluginContentSection() {
		super();
		setPageCount(1);
		addOption("user", "開発者名", System.getenv("USERNAME"), 0);
		log.debug("PluginContentSection>addOption(user):" + getStringOption("user"));
		addBlankField(0);
		for (int i = 0; i < PKG_OPTIONS.length; i++) {
			addOption(PKG_OPTIONS[i][0], PKG_OPTIONS[i][1], false, 0);
			log.debug("PluginContentSection>addOption(" + PKG_OPTIONS[i][0] + "):" + getBooleanOption(PKG_OPTIONS[i][0]));
		}
	}

	@Override
	public void addPages(Wizard wizard) {
		log.debug("PluginContentSection#addPages>BEGIN");
		WizardPage page = createPage(0);
		page.setTitle("標準OSGiテンプレート");
		page.setDescription("バンドルで使用するサービスにチェックを入れてください。");
		wizard.addPage(page);
		markPagesAdded();
		log.debug("PluginContentSection#addPages>END");
	}

	@Override
	public void execute(IProject project, IPluginModelBase model, IProgressMonitor monitor) throws CoreException {
		log.debug("PluginContentSection#execute>BEGIN");
		boolean servflg = false;
		for (int i = 0; i < PKG_OPTIONS.length; i++) {
			if (getBooleanOption(PKG_OPTIONS[i][0])) {
				if (servflg) {
					addOption(PKG_OPTIONS[i][0] + "If", "", "} else if", 0);
				} else {
					servflg = true;
					addOption(PKG_OPTIONS[i][0] + "If", "", "if", 0);
				}
				log.debug("PluginContentSection#execute>addOption(" + PKG_OPTIONS[i][0] + "If" + "):" + getStringOption(PKG_OPTIONS[i][0] + "If"));
			}
		}
		addOption("servflg", "", servflg, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		addOption("date", "", sdf.format(new Date()), 0);

		super.execute(project, model, monitor);
		log.debug("PluginContentSection#execute>END");
	}

	@Override
	public IPluginReference[] getDependencies(String schemaVersion) {
		return new IPluginReference[0];
	}

	@Override
	public String[] getNewFiles() {
		return new String[0];
	}

	@Override
	public String getSectionId() {
		return "standardTemplate";
	}

	@Override
	public String getUsedExtensionPoint() {
		return null;
	}

	@Override
	protected URL getInstallURL() {
		return Activator.getDefault().getBundle().getEntry("/");
	}

	@Override
	protected ResourceBundle getPluginResourceBundle() {
		Bundle bundle = Platform.getBundle(Activator.getDefault().getBundle().getSymbolicName());
		return Platform.getResourceBundle(bundle);
	}

	@Override
	protected void updateModel(IProgressMonitor monitor) throws CoreException {
	}
}
