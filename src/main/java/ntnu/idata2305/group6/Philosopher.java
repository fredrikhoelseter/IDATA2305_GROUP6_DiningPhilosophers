package ntnu.idata2305.group6;

import java.util.Random;

/**
 * Represents a thread in the dining philosophers problem. Each thread acts as
 * a single philosopher.
 */
public class Philosopher implements Runnable {
    DiningPhilosophers monitor;
    int ID;
    int hungerMeter;
    Random random = new Random();
    public Philosopher(DiningPhilosophers monitor, int ID){
        this.monitor = monitor;
        this.ID=ID;
        hungerMeter = random.nextInt(10);
    }

    public void run(){
        while(true){
            hungerMeter -= 1;

            if (hungerMeter < 0) {
                monitor.getHungry(ID);
                monitor.eat(ID);
                boolean justAte = monitor.stopEating(ID);
                if (justAte) {
                    hungerMeter = random.nextInt(10);
                }
            }

            if (hungerMeter < -5) {
                monitor.starve(ID);
            }
        }
    }
}
