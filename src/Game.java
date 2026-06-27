import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


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

    public void unZip() {
        File saveDir = new File(rootDir, GameStructure.SAVE.getPath());

        File[] allFiles = saveDir.listFiles();

        if (allFiles == null) {
            System.out.println("Папка пуста!");
            return;
        }

        List<File> zip = Arrays.stream(allFiles)
                .filter(z -> z.getName().endsWith("zip"))
                .toList();
        if (!zip.isEmpty()) {
            for (File f : zip) {
                try (ZipInputStream zis = new ZipInputStream(new FileInputStream(f.getPath()))) {
                    ZipEntry zipEntry;
                    int i;
                    byte[] buffer = new byte[4096];
                    while ((zipEntry = zis.getNextEntry()) != null) {
                        File saveFile = new File(rootDir, GameStructure.SAVE.getPath());
                        try (FileOutputStream fos = new FileOutputStream(
                                new File(saveFile.getPath(), zipEntry.getName()))) {
                            while ((i = zis.read(buffer)) != -1) {
                                fos.write(buffer, 0, i);
                            }
                            fos.flush();
                            zis.closeEntry();
                        }
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("Файлы распакованы");
            }
        } else {
            System.out.println("Файлы для распаковки не найдены");
        }
    }

    public GameProgress restoreLastProgress(String dirRoot) {
        File file = new File(dirRoot, GameStructure.SAVE.getPath());
        GameProgress gameProgress = null;
        Optional<File> optionalFile =
                Arrays.stream(file.listFiles())
                        .filter(f -> f.getName().endsWith("dat"))
                        .max((f1, f2) -> f1.getPath().compareTo(f2.getPath()));
        if (optionalFile.isPresent()) {
            File fileSerializable = optionalFile.get();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileSerializable))) {
                gameProgress = (GameProgress) ois.readObject();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return gameProgress;
    }
}