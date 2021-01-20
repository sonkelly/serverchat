package allinone.business;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Phong;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.protocol.messages.PlayEnterZoneRequest;
import allinone.protocol.messages.ReplyRequest;
import allinone.protocol.messages.ReplyResponse;

/**
 * 
 * @author binh_lethanh
 */
public class ReplyBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(ReplyBusiness.class);
	
	private static String PLAYING_TABLE = "Bàn đang chơi mất rồi. Bạn chờ nhé!";

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		MessageFactory msgFactory = aSession.getMessageFactory();
		ReplyResponse resMatchReply = (ReplyResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
                
                resMatchReply.session = aSession;
		try {
			
			mLog.debug("[REPLY]: Catch");
			
			long uid = aSession.getUID();
			
			ReplyRequest rqMatchReply = (ReplyRequest) aReqMsg;
			long buddy_uid = rqMatchReply.buddy_uid;
			
			ISession buddy_session = aSession.getManager().findSession(buddy_uid);

			if (buddy_session != null) {
				
				aSession.setReplyInvite(true);
				
				Zone zone = aSession.findZone(buddy_session.getCurrentZone());
				
				Room currentRoom = zone.findRoom(rqMatchReply.mMatchId);
				
				if (currentRoom != null) {
				
					SimpleTable table = currentRoom.getAttactmentData();
					
					// comment ban dang choi van vao duoc
//					if (!table.isPlaying) {

						Phong tmpPhong = zone.getPhong(aSession.getPhongID());
						
						if (rqMatchReply.mIsAccept) {
//							if (aSession.getCurrentZone() != zone.getZoneId()) {
								aSession.setCurrentZone(zone.getZoneId());
								
								// join zone
								if(rqMatchReply.directKey != 1) { 
									joinZone(aSession, aResPkg);
								}
//							}
							
							
							if (aSession.getPhongID() != 0) {
								
								zone.getPhong(aSession.getPhongID()).outPhong(aSession);

								if (tmpPhong != null)
									tmpPhong.enterPhong(buddy_session);
								else {
									buddy_session.setPhongID(aSession.getPhongID());
									zone.getPhong(aSession.getPhongID()).enterPhong(buddy_session);
								}
							}
							
						} else {
						}
//					} 
						
//					else {
//						resMatchReply.setFailure(ResponseCode.FAILURE,PLAYING_TABLE);
//						aSession.write(resMatchReply);
//						return 0;
//					}
				} else {
					if (rqMatchReply.mIsAccept) {
						resMatchReply
								.setFailure(ResponseCode.FAILURE,
										"Bạn kia đã đặt lệnh hủy trận này rồi. Bạn chờ trận khác nhé!");
						aSession.write(resMatchReply);
					}
				}
                                aSession.setNumInvite(0);
                                
			} else {
				if (rqMatchReply.mIsAccept) {
					resMatchReply.setFailure(ResponseCode.FAILURE, "Bàn chơi đã bị hủy");
					aSession.write(resMatchReply);
				}
			}
                        
//            try
//            {
//                if(aSession.isMXHDevice())
//                {
//                    aSession.setmGroup(null);
//                }
//            }
//            catch(Exception ex)
//            {
//                
//            }

		}
		
		// catch(BusinessException be)
		// {
		// resMatchReply.setFailure(ResponseCode.FAILURE,
		// be.getMessage());
		// try {
		// aSession.write(resMatchReply);
		// } catch (ServerException ex) {
		// mLog.error(ex.getMessage() + ex.getStackTrace());
		// }
		//
		// }
		
		catch (Throwable t) {
			resMatchReply.setFailure(ResponseCode.FAILURE, "Bàn chơi đã bị hủy");
			aResPkg.addMessage(resMatchReply);
//			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
		}
		return 1;
	}
	
	private void joinZone(ISession aSession, IResponsePackage aResPkg) {				
        
		MessageFactory msgFactory = aSession.getMessageFactory();        
		IBusiness business = msgFactory.getBusiness(MessagesID.PLAY_ENTER_ZONE);
		PlayEnterZoneRequest request = (PlayEnterZoneRequest) msgFactory.getRequestMessage(MessagesID.PLAY_ENTER_ZONE);
		request.zoneID = aSession.getCurrentZone();
		
		try {
			business.handleMessage(aSession,request,aResPkg);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
