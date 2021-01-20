package allinone.business;

import org.slf4j.Logger;

import tools.CacheMatch;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.MatchEntity;
import allinone.data.Messages;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.data.ZoneID;
import allinone.protocol.messages.ReadyRequest;
import allinone.protocol.messages.ReadyResponse;
import java.util.Calendar;

public class ReadyBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(ReadyBusiness.class);
@Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {

        //mLog.debug("[READY]: Catch ");
        MessageFactory msgFactory = aSession.getMessageFactory();
        ReadyResponse resReady = (ReadyResponse) msgFactory
                .getResponseMessage(aReqMsg.getID());
        resReady.setSession(aSession);
        aSession.setLastAccessTime(Calendar.getInstance().getTime());
        try {
            ReadyRequest rqMatchNew = (ReadyRequest) aReqMsg;
            // mLog.debug("[READY]: ID - " + rqMatchNew.uid);
            // Zone zone = aSession.findZone(aSession.getCurrentZone());
            // mLog.debug("[READY]: ZONE - " + zone.getZoneName() + " : "
            // + aSession.getCurrentZone() + " : "
            // + aSession.getCurrentZone());

            // Room room = zone.findRoom(rqMatchNew.matchID);
            if (rqMatchNew.uid == 0) {
                rqMatchNew.uid = aSession.getUID();
            }

            MatchEntity matchEntity = CacheMatch.getMatch(rqMatchNew.matchID);
            Room room = null;
            if (matchEntity != null) {
                room = matchEntity.getRoom();
            }

            if (room == null
                    || (room.getAttactmentData().matchID != rqMatchNew.matchID)) {
                Zone zone = aSession.findZone(aSession.getCurrentZone());
                room = zone.findRoom(rqMatchNew.matchID);
            }

            if (room != null) {
                resReady.zone = aSession.getCurrentZone();

                switch (aSession.getCurrentZone()) {
/*
                    case ZoneID.PHOM: {
                        PhomTable newTable = (PhomTable) room.getAttactmentData();
                        if (newTable.isPlaying) {
                            throw new BusinessException(Messages.READY_PLAYING_TABLE);
                        }

                        PhomPlayer player = newTable.findPlayer(rqMatchNew.uid);
                        if (player == null) {
                            ISession is = aSession.getManager().findSession(
                                    rqMatchNew.uid);
                            if (is != null) {
                                room.left(is);
                                is.leftRoom(room.getRoomId());
                            }
                            return 1;
                        }
                        player.isReady = rqMatchNew.ready;
                        long now = System.currentTimeMillis();
                        newTable.owner.setLastActivated(now);
                        player.setLastActivated(now);
                        resReady.session = aSession;
                        resReady.setSuccess(ResponseCode.SUCCESS, rqMatchNew.uid,
                                rqMatchNew.ready);
                        // resReady.mPlayerPhom = newTable.getPlayings();
                        // resReady.mWaitingPlayerPhom = newTable.getWaitings();
                        mLog.debug("Ready Success:");

                        newTable.broadcastMsg(resReady, newTable.getNewPlayings(),
                                newTable.getNewWaitings(), player, true);
                        resReady = null;
                        // ReadyResponse broadcastMsg = (ReadyResponse) msgFactory
                        // .getResponseMessage(MessagesID.MATCH_READY);
                        // broadcastMsg.setSuccess(ResponseCode.SUCCESS,
                        // rqMatchNew.uid, rqMatchNew.ready);
                        // room.broadcastMessage(broadcastMsg, aSession, false);

                        break;
                    }
                    case ZoneID.TIENLEN: {
                        TienLenTable newTable = (TienLenTable) room
                                .getAttactmentData();
                        if (newTable.isPlaying) {
                            throw new BusinessException(Messages.READY_PLAYING_TABLE);
                        }


                        newTable.ready(rqMatchNew.uid, rqMatchNew.ready);
                        resReady.setSuccess(ResponseCode.SUCCESS, rqMatchNew.uid,
                                rqMatchNew.ready);

                        TienLenPlayer player = newTable.findPlayer(rqMatchNew.uid);
                        newTable.broadcastMsg(resReady, newTable.getNewPlayings(),
                                newTable.getNewWaitings(), player, true);

                        resReady = null;

                        // ReadyResponse broadcastMsg = (ReadyResponse) msgFactory
                        // .getResponseMessage(MessagesID.MATCH_READY);
                        // broadcastMsg.setSuccess(ResponseCode.SUCCESS,
                        // rqMatchNew.uid,rqMatchNew.ready);
                        // room.broadcastMessage(broadcastMsg, aSession, false);
                        break;
                    }

//				case ZoneID.XOC_DIA:
//				case ZoneID.NEW_BA_CAY:
//				case ZoneID.BAU_CUA_TOM_CA:
//                                case ZoneID.AILATRIEUPHU:
//				case ZoneID.XITO:
//                                {
//                                    
//					SimpleTable newTable = room.getAttactmentData();
//                                        if(newTable.isPlaying)
//                                            throw new BusinessException(Messages.READY_PLAYING_TABLE);
//
//					newTable.playerReadyWithBroadcast(rqMatchNew.uid,
//							rqMatchNew.ready);
//					resReady = null;
//
//					break;
//				}
                    case ZoneID.TRO_CHOI_AM_NHAC: {
                        MusicArenaTable table = (MusicArenaTable)room.getAttactmentData();
                        table.ready(true, aSession.getUID());
                        
                        resReady.setSuccess(ResponseCode.SUCCESS,
                                aSession.getUID(), true);
                        resReady.session = aSession;
                        //aSession.write(broadcastMsg);
                        return 1;
                    }
                    case ZoneID.GEM_ONLINE:
                    case ZoneID.TANK_ONLINE:
                    case ZoneID.LINE_ONLINE:
                    case ZoneID.PIKACHU: {
                        SimpleTable newTable = room.getAttactmentData();
                        if (newTable.isPlaying) {
                            throw new BusinessException(Messages.READY_PLAYING_TABLE);
                        }


                        newTable.playerReady(rqMatchNew.uid, rqMatchNew.ready);
                        resReady.setSuccess(ResponseCode.SUCCESS, rqMatchNew.uid,
                                rqMatchNew.ready);
                        mLog.debug("Ready Success:");

                        ReadyResponse broadcastMsg = (ReadyResponse) msgFactory
                                .getResponseMessage(MessagesID.MATCH_READY);
                        broadcastMsg.setSuccess(ResponseCode.SUCCESS,
                                rqMatchNew.uid, rqMatchNew.ready);
                        newTable.broadcastMsg(broadcastMsg,
                                newTable.getNewPlayings(),
                                new ArrayList<SimplePlayer>(), newTable.findPlayer(rqMatchNew.uid), false);
                        // room.broadcastMessage(broadcastMsg, aSession, false);
                        return 1;
                    }
                     */
                    case ZoneID.DRAGGER: {
                        SimpleTable newTable = room.getAttactmentData();

                        if (newTable.isPlaying) {
                            throw new BusinessException(Messages.READY_PLAYING_TABLE);
                        }

//                        if (newTable.getNewPlayings().size() <= 1) {
//                            throw new BusinessException(" Hiện tại chỉ còn mình bạn trong bàn");
//                        }                        
                        newTable.playerReadyWithBroadcast(rqMatchNew.uid, true, true);

//                        mLog.debug(" ready size" + newTable.getReadySize()); 
//                        mLog.debug(" playing size" + newTable.getNewPlayings().size());
                        if (newTable.getReadySize() == (newTable.getNewPlayings().size() - 1) && newTable.getNewPlayings().size() > 2) {
                            if (newTable.lastReadyStartActive == 0) {
                                newTable.lastReadyStartActive = System.currentTimeMillis();
                            }
                        }
                        if (newTable.getNewPlayings().stream().allMatch(p -> p.isReady)) {
                            newTable.lastStartActive = System.currentTimeMillis() - 13000;
                        }

//                        mLog.debug(" lastReadyStartActive " + newTable.lastReadyStartActive);
//
//                        if (newTable.lastReadyStartActive > 0) {
//                        	newTable.playerReadyWithBroadcast(rqMatchNew.uid, true, true);
//                        } else {
//                        	newTable.playerReadyWithBroadcast(rqMatchNew.uid, true, false);                        	
//                        }
                        resReady = null;

                        break;
                    }

                    default: {
                    	
                        SimpleTable newTable = room.getAttactmentData();

                        if (newTable.isPlaying) {
                            throw new BusinessException(Messages.READY_PLAYING_TABLE);
                        }
                        
//                        if (newTable.getNewPlayings().size() <= 1) {
//                            throw new BusinessException(" Hiện tại chỉ còn mình bạn trong bàn");
//                        }                        

                    	newTable.playerReadyWithBroadcast(rqMatchNew.uid, true, true);

//                        mLog.debug(" ready size" + newTable.getReadySize()); 
//                        mLog.debug(" playing size" + newTable.getNewPlayings().size());
                        
                        if(newTable.getReadySize() == (newTable.getNewPlayings().size() - 1) && newTable.getNewPlayings().size() > 2) {
                        	if(newTable.lastReadyStartActive == 0) {
                        		newTable.lastReadyStartActive = System.currentTimeMillis();
                        	}                        	
                        }
                        
//                        mLog.debug(" lastReadyStartActive " + newTable.lastReadyStartActive);
//
//                        if (newTable.lastReadyStartActive > 0) {
//                        	newTable.playerReadyWithBroadcast(rqMatchNew.uid, true, true);
//                        } else {
//                        	newTable.playerReadyWithBroadcast(rqMatchNew.uid, true, false);                        	
//                        }

                        resReady = null;

                        break;


                    }
                }
            } else {
                resReady.setFailure(ResponseCode.FAILURE, "Bàn chơi đã bị huỷ!");
            }

        } catch (BusinessException be) {
            resReady.setFailure(ResponseCode.FAILURE, be.getMessage());
//			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } catch (Throwable t) {
            resReady.setFailure(ResponseCode.FAILURE, "Bị lỗi " + t.toString());
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resReady != null)) {
                aResPkg.addMessage(resReady);
            }
        }

        return 1;
    }
}
