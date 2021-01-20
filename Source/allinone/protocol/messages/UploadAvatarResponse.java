package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class UploadAvatarResponse extends AbstractResponseMessage {
	public String mErrorMsg;
        public long fileId;
        public String value;
	public void setSuccess(int aCode) {
		mCode = aCode;
	}
        
        public void setSuccess(String value) {
		mCode = ResponseCode.SUCCESS;
                this.value = value;
	}

	public void setFailure(int aCode, String aErrorMsg) {
		mCode = aCode;
		mErrorMsg = aErrorMsg;
	}

	public IResponseMessage createNew() {
		return new UploadAvatarResponse();
	}
}
