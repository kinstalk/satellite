package com.kinstalk.satellite.domain.packet;


import com.kinstalk.satellite.common.constant.ConstantSocket;

/**
 * Created by digitZhang on 16/5/12.
 */
public class LoginRespPacket extends SocketPacket {
	public LoginRespPacket() {
		type = ConstantSocket.LOGIN_RESP;
	}
}
