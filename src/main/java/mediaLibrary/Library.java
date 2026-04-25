package mediaLibrary;

import java.nio.file.Path;

public class Library {

    private final String name;
    private final String rootPath;
    private boolean isDefault;

    public Library(String name, Path rootPath, boolean isDefault) {
        this.name = name;
        this.rootPath = rootPath.toString();
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public Path getRootPath() {
        return Path.of(rootPath);
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean value) {
        this.isDefault = value;
    }
}