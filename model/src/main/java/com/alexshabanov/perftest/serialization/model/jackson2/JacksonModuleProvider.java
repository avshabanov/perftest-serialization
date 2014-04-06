package com.alexshabanov.perftest.serialization.model.jackson2;

import com.alexshabanov.perftest.serialization.model.Memo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Shabanov
 */
public class JacksonModuleProvider {
  public void bindTo(ObjectMapper mapper) {
    final SimpleModule module = new SimpleModule("JacksonModuleProvider");
    module
        // Memo class
        .addSerializer(Memo.class, new MemoSerializer())
        .addDeserializer(Memo.class, new MemoDeserializer());
    mapper.registerModule(module);
  }

  //
  // Private
  //

  private static final class MemoSerializer extends JsonSerializer<Memo> {

    @Override
    public void serialize(Memo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      serializeMemo(value, gen);
    }
  }

  private static final class MemoDeserializer extends JsonDeserializer<Memo> {

    @Override
    public Memo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
        throw ctxt.mappingException(Memo.class);
      }

      // fields data
      int count = -1;
      List<String> lines = new ArrayList<String>();

      // reader
      for (;;) {
        // read field name or end-of-object
        final JsonToken token = jp.nextToken();
        if (token == JsonToken.END_OBJECT) {
          break;
        } else if (token != JsonToken.FIELD_NAME) {
          throw ctxt.mappingException("Field name expected");
        }
        final String fieldName = jp.getCurrentName();

        // read field value
        jp.nextToken();
        if ("count".equals(fieldName)) {
          count = jp.getIntValue();
        } else if ("lines".equals(fieldName)) {
          lines = jp.readValueAs(new TypeReference<List<String>>() {});
        } else {
          ctxt.handleUnknownProperty(jp, this, Memo.class, fieldName);
        }
      }

      // null adaptation?
      lines = lines != null ? lines : Collections.<String>emptyList();

      return new Memo(lines, count);
    }
  }

  private static void serializeMemo(Memo value, JsonGenerator gen) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("count", value.getCount());
    gen.writeArrayFieldStart("lines");
    for (final String line : value.getLines()) {
      gen.writeString(line);
    }
    gen.writeEndArray();
    gen.writeEndObject();
  }
}
