package allinone.business;



import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AvatarEntity;
import allinone.data.ResponseCode;
import allinone.databaseDriven.AvatarsDB;
import allinone.protocol.messages.GetAvatarListResponse;
import java.util.ArrayList;
import java.util.Calendar;

public class GetAvatarListBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetAvatarListBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {

        mLog.debug("[Get Avatar] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetAvatarListResponse resGetAvatarList
                = (GetAvatarListResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            //Vector<AvatarEntity> avaList = new Vector<AvatarEntity>();//InfoDB.getLstAvatars();//DatabaseDriver.getAvatarList();
            AvatarsDB avaDB = new AvatarsDB();
            ArrayList<AvatarEntity> avaList = avaDB.getAvatars();
            resGetAvatarList.setSuccess(ResponseCode.SUCCESS, avaList);
            aSession.setLastAccessTime(Calendar.getInstance().getTime());
            resGetAvatarList.session = aSession;
            
        } catch (Exception e) {
            mLog.debug("Get avatar list error:" + e.getCause());
            resGetAvatarList.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu");
        } finally {
            if ((resGetAvatarList != null)) {
                aResPkg.addMessage(resGetAvatarList);
            }
        }
        return 1;
    }
}
