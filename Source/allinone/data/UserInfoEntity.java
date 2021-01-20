package allinone.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserInfoEntity implements Serializable {

    public String address;
    public String mPassword;
    public String countryId = "";
    public String cityId;
    public int jobId;
    public Date birthDay;
    public String hobby;
    public String nickYahoo;
    public String nickSkype;
    public String phoneNumber;
    //public int characterId;
    public int cueId;
    public int soccerStarStyle;
    public int soccerStarFormat;

    
    public UserInfoEntity() {
    }
}
