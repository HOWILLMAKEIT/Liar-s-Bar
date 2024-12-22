function moveUp(card) {
    card.classList.toggle('move-up');  // 切换 "move-up" 类
}

const playerElements = {
    player1: {
        element: document.querySelector('.player.top-left'),
        position: 'top-left'
    },
    player2: {
        element: document.querySelector('.player.top'),
        position: 'top'
    },
    player3: {
        element: document.querySelector('.player.top-right'),
        position: 'top-right'
    },
    player4: {
        element: document.querySelector('.player.bottom'),
        position: 'bottom'
    }
};

// 出牌按钮实现
function PLAY() {
    const myPlayerId = playerElements.player4.element.dataset.playerId;
    if (myPlayerId !== currentId) {
        alert('还没轮到你出牌！');
        return;
    }
    if (selectedCards.length === 0) {
        alert('请先选择卡牌');
        return;
    }
    selectedCards.forEach((cardElement, index) => {
        // 从玩家手牌中移除该卡牌
        cardElement.remove();
    });
    sendPlayedCardsMsg();
    // 清空选中的卡牌数组
    selectedCards = [];
}
function DOUBT(){
    const myPlayerId = playerElements.player4.element.dataset.playerId;
    if (myPlayerId !== currentId) {
        alert('还没轮到你！');
        return;
    }
    sendDoubtMsg();
}
function updateRevolverDirection(targetId) {
    const revolver = document.getElementById('revolver-container');

    // 移除所有方向类
    revolver.classList.remove('point-to-top', 'point-to-right', 'point-to-bottom', 'point-to-left');

    // 根据目标玩家位置添加对应方向类
    Object.values(playerElements).forEach(({element, position}) => {
        if (element.dataset.playerId === targetId) {
            switch(position) {
                case 'top':
                    revolver.classList.add('point-to-top');
                    break;
                case 'top-right':
                    revolver.classList.add('point-to-right');
                    break;
                case 'bottom':
                    revolver.classList.add('point-to-bottom');
                    break;
                case 'top-left':
                    revolver.classList.add('point-to-left');
                    break;
            }
        }
    });
}
function showTemporaryMessage(message) {
    // 创建消息元素
    const messageElement = document.createElement('div');
    messageElement.style.position = 'fixed';
    messageElement.style.top = '20%';
    messageElement.style.left = '50%';
    messageElement.style.transform = 'translate(-50%, -50%)';
    messageElement.style.backgroundColor = 'rgba(0,0,0,0.7)';
    messageElement.style.color = 'white';
    messageElement.style.padding = '10px 20px';
    messageElement.style.borderRadius = '5px';
    messageElement.style.zIndex = '1000';
    messageElement.style.transition = 'opacity 0.5s';
    messageElement.textContent = message;

    // 添加到body
    document.body.appendChild(messageElement);

    // 2秒后淡出并移除
    setTimeout(() => {
        messageElement.style.opacity = '0';
        setTimeout(() => {
            document.body.removeChild(messageElement);
        }, 500);
    }, 2000);
}



