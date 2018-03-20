package com.kinstalk.satellite.domain.packet;


import com.kinstalk.satellite.common.constant.ConstantSocket;

/**
 * Created by digitZhang on 16/5/12.
 */
public class LoginPacket extends SocketPacket {
	public LoginPacket() {
		type = ConstantSocket.LOGIN;
	}
}
