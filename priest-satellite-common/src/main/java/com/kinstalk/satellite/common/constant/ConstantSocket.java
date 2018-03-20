package com.kinstalk.satellite.common.constant;

/**
 * Created by digitZhang on 16/6/8.
 */
public class ConstantSocket {

	// -------------------------- packet type --------------------------
	public static final int ACK = 0;
	public static final int CONNECT = 1;
	public static final int CONNECT_RESP = 2;
	public static final int PING = 3;
	public static final int PONG = 4;


	public static final int LOGIN = 11;
	public static final int LOGIN_RESP = 12;

	public static final int TIMER_UPDATE = 13;
	public static final int TIMER_RESULT = 14;
	public static final int TIMER_STOP = 15;

	public static final int TIMER_RUN_START = 16;
	public static final int TIMER_RUN_END = 17;


	// -------------------------- SOCKET Connect Status --------------------------
	public static final int CONNECT_DEFAULT = 0; // 默认
	public static final int CONNECT_ON = 1;        // 连接中
	public static final int CONNECT_SUCC = 2;    // 连接成功
	public static final int CONNECT_FAIL = 3;   // 连接失败

	// -------------------------- SOCKET Config --------------------------
	public static final int MAX_ACK = 255; // socket最大ACK,值范围:1-max, 循环使用
	public static final int MAX_RECONNECT_NUM = 1; // socket连接最大重试次数
	public static final int MAX_RECONNECT_SECONDS = 1; // socket连接最长时间
	public static final int MAX_SOCKET_PACKET_LENGTH = 1024 * 1024 * 50; // 最大socket 包长度
}