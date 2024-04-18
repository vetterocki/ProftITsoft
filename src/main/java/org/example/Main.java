package org.example;

import org.example.attribute.SnakeCaseAttributeVerifier;
import org.example.json.JsonPathCollector;
import org.example.xml.XmlStatisticsWriter;
import picocli.CommandLine;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    CarStatisticsCommand command = new CarStatisticsCommand(
        new JsonPathCollector(), new SnakeCaseAttributeVerifier(), new XmlStatisticsWriter());

    CommandLine cli = new CommandLine(command);
    cli.execute(args);
  }

}
