package allinone.databaseDriven;


public class DBAdmin
        extends DBBase {

    private static DBAdmin _instance;

    public static DBAdmin instance() {
    
        if (_instance == null) {
        
            _instance = new DBAdmin("dbAdmin.properties");
        }
      
        return _instance;
    }


    public DBAdmin(String str) {
        super(str);
      
    }

}
