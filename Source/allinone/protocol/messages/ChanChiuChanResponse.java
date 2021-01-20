package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ChanChiuChanResponse extends AbstractResponseMessage {
	public String errMess;
	public long uid;
	public long destID;
	public void setSuccess(long id,long d) {
		mCode = ResponseCode.SUCCESS;
		uid = id;
		destID = d;
	}

	public void setFailure(String m) {
		mCode = ResponseCode.FAILURE;
		errMess = m;
	}

	public IResponseMessage createNew() {
		return new ChanChiuChanResponse();
	}
}