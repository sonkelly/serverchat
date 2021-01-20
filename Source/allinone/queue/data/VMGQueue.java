/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.queue.data;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.cardservice.CardServicePortTypeProxy;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.QueueUserEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.UseCardRequest;
import allinone.protocol.messages.UseCardResponse;

/**
 *
 * @author mcb
 */
public class VMGQueue implements Job {
//    private static Queue usrQueue = new ArrayDeque();

    private static ConcurrentHashMap<UUID, QueueUserEntity> usrQueue = new ConcurrentHashMap<UUID, QueueUserEntity>();
    private static Logger log = LoggerContext.getLoggerFactory().getLogger(VMGQueue.class);
    private static boolean isInProgress = false;
    
    private static final Object lockObj = new Object();
    
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private static final String PASSWORD_MANUAL = "abaceadsfsdfljiokjkladfdslkfdd";

    public void insertUser(QueueUserEntity entity) {
        try {
//            usrQueue.add(entity);
            UUID uuid = UUID.randomUUID();
            usrQueue.put(uuid, entity);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
@Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        if (!isInProgress) {
//            log.debug("begin execute send image to client job");
            isInProgress = true;

            try {

//                CacheFileInfo cacheFile = new CacheFileInfo();
                Enumeration<UUID> e = usrQueue.keys();

                while (e.hasMoreElements()) {
                    try {
                        QueueUserEntity queryEntity = null;
//                        synchronized(lockObj)
//                        {
//                            queryEntity = (QueueUserEntity)usrQueue.remove();
//                        }
                        UUID key = e.nextElement();

                        queryEntity = usrQueue.get(key);
                        usrQueue.remove(key);


                        if (queryEntity == null) {
                            continue;
                        }


                        ISession aSession = queryEntity.getSession();
                        if (aSession == null || aSession.isExpired()) {
                            continue;
                        }

                        if (queryEntity.getResponse() instanceof UseCardResponse) {
                            UseCardRequest cardRq = (UseCardRequest) queryEntity.getRequest();
                            UseCardResponse cardRes = (UseCardResponse) queryEntity.getResponse();
                            cardRes.setResponse(ResponseCode.FAILURE, AIOConstants.DEFAULT_ERROR_CARD);
                            
                            try {
                                CardServicePortTypeProxy proxy = new CardServicePortTypeProxy();

                                String serviceId = cardRq.serviceId;
                                String userName = aSession.getUserName();
                                String cardId = cardRq.cardId;
                                String cardCode = cardRq.cardCode;

                                String txnTime = format.format(new Date());
                                CacheUserInfo cache = new CacheUserInfo();
                                UserEntity usrEntity = cache.getUserInfo(aSession.getUID(), aSession.getRealMoney());
                                String partnerId = Integer.toString(usrEntity.partnerId);
                                String refCode = cardRq.refCode;

                                long userId = aSession.getUID();

                                String sig = userName + serviceId + cardCode + cardId + txnTime;
//                                sig = getSignature(sig);
                                try {
                                    // proxy.cardService(partnerId, serviceId, userName, cardId, cardCode, txnTime, sig);
                                    String response = proxy.cardRefCodeService(partnerId, PASSWORD_MANUAL, userId, refCode, userName, cardId, serviceId,
                                            cardCode, txnTime, sig);                                    

                                	log.debug(" Response message " + response);

                                	String message = "";
                                    cardRes.mCode = ResponseCode.FAILURE;

                                	if(response.equals("-1")) {
                                		message = "Mã thẻ hoặc số seri không đúng";
                                	} else if (response.equals("-2")) {
                                		message = "Mã thẻ hoặc số seri không đúng";
                                	} else if (response.equals("-3")) {
                                		message = "Thẻ đã được sử dụng";
                                    } else if (response.equals("-4")) {
                                    	message = "Mã thẻ hoặc số seri không đúng";
                                    } else if (response.equals("-5")) {
                                    	message = "Bạn đã nhập sai quá 3 lần, tài khoản bị khóa hết ngày";
                                    } else {
                                    	message = " Bạn đã nạp thành công " + response + " XU ";
                                    	cardRes.mCode = ResponseCode.SUCCESS;                                    	
                                    }
                                    cardRes.message = message;
                                    
                                } catch (RemoteException ex) {
                                    log.error(ex.getMessage(), ex);

                                }
                            } catch (Exception ex) {
                                log.error(ex.getMessage(), ex);
                            }

                            aSession.write(cardRes);
                        }

                    } catch (ServerException ex) {
                        log.error(ex.getMessage(), ex);
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }

                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
//             log.debug("end execute send image to client job");
        }
        isInProgress = false;
    }
    public static boolean containsString(String data, String value) {
    	
    	if (data == null) return false;
    	
    	if(data.contains(value)) {
    		return true;
    	} else {
    		return false;
    	}
    }
}
