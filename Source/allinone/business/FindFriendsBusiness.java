/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.List;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;

import allinone.data.UserEntity;
import allinone.databaseDriven.FriendDB;
import allinone.protocol.messages.FindFriendsRequest;
import allinone.protocol.messages.FindFriendsResponse;


/**
 *
 * @author Dinhpv
 */
public class FindFriendsBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(FindFriendsBusiness.class);
    //private static final int PAGE_SIZE = 10;

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {

        int rtn = PROCESS_FAILURE;
        mLog.debug("[Find mxh friends] : Catch");
        FriendDB userDb = new FriendDB();
        MessageFactory msgFactory = aSession.getMessageFactory();

        FindFriendsResponse resFinds = (FindFriendsResponse) msgFactory
                .getResponseMessage(aReqMsg.getID());
        try {
            FindFriendsRequest rqRegister = (FindFriendsRequest) aReqMsg;
            List<UserEntity> lstUsers = null;

            lstUsers = userDb.findFriends(rqRegister.isMale, rqRegister.countryId, rqRegister.cityId,
                    rqRegister.name, rqRegister.pageIndex);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lstUsers.size(); i++) {
                UserEntity entity = lstUsers.get(i);
                sb.append(entity.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.viewName).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.mIsMale ? "1" : "0").append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.avatarID).append(AIOConstants.SEPERATOR_BYTE_2);
            }
            resFinds.setSuccess(sb.toString());

            rtn = PROCESS_OK;
        } catch (Throwable t) {
            resFinds.setFailure("Có lỗi xảy ra!");
            rtn = PROCESS_OK;
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resFinds != null) && (rtn == PROCESS_OK)) {
                aResPkg.addMessage(resFinds);
            }
        }
        return rtn;
    }
}
