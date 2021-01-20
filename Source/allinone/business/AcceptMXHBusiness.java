/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Phong;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.InviteEntity;
import allinone.data.MessagesID;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.data.ZoneID;
import allinone.protocol.messages.AcceptMXHRequest;
import allinone.protocol.messages.AcceptMXHResponse;

/**
 * 
 * @author binh_lethanh
 */
public class AcceptMXHBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(AcceptMXHBusiness.class);
	private static String PLAYING_TABLE = "Bàn đang chơi mất rồi. Bạn chờ nhé!";

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		MessageFactory msgFactory = aSession.getMessageFactory();
		AcceptMXHResponse resAccept = (AcceptMXHResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
                
                
		try {
			mLog.debug("[REPLY]: Catch");
			
					AcceptMXHRequest rqAccept = (AcceptMXHRequest) aReqMsg;
					long destUid = rqAccept.destUid;
					ISession destSession = aSession.getManager().findSession(destUid);
                        
                        if((destSession == null || destSession.realDead() || !destSession.isLoggedIn()) && rqAccept.isAccept )
                        {
//                            if(dest)
                            throw new BusinessException("Bạn đó đã offline rồi");
                        }
                        
                        if(!rqAccept.isAccept)
                        {
                            AcceptMXHResponse resDestAccept = (AcceptMXHResponse) msgFactory.getResponseMessage(aReqMsg.getID());
                            
                            resDestAccept.setFailure(aSession.getUserName() +  " đã từ chối lời mời của bạn");
                            destSession.write(resDestAccept);
                            return 1;
                                   
                        }
                        
                        InviteEntity inviteEntity = destSession.getInviteEntity();
                        
                        if(inviteEntity == null || inviteEntity.getInviteId() != rqAccept.requestId)
                        {
                            throw new BusinessException(destSession.getUserName() +  " đã mời người chơi khác rồi");
                        }
                        
                        if(destSession.getCurrentZone() >0 && destSession.getCurrentZone()!= inviteEntity.getGameId())
                        {
                            throw new BusinessException(destSession.getUserName() +  " đã chơi game khác rồi");
                        }
                        
                        
                        if(destSession.getJoinedRooms().size()>0)
                        {
                            throw new BusinessException(destSession.getUserName() +  " đã chơi với người khác rồi");
                        }
                        
                        Zone zone = aSession.findZone(inviteEntity.getGameId());
                        
                        
                        //create new Table and send create new table to this user
                        Phong phongAvailable = zone.phongAvailable(aSession.getRealMoney());
                        
                        destSession.setRoom(null);
                        IResponsePackage responsePkg = destSession.getDirectMessages();
                        IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_NEW);
                        
                      
                        
                        aSession.setChatRoom(0); 
                        destSession.setCurrentZone(inviteEntity.getGameId());
                        try
                        {
                            aSession.setmGroup(null);
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        
                        
                        if(destSession.getRoom() == null)
                            throw new BusinessException("Người chơi không thể tạo bàn");
                        
                        IBusiness joinBusiness = msgFactory.getBusiness(MessagesID.MATCH_JOIN);
                       
                        // resMatchNew.setFailure(ResponseCode.FAILURE,
                        // "Bàn này đã có người tạo rồi");
                        
                        destSession.setChatRoom(0);
                        try
                        {
                            destSession.setmGroup(null);
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        
                        IResponsePackage joinResPkg = aSession.getDirectMessages();
                        
                      
                        
                        CacheUserInfo cacheUser = new CacheUserInfo();
                        UserEntity destUser = cacheUser.getUserInfo(aSession.getUID(),aSession.getRealMoney());
                        
                      
                        
                        aSession.setCurrentZone(inviteEntity.getGameId());
                        
                        return 1;

 		}
		 catch(BusinessException be)
		 {
                     resAccept.setFailure(be.getMessage());
                    
                     aResPkg.addMessage(resAccept);
		
		 }
		catch (Throwable t) {
			resAccept.setFailure("có lỗi xảy ra");
			 aResPkg.addMessage(resAccept);
			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
		}
		return 1;
	}

	
}
