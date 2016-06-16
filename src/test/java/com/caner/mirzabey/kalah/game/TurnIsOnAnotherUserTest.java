package com.caner.mirzabey.kalah.game;

import com.caner.mirzabey.kalah.Application;
import com.caner.mirzabey.kalah.game.data.Game;
import com.caner.mirzabey.kalah.game.exception.GameException;
import com.caner.mirzabey.kalah.user.User;
import com.caner.mirzabey.kalah.web.socket.message.GameAction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by ecanmir on 15.06.2016.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Application.class)
public class TurnIsOnAnotherUserTest {
    private static final int    SEED_COUNT        = 6;
    private static final String GAME_NAME         = "test";
    private static final String PLAYER_USERNAME   = "player";
    private static final String OPPONENT_USERNAME = "opponent";

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private GameRepository gameRepository;

    @Before
    public void init() {
        System.out.println("init");
        User player   = new User(UUID.randomUUID().toString(), PLAYER_USERNAME);
        User opponent = new User(UUID.randomUUID().toString(), OPPONENT_USERNAME);
        Game game     = new Game(GAME_NAME, SEED_COUNT, player, opponent);
        game.getBoard().getPlayerSideByUsername(PLAYER_USERNAME).setTurnOnMe(true);
        game.getBoard().getOpponentSideByUsername(PLAYER_USERNAME).setTurnOnMe(false);
        gameRepository.insert(game);
    }

    @Test (expected = GameException.class)
    public void test() throws GameException {
        System.out.println("test");
        GameAction gameAction = new GameAction(GAME_NAME,
                                               null,
                                               OPPONENT_USERNAME,
                                               1,
                                               "First game action for having another turn");
        Game result = gameEngineService.action(gameAction);
    }

}
