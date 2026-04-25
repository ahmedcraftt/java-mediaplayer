package application;

import infrastructure.media.LibraryStorage;
import mediaLibrary.Library;

import java.util.*;

public class LibraryService {

    private final List<Library> libraries;
    private Library activeLibrary;

    public LibraryService() {
        this.libraries = LibraryStorage.load();

        if (!libraries.isEmpty()) {
            activeLibrary = libraries.stream()
                    .filter(Library::isDefault)
                    .findFirst()
                    .orElse(libraries.get(0));
        }
    }

    public void addLibrary(Library library) {
        libraries.add(library);
        save();
    }

    public void setActiveLibrary(Library library) {
        this.activeLibrary = library;
        save();
    }

    public Library getActiveLibrary() {
        return activeLibrary;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    private void save() {
        LibraryStorage.save(libraries);
    }

    public boolean hasLibraries() {
        return !libraries.isEmpty();
    }

    public boolean hasActiveLibrary() {
        return activeLibrary != null;
    }
}