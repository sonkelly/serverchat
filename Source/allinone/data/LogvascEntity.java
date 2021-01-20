package allinone.data;

import java.io.Serializable;
import java.util.Date;

public class LogvascEntity implements Serializable {

//    dateTime	datetime
//money	double
//description	varchar
//logTypeId	int
//phoneNumber	varchar
//balanceAfter	double
//isWin	smallint
//experience	smallint
//dateCalExp	datetime
//achievementQuestion	int
//matchId	int
//matchNum	int
//tip	int
    public long id;
    public long userID;
    public long money;
    public long balanceAfter;
    public String description;
    public int logTypeId;

    public int isWin;
    public Date dateCalExp;
    public Date dateTime;
    public int matchId;
    public int matchNum;
    public int tip;
    public long cuoc;
    public long ownerid;
 

    public LogvascEntity() {
    }

}
