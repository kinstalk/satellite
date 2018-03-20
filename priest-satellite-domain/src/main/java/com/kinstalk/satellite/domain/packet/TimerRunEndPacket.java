package com.kinstalk.satellite.domain.packet;


import com.kinstalk.satellite.common.constant.ConstantSocket;

/**
 * Created by digitZhang on 18/03/01.
 */
public class TimerRunEndPacket extends SocketPacket {

	protected Long timerId; // 任务id
	protected Integer appId; // 任务id
	private boolean isSucc;
	private String message;

	public TimerRunEndPacket(Long timerId, Integer appId) {
		type = ConstantSocket.TIMER_RUN_END;
		this.appId = appId;
		this.isSucc = true;
		this.timerId = timerId;
	}

	public TimerRunEndPacket(Long timerId, Integer appId, String errorMessage) {
		this(timerId, appId);
		this.isSucc = false;
		this.message = errorMessage;
	}

	public Long getTimerId() {
		return timerId;
	}

	public void setTimerId(Long timerId) {
		this.timerId = timerId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public boolean isSucc() {
		return isSucc;
	}

	public void setSucc(boolean succ) {
		isSucc = succ;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}
}
