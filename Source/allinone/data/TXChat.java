package allinone.data;

import java.io.Serializable;
import java.util.Date;

public class TXChat implements Serializable {

    public String viewname;
    public String msg;
   

    public TXChat(String _viewname, String _msg) {
        this.viewname = _viewname;
        this.msg = _msg;
       
    }
}
