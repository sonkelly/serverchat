/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.common;

import allinone.business.BusinessException;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.game.session.ISession;

/**
 *
 * @author vipgame10
 */
public class CommonUtils {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CommonUtils.class);

    public static Object clone(Object object) {
        try {
            Class<?> myClass = object.getClass();
            Object newObject = myClass.newInstance();
            Field[] allFields = myClass.getDeclaredFields();
            for (Field field : allFields) {
                if (field.getType() != ISession.class) {

                    field.setAccessible(true);
                    field.set(newObject, field.get(object));
                }
            }
            return newObject;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException e) {
            System.out.println(e);
            return null;
        }
    }

    public static Object cloneList(Object object) {
        try {
            Class<?> myClass = object.getClass();
            Object newObject = myClass.newInstance();
            Field[] allFields = myClass.getDeclaredFields();
            for (Field field : allFields) {
                if (field.getType() != ISession.class) {

                    field.setAccessible(true);
                    field.set(newObject, field.get(object));
                }
            }
            return newObject;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException e) {
            System.out.println(e);
            return null;
        }
    }

    public static int getRandom(Random random, int start, int end, List<Integer> ex) {

        int result = random.nextInt(end - start + 1) + start;
        if (ex.contains(result)) {
            return getRandom(random, start, end, ex);
        } else {
            return result;
        }

    }

    public static String convertToJson(Object object) {
        if (object != null) {
            Gson gson = new Gson();
            return gson.toJson(object);
        } else {
            return "";

        }
    }

    public static String doGet(String myUrl) throws Exception {
        mLog.debug(" Call api data ");

        String result = "";

        HttpURLConnection connection = null;

        URL url = new URL(myUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.setUseCaches(false);

        int HttpResult = connection.getResponseCode();
        StringBuilder sb = new StringBuilder();

        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

//            mLog.debug("Resulst content " + sb.toString());
            result = sb.toString();

        } else {
            mLog.debug(" Error post " + connection.getResponseMessage());
        }

        if (connection != null) {
            connection.disconnect();
        }

        return result;
    }

    // call request to iap server to validate
    public static String doPost(String myUrl, String jsonData) throws Exception {
        mLog.debug(" Call api data ");

        String dataResponse = "";

        HttpURLConnection connection = null;

        URL url = new URL(myUrl);
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

        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

//	        mLog.debug("Resulst content " + sb.toString());
            dataResponse = sb.toString();

//	        JSONObject resultData = new JSONObject(sb.toString());
//	        
//	        int result = resultData.getInt("status");
//	        
//	        mLog.debug(" result " + resultData.getString("status"));	        
//	        System.out.println(" result " + resultData.getString("status"));
//	        	        
//	        if(result == 0) {
//	        	dataResponse = resultData.getJSONObject("receipt").getString("product_id");
//		        mLog.debug(" productId " + dataResponse);	        
//		        System.out.println(" productId " + dataResponse);
//	        	
//	        } else {
//	        	throw new BusinessException("Dữ liệu không chính xác");
//	        }	        	        
        } else {
            mLog.debug(" Error post " + connection.getResponseMessage());
        }

        if (connection != null) {
            connection.disconnect();
        }

        return dataResponse;
    }

    public static boolean checkAccessJoinRoom(long roomMoney, int level) {
        if (roomMoney <= 2000l) {
            return true;
        } else if (roomMoney <= 5000l) {
            return level >= 5;
        } else if (roomMoney <= 10000l) {
            return level >= 10;
        } else if (roomMoney <= 50000l) {
            return level >= 15;
        } else if (roomMoney <= 100000l) {
            return level >= 20;
        } else if (roomMoney <= 5000000l) {
            return level >= 25;
        } else {
            return true;
        }

    }

    public static void main(String[] args) {
        Map<String, Object> dataRequest = new HashMap<>();

        dataRequest.put("uid", 104);
        dataRequest.put("zoneId", 4);
        dataRequest.put("isWin", true);

        try {
            String respone = doPost("http://localhost/MissionUser/updateEndgame", convertToJson(dataRequest));
            int a = 1;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
