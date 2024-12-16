package com.liarsbar.liarbar.model;

import java.util.Arrays;
import java.util.Random;

public class Gun {
    private int[] gun; // 长度为6的数组，表示左轮手枪的弹仓
    private int currentPosition; // 当前检查的位置

    // 构造函数，初始化gun数组并随机放置子弹
    public Gun() {
//        gun = new int[6];  // 创建长度为6的数组
//        Random random = new Random();
//        // 随机选择一个位置放置子弹
//        int bulletPosition = random.nextInt(6);
//        gun[bulletPosition] = 1; // 设置一个位置为1，代表子弹
//        currentPosition = 0; // 从第一个位置开始
        // 测试使用，百分百中枪
        gun = new int[6];
        Arrays.fill(gun, 1);
    }

    // 每次开枪，检查当前位置是否为子弹（1）
    public boolean shot() {
        // 检查当前位置是否有子弹
        if (gun[currentPosition] == 1) {
            System.out.println("嘭！你中弹了！游戏结束！");
            return true; // 中弹，游戏结束
        } else {
            System.out.println("恭喜！你没有中弹!");
            // 移动到下一个位置
            currentPosition = (currentPosition + 1) % 6;
            return false; // 没有中弹，继续游戏
        }
    }
}
