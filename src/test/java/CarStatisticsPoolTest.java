import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;
import org.example.CarStatisticsPool;
import org.example.json.JsonPathCollector;
import org.example.xml.XmlStatisticsWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarStatisticsPoolTest {

  @Mock
  private XmlStatisticsWriter xmlStatisticsWriter;

  @InjectMocks
  private CarStatisticsPool carStatisticsPool;

  @Captor
  ArgumentCaptor<Map<String, Long>> groupedByAttributeCaptor;

  @BeforeEach
  public void setup() {
    carStatisticsPool = new CarStatisticsPool(new JsonPathCollector(), xmlStatisticsWriter);
  }

  @ParameterizedTest
  @MethodSource("provideAttributesAndExpectedResults")
  @DisplayName("Testing generated statistics (Map<String, Long> map, which is created inside CarStatisticsPool) values based on attribute")
  public void testGroupedByAttributeMap(String attribute, Map<String, Long> map)
      throws InterruptedException {
    Path directoryPath = Path.of("src/main/resources");
    carStatisticsPool.execute(3, directoryPath, attribute);

    verify(xmlStatisticsWriter).writeStatistics(groupedByAttributeCaptor.capture(), eq(attribute),
        eq(directoryPath));

    assertEquals(map, groupedByAttributeCaptor.getValue());
  }

  private static Stream<Arguments> provideAttributesAndExpectedResults() {
    return Stream.of(
        Arguments.of("model", Map.of("Mustang", 2L, "Model 3", 1L, "Prius", 3L, "Civic", 1L)),
        Arguments.of("car_types",
            Map.of("Sport", 3L, "Electric", 1L, "Muscle", 2L, "Coupe", 2L, "Sedan", 5L)),
        Arguments.of("car_manufacturer", Map.of("Toyota", 3L, "Tesla", 1L, "Ford", 2L, "Honda", 1L))
    );
  }
}
