package com.alexshabanov.perftest.serialization.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Animal {
  private final long id;
  private final String name;
  private final Description description;
  private final int type;

  public Animal(@JsonProperty("animals") long id,
                @JsonProperty("name") String name,
                @JsonProperty("description") Description description,
                @JsonProperty("type") int type) {
    if (name == null) {
      throw new IllegalArgumentException("name");
    }
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Description getDescription() {
    return description;
  }

  public int getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Animal animal = (Animal) o;

    if (id != animal.id) return false;
    if (type != animal.type) return false;
    if (description != null ? !description.equals(animal.description) : animal.description != null)
      return false;
    if (!name.equals(animal.name)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + name.hashCode();
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + type;
    return result;
  }


  @Override
  public String toString() {
    return "Animal{" +
        "id=" + getId() +
        ", name='" + getName() + '\'' +
        ", description='" + getDescription() + '\'' +
        ", type=" + getType() +
        '}';
  }
}
