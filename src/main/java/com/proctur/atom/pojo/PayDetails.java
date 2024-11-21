package com.proctur.atom.pojo;

public class PayDetails {

	private String amount;
	private String product;
	private String custAccNo;
	private String txnCurrency;
	private String signature;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCustAccNo() {
		return custAccNo;
	}

	public void setCustAccNo(String custAccNo) {
		this.custAccNo = custAccNo;
	}

	public String getTxnCurrency() {
		return txnCurrency;
	}

	public void setTxnCurrency(String txnCurrency) {
		this.txnCurrency = txnCurrency;
	}

	@Override
	public String toString() {
		return "PayDetails [amount=" + amount + ", product=" + product + ", custAccNo=" + custAccNo + ", txnCurrency="
				+ txnCurrency + "]";
	}

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
