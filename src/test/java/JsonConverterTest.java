import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;
import org.example.json.JsonConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class JsonConverterTest {

  @TestTemplate
  @ParameterizedTest
  @MethodSource("provideArguments")
  @DisplayName("Testing converting .json file to the list of attribute values")
  public void testConvertingMethod(String field, List<String> expected) {
    String filePath = "src/test/resources/cars.json";
    JsonConverter jsonConverter = new JsonConverter(Path.of(filePath), field);
    List<String> result = jsonConverter.call();
    assertEquals(expected, result);
  }

  @Test
  public void testWhenJsonFileIsNotArray() {
    Path nonArrayJson = Path.of("src/test/resources/non_array_cars.json");
    JsonConverter jsonConverter = new JsonConverter(nonArrayJson, "car_manufacturer");

    assertThrows(IllegalStateException.class, jsonConverter::call);
  }

  private static List<Arguments> provideArguments() {
    return List.of(
        Arguments.of("car_manufacturer",
            List.of("Toyota", "Tesla", "Honda", "Ford", "Ford", "Toyota", "Toyota")),
        Arguments.of("car_types",
            List.of("Sport", "Sedan", "Electric", "Sedan", "Sedan", "Muscle", "Coupe", "Muscle",
                "Coupe", "Sport", "Sedan", "Sport", "Sedan")),
        Arguments.of("model",
            List.of("Prius", "Model 3", "Civic", "Mustang", "Mustang", "Prius", "Prius"))
    );
  }
}
