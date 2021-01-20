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
public class VTVQuestionAnswerResponse extends AbstractResponseMessage {
	public String detail;
        public String errMess;
	public void setFailure(String errorMsg) {
		this.mCode = ResponseCode.FAILURE;
		this.errMess = errorMsg;
	}

	public void setSuccess(String d){
		detail = d;
		mCode = ResponseCode.SUCCESS;
	}
	public IResponseMessage createNew() {
		return new VTVQuestionAnswerResponse();
	}
}
