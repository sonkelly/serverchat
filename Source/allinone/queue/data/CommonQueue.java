package allinone.queue.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.AdvertisingEntity;
import allinone.data.AlertUserEntity;
import allinone.data.MessagesID;
import allinone.data.QueueEntity;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.LoginResponse;
import allinone.protocol.messages.SendAdvResponse;
import java.util.logging.Level;

public class CommonQueue {
//    private static Queue queue;

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(CommonQueue.class);

    private static ConcurrentHashMap<UUID, QueueEntity> queue = new ConcurrentHashMap<>();

    private static Logger log = LoggerContext.getLoggerFactory()
            .getLogger(CommonQueue.class);

    private static boolean isInProgress = false;
    private static final int ADV_POPUP = 1;
    private static final int DEVICE_ID_ADV_POPUP = 5;
    private static final int SWITCH_USER_ADV_POPUP = 6;

    private static int count = 0;
    private static final long ACTIVATE_ADV_AFTER_LOGIN = 2000;
    private static final int PARTNER_SWITCH_USER = 96;

//    private static  Date dateEvent;
//    private static final String SERVER_1_ADDR="125.212.192.97";
//    private static final String SERVER_1_PORT="6996";
//    private static final String SERVER_2_ADDR="125.212.192.97";
//    private static final String SERVER_2_PORT="6996";
    private static final String SERVER_1_ADDR = "127.0.0.1";
    private static final String SERVER_1_PORT = "6996";
    private static final String SERVER_2_ADDR = "127.0.0.1";
    private static final String SERVER_2_PORT = "6996";

//    static{
//        init();
//    }
//    
//    private static void init()
//    {
//        try
//        {
//            Calendar c = Calendar.getInstance();
//            Date dtNow = new Date();
//            c.setTime(dtNow);
//            c.add(Calendar.DATE, -3);
//            c.setTime(dateEvent);
//            c.add(Calendar.DATE, -1);
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
    public CommonQueue() {

    }

    public String getAdv(UserEntity usrEntity) {
        int partnerId = usrEntity.partnerId;
//        InfoDB db = new InfoDB();
//        List<AdvertisingEntity> lstAdversts = db.getAdvertising();

        CacheEventInfo cacheEvent = new CacheEventInfo();
        List<AdvertisingEntity> lstAdversts = cacheEvent.getAdv();

        boolean onlyPartner = false;
        int size = lstAdversts.size();
        List<AdvertisingEntity> eventPartners = new ArrayList<AdvertisingEntity>();
        List<AdvertisingEntity> allPartners = new ArrayList<AdvertisingEntity>();

        for (int i = 0; i < size; i++) {
            AdvertisingEntity entity = lstAdversts.get(i);
            if (entity.getPartnerId() == 0) {
                allPartners.add(entity);
            }

            if (entity.getPartnerId() == partnerId) {
                eventPartners.add(entity);
                onlyPartner = true;
            }
        }
        List<AdvertisingEntity> retAdvs = null;
        if (onlyPartner) {
            retAdvs = eventPartners;
        } else {
            retAdvs = allPartners;
        }

        int advSize = retAdvs.size();
        if (advSize == 0) {
            return "";
        }

        if (count >= advSize) {
            count = 0;
        }

        return retAdvs.get(count).getContent();
    }

    public void insertQueue(QueueEntity entity) {
        try {
            UUID uuid = UUID.randomUUID();
            queue.put(uuid, entity);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void sendQueueMessage() {
        if (!isInProgress) {
            isInProgress = true;
            try {
                Enumeration<UUID> e = queue.keys();
                List<QueueEntity> lstNotActivated = new ArrayList<QueueEntity>();

                while (e.hasMoreElements()) {
                    try {
                        long currentTime = System.currentTimeMillis();
                        UUID key = e.nextElement();

                        QueueEntity queryEntity = queue.get(key);
                        queue.remove(key);
                        if (queryEntity == null) {
                            continue;
                        }

                        ISession aSession = queryEntity.getSession();

                        if (aSession == null || aSession.isExpired() || aSession.isClosed()) {
                            continue;
                        }

                      
                    } catch (Exception ex) {
                        try {
                            log.error(ex.getMessage(), ex);
                        } catch (Exception ex1) {

                        }
                    }

                }

                int notActivateSize = lstNotActivated.size();
                for (int i = 0; i < notActivateSize; i++) {
                    insertQueue(lstNotActivated.get(i));
                }

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        isInProgress = false;

    }

    private void alertUser(ISession aSession) {
        try {
            UserDB db = new UserDB();
            List<AlertUserEntity> lstAlerts = db.getAlertUser(aSession.getUID());

            int size = lstAlerts.size();
            if (size > 0) {

                Date dtNow = new Date();
                long lastActivate = dtNow.getTime();

                MessageFactory msgFactory = aSession.getMessageFactory();
                CommonQueue queue = new CommonQueue();
                for (int i = 0; i < size; i++) {
                    AlertUserEntity alertEntity = lstAlerts.get(i);
                    //SendAdvResponse advRes = (SendAdvResponse) msgFactory.getResponseMessage(MessagesID.SEND_ADV);
                    SendAdvResponse advRes = (SendAdvResponse) msgFactory.getResponseMessage(MessagesID.MSG_SYSTEM);
                    QueueEntity entity = new QueueEntity(aSession, advRes);

                    advRes.session = aSession;

                    entity.setSendMessage(false);
                    StringBuilder sb = new StringBuilder();
                    //        sb.append(Integer.toString(ADV_POPUP)).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(Integer.toString(ADV_POPUP)).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(alertEntity.getContent());

                    lastActivate = lastActivate + ACTIVATE_ADV_AFTER_LOGIN;
                    advRes.activateTime = lastActivate;
                    advRes.setSuccess(sb.toString(), ADV_POPUP);

                    queue.insertQueue(entity);

                }
            }
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }
    }

//    public void changeAdv()
//    {
//        try {
//            
//        	mLog.debug("Reload ADV:");
//            InfoDB.reloadAdv();
//            
////            mLog.debug("Reload superUser:");
////            Utils.reloadSuperUser();
//        }catch (Throwable e)  {
//            
//        }
//        count++;
//    }
}
