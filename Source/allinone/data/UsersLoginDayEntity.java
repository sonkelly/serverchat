package allinone.data;

import java.io.Serializable;
import java.util.Date;

public class UsersLoginDayEntity implements Serializable {

    public long uid;
    public String viewname;
    public Date startDay;
    public Date curLoginDay;
    public long moneyDay;
    public long moneyLevel;
    public long moneyFriend;
    public long moneyTotal;
    public int countDay;
   

    public UsersLoginDayEntity() {
    }

}
