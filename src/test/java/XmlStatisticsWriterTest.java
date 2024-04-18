import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.example.xml.XmlStatisticsWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class XmlStatisticsWriterTest {
  private XmlStatisticsWriter xmlStatisticsWriter;

  @BeforeEach
  public void setup() {
    xmlStatisticsWriter = new XmlStatisticsWriter();
  }

  @ParameterizedTest
  @Order(1)
  @MethodSource("provideArgumentsForXmlStatistics")
  @DisplayName("Testing creating XML files (file name, location etc.) by XmlStatisticsWriter instance")
  public void testWriteStatistics(String attribute, Map<String, Long> map) {
    Path path = Paths.get("src/test/resources");
    xmlStatisticsWriter.writeStatistics(map, attribute, path);
    Path xmlFile = path.resolve(String.format("statistics_by_%s.xml", attribute));
    assertTrue(Files.exists(xmlFile));
  }


  /*
  Please, run this test AFTER xml files were created.
   */

  @Test
  @Order(2)
  @DisplayName("Testing XML file content to meet requirements.")
  public void testXmlFileContent() {
    Path xmlFile = Paths.get("src/test/resources/statistics_by_car_manufacturer.xml");
    String expectedContent = """
        <statistics>
          <item>
            <value>Toyota</value>
            <count>3</count>
          </item>
          <item>
            <value>Ford</value>
            <count>2</count>
          </item>
          <item>
            <value>Tesla</value>
            <count>1</count>
          </item>
        </statistics>""";

    try (Stream<String> lines = Files.lines(xmlFile)) {
      String xmlToString = lines.collect(Collectors.joining("\n"));

      assertEquals(expectedContent, xmlToString);
    } catch (IOException ioException) {
      throw new RuntimeException();
    }
  }


  private static Stream<Arguments> provideArgumentsForXmlStatistics() {
    return Stream.of(
        Arguments.of("car_manufacturer", Map.of("Toyota", 3L, "Ford", 2L, "Tesla", 1L)),
        Arguments.of("model", Map.of("Prius", 3L, "Mustang", 2L, "Model 3", 1L)),
        Arguments.of("car_types", Map.of("Sedan", 5L, "Sport", 3L, "Muscle", 2L, "Electric", 1L)));
  }
}
