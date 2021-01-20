/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

import allinone.business.RegisterBusiness;
import static allinone.data.SimpleTable.mLog;
import allinone.server.Server;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.game.common.LoggerContext;
/**
 *
 * @author mac
 */
public class CountryEntity {
   
    public String getCountryByIP(String ip) throws UnknownHostException, IOException, GeoIp2Exception {
        String countryCode = "";
        String path = Server.basePath + "/lib/GeoLite2-City.mmdb";
        mLog.debug("path:"+path);
        File dbFile = new File(path);
        //File dbFile = new File("/Users/mac/Projects/VipGame/GameServer/GameServerVip/lib/GeoLite2-City.mmdb");
        // Tạo ra một đối tượng đọc Database.
        try {
            // nó có thể tái sử dụng trong các lần tra cứu.
            DatabaseReader reader = null;
            try {
                reader = new DatabaseReader.Builder(dbFile).build();
            } catch (IOException ex) {
                //Logger.getLogger(NLogin.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Một địa chỉ IP
            InetAddress ipAddress = null;
            try {
                ipAddress = InetAddress.getByName(ip);
            } catch (Exception ex) {
                //Logger.getLogger(NLogin.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Thông tin định vị thành phố ứng với địa chỉ IP.
            CityResponse response = null;
            try {
                response = reader.city(ipAddress);
            } catch (Exception ex) {
                // Logger.getLogger(NLogin.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Thông tin quốc gia
            Country country = response.getCountry();

            System.out.println(country.getIsoCode());            // 'US'
            System.out.println(country.getName());               // 'United States'

            countryCode = country.getIsoCode();

//            City city = response.getCity();
//            System.out.println(city.getName());       // 'Minneapolis'
//
//            Postal postal = response.getPostal();
//            System.out.println(postal.getCode());       // '55455'
//
//            Location location = response.getLocation();
//            System.out.println(location.getLatitude());        // 44.9733
//            System.out.println(location.getLongitude());       // -93.2323

        } catch (Exception e) {

        }
        return countryCode.toLowerCase();
    }

    public static void main(String[] args) throws IOException, UnknownHostException, GeoIp2Exception {
        CountryEntity ob = new CountryEntity();
        ob.getCountryByIP("42.113.220.101");
    }

}
