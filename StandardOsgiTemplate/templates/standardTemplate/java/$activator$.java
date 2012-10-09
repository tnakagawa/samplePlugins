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
 * アクティベータ
 * 
 * @author $user$
 * @since $date$
 */
%if servflg
public class $activator$ implements BundleActivator, ServiceTrackerCustomizer {
%else
public class $activator$ implements BundleActivator {
%endif
	
	/** バンドルコンテキスト */
	private BundleContext bundleContext = null;
%if servflg
%if log

	/** サービストラッカー（ログサービス用） */
	private ServiceTracker logServiceTracker = null;
	
	/** ログサービス */
	private LogService logService = null;
%endif
%if http

	/** サービストラッカー（HTTPサービス用） */
	private ServiceTracker httpServiceTracker = null;
%endif
%if event

	/** サービストラッカー（イベントアドミン用） */
	private ServiceTracker eventAdminTracker = null;

	/** イベントアドミン */
	private EventAdmin eventAdmin = null;
%endif
%if config

	/** サービストラッカー（コンフィグアドミン用） */
	private ServiceTracker configurationAdminTracker = null;
	
	/** コンフィグアドミン */
	private ConfigurationAdmin configurationAdmin = null;
%endif
%endif
	
	/**
	 * @see BundleActivator#start(BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		// バンドルコンテキスト設定
		this.bundleContext = bundleContext;
%if servflg
%if log

		// ログサービストラッカー開始
		logServiceTracker = new ServiceTracker(bundleContext, LogService.class.getName(), this);
		logServiceTracker.open();
%endif
%if http

		// HTTPサービストラッカー開始
		httpServiceTracker = new ServiceTracker(bundleContext, HttpService.class.getName(), this);
		httpServiceTracker.open();
%endif
%if event
		
		// イベントアドミントラッカー開始
		eventAdminTracker = new ServiceTracker(bundleContext, EventAdmin.class.getName(), this);
		eventAdminTracker.open();
%endif
%if config

		// コンフィグアドミントラッカー開始
		configurationAdminTracker = new ServiceTracker(bundleContext, ConfigurationAdmin.class.getName(), this);
		configurationAdminTracker.open();
%endif
%endif

		// ここから開始処理を記述してください。
	}

	/**
	 * @see BundleActivator#stop(BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
%if servflg
%if config

		// コンフィグアドミントラッカー停止
		configurationAdminTracker.close();
%endif
%if event
		
		// イベントアドミントラッカー停止
		eventAdminTracker.close();
%endif
%if http
		
		// HTTPサービストラッカー停止
		httpServiceTracker.close();
%endif
%if log

		// ログサービストラッカー停止
		logServiceTracker.close();
%endif
%endif

		// ここから停止処理を記述してください。
	}
%if servflg

	/**
	 * @see ServiceTrackerCustomizer#addingService(ServiceReference)
	 */
	public Object addingService(ServiceReference reference) {
		// 返り値用
		Object service = null;
		// リファレンスに属するクラス名を抽出
		String[] objectClass = (String[]) reference.getProperty(Constants.OBJECTCLASS);
		// クラス名をチェック
		for (int i = 0; i < objectClass.length; i++) {
			// クラス名に該当するサービスがあるか判別
%if log
			$logIf$ (LogService.class.getName().equals(objectClass[i])) {
				// ログサービスの場合
				if (logService == null) {
					// ログサービスを取得していなかった場合、取得＆設定
					logService = (LogService) bundleContext.getService(reference);
					service = logService;
				}
				break;
%endif
%if http
			$httpIf$ (HttpService.class.getName().equals(objectClass[i])) {
				// HTTPサービスの場合
				HttpService httpService = (HttpService) bundleContext.getService(reference);
				service = httpService;
//				// HTTPサービスに追加するサーブレット、リソースを登録
//				try {
//					// サーブレット登録
//					String alias = null;// エイリアス
//					Servlet servlet = null;// サーブレット実態
//					Dictionary initparams = new Hashtable();//　初期パラメータ
//					HttpContext context = httpService.createDefaultHttpContext();// HTTPコンテキスト
//					httpService.registerServlet(alias, servlet, initparams, context);
//					// リソース登録
//					String name = null;
//					httpService.registerResources(alias, name, context);
//				} catch (Exception e) {
//					// ログはプロジェクトにそって出力する事
//					e.printStackTrace();
//				}
				break;
%endif
%if event
			$eventIf$ (EventAdmin.class.getName().equals(objectClass[i])) {
				// イベントアドミンの場合
				if (eventAdmin == null) {
					// イベントアドミンを取得していなかった場合、取得＆設定
					eventAdmin = (EventAdmin) bundleContext.getService(reference);
					service = eventAdmin;
				}
				break;
%endif
%if config
			$configIf$ (ConfigurationAdmin.class.getName().equals(objectClass[i])) {
				// コンフィグアドミンの場合
				if (configurationAdmin == null) {
					// コンフィグアドミンを取得していなかった場合、取得＆設定
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
		// ここは考慮しない
	}

	/**
	 * @see ServiceTrackerCustomizer#addingService(ServiceReference)
	 */
	public void removedService(ServiceReference reference, Object service) {
		// リファレンスに属するクラス名を抽出
		String[] objectClass = (String[]) reference.getProperty(Constants.OBJECTCLASS);
		// クラス名を全てチェック
		for (int i = 0; i < objectClass.length; i++) {
			// クラス名に該当するサービスがあるか判別
%if log
			$logIf$ (LogService.class.getName().equals(objectClass[i])) {
				// ログサービスの場合
				if (logService != null) {
					// ログサービスが設定されてた場合、nullを設定
					logService = null;
				}
				break;
%endif
%if http
			$httpIf$ (HttpService.class.getName().equals(objectClass[i])) {
				// HTTPサービスの場合
				// 基本的に登録のみなので処理なし
				break;
%endif
%if event
			$eventIf$ (EventAdmin.class.getName().equals(objectClass[i])) {
				// イベントアドミンの場合
				if (eventAdmin != null) {
					// イベントアドミンが設定されてた場合、nullを設定
					eventAdmin = null;
				}
				break;
%endif
%if config
			$configIf$ (ConfigurationAdmin.class.getName().equals(objectClass[i])) {
				// コンフィグアドミンの場合
				if (configurationAdmin != null) {
					// コンフィグアドミンが設定されてた場合、nullを設定
					configurationAdmin = null;
				}
				break;
%endif
			}
		}
	}
%endif

	/**
	 * バンドルコンテキスト取得
	 * 
	 * @return バンドルコンテキスト
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}
%if servflg
%if log

	/**
	 * ログサービス取得
	 * 
	 * @return ログサービス
	 */
	public LogService getLogService() {
		return logService;
	}
%endif
%if event

	/**
	 * イベントアドミン取得
	 * 
	 * @return イベントアドミン
	 */
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}
%endif
%if config

	/**
	 * コンフィグアドミン取得
	 * 
	 * @return コンフィグアドミン
	 */
	public ConfigurationAdmin getConfigurationAdmin() {
		return configurationAdmin;
	}
%endif
%endif
}
