package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ChanDanhChanResponse extends AbstractResponseMessage {
	public String errMess;
	public long uid;
	public int card;
	public long nextID;
	public long destID;// Tra chiu
	public long chiuUid = 0;
	public int errorCode;
	public void setSuccess(long id, int c, long nID, long cUID, long dID, int code) {
		mCode = ResponseCode.SUCCESS;
		uid = id;
		card = c;
		nextID = nID;
		chiuUid = cUID;
		destID = dID;
		errorCode = code;
	}
	/*public void setSuccess(long id, int c, long nID, long cUid,)*/
	/*public void traCua(long uid){
		destID = uid;
		isTraChiu = true;
	}*/
	public void setFailure(String m) {
		mCode = ResponseCode.FAILURE;
		errMess = m;
	}

	public IResponseMessage createNew() {
		return new ChanDanhChanResponse();
	}
}