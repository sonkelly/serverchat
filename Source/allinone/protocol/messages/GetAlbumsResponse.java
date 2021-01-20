package allinone.protocol.messages;


import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AlbumEntity;
import allinone.data.ResponseCode;

public class GetAlbumsResponse extends AbstractResponseMessage {
        public String errMsg;
	public List<AlbumEntity> albums;
        

	public void setFailure(String message)
        {
            mCode = ResponseCode.FAILURE;
            errMsg = message;
            
        }

	public IResponseMessage createNew() {
		return new GetAlbumsResponse();
	}
}
