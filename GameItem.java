package wumpus;

public abstract class GameItem {
    private char gameItemType;

    protected GameItem(char c) {
        this.gameItemType = c;
    }

    public char display() {
        return gameItemType;
    }
}
