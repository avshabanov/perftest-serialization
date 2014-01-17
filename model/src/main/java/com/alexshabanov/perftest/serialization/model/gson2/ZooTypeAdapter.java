package com.alexshabanov.perftest.serialization.model.gson2;

import com.alexshabanov.perftest.serialization.model.Animal;
import com.alexshabanov.perftest.serialization.model.Zoo;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Shabanov
 */
public final class ZooTypeAdapter extends TypeAdapter<Zoo> {
  @Override
  public void write(JsonWriter out, Zoo value) throws IOException {
    writeObject(out, value);
  }

  @Override
  public Zoo read(JsonReader in) throws IOException {
    return readObject(in);
  }

  public static void writeObject(JsonWriter out, Zoo value) throws IOException {
    out.beginObject();
    out.name("name");
    out.value(value.getName());
    out.name("animals");
    out.beginArray();
    for (final Animal animal : value.getAnimals()) {
      AnimalTypeAdapter.writeObject(out, animal);
    }
    out.endArray();
    out.endObject();
  }

  public static Zoo readObject(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    String name = null;
    final List<Animal> animals = new ArrayList<Animal>();

    in.beginObject();
    while (in.hasNext()) {
      final String fieldName = in.nextName();
      if (fieldName.equals("name")) {
        name = in.nextString();
      } else if (fieldName.equals("animals")) {
        in.beginArray();
        while (in.hasNext()) {
          animals.add(AnimalTypeAdapter.readObject(in));
        }
        in.endArray();
      } else {
        // WARN: unknown field
        System.err.println("Unknown field " + fieldName + " for Zoo object");
      }
    }
    in.endObject();

    if (name == null) {
      throw new JsonParseException("No name");
    }

    return new Zoo(name, animals);
  }
}
