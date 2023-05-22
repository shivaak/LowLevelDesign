package lowleveldesigns.snakeandladder.model;

import java.util.Objects;

public class Snake {

    int position;
    int nextPosition;

    public Snake(int position, int nextPosition) {
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
        Snake snake = (Snake) o;
        return position == snake.position && nextPosition == snake.nextPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, nextPosition);
    }
}
