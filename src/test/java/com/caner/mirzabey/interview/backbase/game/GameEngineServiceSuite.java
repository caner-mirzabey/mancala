package com.caner.mirzabey.interview.backbase.game;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Created by ecanmir on 15.06.2016.
 */
@RunWith (Suite.class)
@SuiteClasses ({PlayerHasAnotherTurnTest.class,
                TurnIsOnAnotherUserTest.class,
                CollectOpponentOppositeHouseSeedsTest.class})
public class GameEngineServiceSuite {
}
