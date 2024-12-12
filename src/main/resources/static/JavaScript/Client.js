function moveUp(card) {
    card.classList.toggle('move-up');  // 切换 "move-up" 类
}

const playerElements = {
    player1: document.querySelector('.player.top-left'),
    player2: document.querySelector('.player.top'),
    player3: document.querySelector('.player.top-right'),
    player4: document.querySelector('.player.bottom')
};

// 出牌按钮实现
function playCard() {
    if (selectedCards.length === 0) {
        alert('请先选择卡牌');
        return;
    }
    // 获取打出卡牌容器
    const playcardsContainer = document.getElementById('playedcards');

    selectedCards.forEach((cardElement, index) => {
        const playedCardElement = document.createElement('div');
        playedCardElement.classList.add('card');  // 使用 card 样式
        // 设置卡牌的内容
        playedCardElement.innerText = cardElement.innerText;
        // 将打出的卡牌插入到 playcards 容器中
        playcardsContainer.appendChild(playedCardElement);
        // 从玩家手牌中移除该卡牌
        cardElement.remove();
    });
    sendPlayedCardsMsg();
    // 清空选中的卡牌数组
    selectedCards = [];
}



