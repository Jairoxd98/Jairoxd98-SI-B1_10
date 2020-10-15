package wilson_algoritmo;

public class Cell {

    transient private int x;
    transient private int y;

    transient private String direction;
    transient private boolean visited;
    transient private boolean blank;
    private boolean[] neighbors;
    private int value;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
        this.blank = true;
        this.neighbors = new boolean[4];
    }

    public Cell(int x, int y, boolean blank) {
        this.x = x;
        this.y = y;
        this.value = 0;
        this.blank = blank;
        this.neighbors = new boolean[4];
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isBlank() {
        return blank;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    public boolean[] getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(boolean[] neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
