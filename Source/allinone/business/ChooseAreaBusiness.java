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
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.protocol.messages.ChooseAreaRequest;
import allinone.protocol.messages.ChooseAreaResponse;
import allinone.protocol.messages.VivuAppearResponse;
import allinone.protocol.messages.VivuDisappearResponse;

import com.allinone.vivu.FuckingPlayer;

/**
 *
 * @author Vostro 3450
 */
public class ChooseAreaBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(ChooseAreaBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        ChooseAreaResponse resChoose =
                (ChooseAreaResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        mLog.debug("[Choose Area]: Catch");
        try {
            ChooseAreaRequest rqChoose = (ChooseAreaRequest) aReqMsg;
            long uid = aSession.getUID();
            aSession.setHide(false);
            aSession.setCurrentZone(0);
            
            if(aSession.getCity() == null) {
                aSession.setmCity(1);
            }
            if (aSession.getGroup() != null) {
                VivuDisappearResponse disA =
                        (VivuDisappearResponse) msgFactory.getResponseMessage(MessagesID.ViVuDisappear);
                disA.setSuccess(uid);
                aSession.getGroup().broadcast(disA, false, aSession.getUID());
                aSession.getGroup().left(aSession);
            }
            if(aSession.getCity().getSubZone(rqChoose.zone) == null){ 
                aSession.setmSubZone(null);
                return 1;// into unknown zone, just break
            }
            aSession.setmSubZone(rqChoose.zone);
            //Thread.sleep(100);
            resChoose.setSuccess(aSession.getGroup().getUsers(),
                    aSession.getGroup().getmGroupIndex());
            if(aSession.getGroup().getMsgFactory() == null){
                aSession.getGroup().setMsgFactory(msgFactory);
            }

            FuckingPlayer user;
            if (aSession.getUser() != null) {
                user = aSession.getUser();
            } else {
                user = new FuckingPlayer(aSession.getUserEntity());
                user.init(uid, aSession.getUserName());
                aSession.setUser(user);
//                user.entity = aSession.getUserEntity();
            }
            user.setZone(aSession.getSubZone());
            user.setPosition(rqChoose.x, rqChoose.y);
            
            VivuAppearResponse broadcast = (VivuAppearResponse) 
            		msgFactory.getResponseMessage(MessagesID.ViVuAppear);
            broadcast.setSuccess(uid, user.username, user.getAttr(), user.xPos, user.yPos);
            /*MovingResponse broadcast =
                    (MovingResponse) msgFactory.getResponseMessage(MessagesID.Moving);
            ArrayList<MovingInfo> data = new ArrayList<MovingInfo>();
            data.add(new MovingInfo(rqChoose.x, rqChoose.y, uid,
                    user.username, user.getAttr(), false));
            broadcast.setSuccess(data);*/
            
            aSession.broadcast(broadcast, 0);//Broadcast to Group
            //if (!resChoose.users.isEmpty()) {
                aResPkg.addMessage(resChoose);
            //}
        } catch (Throwable t) {
            resChoose.setFailure(t.getMessage());
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resChoose);
        }
        return 1;
    }
}
