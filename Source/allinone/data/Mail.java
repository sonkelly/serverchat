package allinone.data;

import java.io.Serializable;
import java.util.Date;

public class Mail implements Serializable {

    public long id;
    public long sID;
    public String detail;
    public Date date;
    public long dID;
    public String sName = "";
    public String title;
    public int isRead;
    public int isAlert;

    public Mail(long i, long sid, long did, String d, String t, Date date, String sN, int isRead, int isAlert) {
        this.id = i;
        this.sID = sid;
        this.date = date;
        this.detail = d;
        this.title = t;
        this.dID = did;
        this.sName = sN;
        this.isRead = isRead;
        this.isAlert = isAlert;
    }
}
