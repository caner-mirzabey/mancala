package com.caner.mirzabey.interview.backbase.mancala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class Mancala {

    public static void main(String[] args) {
        SpringApplication.run(Mancala.class, args);
    }
}
