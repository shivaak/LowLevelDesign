package snakeandladdergame.model;

import java.util.Objects;

public class Ladder {

    private int position;

    private int nextPosition;

    public Ladder(int position, int nextPosition) {
        this.position = position;
        this.nextPosition = nextPosition;
    }

    public int getPosition() {
        return position;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ladder ladder = (Ladder) o;
        return position == ladder.position && nextPosition == ladder.nextPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, nextPosition);
    }
}
