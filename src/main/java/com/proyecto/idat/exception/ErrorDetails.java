package com.proyecto.idat.exception;

import java.util.Date;

public class ErrorDetails {
	
	private Date date;
	private String message;
	private String detail;
	
	public ErrorDetails() {
		super();
	}
	
	public ErrorDetails(Date date, String message, String detail) {
		super();
		this.date = date;
		this.message = message;
		this.detail = detail;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}

}
