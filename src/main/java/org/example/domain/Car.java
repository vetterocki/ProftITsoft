package org.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Car {
  private String model;

  @JsonProperty("car_manufacturer")
  private String carManufacturer;

  @JsonProperty("year_manufactured")
  private Integer yearManufactured;

  private String color;

  @JsonProperty("car_types")
  private String carTypes;


}
