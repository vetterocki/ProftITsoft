package org.example.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonPathCollector implements PathCollector {
  @Override
  public List<Path> collectAllPathsInDirectory(Path directoryPath) {
    try (Stream<Path> paths = Files.list(directoryPath)) {
      return paths.filter(this::isJsonByExtension)
          .filter(Files::isRegularFile)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean isJsonByExtension(Path path) {
    String fileName = path.getFileName().toString();
    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
    return extension.equalsIgnoreCase("json");
  }
}
