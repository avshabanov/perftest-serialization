package com.alexshabanov.perftest.serialization.model.gson2;

import com.alexshabanov.perftest.serialization.model.Memo;
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
public final class MemoTypeAdapter extends TypeAdapter<Memo> {

  @Override
  public void write(JsonWriter out, Memo value) throws IOException {
    writeObject(out, value);
  }

  @Override
  public Memo read(JsonReader in) throws IOException {
    return readObject(in);
  }

  public static void writeObject(JsonWriter out, Memo value) throws IOException {
    out.beginObject();
    out.name("count");
    out.value(value.getCount());
    out.name("lines");
    out.beginArray();
    for (final String line : value.getLines()) {
      out.value(line);
    }
    out.endArray();
    out.endObject();
  }

  public static Memo readObject(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    int count = 0;
    final List<String> lines = new ArrayList<String>();

    in.beginObject();
    while (in.hasNext()) {
      final String name = in.nextName();
      if (name.equals("count")) {
        count = in.nextInt();
      } else if (name.equals("lines")) {
        in.beginArray();
        while (in.hasNext()) {
          lines.add(in.nextString());
        }
        in.endArray();
      } else {
        // WARN: unknown field
        System.err.println("Unknown field " + name + " for Memo object");
      }
    }
    in.endObject();

    return new Memo(lines, count);
  }
}
