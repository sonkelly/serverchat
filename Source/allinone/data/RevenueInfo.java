package allinone.data;

import java.io.Serializable;

public class RevenueInfo implements Serializable {

    public int id;
    public String title;
    public String serviceCode;
    public int partnerId;
    public int vnd;
    public int cash;
    public int type;
    public String telNumber;
    public int rate;
    public String image;

    public RevenueInfo() {
        // TODO Auto-generated constructor stub
    }

    public RevenueInfo(int id, String title, String serviceCode, int partnerId,
            int vnd, int cash, int type, String telNumber, int rate, String image) {
        this.id = id;
        this.title = title;
        this.serviceCode = serviceCode;
        this.partnerId = partnerId;
        this.vnd = vnd;
        this.cash = cash;
        this.type = type;
        this.telNumber = telNumber;
        this.rate = rate;
        this.image = image;
    }
}
