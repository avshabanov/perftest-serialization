package com.alexshabanov.perftest.serialization.model.gson;

import com.alexshabanov.perftest.serialization.model.Animal;
import com.alexshabanov.perftest.serialization.model.Description;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public final class AnimalGsonDeserializer implements JsonDeserializer<Animal> {

  @Override
  public Animal deserialize(JsonElement e, Type t, JsonDeserializationContext c) throws JsonParseException {
    if (!e.isJsonObject()) {
      throw new JsonParseException("Unable to deserialize ~.Animal type from json=" + e);
    }

    final JsonObject jo = e.getAsJsonObject();
    if (!jo.has("id") || !jo.has("name") || !jo.has("description") || !jo.has("type")) {
      throw new JsonParseException("One of the mandatory fields of ~.Animal is " +
          "ommitted in " + jo);
    }

    final Description description = c.deserialize(jo.get("description"), Description.class);
    return new Animal(jo.get("id").getAsLong(), jo.get("name").getAsString(),
        description, jo.get("type").getAsInt());
  }
}
