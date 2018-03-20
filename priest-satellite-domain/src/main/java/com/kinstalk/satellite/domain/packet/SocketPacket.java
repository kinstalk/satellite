package com.kinstalk.satellite.domain.packet;

import java.io.Serializable;

/**
 * Created by digitZhang on 16/5/12.
 */
public class SocketPacket implements Serializable {

	private int ack;   		// ack
	protected int type;		// 包类型
	private int code;		// 应答码
	private String message="";	// 应答错误信息,出错时使用

	// ************************
	//   具体包内容请在子类里定义
	// ************************


	public int getAck() {
		return ack;
	}

	public void setAck(int ack) {
		this.ack = ack;
	}

	public int getType() {
		return type;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



	@Override
	public String toString() {

		StringBuilder sbu = new StringBuilder(100);
		sbu.append("SocketPacket{");
		sbu.append("type=" + type);
		if(ack > 0) {
			sbu.append(", ack=" + ack);
		}
		sbu.append(", code=" + code);
		sbu.append(", message=" + message);
		sbu.append("}");




		return sbu.toString();
	}
}
