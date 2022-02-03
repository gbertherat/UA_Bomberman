package server;

import lombok.Getter;

@Getter
public class Timer extends Thread{
    private final int timeLimit;
    private boolean done;

    public Timer(int timeLimitSecond){
        this.timeLimit = timeLimitSecond;
        this.done = false;
    }

    @Override
    public void run() {
        int timer = 0;

        while(timer < timeLimit){
            timer++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        done = true;
    }
}
