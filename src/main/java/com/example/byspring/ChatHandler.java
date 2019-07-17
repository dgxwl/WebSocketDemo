package com.example.byspring;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * 使用Spring实现websocket服务端;
 * 自定义类实现WebSocketHandler接口, 
 * 或继承TextWebSocketHandler/BinaryWebSocketHandler类
 * @author Administrator
 *
 */
public class ChatHandler extends AbstractWebSocketHandler {
	
	private static Queue<WebSocketSession> allConnections = new LinkedBlockingQueue<>(10);
	
	/**
	 * 连接建立事件
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		allConnections.add(session);
		System.out.println("一个新连接, 当前在线: " + allConnections.size());
	}
	
	/**
	 * 连接出错事件
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable error) throws Exception {
		error.printStackTrace();
	}
	
	/**
	 * 接收文本消息事件
	 */
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		//群发
		for (WebSocketSession se : allConnections) {
			se.sendMessage(message);
		}
	}
	
	/**
	 * 接收二进制消息事件
	 */
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		//群发
		for (WebSocketSession se : allConnections) {
			se.sendMessage(message);
		}
	}

	/**
	 * 连接关闭事件
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		allConnections.remove(session);
		System.out.println("close code: " + closeStatus.getCode());
		System.out.println("close reason: " + closeStatus.getReason());
		
		System.out.println("一个客户端下线, 当前在线: " + allConnections.size());
	}
	
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
