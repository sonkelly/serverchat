package allinone.business;

import vn.game.protocol.BusinessProperties;
import vn.game.protocol.IBusinessPropertiesFactory;

public class BacayBusinessPropertiesFactory implements IBusinessPropertiesFactory{
	@Override
	public BusinessProperties createBusinessProperties() {
		
		return new BacayBusinessProperties();
	}
}
