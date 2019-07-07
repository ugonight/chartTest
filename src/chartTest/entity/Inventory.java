package chartTest.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Inventory implements Serializable {
	private int prodId;
	private String prodName;
	private int amount;
	private int amountD;
	private LocalDateTime date;

	public int getProdId() {
		return prodId;
	}

	public void setProdId(int prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmountD() {
		return amountD;
	}

	public void setAmountD(int amountD) {
		this.amountD = amountD;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Inventory [prodId=" + prodId + ", " + (prodName != null ? "prodName=" + prodName + ", " : "")
				+ "amount=" + amount + ", amountD=" + amountD + ", " + (date != null ? "date=" + date : "") + "]";
	}

}
