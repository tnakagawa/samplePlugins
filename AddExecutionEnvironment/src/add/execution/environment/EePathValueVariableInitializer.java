package add.execution.environment;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.IValueVariableInitializer;

/**
 * ��`�t�@�C���̊�{�p�X��Ԃ��B
 * 
 * @author tnakagawa
 */
public class EePathValueVariableInitializer implements IValueVariableInitializer {

	/** ���O */
	private LogUtil log = new LogUtil();

	@Override
	public void initialize(IValueVariable variable) {
		log.debug("EePathValueVariableInitializer#initialize>BEGIN:" + variable);
		
		// �ݒ�l������
		String value = null;
		
		// null����
		if (variable != null) {
			
			
			try {
				// �t�H���_URL�擾
				URL url = Activator.getDefault().getBundle().getEntry("/ee");
				log.debug("EePathValueVariableInitializer#initialize>URL:" + url);
				// �t�H���_�p�X�擾
				value = FileLocator.resolve(url).getPath();
				log.debug("EePathValueVariableInitializer#initialize>value:" + value);
			} catch(Exception e) {
				log.error("EePathValueVariableInitializer#initialize>Exception:" + e.getMessage(), e);
			}
			
			// �l�ݒ�
			variable.setValue(value);
		}
		
		log.debug("EePathValueVariableInitializer#initialize>END");
	}

}
