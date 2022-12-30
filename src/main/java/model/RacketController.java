package model;

public interface RacketController {
    enum State { GOING_UP, IDLE, GOING_DOWN }
    int score = 0;
    State getState();
    int getScore();
    void incrementScore();
    void doubleIncrementScore();
    boolean inverted = false;
    void setInverted(boolean b);
    boolean getInverted();
}
