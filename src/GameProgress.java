import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;


public class GameProgress implements Serializable {
    private static final long serialVersionUID = 1L;

    private int health;
    private int weapons;
    private int lvl;
    private double distance;

    public GameProgress(int health, int weapons, int lvl, double distance) {
        this.health = health;
        this.weapons = weapons;
        this.lvl = lvl;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "health=" + health +
                ", weapons=" + weapons +
                ", lvl=" + lvl +
                ", distance=" + distance +
                '}';
    }

    public void saveGame(String dirRoot) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
        File saveGames = new File(dirRoot, GameStructure.SAVE.getPath());
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(
                        new File(saveGames, "save_" + timestamp + ".dat")))) {
            oos.writeObject(this);
            System.out.println("Игровой прогресс сохранен");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}