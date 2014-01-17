package com.alexshabanov.perftest.serialization.model.gson;

import com.alexshabanov.perftest.serialization.model.Description;
import com.alexshabanov.perftest.serialization.model.Memo;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public final class DescriptionGsonDeserializer implements JsonDeserializer<Description> {
  @Override
  public Description deserialize(JsonElement e, Type t, JsonDeserializationContext c) throws JsonParseException {
    if (!e.isJsonObject()) {
      throw new JsonParseException("Unable to deserialize ~.Description type from json=" + e);
    }

    final JsonObject jo = e.getAsJsonObject();
    if (!jo.has("note") || !jo.has("timestamp")) {
      throw new JsonParseException("One of the mandatory fields of ~.Description is " +
          "ommitted in " + jo);
    }

    final Memo memo = c.deserialize(jo.get("memo"), Memo.class);
    return new Description(jo.get("note").getAsString(), jo.get("timestamp").getAsLong(), memo);
  }
}
