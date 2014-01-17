package com.alexshabanov.perftest.serialization.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class Zoo {
  private final String name;
  private final List<Animal> animals;

  public Zoo(@JsonProperty("name") String name, @JsonProperty("animals") List<Animal> animals) {
    if (name == null) {
      throw new IllegalArgumentException("name");
    }
    if (animals == null) {
      throw new IllegalArgumentException("animals");
    }
    this.name = name;
    this.animals = animals;
  }

  public String getName() {
    return name;
  }

  public List<Animal> getAnimals() {
    return animals;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Zoo zoo = (Zoo) o;

    if (!animals.equals(zoo.animals)) return false;
    if (!name.equals(zoo.name)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + (animals.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "Zoo{" +
        "name='" + name + '\'' +
        ", animals=" + animals +
        '}';
  }
}
