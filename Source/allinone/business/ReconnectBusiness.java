package allinone.business;



import org.slf4j.Logger;

import tools.CacheMatch;
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
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.MatchEntity;
import allinone.data.Messages;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.SimplePlayer;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.protocol.messages.CancelRequest;
import allinone.protocol.messages.PlayEnterZoneRequest;
import allinone.protocol.messages.ReconnectRequest;
import allinone.protocol.messages.ReconnectResponse;
import java.util.Calendar;
import vn.game.common.SessionUtils;
import vn.game.common.sendCommon;
import vn.game.protocol.messages.ExpiredSessionResponse;

public class ReconnectBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ReconnectBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[Reconnect ROOM]: Catch " + aSession.getID() + " UID " + aSession.getUID());

        MessageFactory msgFactory = aSession.getMessageFactory();
        ReconnectResponse resReconn = (ReconnectResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        ReconnectRequest rqReconn = (ReconnectRequest) aReqMsg;

        aSession.setByteProtocol(AIOConstants.PROTOCOL_REFACTOR);
        aSession.setMobileDevice(true);
        aSession.setLastAccessTime(Calendar.getInstance().getTime());

//		if(rqReconn.versionCode >= 5) {
//			mLog.debug(" Code version " + rqReconn.versionCode);
//			aSession.setTopplayVersion(rqReconn.versionCode);
//		}
        mLog.debug("reconnect username " + rqReconn.username + " uid:" + rqReconn.uid);
//        try {
//            SessionDB sdb = new SessionDB();
//            sdb.addSessionDB(rqReconn.uid, aSession.getID(), aSession.getUserName(), aSession.getOnlyIP(), "");
//        } catch (Exception e) {
//
//        }
        if (!SessionUtils.checkSessionDB(aSession)) {
            return 0;
        }
        try {

            ISession temp = aSession.getManager().findSession(rqReconn.uid);

            if (temp != null) {
                mLog.debug(" Temp Session ID " + temp.getID());
            }

            if (temp != null && temp.getID() != aSession.getID() && !temp.realDead() && !temp.isExpired() && temp.isLoggedIn()) {

                // thanh phan day ra ngoai
//	        	if(temp.getTopplayVersion() == 1000) {
                mLog.debug(" CLOSE OLD SESSION ");
//	        		aSession.setLoggedIn(false);
//					aSession.setCommit(false);		
//					resReconn.setFailure(ResponseCode.FAILURE, "Vui lòng đăng nhập lại!");
////					aResPkg.addMessage(resReconn);
//					aSession.write(resReconn);	
//					aSession.setUIDNull();

                ExpiredSessionResponse expiredResponse = (ExpiredSessionResponse) aSession.getMessageFactory().getResponseMessage(9999);
                expiredResponse.mErrorMsg = "Vui lòng đăng nhập lại.";
                aSession.write(expiredResponse);
                aSession.setLoggedIn(false);
                aSession.setUIDNull();

                return 0;

////	        	} else {	        		
//		        	temp.setLoggedIn(false);        
//		        	temp.setUIDNull();
//	        	}	        	
            }

            UserEntity newUser;
            aSession.setMXHDevice(rqReconn.isMxh);

//            if(rqReconn.isMxh) aSession.setMobile("mxhReconn");
            if (rqReconn.protocol > 0) {
                //aSession.setByteProtocol(rqReconn.protocol);
                aSession.setByteProtocol(AIOConstants.PROTOCOL_REFACTOR);
            }

            CacheUserInfo cacheUser = new CacheUserInfo();
            newUser = cacheUser.getUserInfo(rqReconn.uid, aSession.getRealMoney());

            // validate password
            if (newUser != null) {
                mLog.debug("reconnect pass1 " + newUser.mPassword);
                mLog.debug("reconnect pass2 " + rqReconn.pass);
//	            if(rqReconn.pass != null && !rqReconn.pass.equalsIgnoreCase(newUser.mPassword)) {
//	            	throw new BusinessException("Tài khoản không hợp lệ");
//	            }	            
            } else {
                throw new BusinessException("Tài khoản không hợp lệ");
            }

            newUser.isOnline = true;
            cacheUser.updateCacheUserInfo(newUser);

            switch (rqReconn.type) {

                case 1: { // home
//				resReconn.setSuccess();
                    resReconn.setFailure(ResponseCode.FAILURE, "");
                    resReconn.isNeeded = false;
                    newUser = cacheUser.getUserInfo(rqReconn.uid, aSession.getRealMoney());
                    aSession.setUID(newUser.mUid);
                    aSession.setuType(newUser.utype);
                    aSession.setUserName(newUser.mUsername);
                    aSession.setLoggedIn(true);
                    aSession.setUserEntity(newUser);
                    aSession.write(resReconn);
                    sendCommon.sendConfigGame(aSession, msgFactory);//config_game
                    sendCommon.sendZoneConfig(aSession, msgFactory);

                    sendCommon.sendCountEmail(aSession, msgFactory);
                    //sendCommon.sendMsgSystem(aSession, msgFactory);//tạm bỏ chuyển sang client call, vì cơ chế sys có thể phải slow
                    return 1;
                }

                case 2: { // Vao Game
                    newUser = cacheUser.getUserInfo(rqReconn.uid, aSession.getRealMoney());
                    aSession.setUID(newUser.mUid);
                    aSession.setuType(newUser.utype);
                    aSession.setUserName(newUser.mUsername);
                    aSession.setLoggedIn(true);
                    aSession.setUserEntity(newUser);
//                enterZone(aSession, rqReconn.zone, resReconn);
                    playEnterZone(aSession, rqReconn.zone, resReconn);
                    return 1;
                }

                case 21: { // topplay Vao Game
                    newUser = cacheUser.getUserInfo(rqReconn.uid, aSession.getRealMoney());
                    aSession.setUID(newUser.mUid);
                    aSession.setuType(newUser.utype);
                    aSession.setUserName(newUser.mUsername);
                    aSession.setLoggedIn(true);
                    aSession.setUserEntity(newUser);
                    playEnterZone(aSession, rqReconn.zone, resReconn);
                    return 1;
                }

                case 3: { // Vao Phong
                    newUser = cacheUser.getUserInfo(rqReconn.uid, aSession.getRealMoney());
                    aSession.setUID(newUser.mUid);
                    aSession.setuType(newUser.utype);
                    aSession.setUserName(newUser.mUsername);
                    aSession.setLoggedIn(true);
                    aSession.setUserEntity(newUser);
//				enterRoom(aSession, rqReconn.phong, rqReconn.zone, resReconn);
                    playEnterZone(aSession, rqReconn.zone, resReconn);
                    return 1;
                }

                case 4: {// match
                    MatchEntity matchEntity = CacheMatch.getMatch(rqReconn.matchId);

                    if (matchEntity == null || matchEntity.getRoom() == null) {
                        newUser = cacheUser.getUserInfo(rqReconn.uid, aSession.getRealMoney());
                        aSession.setUID(newUser.mUid);
                        aSession.setuType(newUser.utype);
                        aSession.setUserName(newUser.mUsername);
                        aSession.setLoggedIn(true);
                        aSession.setCurrentZone(rqReconn.zone);
                        aSession.setUserEntity(newUser);
//					enterRoom(aSession, rqReconn.phong, rqReconn.zone, resReconn);                    
//                    playEnterZone(aSession, rqReconn.zone, resReconn);

                        // cancel table
                        IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
                        CancelRequest rqMatchCancel = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
                        rqMatchCancel.uid = rqReconn.uid;
                        rqMatchCancel.mMatchId = rqReconn.matchId;
                        rqMatchCancel.isLogout = true;
                        rqMatchCancel.isSendMe = true;

                        try {
                            business.handleMessage(aSession, rqMatchCancel, aSession.getDirectMessages());
                        } catch (ServerException se) {
                            mLog.error(se.getMessage(), se);
                        } catch (Throwable e) {
                            mLog.error(e.getMessage(), e);
                        }

                        return 1;
                    }

                    Room room = null;
                    int zoneID = 0;
                    long uid = rqReconn.uid;
                    room = matchEntity.getRoom();
                    room.join(aSession);

                    zoneID = matchEntity.getZoneId();

                    aSession.setCurrentZone(zoneID);

                    Phong enterPhong = aSession.findZone(zoneID).getPhong(matchEntity.getPhongID());

                    if (enterPhong != null) {
                        enterPhong.enterPhong(aSession);
                    }

                    newUser = cacheUser.getUserInfo(uid, aSession.getRealMoney());
                    aSession.setuType(newUser.utype);
                    aSession.setUserEntity(newUser);

                    if (newUser == null) {
                        throw new BusinessException(Messages.NONE_EXISTS_PLAYER);
                    }

                    SimpleTable tb = room.getAttactmentData();

                    boolean isNotPlayerPlaying = true;

                    for (SimplePlayer p : tb.getNewPlayings()) {
                        if (p.id == newUser.mUid) {
                            isNotPlayerPlaying = false;
                            break;
                        }
                    }

                    if (!tb.isPlaying || isNotPlayerPlaying) {

                        aSession.setUID(newUser.mUid);
                        aSession.setuType(newUser.utype);
                        aSession.setUserName(newUser.mUsername);
                        aSession.setLoggedIn(true);

                        // Cancel table
                        IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
                        CancelRequest rqMatchCancel = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
                        rqMatchCancel.uid = uid;
                        rqMatchCancel.mMatchId = rqReconn.matchId;
                        rqMatchCancel.isLogout = true;
                        rqMatchCancel.isSendMe = true;

                        try {
                            business.handleMessage(aSession, rqMatchCancel, aSession.getDirectMessages());
                        } catch (ServerException se) {
                            mLog.error(se.getMessage(), se);
                        } catch (Throwable e) {
                            mLog.error(e.getMessage(), e);
                        }

                        return 1;
                    }

                    mLog.debug(" ZoneID " + zoneID);
                    aSession.setUID(newUser.mUid);
                    aSession.setuType(newUser.utype);
                    aSession.setUserName(newUser.mUsername);
                    aSession.setLoggedIn(true);
                    return 1;
                }
            }
            return 1;
        } catch (ServerException ex) {
            aSession.setLoggedIn(false);
            aSession.setCommit(false);
            mLog.error(ex.getMessage(), ex);
            resReconn.setFailure(ResponseCode.FAILURE, ex.getMessage());
            aResPkg.addMessage(resReconn);
        } catch (Exception ex) {
            aSession.setLoggedIn(false);
            aSession.setCommit(false);
            mLog.error(ex.getMessage(), ex);
            resReconn.setFailure(ResponseCode.FAILURE, ex.getMessage());
            aResPkg.addMessage(resReconn);
        }
        return 1;
    }

