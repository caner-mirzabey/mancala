package com.caner.mirzabey.interview.backbase.mancala.game;

import com.caner.mirzabey.interview.backbase.mancala.game.data.Game;
import com.caner.mirzabey.interview.backbase.mancala.game.data.Seed;
import com.caner.mirzabey.interview.backbase.mancala.game.data.Side;
import com.caner.mirzabey.interview.backbase.mancala.game.exception.GameException;
import com.caner.mirzabey.interview.backbase.mancala.ws.GameAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Stack;

/**
 * Created by ecanmir on 14.06.2016.
 */
@Service
public class GameEngineService {

    @Autowired
    private GameRepository gameRepository;

    public Game action(final GameAction action) throws GameException {
        if ((!StringUtils.isEmpty(action.getUsername()) || !StringUtils.isEmpty(action.getUserUUID())) &&
            !StringUtils.isEmpty(action.getGameName()) && action.getMove() > 0) {
            String username         = action.getUsername();
            int    houseIndexOnMove = action.getMove() - 1;
            Game   game             = gameRepository.find(action.getGameName());

            Side        playerSide   = game.getBoard().getPlayerSideByUsername(username);
            Side        opponentSide = game.getBoard().getOpponentSideByUsername(username);
            Stack<Seed> houseOnMove  = pickSeedsFromHouseOnMove(playerSide, houseIndexOnMove);

            if (!playerSide.isTurnOnMe()) {
                throw new GameException("Illegal move: turn is not on player " + username);
            }

            sow(houseIndexOnMove, houseOnMove, playerSide, opponentSide);
            gameRepository.update(game);
            return game;
        }
        return null;
    }

    /**
     * @param houseIndexOnAction
     * @param houseOnMove
     * @param playerSide
     * @param opponentSide
     * @return boolean: identifies whether the action owner has another turn or not
     */
    private void sow(int houseIndexOnAction, Stack<Seed> houseOnMove, Side playerSide, Side opponentSide) {
        boolean moveOnPlayerSide = true;
        int     startHouseIndex  = houseIndexOnAction + 1;
        while (!houseOnMove.isEmpty()) {
            int lastMoveHouseIndex = startHouseIndex;
            if (moveOnPlayerSide) {
                lastMoveHouseIndex = distribute(startHouseIndex, moveOnPlayerSide, houseOnMove, playerSide);
                if (houseOnMove.isEmpty()) {
                    // Turn has ended up on putting the last seed on player store which means player has another turn
                    if (lastMoveHouseIndex == 0) {
                        playerSide.setTurnOnMe(true);
                        opponentSide.setTurnOnMe(false);
                    }
                    // Turn has ended up on player house
                    else if (lastMoveHouseIndex != 0) {
                        // Was player house empty before the last seed drop?
                        if (playerSide.getHouses().get(lastMoveHouseIndex).size() == 1) {
                            //Collect the opponent opposite house seeds and the seeds in this house and drop to player store
                            int  opponentOppositeHouseIndex   = (Side.HOUSE_COUNT - 1) - lastMoveHouseIndex;
                            Seed playerSeedInHouseOnLasteMove = playerSide.getHouses().get(lastMoveHouseIndex).pop();
                            playerSide.getStore().push(playerSeedInHouseOnLasteMove);

                            Stack<Seed> opponentSideOppositeHouse = opponentSide.getHouses().get(
                                    opponentOppositeHouseIndex);
                            while (!opponentSideOppositeHouse.isEmpty()) {
                                playerSide.getStore().push(opponentSideOppositeHouse.pop());
                            }
                        }
                    }
                }
            }
            else {
                lastMoveHouseIndex = distribute(startHouseIndex, moveOnPlayerSide, houseOnMove, opponentSide);
            }
            moveOnPlayerSide = !moveOnPlayerSide;
            startHouseIndex = 0;
        }
        return;
    }

    private int distribute(int startHouseIndex, boolean isPlayer, Stack<Seed> houseOnMove, Side side) {
        int houseIndex = startHouseIndex;
        while (!houseOnMove.isEmpty() && houseIndex < Side.HOUSE_COUNT) {
            side.getHouses().get(houseIndex).push(houseOnMove.pop());
            houseIndex++;
        }

        if (houseIndex == Side.HOUSE_COUNT) {
            if (isPlayer && !houseOnMove.isEmpty()) {
                side.getStore().push(houseOnMove.pop());
                houseIndex++;
            }
        }
        return --houseIndex;
    }

    public Stack<Seed> pickSeedsFromHouseOnMove(Side playerSide, int houseIndexOnMove) {
        Stack<Seed> seeds = new Stack<>();
        while (!playerSide.getHouses().get(houseIndexOnMove).isEmpty()) {
            seeds.push(playerSide.getHouses().get(houseIndexOnMove).pop());
        }
        return seeds;
    }

}
