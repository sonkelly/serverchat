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
import allinone.protocol.messages.ChooseAreaResponse;
import allinone.protocol.messages.VivuAppearResponse;
import allinone.protocol.messages.VivuChooseGroupRequest;
import allinone.protocol.messages.VivuChooseGroupResponse;
import allinone.protocol.messages.VivuDisappearResponse;

import com.allinone.vivu.Area;
import com.allinone.vivu.FuckingPlayer;
import com.allinone.vivu.Group;

/**
 *
 * @author Vostro 3450
 */
public class VivuChooseGroupBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(VivuChooseGroupBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        VivuChooseGroupResponse resMove =
                (VivuChooseGroupResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        mLog.debug("[Vivu Choose Group]: Catch");
        try {
            VivuChooseGroupRequest req = (VivuChooseGroupRequest) aReqMsg;
            long uid = aSession.getUID();
            if (aSession.getGroup() != null) {
                Area area = aSession.getSubZone();
                if (req.groupID == aSession.getGroup().getmGroupIndex()) {
                    resMove.setFailure("No change group!");
                    aSession.write(resMove);
                    return 1;
                } else {
                    Group newGroup = area.getGroup(req.groupID);
                    if (newGroup.isFull()) {
                        resMove.setFailure("Group is full!");
                        aSession.write(resMove);
                        return 1;
                    } else {
                        resMove.setSuccess();
                        aSession.write(resMove);
                        //Broadcast Disappear in old group
                        VivuDisappearResponse disA =
                                (VivuDisappearResponse) msgFactory.getResponseMessage(MessagesID.ViVuDisappear);
                        disA.setSuccess(uid);
                        aSession.getGroup().broadcast(disA, false, aSession.getUID());
                        aSession.getGroup().left(aSession);

                        // Set new Group

                        aSession.setmGroup(newGroup);
                        ChooseAreaResponse resChoose =
                                (ChooseAreaResponse) msgFactory.getResponseMessage(MessagesID.ChooseArea);
                        resChoose.setSuccess(newGroup.getUsers(),
                                newGroup.getmGroupIndex());
                        if (aSession.getGroup().getMsgFactory() == null) {
                            aSession.getGroup().setMsgFactory(msgFactory);
                        }
                        FuckingPlayer user = aSession.getUser();
                        VivuAppearResponse broadcast = (VivuAppearResponse) 
                        		msgFactory.getResponseMessage(MessagesID.ViVuAppear);
                        broadcast.setSuccess(uid, user.username, user.getAttr(), user.xPos, user.yPos);
                        /*MovingResponse broadcast =
                                (MovingResponse) msgFactory.getResponseMessage(MessagesID.Moving);
                        ArrayList<MovingInfo> data = new ArrayList<MovingInfo>();
                        data.add(new MovingInfo(user.xPos, user.yPos, uid,
                                user.username, user.getAttr(), false));
                        broadcast.setSuccess(data);*/
                        
                        mLog.debug("Group:" + aSession.getGroup().getmGroupIndex());
                        aSession.broadcast(broadcast, 0);//Broadcast to Group
                        if (!resChoose.users.isEmpty()) {
                            aResPkg.addMessage(resChoose);
                        }
                    }
                }
            } else {
                resMove.setFailure("You haven't joined any group!");
                aSession.write(resMove);
                return 1;
            }
        } catch (Throwable t) {
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            resMove.setFailure("Can not update status!");
            aResPkg.addMessage(resMove);
        }
        return 1;
    }
}
