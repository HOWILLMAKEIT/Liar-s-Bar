// 页面加载时，自动建立WebSocket连接
var websocket = null;
var currentId = null;
let selectedCards = []; // 存储选中的卡牌元素
let remainingPlayers = 4; // 在全局声明剩余玩家数


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
        case "player-id":
            handlePlayerId(message.playerId);
            break;
        case "player-ids":
            handlePlayerIds(message.playerIds);
            break;
        case "chosen-card":
            handleCurrentCard(message.card);
            break;
        case "hand-card":
            handleHandCards(message.cards);
            break;
        case "current-player":
            handleCurrentPlayer(message.id);
            break;
        case "played-card":
            handlePlayedCard(message.cards,message.id);
            break;
        case "shot":
            handleShot(message.id,message.ShotRes);
            break;
        default:
            console.log('未知的消息类型:', message.type);
            break;
    }
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
    // 重置
    remainingPlayers = 4;
    // 清除所有玩家的eliminated状态
    Object.values(playerElements).forEach(({element}) => {
        if (element) {
            element.classList.remove( 'eliminated');
        }
    });


    // 确保 playerIds 是有效数组，并且长度为 4
    if (!Array.isArray(playerIds) || playerIds.length !== 4) {
        console.error("收到的玩家 ID 数据无效:", playerIds);
        return;
    }
// 获取当前玩家的 ID（player4，即自己）
    const currentPlayerId = playerElements.player4.element.dataset.playerId;
// 查找当前玩家的位置（index）
    const currentPlayerIndex = playerIds.indexOf(currentPlayerId);

    if (currentPlayerIndex === -1) {
        console.error("未找到当前玩家的 ID");
        return;
    }
// 清空所有玩家的 ID
    Object.values(playerElements).forEach(({element}) => {
        if (element && element.dataset) {
            element.dataset.playerId = '';
        }
    });
// 计算玩家位置
    const positions = {
        [(currentPlayerIndex + 1) % 4]: 'player1', // 左侧第一个玩家
        [(currentPlayerIndex + 2) % 4]: 'player2', // 上方玩家
        [(currentPlayerIndex + 3) % 4]: 'player3', // 右侧玩家
        [currentPlayerIndex]: 'player4'// 底部玩家（自己）
    };

// 设置玩家ID
    playerIds.forEach((playerId, index) => {
        const position = positions[index];
        if (position && playerElements[position] && playerElements[position].element) {
            playerElements[position].element.dataset.playerId = playerId;
        }
    });

// 左侧玩家数组（保持原有逻辑）
    const leftSidePlayers = [
        playerIds[(currentPlayerIndex + 1) % 4],
        playerIds[(currentPlayerIndex + 2) % 4]
    ];

