package com.caner.mirzabey.interview.backbase;

import com.caner.mirzabey.interview.backbase.mancala.Mancala;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith (SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Mancala.class)
@WebAppConfiguration
public class MancalaApplicationTests {

    @Test
    public void testHasAnotherTurn() {

    }

}
