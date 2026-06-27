package task1;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Game {
    private final StringBuilder logger;
    private final String rootDir;

    public Game(String rootDir) {
        this.logger = new StringBuilder();
        this.rootDir = rootDir;
    }

    public void createStructure() {
        GameStructure[] gameStructure = GameStructure.values();
        File file;
        for (GameStructure gs : gameStructure) {
            file = new File(gs.getPath());
            if (gs.checkItem()) {
                logger.append("Проверка существования директории " + gs.getPath() + " для целесообразности его создания\n");
                if (!file.exists()) {
                    if (file.mkdirs()) {
                        logger.append("Каталог " + gs.getPath() + " создан\n");
                    } else {
                        logger.append("Каталог " + gs.getPath() + " был создан ранее\n");
                    }
                }
            } else {
                logger.append("Проверка существования файла " + gs.getPath() + " для целесообразности его создания\n");
                if (!file.exists()) {
                    try {
                        if (file.createNewFile()) {
                            logger.append("Файл " + gs.getPath() + " создан\n");
                        } else {
                            logger.append("Файл " + gs.getPath() + " был создан ранее\n");
                        }
                    } catch (IOException ex) {
                        logger.append("!!! Файл не создан. Проверьте корректность вводимых данных");
                    }
                }
            }
        }
        logWriter(logger);
    }

    public String getRootDir() {
        return this.rootDir;
    }

    private void logWriter(StringBuilder stringBuilder) {
        byte[] bytes = stringBuilder.toString().getBytes();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("Games/temp/temp.txt"))) {
            bos.write(bytes);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
