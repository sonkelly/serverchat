package allinone.data;

import java.io.Serializable;
import java.util.Date;

public class SessionEntity implements Serializable {


    public long uid;
    public String name;
    public String avatar;
    public String sid;
    public String hostname;
    public int timestamp;
    public String session;

 

    public SessionEntity() {
    }

}
