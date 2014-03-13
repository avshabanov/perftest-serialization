package com.alexshabanov.perftest.serialization.model.jackson2;

import com.alexshabanov.perftest.serialization.model.Memo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.ArrayList;
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
      expectStartObject(jp, Memo.class);

      // fields data
      int count = -1;
      List<String> lines = new ArrayList<String>();

      // reader
      for (;;) {
        JsonToken token = jp.nextToken();
        if (token == JsonToken.END_OBJECT) {
          break;
        } else if (token != JsonToken.FIELD_NAME) {
          throw new JsonMappingException("Expected field for " + Memo.class, jp.getCurrentLocation());
        }

        final String fieldName = jp.getCurrentName();
        jp.nextToken(); // move to field value

        if ("count".equals(fieldName)) {
          count = expectIntValue(jp, Memo.class, "count");
        } else if ("lines".equals(fieldName)) {
          nextStartArray(jp, Memo.class);
          for (;;) {
            token = jp.nextToken();
            if (token == JsonToken.END_ARRAY) {
              break;
            } else if (token != JsonToken.VALUE_STRING) {
              throw new JsonMappingException("String[] values expected for " + fieldName + " for " + Memo.class, jp.getCurrentLocation());
            }

            lines.add(jp.getText());
          }
        } else {
          throw new JsonMappingException("Unknown field " + fieldName + " for " + Memo.class, jp.getCurrentLocation());
        }
      }

      return new Memo(lines, count);
    }
  }

  private static void serializeMemo(Memo value, JsonGenerator gen) throws IOException {
    gen.writeStartObject();
    gen.writeFieldName("count");
    gen.writeNumber(value.getCount());
    gen.writeFieldName("lines");
    gen.writeStartArray();
    for (final String line : value.getLines()) {
      gen.writeString(line);
    }
    gen.writeEndArray();
    gen.writeEndObject();
  }

  //
  // helpers
  //


  // deserializer helper

  private static void expectStartObject(JsonParser jp, Class<?> type) throws IOException {
    if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
      throw new JsonMappingException("Expected object for " + type, jp.getCurrentLocation());
    }
  }

  private static void nextStartArray(JsonParser jp, Class<?> type) throws IOException {
    if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
      throw new JsonMappingException("Expected array for " + type, jp.getCurrentLocation());
    }
  }

  private static int expectIntValue(JsonParser jp, Class<?> type, String fieldName) throws IOException {
    if (jp.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
      throw new JsonMappingException("Expected int value for field " + fieldName + " in class " + type,
          jp.getCurrentLocation());
    }

    return (Integer) jp.getNumberValue();
  }
}
