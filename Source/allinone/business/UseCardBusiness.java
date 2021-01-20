package allinone.business;

import allinone.data.HTTPPoster;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.QueueUserEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.UseCardRequest;
import allinone.protocol.messages.UseCardResponse;
import allinone.queue.data.VMGQueue;
import java.net.URLEncoder;
import org.json.JSONObject;
import vn.game.common.MD5;
import vn.game.common.ServerException;
import vn.game.common.sendCommon;

public class UseCardBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog = LoggerContext.getLoggerFactory().getLogger(UseCardBusiness.class);
    private static final String SOON_SERVICE = "Dịch vụ này sớm ra mắt";
    private static final String TXN_IN_PROGRESS = "Giao dịch đang được xử lý. Tiền sẽ vào tài khoản nếu giao dịch thành công";
    private static final String BUSY_SYSTEM = "Hệ thống nạp tiền tạm thời ngừng dịch vụ";
    private static final String WRONG_SERVICE = "Sai dịch vụ";
    private static final String WRONG_INPUT_SERVICE = "Dữ liệu nhập vào có ký tự đặc biệt";
    private static final String WRONG_INPUT_CARD_ID = "Bạn cần phải nhập serial";
    private static final String WRONG_INPUT_CARD_CODE = "Bạn cần phải nhập mã thẻ";
    private static final String WRONG_INPUT_TYPE_MONEY = "Bạn cần phải chọn lại tiền nạp";

    private boolean containSpecialWord(String strChecker) {
        String WORD_DIGIT_PATTERN = "\\w{1,20}$";
        Pattern pattern = Pattern.compile(WORD_DIGIT_PATTERN);
        Matcher matcher = pattern.matcher(strChecker);
        return matcher.matches();
    }

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[Use Card] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        //UseCardResponse useCardRes = (UseCardResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        UseCardResponse useCardRes = (UseCardResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        UseCardRequest useCardRq = (UseCardRequest) aReqMsg;

        try {
            try {
                useCardRes.mCode = ResponseCode.FAILURE;
                useCardRes.session = aSession;

                if (!containSpecialWord(useCardRq.cardCode) || !containSpecialWord(useCardRq.cardId)) {
                    throw new BusinessException(WRONG_INPUT_SERVICE);
                }

                if (useCardRq.cardCode == null || useCardRq.cardCode.equals("")) {
                    throw new BusinessException(WRONG_INPUT_CARD_CODE);
                }

                if ((useCardRq.cardId == null || useCardRq.cardId.equals("")) && !useCardRq.serviceId.equals("mobifone")) {
                    throw new BusinessException(WRONG_INPUT_CARD_ID);
                }
                if (useCardRq.refCode == null || useCardRq.refCode.equals("")) {
                    throw new BusinessException(WRONG_INPUT_TYPE_MONEY);
                }

                if (useCardRq.serviceId.equals("viettel") || useCardRq.serviceId.equals("mobifone")
                        || useCardRq.serviceId.equals("vinaphone") || useCardRq.serviceId.equals("vcoin") || useCardRq.serviceId.equals("gate")) {
                    mLog.debug(" Validate cash ");

                    CacheUserInfo cache = new CacheUserInfo();
                    UserEntity entity = cache.getUserInfo(aSession.getUID(), aSession.getRealMoney());
                    //useCardRq.refCode = Integer.toString(entity.refCode);
                    //add by zep
                    try {
                        long userId = aSession.getUID();
                        String username = aSession.getUserName();
                        String ip = aSession.getIP();
                        //$mySign = $userId . "|" . $userName . "|" . $partnerId . "|" . $serviceId . "|" . $cardId . "|" . $cardCode . "|" . $ip."|";
                        int partnerId = 9;
                        String signStr = userId + "|" + username + "|" + partnerId + "|" + useCardRq.serviceId + "|" + useCardRq.cardId + "|" + useCardRq.cardCode + "|" + URLEncoder.encode(ip, "UTF-8") + "|" + "23aasd22#4@1490kdj34&^45u234";
                        //String sign = MD5.toMD5(signStr);
                        String sign = MD5.toMD5(signStr);

                        StringBuffer url = new StringBuffer();
                        url.append("?").append("partnerId=").append(partnerId);
                        url.append("&serviceId=").append(URLEncoder.encode(useCardRq.serviceId, "UTF-8"));
                        url.append("&userId=").append(userId);
                        url.append("&userName=").append(URLEncoder.encode(username, "UTF-8"));

                        url.append("&cardId=").append(URLEncoder.encode(useCardRq.cardId, "UTF-8"));
                        url.append("&cardCode=").append(URLEncoder.encode(useCardRq.cardCode, "UTF-8"));
                        url.append("&ip=").append(URLEncoder.encode(ip, "UTF-8"));
                        url.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
                        url.append("&typeMoney=").append(useCardRq.refCode);
                        url.append("&isGameBai=").append(1);
                        url.append("&menhgia=").append(useCardRq.menhgia);
                        url.append("&refCode=").append(useCardRq.refCode);
                        HTTPPoster p = new HTTPPoster();
                        p.setParameCall(HTTPPoster.API_PAYMENT_CARD);
                        //mLog.debug("url.toString()" + url.toString());
                        String res = p.callGet(url.toString());
                        if (!res.equals("")) {
                            //mLog.debug("res" + res);

                            JSONObject json = new JSONObject(res);
                            //mLog.debug("json" + json);
                            //if (json != null) {
                            int error = json.getInt("error");

                            String message = json.getString("msg");

                            useCardRes.message = message;
                            if (error == 0) {
                                long currentMoney = json.getLong("currentMoney");
                                useCardRes.mCode = ResponseCode.SUCCESS;
                                if (aSession.getIsRealMoney()) {
                                    aSession.getUserEntity().realmoney = currentMoney;
                                } else {
                                    aSession.getUserEntity().money = currentMoney;
                                }
                            } else if (error == 1000000) {//cho vao cho xu ly nap card thanh cong
                                int cardtxnid = 0;
                                try {
                                    cardtxnid = json.getInt("cardtxnid");
                                } catch (Exception e) {
                                }
                                if (cardtxnid > 0) {
                                    aSession.setWaitRecharge(cardtxnid);
                                }

                            } else {
                                useCardRes.mCode = ResponseCode.FAILURE;
                            }

                            useCardRes.setResponse(useCardRes.mCode, message);
                            //Send update card
                            sendCommon.sendUpdateMoney(aSession, msgFactory);
                            //}
                        } else {
                            useCardRes.setResponse(ResponseCode.SUCCESS, "Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
                        }
                    } catch (Exception e) {

                    }

                } else {
                    useCardRes.message = WRONG_SERVICE;
                }
                return 1;
            } catch (BusinessException be) {
                useCardRes.message = be.getMessage();
            }
        } finally {
            if ((useCardRes != null)) {
                aResPkg.addMessage(useCardRes);
            }
        }
        return 1;
    }
}