// 右侧玩家数组（保持原有逻辑）
    const rightSidePlayers = [
        playerIds[(currentPlayerIndex + 3) % 4],
        playerIds[(currentPlayerIndex + 0) % 4]
    ];

    console.log("玩家ID分配情况:", {
        左边: playerIds[(currentPlayerIndex + 1) % 4],
        上方: playerIds[(currentPlayerIndex + 2) % 4],
        右边: playerIds[(currentPlayerIndex + 3) % 4],
        自己: playerIds[currentPlayerIndex]
    });

}
function handleCurrentCard(card) {
    // 直接获取元素并设置内容
    const cardElement = document.getElementById('card-content');
    cardElement.textContent = card;
}
function handleHandCards(cards) {
    const cardsContainer = document.getElementById('player-cards'); // 获取卡牌容器
    cardsContainer.innerHTML = ''; // 清空现有卡牌
    selectedCards = [];//清空选中的牌

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
    Object.values(playerElements).forEach(({element}) => {
        if (element) {
            element.classList.remove('highlight');
        }
    });

    // 根据 playerId 找到对应的玩家元素，并添加高亮类
    Object.values(playerElements).forEach(({element}) => {
        if (element && element.dataset.playerId === playerId) {
            currentId = playerId;
            element.classList.add('highlight');
        }
    });

    // 更新手枪指向
    updateRevolverDirection(playerId);
    console.log(`玩家 ${playerId} 被高亮显示`);
}
function handlePlayedCard(cards, id) {
    let playedCardsContainer = null;
    let isBottomPlayer = false;


    Object.values(playerElements).forEach(({element, position}) => {
        if (element.dataset.playerId === id) {
            playedCardsContainer = document.getElementById(`${position}-played-cards`);
            isBottomPlayer = position === 'bottom';
        }
    });
    // 清空所有玩家的出牌区域
    Object.values(playerElements).forEach(({position}) => {
        const container = document.getElementById(`${position}-played-cards`);
        if (container) {
            container.innerHTML = '';
        }
    });
    // 添加新的卡牌
    cards.forEach((card, index) => {
        const cardElement = document.createElement('div');
        cardElement.className = 'played-card';

        // 创建卡牌正面和背面
        const cardFront = document.createElement('div');
        cardFront.className = 'card-front';
        cardFront.textContent = card;

        const cardBack = document.createElement('div');
        cardBack.className = 'card-back';
        cardBack.textContent = '';

        // 将正面和背面添加到卡牌元素中
        cardElement.appendChild(cardFront);
        cardElement.appendChild(cardBack);

        // 如果是底部玩家的卡牌，直接显示正面
        if (isBottomPlayer) {
            cardElement.classList.add('flipped');
        }

        // 存储实际卡牌值供later使用
        cardElement.dataset.cardValue = card;

        // 添加初始样式
        cardElement.style.opacity = '0';
        playedCardsContainer.appendChild(cardElement);

        // 触发动画
        setTimeout(() => {
            cardElement.style.transition = 'all 0.3s ease';
            cardElement.style.opacity = '1';
        }, index * 100);
    });
}
function handleShot(id, ShotRes) {
    // 找到对应玩家的元素
    let targetPlayer = null;
    Object.values(playerElements).forEach(({ element }) => {
        if (element.dataset.playerId === id) {
            targetPlayer = element;
        }
    });

    if (!targetPlayer) {
        console.error("未找到对应玩家的元素");
        return;
    }

    // 翻转所有玩家的出牌区域
    Object.values(playerElements).forEach(({position}) => {
        const container = document.getElementById(`${position}-played-cards`);
        if (container) {
            const cards = container.getElementsByClassName('played-card');
            Array.from(cards).forEach((card, index) => {
                setTimeout(() => {
                    card.classList.add('flipped');
                }, index * 200); // 每张卡延迟200ms翻转
            });
        }
    });

    // 等待卡牌翻转动画完成后再显示结果
    setTimeout(() => {
        if (ShotRes) {
            targetPlayer.classList.add("eliminated");
            remainingPlayers--;
            document.getElementById('player-count').textContent = remainingPlayers;
            showTemporaryMessage("一发入魂！");

            if (remainingPlayers === 1) {
                showTemporaryMessage("游戏结束！")
                setTimeout(() => {
                    sendGameOverMsg();
                }
                , 2000); // 2s方便玩家看清游戏结束信息

            }

        } else {
            showTemporaryMessage("太幸运了！");

        }
    }, 1000); // 等待翻牌动画完成
}


function sendPlayedCardsMsg() {
    // 构造要发送的消息对象
    const message = {
        type: 'PLAY',  // 指示出牌操作
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
function sendDoubtMsg() {
    const message = {
        type: 'DOUBT' // 指示出牌操作
    };
    if (websocket.readyState === WebSocket.OPEN) {
        // 发送消息到后端
        websocket.send(JSON.stringify(message));
        console.log('已发送质疑信息:', message);
    } else {
        console.error('WebSocket 连接未打开');
    }
}
function sendGameOverMsg(){
    const message = {
        type: 'GAMEOVER'
    };

    if (websocket.readyState === WebSocket.OPEN) {
        websocket.send(JSON.stringify(message));
        console.log('已发送游戏结束信息');
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

