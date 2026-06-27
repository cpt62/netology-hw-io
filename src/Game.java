import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//import static GameStructure.TEMP;
//import static GameStructure.TEMP_TXT;


public class Game {
    private final String rootDir;

    public Game(String rootDir) {
        this.rootDir = rootDir;
        System.out.println("Выбран корневой каталог: " + rootDir);
    }

    public void createStructure() {
        System.out.println("Создание файловой структуры...");
        GameStructure[] gameStructure = GameStructure.values();
        File file;

        try (BufferedWriter logger = createLogger()) {
            System.out.println("Данные о создании файловой структуры содержатся в файле: " + GameStructure.TEMP_TXT.getPath());
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
        File tempFolder = new File(rootDir, GameStructure.TEMP.getPath());
        File tempFile = new File(rootDir, GameStructure.TEMP_TXT.getPath());
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

    public void savesToZip(String dirRoot) {
        File saveDir = new File(dirRoot, GameStructure.SAVE.getPath());
        File[] files = saveDir.listFiles();
        File fileZip = new File(saveDir, "savesGame.zip");

        if (files == null || files.length == 0) return;
        System.out.println("Архивация файлов *.dat ...");
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
        System.out.println("Архивация завершена");
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
        System.out.println("Сохранения очищены после архивации");
    }
}