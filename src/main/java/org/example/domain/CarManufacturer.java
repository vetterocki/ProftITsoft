package org.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.ToString;

@Data
public class CarManufacturer {
  @ToString.Exclude
  private Set<Car> cars = new HashSet<>();

  private String name;

  private String country;

  private int yearFounded;

  private boolean isPubliclyTraded;

  private String CEO;

}
