package com.caner.mirzabey.kalah.game;

import com.caner.mirzabey.kalah.game.data.Game;
import com.caner.mirzabey.kalah.game.data.Game.Status;
import com.caner.mirzabey.kalah.game.data.Seed;
import com.caner.mirzabey.kalah.game.data.Side;
import com.caner.mirzabey.kalah.game.exception.GameException;
import com.caner.mirzabey.kalah.web.socket.message.GameAction;

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
            Stack<Seed> houseOnMove  = collectSeedsFromHouse(playerSide, houseIndexOnMove);

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
                        if (playerSide.getPit().get(lastMoveHouseIndex).size() == 1) {
                            //Collect the opponent opposite house seeds and the seeds in this house and drop to player store
                            int  opponentOppositeHouseIndex   = (Side.HOUSE_COUNT - 1) - lastMoveHouseIndex;
                            Seed playerSeedInHouseOnLasteMove = playerSide.getPit().get(lastMoveHouseIndex).pop();
                            playerSide.getKalah().push(playerSeedInHouseOnLasteMove);

                            Stack<Seed> opponentSideOppositeHouse =
                                    opponentSide.getPit().get(opponentOppositeHouseIndex);
                            while (!opponentSideOppositeHouse.isEmpty()) {
                                playerSide.getKalah().push(opponentSideOppositeHouse.pop());
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

    private Game.Status processOnPostMove(Game game) {
        Game.Status status = Status.ONGOING;
        //Checking side1 houses
        int totalSeedsOnSide1House = 0;
        for (Stack<Seed> house : game.getBoard().getSide1().getPit()) {
            totalSeedsOnSide1House += house.size();
        }
        if (totalSeedsOnSide1House == 0) {
            status = Status.FINISHED;
            for (Stack<Seed> house : game.getBoard().getSide2().getPit()) {
                game.getBoard().getSide2().getKalah().addAll(house);
            }
        }

        int totalSeedsOnSide2House = 0;
        for (Stack<Seed> house : game.getBoard().getSide1().getPit()) {
            totalSeedsOnSide2House += house.size();
        }
        if (totalSeedsOnSide2House == 0) {
            status = Status.FINISHED;
            for (Stack<Seed> house : game.getBoard().getSide1().getPit()) {
                game.getBoard().getSide1().getKalah().addAll(house);
            }
        }
        return status;
    }

    private int distribute(int startHouseIndex, boolean isPlayer, Stack<Seed> houseOnMove, Side side) {
        int houseIndex = startHouseIndex;
        while (!houseOnMove.isEmpty() && houseIndex < Side.HOUSE_COUNT) {
            side.getPit().get(houseIndex).push(houseOnMove.pop());
            houseIndex++;
        }

        if (houseIndex == Side.HOUSE_COUNT) {
            if (isPlayer && !houseOnMove.isEmpty()) {
                side.getKalah().push(houseOnMove.pop());
                houseIndex++;
            }
        }
        return --houseIndex;
    }

    public Stack<Seed> collectSeedsFromHouse(Side playerSide, int houseIndex) {
        Stack<Seed> seeds = new Stack<>();
        while (!playerSide.getPit().get(houseIndex).isEmpty()) {
            seeds.push(playerSide.getPit().get(houseIndex).pop());
        }
        return seeds;
    }

}
