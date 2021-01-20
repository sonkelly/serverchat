package allinone.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import org.slf4j.Logger;
import vn.game.common.LoggerContext;
import vn.game.common.getConfigGame;

public class HTTPPoster {

    //public static final String HOME_HTTP = "http://209.58.167.90:8899/apivip/";
    public static String HOME_HTTP = "";
    public static final String AVATAR_HTTP = "http://ip40.api1chan.info/img/avatars/";

    //public static final String FIXBAI_HTTP = "http://ec2-52-221-222-96.ap-southeast-1.compute.amazonaws.com/ktcAdmin2018_k_tc/";
    public static String FIXBAI_HTTP = "";
    public static final String ORDER_ITEM = "service-order-item";
    public static final String CANCEL_ORDER_ITEM = "cancel-order";
    public static final String API_PAYMENT_CARD = "service-payment";
    public static final String API_GIFT_CODE = "gift-code";
    public static final String API_EVENTS = "api-events";
    public static final String API_NEWS = "api-events/news";
    public static final String API_SYS_MSG = "api-msgsys";
    public static final String API_FIX_BAI = "fixbai-api";
    public static final String API_FIX_BAI_TLMN = "fixbai-tlmn-api";
    public static final String API_FIX_BAI_SAM = "fixbai-sam-api";
    public static final String API_TRANFER_MONEY = "api-tranfer-money";
    public static final String API_HU_SPIN = "api-hu-rong-spin";
    public static final String API_HU_LIXI = "api-hu-lixi";
    public static final String API_ACCOUNT_KIT = "service-otp";
    public static final String API_LOG_ADMOB = "LogAdmod";
    public static final String API_MINIPOKER_SPIN = "mini-poker-spin";
    public static final String API_VQMN_SPIN = "vqmn-spin";
    public static final String API_GIFT_MONEY_AUTO = "service-gift-money-auto";
    private String parame_call;

    /**
     * Sends an HTTP GET request to a url
     *
     * @param endpoint - The URL of the server. (Example: "
     * http://www.yahoo.com/search")
     * @param requestParameters - all the request parameters (Example:
     * "param1=val1&param2=val2"). Note: This method will add the question mark
     * (?) to the request - DO NOT add it yourself
     * @return - The response from the end point
     */
    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(HTTPPoster.class);

    public HTTPPoster() {

        if (HTTPPoster.HOME_HTTP.equals("")) {
            getConfigGame gcg = new getConfigGame();
            HTTPPoster.HOME_HTTP = gcg.apiUrl();

        }
//        if (FIXBAI_HTTP.equals("")) {
//           
//            getConfigGame gcg = new getConfigGame();
//            FIXBAI_HTTP = gcg.apiUrlFixBai();
//            
//
//        }
    }

    public static String getHomeHTTP() {
        if (HTTPPoster.HOME_HTTP.equals("")) {
            getConfigGame gcg = new getConfigGame();
            HTTPPoster.HOME_HTTP = gcg.apiUrl();

        }
        return HOME_HTTP;
    }

    public String sendGetRequest(String endpoint,
            String requestParameters) {
        String result = null;
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")
                || endpoint.startsWith("https%3A%2F%2Fwww%2E")) {
            try {

                // Send data
                String urlStr = endpoint;
                if (requestParameters != null && requestParameters.length() > 0) {
                    urlStr += "?" + requestParameters;
                }
                URL url = new URL(urlStr);
                //System.out.println(urlStr);
                URLConnection conn = url.openConnection();

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                result = sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Reads data from the data reader and posts it to a server via POST
     * request. data - The data you want to send endpoint - The server's address
     * output - writes the server's response to output
     *
     * @throws Exception
     */
    public void postData(Reader data, URL endpoint, Writer output)
            throws Exception {
        HttpURLConnection urlc = null;
        try {
            urlc = (HttpURLConnection) endpoint.openConnection();
            try {
                urlc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new Exception(
                        "Shouldn't happen: HttpURLConnection doesn't support POST??",
                        e);
            }
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml; charset="
                    + "UTF-8");

            OutputStream out = urlc.getOutputStream();

            try {
                Writer writer = new OutputStreamWriter(out, "UTF-8");
                pipe(data, writer);
                writer.close();
            } catch (IOException e) {
                throw new Exception("IOException while posting data", e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }

            InputStream in = urlc.getInputStream();
            try {
                Reader reader = new InputStreamReader(in);
                pipe(reader, output);
                reader.close();
            } catch (IOException e) {
                throw new Exception("IOException while reading response", e);
            } finally {
                if (in != null) {
                    in.close();
                }
            }

        } catch (IOException e) {
            throw new Exception("Connection error (is server running at "
                    + endpoint + " ?): " + e);
        } finally {
            if (urlc != null) {
                urlc.disconnect();
            }
        }
    }

    /**
     * Pipes everything from the reader to the writer via a buffer
     */
    private void pipe(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[1024];
        int read = 0;
        while ((read = reader.read(buf)) >= 0) {
            writer.write(buf, 0, read);
        }
        writer.flush();
    }

    /**
     * @return the userId
     */
    public String getParameCall() {
        return parame_call;
    }

    /**
     * @param userId the userId to set
     */
    public void setParameCall(String parame_call) {
        this.parame_call = parame_call;
    }

    public synchronized String callPost(String url) throws IOException {
        String domain = HOME_HTTP + this.getParameCall();
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

            os.write(url.getBytes());

            os.flush();
            os.close();
            // For POST only - END

            int responseCode = con.getResponseCode();

            StringBuffer response = new StringBuffer();
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

            return response.toString();
        } catch (java.net.SocketTimeoutException e) {
            return "{}";
        } catch (java.io.IOException e) {
            return "{}";
        }

    }

    public synchronized String callGet(String Parame) throws IOException {
        String domain = HOME_HTTP + this.getParameCall() + Parame;

        //System.out.println("domain get:"+domain);
        try {
            URL obj = new URL(domain);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setConnectTimeout(4000); //set timeout to 5 seconds

            int responseCode = con.getResponseCode();

            StringBuffer response = new StringBuffer();
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

            return response.toString();
        } catch (java.net.SocketTimeoutException e) {
            return "{}";
        } catch (java.io.IOException e) {
            return "{}";
        }

//        String endpoint = HOME_HTTP + this.getParameCall() + Parame;
//        URL url = new URL(endpoint.toString());
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(url.openStream()));
//
//        String inputLine;
//        String rs = "";
//        while ((inputLine = in.readLine()) != null) {
//            if (!inputLine.equalsIgnoreCase("")) {
//                rs = inputLine;
//                break;
//
//            }
//
//        }
//        in.close();
//
//        return rs;
    }

    //dung cho fix bai
    public synchronized String callPostFixBai(String url) throws IOException {
        String domain = FIXBAI_HTTP + this.getParameCall();

        URL obj = new URL(domain);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();

        os.write(url.getBytes());

        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();

        StringBuffer response = new StringBuffer();
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

        return response.toString();
    }

}
