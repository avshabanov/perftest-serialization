package com.alexshabanov.perftest.serialization.model.gson2;

import com.alexshabanov.perftest.serialization.model.Animal;
import com.alexshabanov.perftest.serialization.model.Description;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author Alexander Shabanov
 */
public final class AnimalTypeAdapter extends TypeAdapter<Animal> {
  @Override
  public void write(JsonWriter out, Animal value) throws IOException {
    writeObject(out, value);
  }

  @Override
  public Animal read(JsonReader in) throws IOException {
    return readObject(in);
  }

  public static void writeObject(JsonWriter out, Animal value) throws IOException {
    out.beginObject();
    out.name("id");
    out.value(value.getId());
    out.name("name");
    out.value(value.getName());
    out.name("description");
    DescriptionTypeAdapter.writeObject(out, value.getDescription());
    out.name("type");
    out.value(value.getType());
    out.endObject();
  }

  public static Animal readObject(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    int id = 0;
    String name = null;
    Description description = null;
    int type = 0;

    in.beginObject();
    while (in.hasNext()) {
      final String fieldName = in.nextName();
      if (fieldName.equals("id")) {
        id = in.nextInt();
      } else if (fieldName.equals("name")) {
        name = in.nextString();
      } else if (fieldName.equals("description")) {
        description = DescriptionTypeAdapter.readObject(in);
      } else if (fieldName.equals("type")) {
        type = in.nextInt();
      }else {
        // WARN: unknown field
        System.err.println("Unknown field " + fieldName + " for Animal object");
      }
    }
    in.endObject();

    if (name == null || description == null) {
      throw new JsonParseException("No name or description");
    }

    return new Animal(id, name, description, type);
  }
}
