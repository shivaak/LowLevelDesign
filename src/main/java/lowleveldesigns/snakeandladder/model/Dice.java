package lowleveldesigns.snakeandladder.model;

import java.util.Random;

public class Dice {
    private int numberOfDice;
    private int maxValue;

    public Dice(int numberOfDice){
        this.numberOfDice = numberOfDice;
        this.maxValue = numberOfDice * 6;
    }

    public int rollDice(){
        Random random = new Random();
        int randomDiceValue = random.nextInt(maxValue);
        return randomDiceValue+1;
    }


}
