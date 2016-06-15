package com.caner.mirzabey.interview.backbase.game;

import com.caner.mirzabey.interview.backbase.mancala.Mancala;
import com.caner.mirzabey.interview.backbase.mancala.game.GameEngineService;
import com.caner.mirzabey.interview.backbase.mancala.game.GameRepository;
import com.caner.mirzabey.interview.backbase.mancala.game.data.Game;
import com.caner.mirzabey.interview.backbase.mancala.game.data.Seed;
import com.caner.mirzabey.interview.backbase.mancala.game.data.Side;
import com.caner.mirzabey.interview.backbase.mancala.user.User;
import com.caner.mirzabey.interview.backbase.mancala.ws.GameAction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

/**
 * Created by ecanmir on 15.06.2016.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Mancala.class)
public class CollectOpponentOppositeHouseSeedsTest {
    private static final int    SEED_COUNT                          = 6;
    private static final String GAME_NAME                           = "test";
    private static final String PLAYER_USERNAME                     = "player";
    private static final String OPPONENT_USERNAME                   = "opponent";
    private static final int    MOVE                                = 6;
    private static final int    ON_MOVE_HOUSE_SEED_COUNT            = 9;
    private static final int    OPPONENTS_OPPOSITE_HOUSE_INDEX      = 4;
    private static final int    OPPONENTS_OPPOSITE_HOUSE_SEED_COUNT = 7;

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

        Side playerSide = game.getBoard().getPlayerSideByUsername(PLAYER_USERNAME);
        playerSide.setTurnOnMe(true);
        playerSide.setHouses(prepareHouses(ON_MOVE_HOUSE_SEED_COUNT, MOVE - 1));

        Side opponentSide = game.getBoard().getOpponentSideByUsername(PLAYER_USERNAME);
        opponentSide.setTurnOnMe(false);
        opponentSide.setHouses(prepareHouses(OPPONENTS_OPPOSITE_HOUSE_SEED_COUNT, OPPONENTS_OPPOSITE_HOUSE_INDEX));
        gameRepository.insert(game);
    }

    @Test
    public void test() throws Exception {
        System.out.println("test");
        GameAction gameAction = new GameAction(GAME_NAME,
                                               null,
                                               PLAYER_USERNAME,
                                               MOVE,
                                               "First game action for having another turn");
        Game result          = gameEngineService.action(gameAction);
        int  playerStoreSize = result.getBoard().getPlayerSideByUsername(PLAYER_USERNAME).getStore().size();
        int opponentsOppositeHouseSize = result.getBoard().getOpponentSideByUsername(PLAYER_USERNAME).getHouses().get(
                OPPONENTS_OPPOSITE_HOUSE_INDEX).size();

        Assert.assertEquals(OPPONENTS_OPPOSITE_HOUSE_SEED_COUNT + 3, playerStoreSize);
        Assert.assertEquals(0, opponentsOppositeHouseSize);
    }

    private List<Stack<Seed>> prepareHouses(int seedCount, int houseIndex) {
        List<Stack<Seed>> houses = new ArrayList<>(Side.HOUSE_COUNT);
        for (int i = 0; i < Side.HOUSE_COUNT; i++) {
            Stack<Seed> seeds = new Stack<>();
            if (i == houseIndex) {
                for (int j = 1; j <= seedCount; j++) {
                    seeds.push(new Seed((houseIndex * 10) + j));
                }
            }
            houses.add(seeds);
        }
        return houses;
    }

}
