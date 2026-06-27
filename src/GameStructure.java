public enum GameStructure {

    SRC_MAIN("src/main", true),
    MAIN_JAVA("src/main/Main.java", false),
    UTILS_JAVA("src/main/Utils.java", false),
    SRC_TEST("src/test", true),

    DRAWABLES("res/drawables", true),
    VECTORS("res/vectors", true),
    ICONS("res/icons", true),

    SAVE("savegames", true),

    TEMP("temp", true),
    TEMP_TXT("temp/temp.txt", false);

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


}
