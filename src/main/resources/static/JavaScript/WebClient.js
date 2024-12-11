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


//CurrentCard：{"@type":"com.liarsbar.liarbar.message.CurrentCard","card":0,"type":0}

// WebSocket连接收到消息时，显示消息
websocket.onmessage = function(event) {
    const message = JSON.parse(event.data);  // 解析消息
    // 打印接收到的原始消息
    console.log('收到消息：', message);
    // 使用 switch 语句根据消息的 type 字段来处理不同类型的消息
    switch (message.type) {
        case 0: // CurrentCard
            handleCurrentCard(message.card);
            break;
        case 1: // HandCards
            handleHandCards(message.cards);
            break;
        default:
            console.log('未知的消息类型:', message.type);
            break;
    }
};
const cardMap = {
    0: 'A',
    1: 'K',
    2: 'Q',
};
function handleCurrentCard(card) {

    const cardContent = cardMap[card] || '?'; // 根据 card 值映射到字母
    //注意这里要用内层的id
    const cardElement = document.getElementById('card-content');
    cardElement.textContent = cardContent;
}

function handleHandCards(cards) {
    const cardsContainer = document.getElementById('player-cards'); // 获取卡牌容器
    cardsContainer.innerHTML = ''; // 清空现有卡牌

    // 根据收到的卡牌信息更新显示
    cards.forEach(card => {
        const cardElement = document.createElement('div');  // 创建新的卡牌元素
        cardElement.classList.add('card');  // 添加卡牌样式
        cardElement.innerText = card;  // 设置卡牌内容

        // 给每张卡片添加点击事件
        cardElement.onclick = function() {
            moveUp(cardElement);
        };

        cardsContainer.appendChild(cardElement);  // 将卡牌元素添加到容器中
    });
}

// WebSocket连接关闭时，显示关闭信息
websocket.onclose = function(event) {
    console.log('WebSocket连接关闭！');
};

// WebSocket连接出错时，显示错误信息
websocket.onerror = function(event) {
    console.log('WebSocket连接出错！');
};

