package task1;


import java.io.File;

public enum GameStructure {

    SRC_MAIN("src/main", true),
    MAIN_JAVA("Games/src/main/Main.java", false),
    UTILS_JAVA("Games/src/main/Utils.java", false),
    SRC_TEST("Games/src/test", true),

    DRAWABLES("Games/res/drawables", true),
    VECTORS("Games/res/vectors", true),
    ICONS("Games/res/icons", true),

    SAVE("Games/savegames", true),

    TEMP("Games/temp", true),
    TEMP_TXT("Games/temp/temp.txt", false);



    private final String path;
    private final boolean isDirectory;

    GameStructure(String name, boolean isDirectory) {
        this.path = name;
        this.isDirectory = isDirectory;
    }

    public String getPath() {
        return path;
    }

    public boolean checkItem() {
        return isDirectory;
    }

    public void create(String rootDir) {
        File file = new File(rootDir, path);
        file.getParentFile()

    }


}
