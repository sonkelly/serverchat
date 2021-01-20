package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;

public class GetRefContentResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public String _smsContent;
    public String _dialogueContent;
    public String _referenceCode;
    public String _urlReference;

    public GetRefContentResponse() {
    }

    public void setSuccess(int aCode, String sContent, String dContent) {
        mCode = aCode;
        _smsContent = sContent;
        _dialogueContent = dContent;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public vn.game.protocol.IResponseMessage createNew() {
        return new GetRefContentResponse();
    }
}
