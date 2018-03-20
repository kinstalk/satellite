package com.kinstalk.satellite.socket;



import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by digitZhang on 16/6/8.
 */
public class Agent implements Serializable {
	private int id;
	private String ip;
	private int port;
	private String token;
	private Long lastPongTime=0l;

	private SocketThread socket;

	public Agent(int id, String ip, int port, String token) {
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.token = token;
		socket = new SocketThread(this);
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}


	public int getId(){
		return id;
	}
	public SocketThread getSocket() {

		return socket;
	}
	
	public boolean isConnecting() {
		return socket.getSocketStatus();
	}

	public Long getLastPongTime() {
		return lastPongTime;
	}

	public void setLastPongTime(Long lastPongTime) {
		this.lastPongTime = lastPongTime;
	}

	@Override
	public String toString() {
		return "Agent{" +
				"id=" + id +
				", ip='" + ip + '\'' +
				", lastPongTime=" +  lastPongTime +
				'}';
	}
}
