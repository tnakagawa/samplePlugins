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
 * ターゲット・プラットフォーム追加スタートアップ
 * 
 * @author tnakagawa
 */
public class TargetPlatformStartup implements IStartup, ServiceTrackerCustomizer<ITargetPlatformService	, ITargetPlatformService> {
	
	private static final String TARGET_PLATFORM_NAME = "osgi4.2";

	/** ログ */
	private LogUtil log = new LogUtil();

	/** バンドルコンテキスト */
	private BundleContext bundleContext = null;
	
	private ServiceTracker<ITargetPlatformService, ITargetPlatformService> serviceTracker = null;
	
	@Override
	public void earlyStartup() {
		log.debug("TargetPlatformStartup#earlyStartup>BEGIN");
		// バンドルコンテキストの有無判定
		if (bundleContext == null) {
			bundleContext = Activator.getDefault().getBundle().getBundleContext();
		}

		// サービストラッカー生成
		serviceTracker = new ServiceTracker<ITargetPlatformService, ITargetPlatformService>(bundleContext, ITargetPlatformService.class, this);
		
		// サービストラッカー開始
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
					// 追加フラグ＝真
					boolean add = true;
					// ターゲット一覧取得
					ITargetHandle[] handles = service.getTargets(null);
					// ターゲット一覧分ループ
					for (int i = 0; i < handles.length; i++) {
						// 名前取得
						String name = handles[i].getTargetDefinition().getName();
						log.debug("TargetPlatformStartup#addingService>handles[" + i + "]:" + name);
						// 名前が追加するターゲット・プラットフォームと一致するか確認
						if (TARGET_PLATFORM_NAME.equals(name)) {
							// 一致したので、追加フラグ＝偽
							add = false;
							log.debug("TargetPlatformStartup#addingService>already installed:" + name);
						}
					}
					// 追加判定
					if (add) {
						// 新ターゲット・プラットフォーム生成
						ITargetDefinition targetDefinition = service.newTarget();
						// ターゲット・プラットフォームの名前設定
						targetDefinition.setName(TARGET_PLATFORM_NAME);
						// フォルダURL取得
						URL url = Activator.getDefault().getBundle().getEntry("/targetPlatform/osgi_4_2");
						log.debug("TargetPlatformStartup#addingService>URL:" + url);
						// フォルダパス取得
						String path = FileLocator.resolve(url).getPath();
						log.debug("TargetPlatformStartup#addingService>path:" + path);
						// ターゲット・プラットフォームのロケーション生成
						ITargetLocation location = service.newDirectoryLocation(path);
						// ターゲット・プラットフォームのロケーション設定
						targetDefinition.setTargetLocations(new ITargetLocation[] { location });
						// ターゲット・プラットフォーム保存
						service.saveTargetDefinition(targetDefinition);
						// ターゲット・プラットフォームをデフォルトに設定
						LoadTargetDefinitionJob.load(targetDefinition);
					}
					
					// サービストラッカー停止
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
