package com.alexshabanov.perftest.serialization.model.gson;

import com.alexshabanov.perftest.serialization.model.Memo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class MemoGsonDeserializer implements JsonDeserializer<Memo> {
  @Override
  public Memo deserialize(JsonElement e, Type t, JsonDeserializationContext c) throws JsonParseException {
    if (!e.isJsonObject()) {
      throw new JsonParseException("Unable to deserialize ~.Description type from json=" + e);
    }

    final JsonObject jo = e.getAsJsonObject();
    if (!jo.has("lines") || !jo.has("count")) {
      throw new JsonParseException("One of the mandatory fields of ~.Description is " +
          "ommitted in " + jo);
    }

    final JsonArray jsonLines = jo.get("lines").getAsJsonArray();
    final List<String> lines = new ArrayList<String>(jsonLines.size());
    for (final JsonElement jl : jsonLines) {
      lines.add(jl.getAsString());
    }

    return new Memo(lines, jo.get("count").getAsInt());
  }
}
