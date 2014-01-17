package com.alexshabanov.perftest.serialization.model.protobuf;

import com.alexshabanov.perftest.serialization.model.Memo;
import com.alexshabanov.perftest.serialization.model.Animal;
import com.alexshabanov.perftest.serialization.model.Description;
import com.alexshabanov.perftest.serialization.model.Zoo;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for adapting existing domain model to the protobuf domain model
 */
public final class PbAdapter {
  private PbAdapter() {
  }

  public static Pb.Memo convert(Memo memo) {
    return Pb.Memo.newBuilder()
        .setCount(memo.getCount())
        .addAllLines(memo.getLines())
        .build();
  }

  public static Pb.Description convert(Description description) {
    return Pb.Description.newBuilder()
        .setMemo(convert(description.getMemo()))
        .setNote(description.getNote())
        .setTimestamp(description.getTimestamp())
        .build();
  }

  public static Pb.Animal convert(Animal animal) {
    return Pb.Animal.newBuilder()
        .setDescription(convert(animal.getDescription()))
        .setId(animal.getId())
        .setType(animal.getType())
        .setName(animal.getName())
        .build();
  }

  public static Pb.Zoo convert(Zoo zoo) {
    final Pb.Zoo.Builder zooBuilder = Pb.Zoo.newBuilder();
    zooBuilder.setName(zoo.getName());
    for (final Animal animal : zoo.getAnimals()) {
      zooBuilder.addAnimals(convert(animal));
    }
    return zooBuilder.build();
  }

  public static Memo convert(Pb.Memo memo) {
    return new Memo(memo.getLinesList(), memo.getCount());
  }

  public static Description convert(Pb.Description description) {
    return new Description(description.getNote(), description.getTimestamp(),
        convert(description.getMemo()));
  }

  public static Animal convert(Pb.Animal animal) {
    return new Animal(animal.getId(), animal.getName(),
        convert(animal.getDescription()), animal.getType());
  }

  public static Zoo convert(Pb.Zoo zoo) {
    final List<Animal> animals = new ArrayList<Animal>(zoo.getAnimalsCount());
    for (final Pb.Animal animal : zoo.getAnimalsList()) {
      animals.add(convert(animal));
    }
    return new Zoo(zoo.getName(), animals);
  }
}
