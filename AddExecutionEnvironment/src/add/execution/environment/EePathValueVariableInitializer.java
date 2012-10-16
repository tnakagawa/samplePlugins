package add.execution.environment;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.IValueVariableInitializer;

/**
 * 定義ファイルの基本パスを返す。
 * 
 * @author tnakagawa
 */
public class EePathValueVariableInitializer implements IValueVariableInitializer {

	/** ログ */
	private LogUtil log = new LogUtil();

	@Override
	public void initialize(IValueVariable variable) {
		log.debug("EePathValueVariableInitializer#initialize>BEGIN:" + variable);
		
		// 設定値初期化
		String value = null;
		
		// null判定
		if (variable != null) {
			
			
			try {
				// フォルダURL取得
				URL url = Activator.getDefault().getBundle().getEntry("/ee");
				log.debug("EePathValueVariableInitializer#initialize>URL:" + url);
				// フォルダパス取得
				value = FileLocator.resolve(url).getPath();
				log.debug("EePathValueVariableInitializer#initialize>value:" + value);
			} catch(Exception e) {
				log.error("EePathValueVariableInitializer#initialize>Exception:" + e.getMessage(), e);
			}
			
			// 値設定
			variable.setValue(value);
		}
		
		log.debug("EePathValueVariableInitializer#initialize>END");
	}

}
