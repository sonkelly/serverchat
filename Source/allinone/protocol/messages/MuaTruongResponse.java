package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class MuaTruongResponse extends AbstractResponseMessage {

	public String mErrorMsg;
	public long mUid;
	

	public void setFailure(int aCode, String aErrorMsg) {
		mCode = aCode;
		mErrorMsg = aErrorMsg;
	}

	public void setSuccess(int aCode, long uid) {
		mCode = aCode;
		mUid = uid;
	}

	public IResponseMessage createNew() {
		return new MuaTruongResponse();
	}
}
