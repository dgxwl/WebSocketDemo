package com.example.byjavaee;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * 用JavaEE提供的@ServerEndpoint声明websocket服务端, 
 * 并指定访问url; 每建立一个连接,这个类就实例化一次.
 * @author Administrator
 *
 */
@ServerEndpoint("/javaee/chat")
public class ChatServer {
	/*
	 * 存储已建立的连接会话
	 */
	private static Queue<Session> allConnections = new ArrayBlockingQueue<>(10);
	
	/**
	 * @@OnOpen注解声明连接建立事件
	 * 可选参数:
	 * session javax.websocket.Session 服务端与客户端的连接会话
	 * config
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		allConnections.add(session);
		System.out.println(config.getUserProperties());
		
		System.out.println("一个新连接!当前在线: " + allConnections.size());
	}
	
	/**
	 * @@OnError注解声明连接出错事件
	 * session 可选参数
	 * error 错误类型, 可选参数
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("error session: " + session);
		error.printStackTrace();
	}
	
	/**
	 * @@OnMessage注解声明接收消息事件
	 * session 可选参数
	 * message 接收到的消息, 可选参数
	 */
	@OnMessage
	public void onMessage(Session session, String message) {
		//群发
		for (Session se : allConnections) {
			try {
				se.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 连接关闭事件;
	 * 参数可选
	 */
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		allConnections.remove(session);
		System.out.println("close code: " + closeReason.getCloseCode());
		System.out.println("close reson: " + closeReason.getReasonPhrase());
		
		System.out.println("一个客户端下线, 当前在线: " + allConnections.size());
	}
}
