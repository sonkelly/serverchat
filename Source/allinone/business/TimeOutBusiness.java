/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.data.ZoneID;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.TimeOutRequest;
import allinone.protocol.messages.TimeOutResponse;

/**
 *
 * @author Thomc
 */
public class TimeOutBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(TimeOutBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        boolean isSuccess = false;
        MessageFactory msgFactory = aSession.getMessageFactory();
        TimeOutResponse resTimeOut = (TimeOutResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            mLog.debug("[ SUGGEST ]: Catch");
            TimeOutRequest rqTimeOut = (TimeOutRequest) aReqMsg;
            Zone bacayZone = aSession.findZone(aSession.getCurrentZone());
            Room currentRoom = bacayZone.findRoom(rqTimeOut.mMatchId);

            UserDB userDb = new UserDB();
            UserEntity newUser = userDb.getUserInfo(rqTimeOut.player_friend_id,aSession.getRealMoney());
            if (newUser != null) {
                if (currentRoom != null) {
                    switch (aSession.getCurrentZone()) {
                        case ZoneID.COTUONG: {
                            long player_friend_id = rqTimeOut.player_friend_id;
                            resTimeOut.setSuccess(ResponseCode.SUCCESS, player_friend_id, newUser.mUsername);
                            
                            //currentRoom.broadcastMessage(resTimeOut, aSession, false);
                            isSuccess = true;
                            return 1;
                        }
                    }
                } else {
                    resTimeOut.setFailure(ResponseCode.FAILURE,
                            "Bạn đã thoát khỏi room");
                }

            } else {
                resTimeOut.setFailure(ResponseCode.FAILURE, "Không tìm thấy bạn chơi nữa!");
            }
        } catch (Throwable t) {
            resTimeOut.setFailure(ResponseCode.FAILURE, "bị lỗi!");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if (resTimeOut != null && !isSuccess) {
                aResPkg.addMessage(resTimeOut);
            }
        }
        return 1;
    }
}
