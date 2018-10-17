package wumpus;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class Game {
    private Random randomGenerator;
    private GameItem[][] board;
    private int[] playerLocation; // player location array with size 2 as [rowIndex, columnIndex]
    private int score;
    private int goldCount;

    public Game() {
        board = new GameItem[4][4];
        playerLocation = new int[2];
        randomGenerator = new Random();
        setBoard();
    }

    public void runGame() {
        display();
        senseNearby();
        menu();
    }

    private void setBoard() {
        placePlayer(); // initially placing player at [3,3]
        placeWumpus();
        placeGold();
        placePit();
        fillClearGround();
    }

    private void display() {
        System.out.println("Current status of board:");
        for (int i = 0; i < 4; i++) {
            System.out.print("\t");
            for (int j = 0; j < 4; j++) {
                System.out.print(board[i][j].display());
                System.out.print(' ');
            }
            System.out.println("");
        }
    }

    private void senseNearby() {
        int[] left = getLeftCell();
        int[] right = getRightCell();
        int[] up = getUpCell();
        int[] down = getDownCell();
        System.out.println("Player senses following smells nearby:" +
                "\n\tLeft: " + sense(left) +
                "\n\tRight: " + sense(right) +
                "\n\tUp: " + sense(up) +
                "\n\tDown: " + sense(down));
    }

    private void menu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=======Wumpus======");
        System.out.println("\t1. Move player left");
        System.out.println("\t2. Move player right");
        System.out.println("\t3. Move player up");
        System.out.println("\t4. Move player down");
        System.out.println("\t5. Quit");
        System.out.println("Please make a choice from above: ");
        int choice;
        try {
            choice = sc.nextInt();
        } catch (InputMismatchException ex) {
            choice = 0;
        }
        performGameAction(choice);
    }

    private void performGameAction(int userChoice) {
        switch (userChoice) {
            case 1: {
                int[] left = getLeftCell();
                movePlayer(left);
                break;
            }
            case 2: {
                int[] right = getRightCell();
                movePlayer(right);
                break;
            }
            case 3: {
                int[] up = getUpCell();
                movePlayer(up);
                break;
            }
            case 4: {
                int[] down = getDownCell();
                movePlayer(down);
                break;
            }
            case 5: {
                System.out.println("Thank you for Playing! Bye Bye!");
                System.exit(0);
            }
            default: {
                System.out.println("Oops! Invalid Choice....");
            }
        }
        menu();
    }

    private void movePlayer(int[] position) {
        GameItem gameItem = board[position[0]][position[1]];
        if (gameItem instanceof Wumpus) {
            System.out.println("This location contains Wumpus");
            System.out.println("You lost the game!!!");
            System.exit(0);
        }

        if (gameItem instanceof Pit) {
            System.out.println("This location contains a pit");
            System.out.println("You lost the game!!!");
            System.exit(0);
        }

        if (gameItem instanceof Gold) {
            System.out.println("This location contains gold");
            System.out.println("You got 1 point. Your current point(s): " + ++score);
            if (score == goldCount) {
                System.out.println("Congratulations!!! You collected all the pieces of gold and won the game");
                System.exit(0);
            }
        }

        if (gameItem instanceof ClearGround) {
            System.out.println("This location is clear ground.");
        }
        board[playerLocation[0]][playerLocation[1]] = new ClearGround();
        playerLocation = position;
        board[playerLocation[0]][playerLocation[1]] = new ClearGround('*');
        display();
    }

    private String sense(int[] pos) {
        GameItem gameItem = board[pos[0]][pos[1]];
        if (gameItem instanceof Wumpus)
            return "Vile Smell";
        if (gameItem instanceof Pit)
            return "Breeze";
        if (gameItem instanceof Gold)
            return "Faint Glitter";
        if (gameItem instanceof ClearGround)
            return "";
        throw new UnsupportedOperationException("Unable to sense");
    }

    private int[] getLeftCell() {
        int pos[] = new int[2];
        int x = playerLocation[0];
        int y = playerLocation[1] - 1;
        if (y == -1) y = 3;
        pos[0] = x;
        pos[1] = y;
        return pos;
    }

    private int[] getRightCell() {
        int pos[] = new int[2];
        int x = playerLocation[0];
        int y = playerLocation[1] + 1;
        if (y == 4) y = 0;
        pos[0] = x;
        pos[1] = y;
        return pos;
    }

    private int[] getUpCell() {
        int pos[] = new int[2];
        int x = playerLocation[0] - 1;
        int y = playerLocation[1];
        if (x == -1) x = 3;
        pos[0] = x;
        pos[1] = y;
        return pos;
    }

    private int[] getDownCell() {
        int pos[] = new int[2];
        int x = playerLocation[0] + 1;
        int y = playerLocation[1];
        if (x == 4) x = 0;
        pos[0] = x;
        pos[1] = y;
        return pos;
    }

    private void placePlayer() {
        playerLocation[0] = 3;
        playerLocation[1] = 3;
        board[3][3] = new ClearGround('*');
    }

    private void placeWumpus() {
        int x;
        int y;
        do {
            x = randomGenerator.nextInt(4);
            y = randomGenerator.nextInt(4);
        } while (board[x][y] != null);
        board[x][y] = new Wumpus();
    }

    private void placeGold() {
        goldCount = randomGenerator.nextInt(4) + 1;
        for (int i = 1; i <= goldCount; i++) {
            int x;
            int y;
            do {
                x = randomGenerator.nextInt(4);
                y = randomGenerator.nextInt(4);
            } while (board[x][y] != null);
            board[x][y] = new Gold();
        }
    }

    private void placePit() {
        for (int i = 1; i <= 3; i++) {
            int x;
            int y;
            do {
                x = randomGenerator.nextInt(4);
                y = randomGenerator.nextInt(4);
            } while (board[x][y] != null);
            board[x][y] = new Pit();
        }
    }

    private void fillClearGround() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null) {
                    board[i][j] = new ClearGround();
                }
            }
        }
    }
} 
      


     



