package org.example.json;

import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class JsonConverter implements Callable<List<String>> {
  private final Path filePath;
  private final String attribute;

  @Override
  public List<String> call() {
    JsonFactory jsonFactory = new JsonFactory();

    try (BufferedReader br = Files.newBufferedReader(filePath);
         JsonParser jsonParser = jsonFactory.createParser(br)) {

      if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
        throw new IllegalStateException("Expected content to be an array");
      }

      List<String> result = new ArrayList<>();
      while (jsonParser.nextToken() != null) {
        String currentFieldName = jsonParser.getCurrentName();

        if (jsonParser.getCurrentToken().equals(FIELD_NAME) && currentFieldName.equals(attribute)) {
          Optional.ofNullable(jsonParser.nextTextValue())
              .map(textValue -> textValue.split(",\\s*"))
              .map(Arrays::asList)
              .ifPresent(result::addAll);
        }
      }
      return result;
    } catch (IOException ioException) {
      log.warning("IO exception occurred while parsing file " + ioException.getMessage());
    }
    return null;
  }
}
