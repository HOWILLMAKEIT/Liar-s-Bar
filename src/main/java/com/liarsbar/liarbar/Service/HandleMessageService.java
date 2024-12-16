package com.liarsbar.liarbar.Service;

import com.liarsbar.liarbar.Message.Msg_Client.PlayCard;
import com.liarsbar.liarbar.model.Card;
import com.liarsbar.liarbar.model.Game;
import com.liarsbar.liarbar.model.Player;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleMessageService {
    private final SendMessageService sendMessageService;
    @Autowired
    public HandleMessageService(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    public void HandlePLAY(Game game, Player player, List<Card> cards, List<Player> roomPlayers, PlayCard playCard) {
        // 后端处理
        game.PLAY(player,cards);
        //发送消息到前端：1、cuurent player的改变 2、其他选手要要看到出牌
        for (Player eachPlayer : roomPlayers) {
            Session eachSession = eachPlayer.getSession();
            sendMessageService.sendCurrentPlayer(eachSession,game.getGameState().getCurrentPlayer().getSession().getId());
            sendMessageService.sendPlayedCard(eachSession,playCard.getCards(),player.getSession().getId());
        }
    }
    public Player HandleDOUBT(Game game, Player player,List<Player> roomPlayers) {
        // 后端处理：
        Player ShotPlayer = game.DOUBT(player);// 要开枪射自己的人
        boolean ShotRes = ShotPlayer.shothimself();
        Player PlayerNextRound ;
        if(player.getisAlive()) PlayerNextRound = player;
        else PlayerNextRound = game.getGameState().getlastPlayer();
        // 发送消息到前端 1、谁开枪+结果
        for (Player eachPlayer : roomPlayers) {
            Session eachSession = eachPlayer.getSession();
            sendMessageService.sendShot(eachSession,ShotRes,ShotPlayer.getSession().getId());
        }
        return PlayerNextRound;
    }
}

