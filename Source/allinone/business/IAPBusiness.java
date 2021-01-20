package allinone.business;

import allinone.data.HTTPPoster;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.IAPRequest;
import allinone.protocol.messages.IAPResponse;
import java.net.URLEncoder;
import tools.CacheUserInfo;
import vn.game.common.MD5;
import vn.game.common.sendCommon;

public class IAPBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(IAPBusiness.class);
    private static final String IOS_IAP_VALIDATE_TEST_PATH = "https://sandbox.itunes.apple.com/verifyReceipt";
    private static final String IOS_IAP_VALIDATE_PRODUCT_PATH = "https://buy.itunes.apple.com/verifyReceipt";
    
    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        
        IAPResponse response = (IAPResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        IAPRequest request = (IAPRequest) aReqMsg;

        mLog.debug("[GET CHARGING]: IAPBusiness");
        
        try {
            String receiptData = request.receiptData;
            String type = request.type;
            
            if(receiptData == null || "".equals(receiptData)) {
            	throw new BusinessException("Dữ liệu không hợp lệ");
            }
            
//            JSONObject data = new JSONObject();
//            data.put("receipt-data", receiptData);
//            
//            String productId = doPost(data.toString());
//            
//            if("".equals(productId)) {
//            	throw new BusinessException("Dữ liệu không hợp lệ");
//            }
           
            
            // add money for user
            //UserDB userDB = new UserDB();
            
//            int money = userDB.useIAP(aSession.getUID(), receiptData);
//            
////            int money = 10000;
//            
//            response.setSuccess(ResponseCode.SUCCESS, String.valueOf(money));
            if (!receiptData.equals("")) {
                    mLog.debug(" Validate cash ");

                    //add by zep
                    try {
                        long userId = aSession.getUID();
                        String username = aSession.getUserName();
                        String ip = aSession.getIP();
                        //$mySign = $userId . "|" . $userName . "|" . $partnerId . "|" . $serviceId . "|" . $cardId . "|" . $cardCode . "|" . $ip."|";
                        int partnerId = 9;
                        String signStr = userId + "|" + "23aasd22#4@1490kdj34&^45u234";
                        //String sign = MD5.toMD5(signStr);
                        String sign = MD5.toMD5(signStr);

                        StringBuffer url = new StringBuffer();
                        url.append("?").append("uid=").append(userId);
                        url.append("&sign=").append(sign);
                         url.append("&type=").append(type);
                        url.append("&token=").append(receiptData);
                        url.append("&userName=").append(URLEncoder.encode(username, "UTF-8"));

                        url.append("&ip=").append(URLEncoder.encode(ip, "UTF-8"));
                       
                        url.append("&typeMoney=").append(2);
                       
                        HTTPPoster p = new HTTPPoster();
                        p.setParameCall("service-iap-google");
                        mLog.debug("url.toString()" + url.toString());
                        String res = p.callGet(url.toString());
                        if (!res.equals("")) {
                            mLog.debug("res" + res);

                            JSONObject json = new JSONObject(res);
                            mLog.debug("json" + json);
                            //if (json != null) {
                            int error = json.getInt("error");

                            String message = json.getString("msg");

                            response.errMgs = message;
                            if (error == 0) {
                                long currentMoney = json.getLong("currentMoney");
                                response.mCode = ResponseCode.SUCCESS;
                                UserDB userDb = new UserDB();
                                long moneyDB = userDb.getMoney(aSession.getUserEntity().mUid, aSession.getRealMoney());
                                if (aSession.getIsRealMoney()) {
                                    aSession.getUserEntity().realmoney = moneyDB;
                                } else {
                                    aSession.getUserEntity().money = moneyDB;
                                }
                                response.setSuccess(ResponseCode.SUCCESS, String.valueOf(moneyDB));
                            } else {
                                response.mCode = ResponseCode.FAILURE;
                                response.setFailure(ResponseCode.SUCCESS, "Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
                            }

                           
                            //Send update card
                            sendCommon.sendUpdateMoney(aSession, msgFactory);
                            //}
                        } else {
                            response.setFailure(ResponseCode.SUCCESS, "Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
                        }
                    } catch (Exception e) {

                    }

                } else {
                    response.setFailure(ResponseCode.FAILURE, "lỗi nạp inapp");
                }
        } catch (Throwable t) {
        	response.setFailure(ResponseCode.FAILURE, t.getMessage());
        } finally {
            aResPkg.addMessage(response);
        }

        return 1;
    }
    
    // call request to iap server to validate
    public String doPost(String jsonData) throws Exception {
    	mLog.debug(" Call api data ");
    	
    	String productId = "";
    	
        HttpURLConnection connection = null;
    	
        URL url = new URL(IOS_IAP_VALIDATE_TEST_PATH);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.setUseCaches(false);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(jsonData);
        wr.flush();
        wr.close();
        
        int HttpResult = connection.getResponseCode(); 
        StringBuilder sb = new StringBuilder();  
        
        if(HttpResult == HttpURLConnection.HTTP_OK){
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));  
	        String line = "";
	        while ((line = br.readLine()) != null) {  
	            sb.append(line + "\n");  
	        }  
	        br.close();  
	        
//	        mLog.debug("Resulst content " + sb.toString());
	        
	        JSONObject resultData = new JSONObject(sb.toString());
	        
	        int result = resultData.getInt("status");
	        
	        mLog.debug(" result " + resultData.getString("status"));	        
	        System.out.println(" result " + resultData.getString("status"));
	        	        
	        if(result == 0) {
	        	productId = resultData.getJSONObject("receipt").getString("product_id");
		        mLog.debug(" productId " + productId);	        
		        System.out.println(" productId " + productId);
	        	
	        } else {
	        	throw new BusinessException("Dữ liệu không chính xác");
	        }	        	        
	       
        } else {
        	mLog.debug(" Error post " + connection.getResponseMessage());
        }
        
        if(connection != null) {
            connection.disconnect(); 
        }
        
    	return productId;    	
    }
    
    public static void main(String[] args) throws Exception {
    	JSONObject data = new JSONObject();
//    	String dataorg    = "7b0a0922 7369676e 61747572 6522203d 20224171 2b62344c 62784943 52666844 6f2b5965 73354f64 36533077 464d512f 72736e6a 58476848 52465335 67485165 64684a57 57586950 506e4641 3574635a 6263544d 504b4e68 67335132 43725471 47616545 484a776d 37383344 2b613569 72623478 637a5535 6e686c4d 32356b53 71554b4c 616d3231 776f6450 59323750 55395754 58644b66 324b324d 4972487a 6e486954 55566e57 2b754c31 46523543 386c514e 396e7535 48594e2b 50344141 4144567a 43434131 4d776767 49376f41 4d434151 49434342 7570342b 5041686d 2f4c4d41 30474353 71475349 62334451 45424251 55414d48 3878437a 414a4267 4e564241 5954416c 56544d52 4d774551 59445651 514b4441 70426348 42735a53 424a626d 4d754d53 59774a41 59445651 514c4442 31426348 42735a53 42445a58 4a306157 5a705932 46306157 39754945 46316447 6876636d 6c306554 457a4d44 45474131 55454177 77715158 42776247 55676156 5231626d 567a4946 4e306233 4a6c4945 4e6c636e 52705a6d 6c6a5958 52706232 34675158 56306147 39796158 52354d42 34584454 45304d44 59774e7a 41774d44 49794d56 6f584454 45324d44 55784f44 45344d7a 457a4d46 6f775a44 456a4d43 45474131 55454177 77615548 56795932 68686332 56535a57 4e6c6158 42305132 56796447 6c6d6157 4e686447 5578477a 415a4267 4e564241 734d456b 46776347 786c4947 6c556457 356c6379 42546447 39795a54 45544d42 45474131 55454367 774b5158 42776247 55675357 356a4c6a 454c4d41 6b474131 55454268 4d435656 4d77675a 38774451 594a4b6f 5a496876 634e4151 45424251 41446759 30414d49 474a416f 4742414d 6d544575 4c676a69 6d4c7752 4a787931 6f456630 6573554e 44564549 65367744 736e6e61 6c313468 4e427431 76313935 58366e39 33594f37 6769336f 72505375 78394435 3534536b 4d702b53 61796738 346c5463 33363255 746d594c 70576e62 33346e71 79477839 4b425654 79354f47 56346c6a 45314f77 432b6f54 6e524d2b 514c5243 6d654e78 4d62505a 68533437 542b655a 74444568 56423975 736b332b 4a4d3243 6f676677 6f374167 4d424141 476a636a 42774d42 30474131 55644467 51574242 534a6145 654e7571 39446636 5a664e36 3846652b 49327532 32737344 414d4267 4e564852 4d424166 3845416a 41414d42 38474131 55644977 51594d42 61414644 5964364f 4b646774 4942474c 55796177 37585177 75525745 4d364d41 34474131 55644477 45422f77 51454177 49486744 41514267 6f71686b 69473932 4e6b4267 55424241 49464144 414e4267 6b71686b 69473977 30424151 55464141 4f434151 45416561 4a563255 35317278 66637141 41653543 322f6645 57384b55 6c34694f 346c4d75 7461374e 36587a50 31705a49 7a314e6b 6b437449 49776579 4e6a3555 5259484b 2b486a52 4b535539 524c6775 4e6c306e 6b667871 4f62694d 636b7752 75644b53 7136394e 496e725a 79434436 3652344b 37376e62 396c4d54 41425353 596c734b 74386f4e 746c6867 522f316b 6a535352 5163486b 74734463 53695147 4b4d646b 536c7034 41795866 37766e48 50426534 79437759 56325070 534e3034 6b626f69 4a337042 6c787347 77562f5a 6c4c3236 4d327565 59484b59 43755868 64714677 7856676d 35326833 6f654a4f 4f742f76 59344563 51713765 71486d36 6d30335a 39623750 527a594d 324b4758 48446d4f 4d6b3776 4470654d 566c4c44 50534759 7a312b55 33734478 4a7a6562 53706261 4a6d5437 696d7a55 4b666767 45593778 78663463 7a664830 796a3577 4e7a5347 544f7651 3d3d223b 0a092270 75726368 6173652d 696e666f 22203d20 2265776f 4a496d39 79615764 70626d46 734c5842 31636d4e 6f59584e 6c4c5752 68644755 7463484e 30496941 39494349 794d4445 314c5441 334c5449 77494449 774f6a4d 324f6a49 7a494546 745a584a 70593245 76544739 7a583046 755a3256 735a584d 694f776f 4a496e56 75615846 315a5331 705a4756 7564476c 6d615756 79496941 39494349 34596a42 6a595749 314f5442 6a4e6d51 314d7a55 3159574d 7a4e5755 794e5745 354d5755 7a4e544d 31596a6b 795a4464 69595467 7a496a73 4b43534a 76636d6c 6e615735 68624331 30636d46 75633246 6a64476c 76626931 705a4349 67505341 694d5441 774d4441 774d4445 324e444d 794d5441 794d5349 3743676b 69596e5a 79637949 67505341 694e6949 3743676b 6964484a 68626e4e 68593352 70623234 74615751 69494430 67496a45 774d4441 774d4441 784e6a51 7a4d6a45 774d6a45 694f776f 4a496e46 31595735 30615852 35496941 39494349 78496a73 4b43534a 76636d6c 6e615735 68624331 7764584a 6a614746 7a5a5331 6b595852 6c4c5731 7a496941 39494349 784e444d 334e4451 354e7a67 7a4d4449 30496a73 4b43534a 31626d6c 78645755 74646d56 755a4739 794c576c 6b5a5735 3061575a 705a5849 69494430 67496a4d 344f5459 304d6a49 794c5459 33517a63 744e4455 32515331 424e3059 7a4c546c 434d3056 4452455a 474e4456 44516949 3743676b 6963484a 765a4856 6a644331 705a4349 67505341 69593239 744c6e5a 745a7935 30645846 31655335 55614755 78496a73 4b43534a 70644756 744c576c 6b496941 39494349 784d4449 774f446b 314e6a51 78496a73 4b43534a 69615751 69494430 67496d4e 76625335 6e595731 6c596d46 704c6e52 31635856 35496a73 4b43534a 7764584a 6a614746 7a5a5331 6b595852 6c4c5731 7a496941 39494349 784e444d 334e4451 354e7a67 7a4d4449 30496a73 4b43534a 7764584a 6a614746 7a5a5331 6b595852 6c496941 39494349 794d4445 314c5441 334c5449 78494441 7a4f6a4d 324f6a49 7a494556 30597939 48545651 694f776f 4a496e42 31636d4e 6f59584e 6c4c5752 68644755 7463484e 30496941 39494349 794d4445 314c5441 334c5449 77494449 774f6a4d 324f6a49 7a494546 745a584a 70593245 76544739 7a583046 755a3256 735a584d 694f776f 4a496d39 79615764 70626d46 734c5842 31636d4e 6f59584e 6c4c5752 68644755 69494430 67496a49 774d5455 744d4463 744d6a45 674d444d 364d7a59 364d6a4d 67525852 6a4c3064 4e564349 37436e30 3d223b0a 0922656e 7669726f 6e6d656e 7422203d 20225361 6e64626f 78223b0a 0922706f 6422203d 20223130 30223b0a 09227369 676e696e 672d7374 61747573 22203d20 2230223b 0a7d";
    	String dataBase64 = "ewoJInNpZ25hdHVyZSIgPSAiQW9mNTBBZVF1b3lOZGl6S0ZEWFJqNSt1djE0NXdwcFdraUVHMmlnbEFpM0JpKzFiWWtWSU1raGIwRkNiNFBxTEd2WHFxU1dJcWRyTmEvNTB5RTBuUlRBV2J1NUVPNUxwdjRqOWtQZjBXNkg2aE52Z01ZNmtOZ1ZXMnIwdTJNK3VwY090cGc0OGx6blZ5a2RrRG13SjRGZmx2UXdDTEVTT0ZnQm05c09LeFlKS0FBQURWekNDQTFNd2dnSTdvQU1DQVFJQ0NHVVVrVTNaV0FTMU1BMEdDU3FHU0liM0RRRUJCUVVBTUg4eEN6QUpCZ05WQkFZVEFsVlRNUk13RVFZRFZRUUtEQXBCY0hCc1pTQkpibU11TVNZd0pBWURWUVFMREIxQmNIQnNaU0JEWlhKMGFXWnBZMkYwYVc5dUlFRjFkR2h2Y21sMGVURXpNREVHQTFVRUF3d3FRWEJ3YkdVZ2FWUjFibVZ6SUZOMGIzSmxJRU5sY25ScFptbGpZWFJwYjI0Z1FYVjBhRzl5YVhSNU1CNFhEVEE1TURZeE5USXlNRFUxTmxvWERURTBNRFl4TkRJeU1EVTFObG93WkRFak1DRUdBMVVFQXd3YVVIVnlZMmhoYzJWU1pXTmxhWEIwUTJWeWRHbG1hV05oZEdVeEd6QVpCZ05WQkFzTUVrRndjR3hsSUdsVWRXNWxjeUJUZEc5eVpURVRNQkVHQTFVRUNnd0tRWEJ3YkdVZ1NXNWpMakVMTUFrR0ExVUVCaE1DVlZNd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFNclJqRjJjdDRJclNkaVRDaGFJMGc4cHd2L2NtSHM4cC9Sd1YvcnQvOTFYS1ZoTmw0WElCaW1LalFRTmZnSHNEczZ5anUrK0RyS0pFN3VLc3BoTWRkS1lmRkU1ckdYc0FkQkVqQndSSXhleFRldngzSExFRkdBdDFtb0t4NTA5ZGh4dGlJZERnSnYyWWFWczQ5QjB1SnZOZHk2U01xTk5MSHNETHpEUzlvWkhBZ01CQUFHamNqQndNQXdHQTFVZEV3RUIvd1FDTUFBd0h3WURWUjBqQkJnd0ZvQVVOaDNvNHAyQzBnRVl0VEpyRHRkREM1RllRem93RGdZRFZSMFBBUUgvQkFRREFnZUFNQjBHQTFVZERnUVdCQlNwZzRQeUdVakZQaEpYQ0JUTXphTittVjhrOVRBUUJnb3Foa2lHOTJOa0JnVUJCQUlGQURBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQUVhU2JQanRtTjRDL0lCM1FFcEszMlJ4YWNDRFhkVlhBZVZSZVM1RmFaeGMrdDg4cFFQOTNCaUF4dmRXLzNlVFNNR1k1RmJlQVlMM2V0cVA1Z204d3JGb2pYMGlreVZSU3RRKy9BUTBLRWp0cUIwN2tMczlRVWU4Y3pSOFVHZmRNMUV1bVYvVWd2RGQ0TndOWXhMUU1nNFdUUWZna1FRVnk4R1had1ZIZ2JFL1VDNlk3MDUzcEdYQms1MU5QTTN3b3hoZDNnU1JMdlhqK2xvSHNTdGNURXFlOXBCRHBtRzUrc2s0dHcrR0szR01lRU41LytlMVFUOW5wL0tsMW5qK2FCdzdDMHhzeTBiRm5hQWQxY1NTNnhkb3J5L0NVdk02Z3RLc21uT09kcVRlc2JwMGJzOHNuNldxczBDOWRnY3hSSHVPTVoydG04bnBMVW03YXJnT1N6UT09IjsKCSJwdXJjaGFzZS1pbmZvIiA9ICJld29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREV6TFRBM0xURXdJREV5T2pNek9qVXhJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0l3TURBd1lqQXlNVGczWWpnaU93b0pJbTl5YVdkcGJtRnNMWFJ5WVc1ellXTjBhVzl1TFdsa0lpQTlJQ0l4TURBd01EQXdNRGd3TURFek9EY3lJanNLQ1NKaWRuSnpJaUE5SUNJeExqQXpJanNLQ1NKMGNtRnVjMkZqZEdsdmJpMXBaQ0lnUFNBaU1UQXdNREF3TURBNE1ERTJNVGcxTlNJN0Nna2ljWFZoYm5ScGRIa2lJRDBnSWpFaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFek56TTBPRFE0TXpFd01EQWlPd29KSW5WdWFYRjFaUzEyWlc1a2IzSXRhV1JsYm5ScFptbGxjaUlnUFNBaVF6aENRakJETmtNdE5UTTVOeTAwUTBVMkxUbEdRell0TVVJeE1EYzJSRU5FT0RFeElqc0tDU0p3Y205a2RXTjBMV2xrSWlBOUlDSlFVRjh3TURFaU93b0pJbWwwWlcwdGFXUWlJRDBnSWpZMk9EVXdNVFV4TmlJN0Nna2lZbWxrSWlBOUlDSnVlaTVqYnk1d2NtOXdhRzkwYjJkeVlYQm9aWEl1WVhCd0lqc0tDU0p3ZFhKamFHRnpaUzFrWVhSbExXMXpJaUE5SUNJeE16Y3pOVFl6TXpjM09UTXlJanNLQ1NKd2RYSmphR0Z6WlMxa1lYUmxJaUE5SUNJeU1ERXpMVEEzTFRFeElERTNPakl5T2pVM0lFVjBZeTlIVFZRaU93b0pJbkIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERXpMVEEzTFRFeElERXdPakl5T2pVM0lFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVWlJRDBnSWpJd01UTXRNRGN0TVRBZ01UazZNek02TlRFZ1JYUmpMMGROVkNJN0NuMD0iOwoJImVudmlyb25tZW50IiA9ICJTYW5kYm94IjsKCSJwb2QiID0gIjEwMCI7Cgkic2lnbmluZy1zdGF0dXMiID0gIjAiOwp9";
    	data.put("receipt-data",dataBase64);
//    	data.put("password","a2ce0e754e924a669cf20befce89709e");
    	System.out.println(" OK Data " + data.toString());
    	new IAPBusiness().doPost(data.toString());
    }
    
}
