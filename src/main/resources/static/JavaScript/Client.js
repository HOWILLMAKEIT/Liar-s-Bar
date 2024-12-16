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

