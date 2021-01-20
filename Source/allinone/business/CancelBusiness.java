/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.ArrayList;

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
import allinone.data.ResponseCode;
import allinone.data.SimplePlayer;
import allinone.data.SimpleTable;
import allinone.data.ZoneID;
import allinone.protocol.messages.CancelRequest;
import allinone.protocol.messages.CancelResponse;
import allinone.server.Server;

/**
 *
 * @author binh_lethanh
 */
public class CancelBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CancelBusiness.class);

    private void writeWithRooms(ISession aSession, CancelRequest request,
            CancelResponse response, int phongId, Zone zone) throws ServerException {
        mLog.debug(" Write Rooms isSendMe " + request.isSendMe);
        if (request.isSendMe) {
            response.phongId = phongId;
            response.zone = zone;
            response.session = aSession;
            aSession.write(response);
        }
        response.mCode = ResponseCode.SUCCESS;
        aSession.setRoom(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        mLog.debug("[CANCEL]: Catch");

        MessageFactory msgFactory;//aSession.getMessageFactory();
        CancelResponse resMatchCancel = null;//(CancelResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        msgFactory = aSession.getMessageFactory();
        resMatchCancel = (CancelResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        Room room = null;
        int zoneID = aSession.getCurrentZone();
        CancelRequest rqMatchCancel = (CancelRequest) aReqMsg;
        long matchId = rqMatchCancel.mMatchId;

        long uid = aSession.getUID();
        resMatchCancel.setUid(uid);

        if (matchId > 0) {
            MatchEntity matchEntity = CacheMatch.getMatch(matchId);

            if (matchEntity != null) {
                room = matchEntity.getRoom(); //find room by new method
                zoneID = matchEntity.getZoneId();
            }
        }
       
        Zone gamezone = aSession.findZone(zoneID);

        try {

//            CancelRequest rqMatchCancel = (CancelRequest) aReqMsg;
//            long matchId = rqMatchCancel.mMatchId;
//            long uid = aSession.getUID();
            mLog.debug("[CANCEL]: ID - " + uid + ", match id - " + matchId + " ZoneID " + zoneID);

            if (matchId > 0) {
                if (room == null) //found by new method
                {
                    room = aSession.getRoom();
                }
                if (room == null || (room.getAttactmentData().matchID != matchId && zoneID != ZoneID.XOC_DIA_ALL)) {
                    //find by old method
                    room = gamezone.findRoom(matchId);
                }
                if (room != null) {
                    resMatchCancel.session = aSession;
                    resMatchCancel.phongId = room.phongID;
//                    resMatchCancel.zone = gamezone;

//					RoomDB roomDB = new RoomDB();
//					roomDB.userOut(zoneID, aSession.getPhongID());
                    if (aSession.getRoomID() != 0) {
                        //gamezone.outRoom(aSession.getRoomID());
                        aSession.setRoomID(0);
                    }

                    aSession.setLastFP(System.currentTimeMillis() - 20000); //for fast play
                    // check if user is viewings
//                    
//                    if (room.getAttactmentData().cancelGuest(uid)) {
//                        mLog.debug(" Guest User is OUT " + uid);
//                        return 1;
//                    }
//mLog.debug("att:"+room.getAttactmentData().isPlaying);
//mLog.debug("rqMatchCancel.isLogout:"+rqMatchCancel.isLogout);
                    if (room.getAttactmentData().isPlaying && !rqMatchCancel.isLogout && zoneID != ZoneID.XOC_DIA && zoneID != ZoneID.XOC_DIA_ALL) {
                        //mLog.debug("vaoday");
                        //&& zoneID != ZoneID.XOC_DIA//add by zep
                        SimplePlayer player = room.getAttactmentData().findPlayer(uid);
//mLog.debug("vaoday2");
                      
//
//                        if(zoneID == ZoneID.BIDA_FOUR) {
//                            player.isMonitor = ((BidaFourPlayer) player).isObserve;
//                        }
                        // check player is playing
                        //mLog.debug(" Player Monitor: " + player.isMonitor);
                        if (player != null && !player.isMonitor) {

                            if (player.registOut == 0) {
                                resMatchCancel.cancelStatus = 1;
                            } else if (player.registOut == 1) {
                                resMatchCancel.cancelStatus = 2;
                            }

                            player.registOut = 1 - player.registOut;
                            resMatchCancel.setSuccess(ResponseCode.SUCCESS, uid);

                            writeWithRooms(aSession, rqMatchCancel, resMatchCancel, room.phongID, gamezone);

                            // send message
                            return 1;
                        }
                    }
                    // find player in list
                   
                    // Finally                    
//                    if(aSession.getCurrentZone() == ZoneID.NEW_BA_CAY || aSession.getCurrentZone() == ZoneID.NXITO || aSession.getCurrentZone() == ZoneID.NXITO
//                    		|| aSession.getCurrentZone() == ZoneID.BINH)
//                    {                        
//                        writeWithRooms(aSession, rqMatchCancel, resMatchCancel, room.phongID, gamezone);
//                        aSession.leftRoom(matchId);
//                        room.left(aSession);
//                    } else
//                    {

                    mLog.debug(" Send cancel to request user ");

                    Room currentRoom = aSession.leftRoom(matchId);
                    if (currentRoom != null) {

                        currentRoom.left(aSession);
                    }

                    if (aSession.getCurrentZone() == ZoneID.TIENLEN) {
                        resMatchCancel.mCode = 1;
                    }

                    if (room != null) {

                        writeWithRooms(aSession, rqMatchCancel, resMatchCancel, room.phongID, gamezone);

                        if (aSession.getCurrentZone() != ZoneID.BINH
                                && aSession.getCurrentZone() != ZoneID.NEW_BA_CAY
                                && aSession.getCurrentZone() != ZoneID.LIENG
                                && aSession.getCurrentZone() != ZoneID.NXITO
                                && aSession.getCurrentZone() != ZoneID.POKER
                                && aSession.getCurrentZone() != ZoneID.SAM
                                && aSession.getCurrentZone() != ZoneID.BIDA
                                && aSession.getCurrentZone() != ZoneID.BIDA_FOUR
                                && aSession.getCurrentZone() != ZoneID.BIDA_PHOM
                                && aSession.getCurrentZone() != ZoneID.COTUONG
                                && aSession.getCurrentZone() != ZoneID.XOC_DIA) {
                            SimpleTable table = room.getAttactmentData();
                            if (table != null) {
                                mLog.debug(" Cancel Send broadcast message ");
                            }
                            table.broadcastMsg(resMatchCancel, table.getNewPlayings(), table.getNewWaitings(), null, false);
                        }
                    }
                } else {
                    writeWithRooms(aSession, rqMatchCancel, resMatchCancel, room.phongID, gamezone);
//                    msgFactory = aSession.getMessageFactory();
//                    resMatchCancel = (CancelResponse) msgFactory.getResponseMessage(aReqMsg.getID());
//                    resMatchCancel.mCode = 1;
//                    resMatchCancel.session = aSession;
//                    resMatchCancel.uid= aSession.getUID();
//                    aSession.write(resMatchCancel);
                }
            } else {

                writeWithRooms(aSession, rqMatchCancel, resMatchCancel, room.phongID, gamezone);

                // Player is not in any match - do nothing
//                if (rqMatchCancel.isLogout) { // if logout - kill session
//                msgFactory = aSession.getMessageFactory();
//                resMatchCancel = (CancelResponse) msgFactory.getResponseMessage(aReqMsg.getID());
//                    if (aSession != null) {
//                        aSession.close();
//                    }
//                }
            }
        } catch (Throwable t) {

            mLog.debug(" Exception khong ton tai user " + t.getMessage());

            try {
                rqMatchCancel.isSendMe = true;
                writeWithRooms(aSession, rqMatchCancel, resMatchCancel, room.phongID, gamezone);
                room.left(aSession);
            } catch (ServerException ex) {
                mLog.error(ex.getMessage(), ex);
            } catch (Exception ex) {

//            	 mLog.error(ex.getMessage(), ex);
//                ex.printStackTrace();
                mLog.debug(" Room is null - accept exist game ");
                resMatchCancel.setSuccess(ResponseCode.SUCCESS, aSession.getUID());
                try {
                    aSession.write(resMatchCancel);
                } catch (ServerException e) {
                    mLog.error(ex.getMessage(), ex);
                }
                aSession.setRoom(null);
            }

//          resMatchCancel.setSuccess(ResponseCode.SUCCESS, aSession.getUID());            
//            try {
//            	resMatchCancel.
//            	aSession.write(resMatchCancel);
//			} catch (ServerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }

        return 1;
    }

    public static void main(String args[]) {

        ArrayList arr = null;

        try {
            System.out.println(arr.size());
        } catch (Exception ex) {
            System.out.println(" Mess: " + ex.getMessage());
//    		System.out.println(" Lot: " + ex.getLocalizedMessage());
//    		System.out.println(" Cause: " + ex.getCause().toString());
        }
    }

}
