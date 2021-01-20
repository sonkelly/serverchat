/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data.languages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.game.common.ConfigProperties;
import vn.game.common.ServerException;

/**
 *
 * @author mac
 */
public class Languages {

    private static Languages _instance;
    private static HashMap<String, Languages> _instances = new HashMap<>();
    private final ConfigProperties Lang;
    final String DEFAULT_WORKFLOW_CONFIG = "languages/";

    public static Languages instance(String lang) {

        Languages languages= _instances.get(lang);
        
        if (languages == null) {

            languages = new Languages(lang);
            _instances.put(lang, languages);
        }

        return languages;
    }

    public Languages(String country) {
        String[] avaiable = {"vn","en"};
        boolean contains = Arrays.stream(avaiable).anyMatch(country::equals);
        if (!contains) {
            country = "vn";
        }
        this.Lang = new ConfigProperties();
        try {

            this.Lang.load(DEFAULT_WORKFLOW_CONFIG + country + ".xml");
        } catch (ServerException ex) {
            try {
                this.Lang.load(DEFAULT_WORKFLOW_CONFIG + "vn.xml");
            } catch (ServerException ex1) {
                Logger.getLogger(Languages.class.getName()).log(Level.SEVERE, null, ex1);
            }
           
            Logger.getLogger(Languages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getString(String text) {
        return this.Lang.getString("lang." + text);
    }

    public static void main(String[] args) throws Exception {
        String kichhoattk = Languages.instance("en").getString("kichhoattkGiftcode");
        System.out.println("kichhoattk:" + kichhoattk);
//        // Creating a new locale 
//        Locale first_locale
//                = new Locale("VIETNAM", "VN");
//
//        // Displaying first locale 
//        System.out.println("First Locale: "
//                + first_locale);
//
//        // Displaying the country_code of this locale 
//        System.out.println("Country: "
//                + first_locale.getCountry());
    }
}
