// 页面加载时，自动建立WebSocket连接
var websocket = null;

//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
    websocket = new WebSocket("ws://localhost:8080/ws");
}
else{
    alert('Not support websocket')
}

// WebSocket连接建立成功时，发送一个消息到服务器
websocket.onopen = function(event) {
    console.log('WebSocket连接建立成功！');
    websocket.send('连接建立成功！');
};

// WebSocket连接收到消息时，显示消息
websocket.onmessage = function(event) {
    console.log('收到消息：' + event.data);
};

// WebSocket连接关闭时，显示关闭信息
websocket.onclose = function(event) {
    console.log('WebSocket连接关闭！');
};

// WebSocket连接出错时，显示错误信息
websocket.onerror = function(event) {
    console.log('WebSocket连接出错！');
};
