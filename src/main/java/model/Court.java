package model;


public class Court {

    private Sound sound = new Sound();

    // instance parameters
    private RacketController playerA, playerB;
    private final double width, height; // m
    // instance state
    private Ball ball;
    private Raquette raquetteA,raquetteB;

    private int scoreA, scoreB;

    public Court(RacketController playerA, RacketController playerB,double width,double height){
        this.width = width;
        this.height = height;
        this.playerA=playerA;
        this.playerB=playerB;
        reset();
    }

    public void playSFX(int i){
        this.sound.setFile(i);
        this.sound.play();
    }
   
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Raquette getRacketA() {
        return raquetteA;
    }

    public Raquette getRacketB() {
        return raquetteB;
    }

    public Ball getBall(){
        return ball;
    }

    public int getScoreA() { return scoreA; }

    public int getScoreB() { return scoreB; }

    public void update(double deltaT) {

        switch (raquetteA.getPlayer().getState()) {
            case GOING_UP:
                raquetteA.setRacketPos(raquetteA.getRacketPos()-raquetteA.getRacketSpeed() * deltaT);
                if (raquetteA.getRacketPos() < 0.0) raquetteA.setRacketPos(0.0);
                break;
            case IDLE:
                break;
            case GOING_DOWN:
                raquetteA.setRacketPos(raquetteA.getRacketPos()+raquetteA.getRacketSpeed() * deltaT);
                if (raquetteA.getRacketPos() + raquetteA.getRacketSize() > height) raquetteA.setRacketPos(height - raquetteA.getRacketSize());
                break;
        }
        switch (raquetteB.getPlayer().getState()) {
            case GOING_UP:
                raquetteB.setRacketPos(raquetteB.getRacketPos()-raquetteB.getRacketSpeed() * deltaT);
                if (raquetteB.getRacketPos() < 0.0) raquetteB.setRacketPos(0.0);
                break;
            case IDLE:
                break;
            case GOING_DOWN:
                raquetteB.setRacketPos(raquetteB.getRacketPos()+raquetteB.getRacketSpeed() * deltaT);
                if (raquetteB.getRacketPos() + raquetteB.getRacketSize() > height) raquetteB.setRacketPos(height - raquetteB.getRacketSize());
                break;
        }
        if (updateBall(deltaT)) {
            scoreA = playerA.getScore();
            scoreB = playerB.getScore();
            reset();
        }
    }


    /**
     * @return true if a player lost
     */
    private boolean updateBall(double deltaT) {
        // first, compute possible next position if nothing stands in the way
        double nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
        double nextBallY = ball.getBallY() + deltaT * ball.getBallSpeedY();
        // next, see if the ball would meet some obstacle
        if (nextBallY < 0 || nextBallY > height) {
            ball.setBallSpeedY(-ball.getBallSpeedY());
            nextBallY = ball.getBallY() + deltaT * ball.getBallSpeedY();
            playSFX(1);
        }
        if ((nextBallX < 0 && nextBallY > raquetteA.getRacketPos() && nextBallY < raquetteA.getRacketPos() + raquetteA.getRacketSize())) {
            ball.setBallSpeedX(-ball.getBallSpeedX()+20);
            ball.setBallSpeedY(ball.getBallSpeedY()+20);
            nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
            playSFX(1);
        } else if ((nextBallX > width && nextBallY > raquetteB.getRacketPos() && nextBallY < raquetteB.getRacketPos() + raquetteB.getRacketSize())) {
            ball.setBallSpeedX(-ball.getBallSpeedX()-20);
            ball.setBallSpeedY(ball.getBallSpeedY()+20);
            nextBallX = ball.getBallX() + deltaT * ball.getBallSpeedX();
            playSFX(1);

        } else if (nextBallX < 0) {
            playerB.incrementScore();
            playSFX(0);
            return true;
        } else if (nextBallX > width) {
            playerA.incrementScore();
            playSFX(0);
            return true;
        }
        ball.setBallX(nextBallX);
        ball.setBallY(nextBallY);
        return false;
    }

    void reset(){
        this.raquetteA = new Raquette(playerA,this.height/2);
        this.raquetteB = new Raquette(playerB,this.height/2);
        this.ball = new Ball(this.width/2,this.height/2,275.0,275.0);
    }
}
