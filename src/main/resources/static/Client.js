function moveUp(card) {
    card.classList.toggle('move-up');  // 切换 "move-up" 类
}

// 可以根据需要添加按钮的点击事件
document.querySelector('.play-btn').addEventListener('click', function() {
    alert("出牌按钮被点击");
});

document.querySelector('.doubt-btn').addEventListener('click', function() {
    alert("质疑按钮被点击");
});

