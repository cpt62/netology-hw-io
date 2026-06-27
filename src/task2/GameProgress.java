package task2;

import task1.GameStructure;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


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
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void savesToZip(String dirRoot) {
        File saveDir = new File(dirRoot, GameStructure.SAVE.getPath());
        File[] files = saveDir.listFiles();
        File fileZip = new File(saveDir, "savesGame.zip");

        if (files == null || files.length == 0) return;
        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(fileZip))) {
            ZipEntry zipEntry;
            byte[] buffer = new byte[4096];
            for (File file : files) {
                if (file.isDirectory()) continue;
                zipEntry = new ZipEntry(file.getName());
                zip.putNextEntry(zipEntry);
                try (FileInputStream fis = new FileInputStream(file)) {
                    int length;
                    while ((length = fis.read(buffer)) != -1) {
                        zip.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                zip.closeEntry();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        clearDat(dirRoot);

    }

    private void clearDat(String dirRoot) {
        File file = new File(dirRoot, GameStructure.SAVE.getPath());
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Сохранения отсутствуют");
            return;
        }
        Arrays.stream(files)
                .filter(f -> f.getName().endsWith(".dat"))
                .forEach(File::delete);
    }
}