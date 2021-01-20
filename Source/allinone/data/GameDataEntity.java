package allinone.data;

import java.io.Serializable;

public class GameDataEntity implements Serializable{

    public int zoneID;
    public int win;
    public int lost;
    public int expr;
    public int totalplay;
    public int typeMoney;
    
    public GameDataEntity() {
    }

	public int getZoneID() {
		return zoneID;
	}

	public void setZoneID(int zoneID) {
		this.zoneID = zoneID;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLost() {
		return lost;
	}

	public void setLost(int lost) {
		this.lost = lost;
	}

	public int getExpr() {
		return expr;
	}

	public void setExpr(int expr) {
		this.expr = expr;
	}
        public int getTotalplay() {
		return totalplay;
	}

	public void setTotalplay(int parame) {
		this.totalplay = parame;
	}
    
}
