package allinone.data;

import java.io.Serializable;

public class ItemOrderEntity implements Serializable {

	private long id;
	private long itemId;
	private String orderDate;
	private String name;
	private int status; // 1 order // 2: duyet // 3: Hoan tat // 4 cancel
	private String comment;
	private int price;
	
	public ItemOrderEntity(long id, long itemId, String orderDate, String name,
			int status, String comment, int price) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.orderDate = orderDate;
		this.name = name;
		this.status = status;
		this.comment = comment;
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
