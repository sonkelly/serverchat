package allinone.business;

import vn.game.protocol.BusinessProperties;
import vn.game.protocol.IBusinessPropertiesFactory;

public class SimpleBusinessPropertiesFactory implements IBusinessPropertiesFactory{
	@Override
	public BusinessProperties createBusinessProperties() {
		
		return new SimpleBusinessProperties();
	}
}
