import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.example.Main;
import org.example.domain.Car;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CarStatisticsApplicationSpeedTest {

  @BeforeAll
  static void setupJsonFiles() {
    int amountOfCars = 1_000_000;
    List<Car> cars = IntStream.range(0, amountOfCars).mapToObj(i -> {
      Car car = new Car();
      car.setCarManufacturer("Manufacturer №" + i);
      car.setModel("Model №" + i);
      car.setCarTypes("Sport, " + "Custom" + i);
      car.setColor("Color " + i);
      car.setYearManufactured(i);
      return car;
    }).toList();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    int filesAmount = 8;
    String pathName = "src/test/resources/benchmark";
    ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    try {
      for (int i = 0; i < filesAmount; i++) {
        File outputFile = new File(pathName + ("/cars-" + i + ".json"));
        objectWriter.writeValue(outputFile, cars);
        System.out.println("JSON file written: " + outputFile.getAbsolutePath());
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to write JSON files", e);
    }
  }


  @ParameterizedTest
  @ValueSource(ints = {1, 2, 4, 8})
  void test(int threadAmount) throws InterruptedException {
    long startTime = System.currentTimeMillis();
    String arg = String.valueOf(threadAmount);
    String[] args = {"-a", "model", "-t", arg, "-p", "src/test/resources/benchmark"};
    Main.main(args);

    long endTime = System.currentTimeMillis();
    long elapsedTime = endTime - startTime;
    System.out.println("Execution time for " + threadAmount + " threads: " + elapsedTime + " ms");
  }


}
