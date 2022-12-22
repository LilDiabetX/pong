package com.mygdx.pong.controllers;

public interface RacketController {
    enum State { GOING_UP, IDLE, GOING_DOWN }
    State getState();
    int getScore();
    void incrementScore();
}
