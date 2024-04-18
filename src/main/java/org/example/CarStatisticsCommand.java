package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.attribute.AttributeVerifier;
import org.example.json.PathCollector;
import org.example.xml.XmlStatisticsWriter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Log
@RequiredArgsConstructor
@Command(name = "CarStatistics", mixinStandardHelpOptions = true)
public class CarStatisticsCommand implements Runnable {

  @Option(names = {"-p", "--path"}, description = "Path of directory with JSON files")
  private Path directoryPath = Paths.get("src/main/resources");

  @Option(names = {"-a", "--attr"}, description = "Attribute to be analyzed")
  private String attribute;

  @Option(names = {"-t", "--threads"}, description = "Threads amount")
  private Integer threadsAmount;

  private final PathCollector pathCollector;
  private final AttributeVerifier attributeVerifier;
  private final XmlStatisticsWriter xmlStatisticsWriter;


  @Override
  public void run() {
    try {
      attributeVerifier.verifyAttributeIsValid(attribute);
      CarStatisticsPool carStatisticsPool = new CarStatisticsPool(pathCollector, xmlStatisticsWriter);
      carStatisticsPool.execute(threadsAmount, directoryPath, attribute);
    } catch (InterruptedException | IllegalArgumentException e) {
      log.log(Level.WARNING, String.format("Failed thread pool execution: %s", e.getMessage()));
    }

  }

}
