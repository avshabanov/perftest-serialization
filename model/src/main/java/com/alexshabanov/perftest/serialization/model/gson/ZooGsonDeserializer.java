package com.alexshabanov.perftest.serialization.model.gson;

import com.alexshabanov.perftest.serialization.model.Animal;
import com.alexshabanov.perftest.serialization.model.Zoo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class ZooGsonDeserializer implements JsonDeserializer<Zoo> {
  @Override
  public Zoo deserialize(JsonElement e, Type t, JsonDeserializationContext c) throws JsonParseException {
    if (!e.isJsonObject()) {
      throw new JsonParseException("Unable to deserialize ~.Zoo type from json=" + e);
    }

    final JsonObject jo = e.getAsJsonObject();
    if (!jo.has("name") || !jo.has("animals")) {
      throw new JsonParseException("One of the mandatory fields of ~.Zoo is " +
          "ommitted in " + jo);
    }

    final JsonArray jsonAnimals = jo.get("animals").getAsJsonArray();
    final List<Animal> animals = new ArrayList<Animal>(jsonAnimals.size());
    for (final JsonElement jae : jsonAnimals) {
      final Animal a = c.deserialize(jae, Animal.class);
      animals.add(a);
    }

    return new Zoo(jo.get("name").getAsString(), animals);
  }
}
