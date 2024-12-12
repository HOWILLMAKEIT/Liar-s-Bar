// 页面加载时，自动建立WebSocket连接
var websocket = null;
var currentId = null;
let selectedCards = []; // 存储选中的卡牌元素




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



websocket.onmessage = function(event) {
    const message = JSON.parse(event.data);  // 解析消息
    // 打印接收到的原始消息
    console.log('收到消息：', message);

    // 使用 switch 语句根据消息的 type 字段来处理不同类型的消息
    switch (message.type) {
        case 0://playerId
            handlePlayerId(message.playerId);
            break;
        case 1: //playerIds
            handlePlayerIds(message.playerIds);
            break;
        case 2: // CurrentCard
            handleCurrentCard(message.card);
            break;
        case 3: // HandCards
            handleHandCards(message.cards);
            break;
        case 4: // CurrentPlayer
            handleCurrentPlayer(message.id);
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
function handlePlayerId(playerId) {
    // 获取 player4 元素（即自己）
    const player4Element = document.querySelector('.player.bottom');

    // 修改 player4 的 data-player-id 为传递过来的 playerId
    if (player4Element) {
        player4Element.dataset.playerId = playerId;
        console.log('Player 4 ID 更新为:', player4Element.dataset.playerId); // 输出更新后的 playerId
    } else {
        console.log('未找到 player4 元素');
    }
}
function handlePlayerIds(playerIds) {
    // 确保 playerIds 是有效数组，并且长度为 4
    if (!Array.isArray(playerIds) || playerIds.length !== 4) {
        console.error("收到的玩家 ID 数据无效:", playerIds);
        return;
    }

    // 获取当前玩家的 ID（player4，即自己）
    const currentPlayerId = playerElements.player4.dataset.playerId;

    // 查找当前玩家的位置（index）
    const currentPlayerIndex = playerIds.indexOf(currentPlayerId);

    if (currentPlayerIndex === -1) {
        console.error("未找到当前玩家的 ID");
        return;
    }

    // 清空所有玩家的 ID
    Object.values(playerElements).forEach(playerElement => {
        playerElement.dataset.playerId = '';
    });

    // 左侧玩家：从当前玩家的下一个玩家开始，逆时针填充
    const leftSidePlayers = [
        playerIds[(currentPlayerIndex + 1) % 4], // 左侧第一个玩家
        playerIds[(currentPlayerIndex + 2) % 4], // 左侧第二个玩家
    ];

    // 右侧玩家：从当前玩家的上一个玩家开始，顺时针填充
    const rightSidePlayers = [
        playerIds[(currentPlayerIndex + 3) % 4], // 右侧第一个玩家
        playerIds[(currentPlayerIndex + 0) % 4], // 右侧第二个玩家
    ];

    // 更新 HTML 元素的数据属性
    playerElements.player1.dataset.playerId = leftSidePlayers[0]; // 左侧第一个玩家（player1）
    playerElements.player2.dataset.playerId = leftSidePlayers[1]; // 左侧第二个玩家（player2）
    playerElements.player3.dataset.playerId = rightSidePlayers[0]; // 右侧第一个玩家（player3）
    playerElements.player4.dataset.playerId = currentPlayerId; // 当前玩家（player4）

    console.log('玩家 ID 分配完成: ', {
        player1: playerElements.player1.dataset.playerId,
        player2: playerElements.player2.dataset.playerId,
        player3: playerElements.player3.dataset.playerId,
        player4: playerElements.player4.dataset.playerId
    });
}
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
            // 检查卡牌是否已经被选中
            if (selectedCards.includes(cardElement)) {
                // 如果已选中，取消选中
                cardElement.classList.remove('move-up');
                selectedCards = selectedCards.filter(selectedCard => selectedCard !== cardElement);
            } else {
                // 如果未选中，添加选中
                cardElement.classList.add('move-up');
                selectedCards.push(cardElement);
            }
        };
        cardsContainer.appendChild(cardElement);  // 将卡牌元素添加到容器中
    });
}
function handleCurrentPlayer(playerId) {
    // 清除所有玩家的高亮状态
    Object.values(playerElements).forEach(playerElement => {
        playerElement.classList.remove('highlight');
    });

    // 根据 playerId 找到对应的玩家元素，并添加高亮类
    Object.values(playerElements).forEach(playerElement => {
        if (playerElement.dataset.playerId === playerId) {
            currentId = playerId;
            playerElement.classList.add('highlight');  // 给当前玩家添加高亮样式
        }
    });

    console.log(`玩家 ${playerId} 被高亮显示`);
}


function sendPlayedCardsMsg() {
    // 构造要发送的消息对象
    const message = {
        action: 'play-cards',  // 指示出牌操作
        cards: selectedCards.map(card => card.innerText)  // 提取卡牌的内容（或其他你需要的信息）
    };

    // 检查 WebSocket 是否已经连接
    if (websocket.readyState === WebSocket.OPEN) {
        // 发送消息到后端
        websocket.send(JSON.stringify(message));
        console.log('已发送出牌信息:', message);
    } else {
        console.error('WebSocket 连接未打开');
    }

}


// WebSocket连接关闭时，显示关闭信息
websocket.onclose = function(event) {
    console.log('WebSocket连接关闭！');
};

// WebSocket连接出错时，显示错误信息
websocket.onerror = function(event) {
    console.log('WebSocket连接出错！');
};

