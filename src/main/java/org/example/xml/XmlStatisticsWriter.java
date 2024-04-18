package org.example.xml;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.extern.java.Log;


@Log
public class XmlStatisticsWriter {
  public void writeStatistics(Map<String, Long> groupedByAttribute, String attribute, Path path) {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    XmlFactory xmlFactory = new XmlFactory();
    xmlFactory.setCodec(xmlMapper);
    String pathForXml = String.format("statistics_by_%s.xml", attribute);

    try (BufferedWriter bw = Files.newBufferedWriter(path.resolve(pathForXml));
         ToXmlGenerator toXmlGenerator = xmlFactory.createGenerator(bw)) {

      List<XmlItem> xmlItems = groupedByAttribute.entrySet().stream()
          .map(entry -> new XmlItem(entry.getKey(), entry.getValue()))
          .sorted(Comparator.comparing(XmlItem::getCount).reversed())
          .toList();

      toXmlGenerator.writeObject(new XmlStatistics(xmlItems));
      log.info(String.format("Statistics for %s generated. Created %s.", attribute, pathForXml));
    } catch (IOException e) {
      log.warning("Exception occurred while generating XML: " + e.getMessage());
    }
  }

}
