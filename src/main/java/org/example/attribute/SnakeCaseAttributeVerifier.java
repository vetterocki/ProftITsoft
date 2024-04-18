package org.example.attribute;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.domain.Car;

public class SnakeCaseAttributeVerifier implements AttributeVerifier {
  @Override
  public void verifyAttributeIsValid(String attribute) {
    boolean isValid = Arrays.stream(Car.class.getDeclaredFields())
        .map(Field::getName)
        .map(this::camelToSnake)
        .anyMatch(attribute::equals);

    if (!isValid)
      throw new IllegalArgumentException(
          String.format("Invalid attribute name {%s} for class {%s}",  attribute, Car.class)
      );
  }

  private String camelToSnake(String camelCase) {
    Pattern pattern = Pattern.compile("([a-z])([A-Z])");
    Matcher matcher = pattern.matcher(camelCase);

    return matcher.replaceAll("$1_$2").toLowerCase();
  }

}
