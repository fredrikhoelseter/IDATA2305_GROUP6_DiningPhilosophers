package ntnu.idata2305.group6;


import java.util.Random;

public class DiningPhilosophers {
    private static final int numberOfPhilosophers = 5;
    private State[] stateOfPhilosophers;
    private  static Thread[] philosophers = new Thread[numberOfPhilosophers];
    private Random random = new Random();

    public DiningPhilosophers() {
        stateOfPhilosophers = new State[numberOfPhilosophers];

        // Sets all philosophers to thinking state at the start of the program.
        for (int i = 0; i < numberOfPhilosophers; i++) {
            philosophers[i] = new Thread(new Philosopher(this, i));
            stateOfPhilosophers[i] = State.THINKING;
        }
    }

    public synchronized void eat(int philosopher) {
        if (stateOfPhilosophers[philosopher] != State.STARVED) {
            try {
                wait(3000);
                System.out.println("Philosopher " + philosopher + " is trying to eat.");
                if (this.getLeft(philosopher) != State.EATING && this.getRight(philosopher) != State.EATING) {
                    if (stateOfPhilosophers[philosopher] == State.HUNGRY) {
                        stateOfPhilosophers[philosopher] = State.EATING;
                    } else {
                        System.out.println("Philosopher " + philosopher + " was not hungry.");
                    }
                } else {
                    System.out.println("Philosopher " + philosopher + " could not find chopsticks.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean stopEating(int philosopher) {
        boolean justAte = false;
        if (stateOfPhilosophers[philosopher] != State.STARVED) {
            try {
                int eatingTime = random.nextInt(1000);

                if (stateOfPhilosophers[philosopher] == State.EATING) {
                    wait(eatingTime);
                    stateOfPhilosophers[philosopher] = State.THINKING;
                    justAte = true;
                    System.out.println("Philosopher " + philosopher + " ate for " + eatingTime + "ms.");
                } else {
                    //System.err.println("Philosopher was not eating, therefore method stopEating was called unnecessarily.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return justAte;
    }

    /**
     * Returns the state of the philosopher to the left of the inputted philosopher position.
     *
     * @param philosopherPosition
     * @return the state of the philosopher to the left of the inputted philosopher position.
     */
    public State getLeft(int philosopherPosition) {
        //System.out.println(stateOfPhilosophers[(philosopherPosition+(numberOfPhilosophers-1))%numberOfPhilosophers] + " : " + (philosopherPosition+(numberOfPhilosophers-1))%numberOfPhilosophers);
        return stateOfPhilosophers[(philosopherPosition+(numberOfPhilosophers-1))%numberOfPhilosophers];
    }


    /**
     * Returns the state of the philosopher to the right of the inputted philosopher position.
     *
     * @param philosopherPosition
     * @return the state of the philosopher to the right of the inputted philosopher position.
     */
    public State getRight(int philosopherPosition) {
        //System.out.println(stateOfPhilosophers[(philosopherPosition+1)%numberOfPhilosophers] + " : " + (philosopherPosition+1)%numberOfPhilosophers);
        return stateOfPhilosophers[(philosopherPosition+1)%numberOfPhilosophers];
    }

    public static void main(String[] args) {
        //DiningPhilosophers diningPhilosophers = new DiningPhilosophers();
        //diningPhilosophers.getLeft(0);
        //diningPhilosophers.getRight(0);

        DiningPhilosophers diningPhilosophers = new DiningPhilosophers();

        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i].start();
        }

        for (int i = 0; i < philosophers.length; i++) {
            try {
                philosophers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getHungry(int id) {
        if (stateOfPhilosophers[id] != State.STARVED) {
            stateOfPhilosophers[id] = State.HUNGRY;
            System.out.println("Philosopher " + id + " is hungry.");
        }
    }

    public void starve(int id) {
        stateOfPhilosophers[id] = State.STARVED;
        System.err.println("Philosopher " + id + " starved to death.");
    }
}