//	private void enterRoom(ISession aSession, int roomID, int zoneID, ReconnectResponse res) {
//		MessageFactory msgFactory = aSession.getMessageFactory();
//		EnterRoomResponse resEnter = (EnterRoomResponse) msgFactory
//				.getResponseMessage(MessagesID.EnterRoom);
//		try {
//			aSession.setCurrentZone(zoneID);
//			resEnter.setZoneID(zoneID);
//			Zone zone = aSession.findZone(zoneID);
//			Phong enterPhong;
//
//			if (aSession.getPhongID() != 0) {
//				enterPhong = zone.getPhong(aSession.getPhongID());
//				if (enterPhong == null) {
//				} else {
//					enterPhong.outPhong(aSession);
//				}
//			}
//			enterPhong = zone.getPhong(roomID);
//			if (enterPhong == null) {
//				mLog.warn("EnterRoomBussiness [RoomID]" + roomID);
//			} else {
//				enterPhong.enterPhong(aSession);
//			}
//			resEnter.session = aSession;
//			List<SimpleTable> tables = zone.dumpNewWaitingTables(roomID);
//			resEnter.setSuccess(ResponseCode.SUCCESS, tables);
//
//			aSession.write(resEnter);
//		} catch (Throwable e) {
//			mLog.debug("Error!");
//			res.setFailure(ResponseCode.FAILURE, e.getMessage());
//			try {
//				aSession.write(res);
//			}catch (Throwable e1) {
//			}
//		}
//	}
//	private void enterZone(ISession aSession, int zoneID, ReconnectResponse res) {
//		try {
//			MessageFactory msgFactory = aSession.getMessageFactory();
//			IResponsePackage resEnter = aSession.getDirectMessages();
//                        
//			IBusiness enterZoneBusiness = msgFactory
//						.getBusiness(MessagesID.ENTER_ZONE);
//                        
//                        EnterZoneRequest rqEnterZone = (EnterZoneRequest) msgFactory
//                                        .getRequestMessage(MessagesID.ENTER_ZONE);
//                        
//                        rqEnterZone.zoneID = zoneID;
//                        aSession.setCurrentZone(zoneID);
//                        
//                        enterZoneBusiness.handleMessage(aSession, rqEnterZone, resEnter);
//
//		} catch (Throwable e) {
//			mLog.debug("Error!");
//			res.setFailure(ResponseCode.FAILURE, e.getMessage());
//			try {
//				aSession.write(res);
//			}catch (Throwable e1) {
//			}
//		}
//	}
    private void playEnterZone(ISession aSession, int zoneID, ReconnectResponse res) {
        try {
            MessageFactory msgFactory = aSession.getMessageFactory();
            IResponsePackage resEnter = aSession.getDirectMessages();

            IBusiness playEnterZoneBusiness = msgFactory
                    .getBusiness(MessagesID.PLAY_ENTER_ZONE);

            PlayEnterZoneRequest rqEnterZone = (PlayEnterZoneRequest) msgFactory
                    .getRequestMessage(MessagesID.PLAY_ENTER_ZONE);

            rqEnterZone.zoneID = zoneID;
            aSession.setCurrentZone(zoneID);

            playEnterZoneBusiness.handleMessage(aSession, rqEnterZone, resEnter);

        } catch (Throwable e) {
            mLog.debug("Error!");
            res.setFailure(ResponseCode.FAILURE, e.getMessage());
            try {
                aSession.write(res);
            } catch (Throwable e1) {
            }
        }
    }

    public void leftRoom(SimplePlayer reconnPlayer, ISession aSession) {
        try {
            if ((reconnPlayer.currentSession != null) && (reconnPlayer.currentSession.getID() != null)) {
                if (reconnPlayer.currentSession.getID().compareTo(aSession.getID()) != 0) {

                    Room r = reconnPlayer.currentSession.getJoinedRooms().lastElement();
                    reconnPlayer.currentSession.leftRoom(r.getRoomId());
                    r.left(aSession);
                    reconnPlayer.currentSession.close();
                }
            }
        } catch (Throwable e) {
            mLog.debug(" Error leftroom " + e.getMessage());
        }
    }

//	private void joinTable(ISession s, int game, long matchID)
//			throws PhomException, TienLenException, ServerException,
//			BusinessException, JSONException, SimpleException {
//		MessageFactory msgFactory = s.getMessageFactory();
//		JoinRequest rqMatchJoin = (JoinRequest) msgFactory
//				.getRequestMessage(MessagesID.MATCH_JOIN);
//		rqMatchJoin.mMatchId = matchID;
//		rqMatchJoin.uid = s.getUID();
//		rqMatchJoin.zone_id = game;
//		rqMatchJoin.roomID = 0;
//		IResponsePackage responsePkg = s.getDirectMessages();
//		IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_JOIN);
//		business.handleMessage(s, rqMatchJoin, responsePkg);
//	}
}
