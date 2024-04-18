package org.example.json;

import java.nio.file.Path;
import java.util.List;

public interface PathCollector {
  List<Path> collectAllPathsInDirectory(Path directoryPath);
}
