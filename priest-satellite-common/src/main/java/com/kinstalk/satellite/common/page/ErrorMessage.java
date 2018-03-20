package com.kinstalk.satellite.common.page;

/**
 * Created by mayan on 14-11-5.
 */
@SuppressWarnings("serial")
public class ErrorMessage implements java.io.Serializable {
	private int errorCode;
	private String errorInfo;
	
	public ErrorMessage(int code, String info) {
		this.errorCode = code;
		this.errorInfo = info;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

}
