package allinone.data;

import java.io.Serializable;

public class GoldenHourEntity implements Serializable{
	private int id;
	private String fromDate;
	private String toDate;
	private int bonusAmount;
	private int partnerId;
	private String extenalParam;
	private String description;
	private int type;
	
	public GoldenHourEntity() {
	}

	
	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public int getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(int bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getExtenalParam() {
		return extenalParam;
	}

	public void setExtenalParam(String extenalParam) {
		this.extenalParam = extenalParam;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
