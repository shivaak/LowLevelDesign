package snakeandladdergame.model;

import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class GameBoard {
    private int boardSize;
    private Dice dice;
    private Map<Integer, Snake> snakes;

    private Map<Integer, Ladder> ladders;

    private Queue<Player> playerQueue;

    private GameBoard(GameBoardBuilder builder) {
        this.boardSize = builder.boardSize;
        this.dice = builder.dice;
        this.snakes = builder.snakes;
        this.ladders = builder.ladders;
        this.playerQueue = builder.playerQueue;
    }

    public void startGame(){
        while(playerQueue.size()>1){
            Player player = playerQueue.poll();
            System.out.println();
            Scanner scanner = new Scanner(System.in);
            System.out.println(String.format("Player %s press any key to roll the dice ..", player.getName()));
            scanner.nextLine();

            int moves = dice.rollDice();
            int nextPosition = player.getPosition() + moves;

            if(nextPosition>boardSize){
                System.out.println(String.format("Player %s rolled %d which is greater than board size. Hence no movement", player.getName(), moves));
                playerQueue.offer(player);
                continue;
            }


            if(nextPosition == boardSize){
                System.out.println(String.format("Player %s rolled %d and won the match", player.getName(), moves));
                continue;
            }else if(snakes.containsKey(nextPosition)) {
                player.setPosition(snakes.get(nextPosition).getNextPosition());
                System.out.println(String.format("Player %s rolled %d, bitten by snake at %d position. Moving to position %d", player.getName(), moves, nextPosition, snakes.get(nextPosition).getNextPosition()));
            }else if(ladders.containsKey(nextPosition)) {
                System.out.println(String.format("Player %s rolled %d, got ladder at %d position. Moving to position %d", player.getName(), moves, nextPosition, ladders.get(nextPosition).getNextPosition()));
                player.setPosition(ladders.get(nextPosition).getNextPosition());
                if(player.getPosition() == boardSize) {
                    System.out.println(String.format("Player %s won the match", player.getName()));
                    continue;
                }
            }else{
                System.out.println(String.format("Player %s rolled %d and moved to position %d", player.getName(), moves, nextPosition));
                player.setPosition(nextPosition);
            }

            playerQueue.offer(player);

            System.out.println("-------------");
        }
    }

    public static class GameBoardBuilder {
        private int boardSize;
        private Dice dice;
        private Map<Integer, Snake> snakes;

        private Map<Integer, Ladder> ladders;

        private Queue<Player> playerQueue;

        public GameBoardBuilder(int boardSize, int numberOfDice) {
            this.boardSize = boardSize;
            this.dice = new Dice(numberOfDice);
        }

        public GameBoardBuilder boardSize(int boardSize) {
            this.boardSize = boardSize;
            return this;
        }

        public GameBoardBuilder dice(Dice dice) {
            this.dice = dice;
            return this;
        }

        public GameBoardBuilder snakes(Map<Integer, Snake> snakes) {
            this.snakes = snakes;
            return this;
        }

        public GameBoardBuilder ladders(Map<Integer, Ladder> ladders) {
            this.ladders = ladders;
            return this;
        }

        public GameBoardBuilder playerQueue(Queue<Player> playerQueue) {
            this.playerQueue = playerQueue;
            return this;
        }

        public GameBoard build(){
            return new GameBoard(this);
        }
    }
}
