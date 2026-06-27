package task2;

import task1.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game("/home/cpt62/projects/netology-hw-io/Games");
        game.createStructure();

        GameProgress progress1 = new GameProgress(100, 5, 1, 0.3);
        GameProgress progress2 = new GameProgress(4, 3, 18, 14.5);
        GameProgress progress3 = new GameProgress(22, 4, 29, 44.1);

        progress1.saveGame(game.getRootDir());
        try {
            Thread.sleep(100);
            progress2.saveGame(game.getRootDir());
            Thread.sleep(100);
            progress3.saveGame(game.getRootDir());
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        }
        progress1.savesToZip(game.getRootDir());
    }
}
