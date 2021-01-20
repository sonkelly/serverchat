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
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.Couple;
import allinone.data.Messages;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.FastPlayRequest;
import allinone.protocol.messages.FastPlayResponse;

public class FastPlayBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(FastPlayBusiness.class);

    private static final String ALL_TABLES_BUSY = "Hiện tại không có bàn nào phù hợp với số tiền của bạn! Xin bạn thử lại sau ít phút";
    private static final String MAX_TWO_REQUEST = "Bạn thực hiện quá nhanh 2 yêu cầu tìm bàn. 2 yêu cầu tìm bàn cách nhau 10s!";

    private static final int MAX_BETWEEN_TWO_REQUEST = 10000;

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        int rtn = PROCESS_FAILURE;
        MessageFactory msgFactory = aSession.getMessageFactory();
        FastPlayResponse resfP = (FastPlayResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        mLog.debug("[FAST PLAY]: Catch");

        try {

            // request message and its values
            FastPlayRequest rqfP = (FastPlayRequest) aReqMsg;
            // broadcast

            resfP.session = aSession;

            long currentTime = System.currentTimeMillis();
            if (currentTime - aSession.getLastFP() < MAX_BETWEEN_TWO_REQUEST && aSession.getLastFP() > 0) {
                throw new BusinessException(MAX_TWO_REQUEST);
            }

            int zoneID = aSession.getCurrentZone();
            if (aSession.getByteProtocol() > AIOConstants.PROTOCOL_ACTIVE_LOGIN) {
                zoneID = rqfP.zoneId;
                aSession.setCurrentZone(zoneID);
            }

            Zone zone = aSession.findZone(zoneID);
            Couple<Integer, Long> res = new Couple<>(0, 0l);
            CacheUserInfo cacheUser = new CacheUserInfo();
            UserEntity usrEntity = cacheUser.getUserInfo(aSession.getUID(), aSession.getRealMoney());

            long minMoney = 1000;
            if (aSession.getRealMoney().equals("realmoney")) {
                minMoney = 500;
            }
            // nho hon muc cuoc toi thieu cua ban
            //mLog.debug("usrEntity.money:" + usrEntity.money + " minMoney:"+minMoney);
            if (usrEntity.money < minMoney) {
                //mLog.debug("fast play error" + Messages.NOT_ENOUGH_MONEY);
                throw new BusinessException(Messages.NOT_ENOUGH_MONEY);
            } else {
                //mLog.debug("vao day"+aSession.getLastFastMatch());
                try {
                    res = zone.fastPlay(aSession.getLastFastMatch(), usrEntity.money, aSession);
                } catch (Exception e) {
                    //mLog.debug("fast play error");
                    throw new BusinessException(ALL_TABLES_BUSY);

                }
                //mLog.debug("res:" + res.toString());
                if (res == null) {
                    //create new Table and send create new table to this user
                    //Phong phongAvailable = zone.phongAvailable(aSession.getRealMoney());
                    Phong phongAvailable = zone.phongAvailableByMinBet(minMoney,aSession.getRealMoney());
                    IResponsePackage responsePkg = aSession.getDirectMessages();
                    IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_NEW);
//mLog.debug("minMoney:" + minMoney);
//mLog.debug("phongAvailable.id:" + phongAvailable.id);
                 

//                if(aSession.getCurrentZone() == ZoneID.POKER || aSession.getCurrentZone() == ZoneID.LIENG || aSession.getCurrentZone() == ZoneID.XITO) {        
//	                if(usrEntity.money > SimpleTable.MONEY_TIME_MIN_POKER * newRequest.moneyBet) {
//	                	newRequest.takeMoney =  SimpleTable.MONEY_TIME_MAX_POKER * newRequest.moneyBet;           		
//	            	} else {
//	            		newRequest.takeMoney =  usrEntity.money;
//	            	}
//                }
                   
                    return 1;
                    //throw new BusinessException(ALL_TABLES_BUSY);
                }
                // try {

                aSession.setLastFastMatch(res.e2);
                IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_JOIN);
          

                // get match Entity
//        	MatchEntity matchEntity = CacheMatch.getMatch(rqJoin.mMatchId);
//        	long minMoney = matchEntity.getRoom().getAttactmentData().firstCashBet;
//            if(aSession.getCurrentZone() == ZoneID.POKER) {                 	
//            	
//            			minMoney * SimpleTable.MONEY_TIME_MAX_POKER
//            			);
//            	
//            	if(usrEntity.money >= SimpleTable.MONEY_TIME_MAX_POKER * minMoney) {
//                	rqJoin.takeMoney =  SimpleTable.MONEY_TIME_MAX_POKER * minMoney;           		
//            	} else {
//            		rqJoin.takeMoney =  usrEntity.money;
//            	}            	
//            	rqJoin.takeMoney = table.validateMoney(usrEntity.money, rqJoin.takeMoney,
//                 		table.MONEY_MINIMUM_JOIN  * moneyBet,
//                 		table.MONEY_MIN_JOIN  * moneyBet, 
//                 		table.MONEY_MAX_JOIN  * moneyBet
//               			);
//
//                 mLog.debug(" Take money " + rqMatchNew.takeMoney);
//            }
//            if(aSession.getCurrentZone() == ZoneID.LIENG || aSession.getCurrentZone() == ZoneID.NXITO) {                 	
//            	if(usrEntity.money >= SimpleTable.MONEY_TIME_MAX_POKER * minMoney) {
//                	rqJoin.takeMoney =  SimpleTable.MONEY_TIME_MAX_POKER * minMoney;           		
//            	} else {
//            		rqJoin.takeMoney =  usrEntity.money;
//            	}            	
//            }
                rtn = PROCESS_OK;
            }
            //rqJoin.uid = aSession.getUID();
            //rqJoin.roomID = rqfP.roomID;

        } catch (BusinessException be) {
            mLog.debug("ex" + be.getMessage());
            resfP.setFailure(ResponseCode.FAILURE,
                    be.getMessage());
            try {
                //System.out.println("Hello:" + resfP.mCode);
                aSession.write(resfP);
                //aResPkg.addMessage(resfP);
            } catch (ServerException ex) {
                mLog.error("fast playing ", ex);
            }

            return PROCESS_OK;
        } catch (Throwable t) {
            // response failure
            resfP.setFailure(ResponseCode.FAILURE, "Lỗi !");
            // aSession.setLoggedIn(false);
            //rtn = PROCESS_OK;
            //mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resfP);
        }

        return 1;
    }
}
