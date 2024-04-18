package org.example;

import static java.util.stream.Collectors.groupingByConcurrent;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.json.JsonConverter;
import org.example.json.PathCollector;
import org.example.xml.XmlStatisticsWriter;

@Log
@RequiredArgsConstructor
public class CarStatisticsPool {
  private final PathCollector pathCollector;
  private final XmlStatisticsWriter xmlStatisticsWriter;


  public void execute(int threadsAmount, Path directoryPath, String attribute)
      throws InterruptedException {
    List<JsonConverter> callables = pathCollector.collectAllPathsInDirectory(directoryPath)
        .stream()
        .map(path -> new JsonConverter(path, attribute))
        .toList();
    ExecutorService executorService = Executors.newFixedThreadPool(threadsAmount);
    Map<String, Long> groupedByAttribute = executorService.invokeAll(callables)
        .stream()
        .parallel()
        .map(this::awaitAsyncResult)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .collect(groupingByConcurrent(Function.identity(), Collectors.counting()));
    log.info(String.format("All directory %s was processed", directoryPath));
    xmlStatisticsWriter.writeStatistics(groupedByAttribute, attribute, directoryPath);
    executorService.shutdown();
  }


  private <E> E awaitAsyncResult(Future<E> future) {
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new IllegalStateException(
          String.format("Exception occurred while getting Future result %s", e.getMessage()));
    }
  }
}
