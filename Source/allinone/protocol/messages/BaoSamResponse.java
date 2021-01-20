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
public class BaoSamResponse extends AbstractResponseMessage {
	
	public String message;
	public long uid;
        public boolean hasBaoSam;
        public void setSuccess(long u,boolean hasBao)
        {
            mCode = ResponseCode.SUCCESS;
            uid = u;
            hasBaoSam = hasBao;
        }
        public void setFailure(String msg){
            mCode = ResponseCode.FAILURE;
            message = msg;
        }
        public IResponseMessage createNew()
        {
            return new BaoSamResponse();
        }
}
