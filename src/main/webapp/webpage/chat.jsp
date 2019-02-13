<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>chat</title>
</head>
<body>
	<header>websocket实现群聊</header>
	<input id="message" placeholder="请输入发送内容">
	<button onclick="sendMessage()">发送</button>
	<button onclick="closeConnection()">下线</button>
	<div id="container"></div>
</body>
<script>
	var websocket = null;

	function main() {
		let container = document.querySelector('#container');
		
		//判断浏览器是否支持websocket
		if ('WebSocket' in window) {
			//websocket是一项新的基于TCP的长连接通信协议, 地址以ws或wss开头, 端口80或443
			//以绝对路径访问, 如果有应用名必须加上
			websocket = new WebSocket('ws://localhost:8080/javaee/chat/IAMPARAM');
// 			websocket = new WebSocket('ws://localhost:8080/spring/chat');
		} else {
			container.innerHTML('当前浏览器不支持websocket');
			return ;
		}
		
		//连接成功事件
		websocket.onopen = function() {
			container.innerHTML = '<p style="color: green">已连接服务器.</p>'
		}
		
		//连接出现错误事件
		websocket.onerror = function(event) {
			container.innerHTML = '<p style="color: red">连接服务器出现错误: ' + event.data + '.</p>'
		}
		
		//接收消息事件
		websocket.onmessage = function(event) {
			container.innerHTML = container.innerHTML + '<p>' + event.data + '</p>';
		}
		
		//连接关闭事件
		websocket.onclose = function() {
			container.innerHTML = container.innerHTML + '<p style="color: green">已下线.</p>'
		}
	}
	
	main();
	
	function sendMessage() {
		let message = document.querySelector('#message').value;
		if (message == '') {
			return ;
		}
		//send() 向服务端发送数据
		websocket.send(message);
		document.querySelector('#message').value = '';
	}
	
	function closeConnection() {
		//close() 关闭连接
		websocket.close();
	}
	
	window.onbeforeunload = function() {
		websocket.close();
	}
</script>
</html>