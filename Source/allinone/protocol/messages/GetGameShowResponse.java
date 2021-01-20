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
public class GetGameShowResponse extends AbstractResponseMessage {
	
	public String value;
        
         public void setSuccess(String v){
            value = v;
            mCode = ResponseCode.SUCCESS;
        }
        public IResponseMessage createNew()
        {
            return new GetGameShowResponse();
        }
}
