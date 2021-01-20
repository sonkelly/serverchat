/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import allinone.data.HTTPPoster;
import allinone.databaseDriven.UserDB;
import allinone.server.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.game.common.MD5;

/**
 *
 * @author mac
 */
public class NodeBBBusiness {

    public static void createUserNodeBB(long uid, String username, String password) {
        String signStr = uid + "|" + username + "|" + password + "|" + "23aasd22#4@1490kdj34&^45u234";
        String sign = MD5.toMD5(signStr);

        StringBuffer url = new StringBuffer();
        url.append("").append("userId=" + uid);
        url.append("&username=").append(username);
        url.append("&password=").append(password);

        String domain = Server.isNodeBB;
        //System.out.println("callPost:"+domain);
        try {
            URL obj = new URL(domain);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setConnectTimeout(4000); //set timeout to 5 seconds
            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();

            os.write(url.toString().getBytes());

            os.flush();
            os.close();
            // For POST only - END

            int responseCode = con.getResponseCode();

            StringBuffer response = new StringBuffer();
            //System.out.println("responseCode:" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
            } else {

            }

           // System.out.println("response.toString():" + response.toString());
        } catch (java.net.SocketTimeoutException e) {
            System.out.println("e:" + e.getMessage());
        } catch (java.io.IOException e) {
            System.out.println("e2:" + e.getMessage());
        }

    }
}
