package snakeandladdergame;

import snakeandladdergame.model.GameBoard;
import snakeandladdergame.model.Ladder;
import snakeandladdergame.model.Player;
import snakeandladdergame.model.Snake;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        Map<Integer, Snake> snakes = new HashMap<>();
        Map<Integer, Ladder> ladders = new HashMap<>();

        snakes.put(40, new Snake(40, 8));
        snakes.put(55, new Snake(55, 12));
        snakes.put(99, new Snake(99, 20));

        ladders.put(14, new Ladder(14, 68));
        ladders.put(27, new Ladder(27, 72));
        ladders.put(50, new Ladder(14, 85));

        Player p1 = new Player(1, "Shivaa");
        Player p2 = new Player(2, "Sathyadevi");

        Queue<Player> players = new LinkedList<>();
        players.offer(p1);
        players.offer(p2);


        GameBoard board = new GameBoard
                .GameBoardBuilder(100, 2)
                .ladders(ladders)
                .snakes(snakes)
                .playerQueue(players)
                .build();

        board.startGame();



    }
}
