package com.example.byspring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 使用注解配置websocket.
 * 效果相同的xml配置方式:
 * 在spring配置文件中添加
 * <websocket:handlers>
 * 	<websocket:mapping path="/spring/chat" handler="myTextHandler"/>
 * </websocket:handlers>
 * <bean id="myTextHandler" class="com.example.byspring.MyTextHandler"/>
 * 
 * @author Administrator
 *
 */
@Configuration
@EnableWebSocket  //声明websocket配置类
public class MyConfigurer implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		/*
		 * addHandler(WebSocketHandler webSocketHandler, String... paths)
		 * 声明websocket服务端及其访问url
		 */
		registry.addHandler(myTextHandler(), "/spring/chat");
	}

	@Bean  //方法级别的注解, 用于@Configuration类中, 等同于在xml配置中声明bean
	public MyTextHandler myTextHandler() {
		return new MyTextHandler();
	}
}
