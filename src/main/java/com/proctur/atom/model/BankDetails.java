package com.proctur.atom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BankDetails {
	private String otsBankId;
	private String bankTxnId;
	private String otsBankName;

	public String getOtsBankId() {
		return otsBankId;
	}

	public void setOtsBankId(String otsBankId) {
		this.otsBankId = otsBankId;
	}

	public String getBankTxnId() {
		return bankTxnId;
	}

	public void setBankTxnId(String bankTxnId) {
		this.bankTxnId = bankTxnId;
	}

	public String getOtsBankName() {
		return otsBankName;
	}

	public void setOtsBankName(String otsBankName) {
		this.otsBankName = otsBankName;
	}

	@Override
	public String toString() {
		return "BankDetails [otsBankId=" + otsBankId + ", bankTxnId=" + bankTxnId + ", otsBankName=" + otsBankName
				+ ", getOtsBankId()=" + getOtsBankId() + ", getBankTxnId()=" + getBankTxnId() + ", getOtsBankName()="
				+ getOtsBankName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
