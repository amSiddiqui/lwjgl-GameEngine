package com.tks.learning;

import com.tks.learning.engine.GameEngine;
import com.tks.learning.engine.IGameLogic;
import com.tks.learning.game.DummyGame2;

public class App {
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame2();
            GameEngine gameEngine = new GameEngine("GAME", 1024, 720, vSync, gameLogic);
            gameEngine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}