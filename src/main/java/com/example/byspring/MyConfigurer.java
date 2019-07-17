package com.example.byspring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * 使用注解配置websocket.
 * 效果相同的xml配置方式:
 * 在spring配置文件中添加
 * <websocket:handlers>
 * 	<websocket:mapping path="/spring/chat" handler="myTextHandler"/>
 * </websocket:handlers>
 * <bean id="myTextHandler" class="com.example.byspring.MyTextHandler"/>
 * 
 * 另外在根标签属性中加上:
 * xmlns:websocket="http://www.springframework.org/schema/websocket"
 * http://www.springframework.org/schema/websocket http://www.springframework.org/schema/websocket/spring-websocket.xsd
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
		registry.addHandler(chatHandler(), "/spring/chat");
	}

	@Bean  //方法级别的注解, 用于@Configuration类中, 等同于在xml配置中声明bean
	public ChatHandler chatHandler() {
		return new ChatHandler();
	}
	
	/*
	 * 配置可接收的文本和二进制内容大小限制
	 * 如果没有配置,很容易超出范围,将会断线:
	 * close code: 1009
	 * close reason: No async message support and buffer too small. Buffer size: [8,192], Message size: [117,617]
	 */
	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
	    ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
	    container.setMaxTextMessageBufferSize(51200);
	    container.setMaxBinaryMessageBufferSize(104857600);
	    return container;
	}
}
