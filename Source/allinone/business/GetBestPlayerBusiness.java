package allinone.business;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.GameDataEntity;
import allinone.data.Messages;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.GetBestPlayerRequest;
import allinone.protocol.messages.GetBestPlayerResponse;

public class GetBestPlayerBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetBestPlayerBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET BEST PLAYER]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetBestPlayerResponse response = (GetBestPlayerResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        GetBestPlayerRequest rq = (GetBestPlayerRequest) aReqMsg;
        response.session = aSession;

        try {
            long uid = aSession.getUID();
            mLog.debug("[GET Rank PLAYER]: Type " + rq.type + uid);

            List<UserEntity> data = null;
            List<UserEntity> top10Data = new ArrayList<UserEntity>();

            CacheUserInfo cache = new CacheUserInfo();
            data = cache.getRankPlayer(rq.type,aSession.getRealMoney());

            UserEntity userEntity = cache.getUserInfo(uid,aSession.getRealMoney());
            response.userName = userEntity.mUsername;
            response.viewname = userEntity.viewName;
            response.avatarID = userEntity.avatarID;
            // get current user rank
            int size = data.size();
            long rankData = 0;
            int ranking = 0;

            for (int i = 0; i < size; i++) {
                UserEntity entity = data.get(i);

                if (i < 10) {
                    top10Data.add(entity);
                }

                if (entity.mUid == uid) {
                    rankData = entity.rank;
                    ranking = i + 1;
                }
            }

            mLog.debug(" Ranking " + ranking + " username " + response.userName + " money " + userEntity.money+ " avatar " + userEntity.avatarID);

            if (ranking == 0) {
                if (rq.type == 1) {
                    rankData = userEntity.money;
                } else {
                    // get rankdata from db
                    UserDB db = new UserDB();
                    List<GameDataEntity> dataList = db.getGameData(uid);
                    int sizeList = dataList.size();

                    for (int i = 0; i < sizeList; i++) {
                        GameDataEntity gameData = dataList.get(i);
                        if (rq.type == 2) {
                            rankData += gameData.getExpr();
                        } else if (rq.type == 3) {
                            rankData += gameData.getWin() + gameData.getLost();
                        } else if (rq.type == 4) {
                            rankData += gameData.getWin();
                        }
                    }
                }
            }
            mLog.debug(" Rankdata " + rankData);

            response.type = rq.type;
            response.rankData = rankData;
            response.ranking = ranking;

            response.setSuccess(ResponseCode.SUCCESS, top10Data);

        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE, Messages.SYSTEM_DELAY);
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((response != null)) {
                aResPkg.addMessage(response);
            }
        }
        return 1;
    }
}
