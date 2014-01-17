package com.alexshabanov.perftest.serialization.model.gson2;

import com.alexshabanov.perftest.serialization.model.Description;
import com.alexshabanov.perftest.serialization.model.Memo;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author Alexander Shabanov
 */
public final class DescriptionTypeAdapter extends TypeAdapter<Description> {
  @Override
  public void write(JsonWriter out, Description value) throws IOException {
    writeObject(out, value);
  }

  @Override
  public Description read(JsonReader in) throws IOException {
    return readObject(in);
  }

  public static void writeObject(JsonWriter out, Description value) throws IOException {
    out.beginObject();
    out.name("note");
    out.value(value.getNote());
    out.name("timestamp");
    out.value(value.getTimestamp());
    out.name("memo");
    MemoTypeAdapter.writeObject(out, value.getMemo());
    out.endObject();
  }

  public static Description readObject(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    Memo memo = null;
    long timestamp = 0;
    String note = null;

    in.beginObject();
    while (in.hasNext()) {
      final String name = in.nextName();
      if (name.equals("note")) {
        note = in.nextString();
      } else if (name.equals("timestamp")) {
        timestamp = in.nextLong();
      } else if (name.equals("memo")) {
        memo = MemoTypeAdapter.readObject(in);
      } else {
        // WARN: unknown field
        System.err.println("Unknown field " + name + " for Description object");
      }
    }
    in.endObject();

    if (memo == null || note == null) {
      throw new JsonParseException("No memo or note");
    }

    return new Description(note, timestamp, memo);
  }
}
