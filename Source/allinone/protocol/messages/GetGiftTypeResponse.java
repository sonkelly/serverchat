/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

/**
 *
 * @author Vostro 3450
 */
public class GetGiftTypeResponse extends AbstractResponseMessage {
        public String errMsg;
	public String value;
        
        public void setSuccess(String value) {
            mCode = ResponseCode.SUCCESS;
            this.value = value;
        }
	public void setFailure(String message)
        {
            mCode = ResponseCode.FAILURE;
            errMsg = message;
            
        }
@Override
	public IResponseMessage createNew() {
		return new GetGiftTypeResponse();
	}
}
