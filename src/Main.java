public class Main {
    public static void main(String[] args) {
        //task1
        Game game = new Game("/home/cpt62/projects/netology-hw-io/Games");
        game.createStructure();

        //task2
        GameProgress progress1 = new GameProgress(100, 5, 1, 0.3);
        GameProgress progress2 = new GameProgress(4, 3, 18, 14.5);
        GameProgress progress3 = new GameProgress(22, 4, 29, 44.1);

        progress1.saveGame(game.getRootDir());
        progress2.saveGame(game.getRootDir());
        progress3.saveGame(game.getRootDir());

        //метод архивации отдал классу Game
        game.savesToZip(game.getRootDir());

        //task3


    }
}
