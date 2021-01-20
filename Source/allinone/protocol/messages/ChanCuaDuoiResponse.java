package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class ChanCuaDuoiResponse extends AbstractResponseMessage {
	public String errMess;
	public long uid;
	public long next_uid;
	public void setSuccess(int aCode, long id , long nid) {
		mCode = aCode;
		uid = id;
		next_uid = nid;
	}

	public void setFailure(int aCode, String m) {
		mCode = aCode;
		errMess = m;
	}

	public IResponseMessage createNew() {
		return new ChanCuaDuoiResponse();
	}
}