package task1;


import java.io.*;

import static task1.GameStructure.TEMP;
import static task1.GameStructure.TEMP_TXT;


public class Game {
    private final String rootDir;

    public Game(String rootDir) {
        this.rootDir = rootDir;
    }

    public void createStructure() {
        GameStructure[] gameStructure = GameStructure.values();
        File file;

        try (BufferedWriter logger = createLogger()) {
            for (GameStructure gs : gameStructure) {
                file = new File(getFullPath(gs));
                if (gs.checkItem()) {
                    logger.write("Проверка существования директории " + getFullPath(gs) + " для целесообразности его создания\n");
                    if (!file.exists()) {
                        if (file.mkdirs()) {
                            logger.write("Каталог " + getFullPath(gs) + " создан\n");
                        }
                    } else {
                        logger.write("Каталог " + getFullPath(gs) + " уже существует\n");
                    }
                } else {
                    logger.write("Проверка существования файла " + getFullPath(gs) + " для целесообразности его создания\n");
                    if (!file.exists()) {
                        try {
                            if (file.createNewFile()) {
                                logger.write("Файл " + getFullPath(gs) + " создан\n");
                            }
                        } catch (IOException ex) {
                            logger.write("Вызвано исключение! Файл не создан. Проверьте корректность вводимых данных");
                        }
                    } else {
                        logger.write("Файл " + getFullPath(gs) + " уже существует");
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getRootDir() {
        return rootDir;
    }

    public String getFullPath(GameStructure gameStructure) {
        return new File(this.rootDir, gameStructure.getPath()).getPath();
    }

    public BufferedWriter createLogger() throws IOException {
        File tempFolder = new File(rootDir, TEMP.getPath());
        File tempFile = new File(rootDir, TEMP_TXT.getPath());
        String folderLogMessage = "Каталог " + tempFolder.getPath() + " уже существует\n";
        String fileLogMessage = "Файл " + tempFile.getPath() + " уже существует\n";
        if (!tempFolder.exists()) {
            if (tempFolder.mkdirs()) {
                folderLogMessage = "Каталог " + tempFolder.getPath() + " создан\n";
            }
        }
        if (!tempFile.exists())
            if (tempFile.createNewFile()) {
                fileLogMessage = "Файл " + tempFile.getPath() + " создан\n";
            }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
        bufferedWriter.write(folderLogMessage);
        bufferedWriter.write(fileLogMessage);
        return bufferedWriter;
    }
}