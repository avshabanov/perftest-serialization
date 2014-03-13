package com.alexshabanov.perftest.serialization.model;

import com.alexshabanov.perftest.serialization.model.jackson2.JacksonModuleProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class JacksonModuleTest {

  private ObjectMapper mapper;

  @Before
  public void init() {
    mapper = new ObjectMapper();
    new JacksonModuleProvider().bindTo(mapper);
  }

  @Test
  public void shouldMarshalMemo() throws IOException {
    final Memo memo = new Memo(Arrays.asList("asd", "def"), 123);
    final String str = mapper.writeValueAsString(memo);
    assertTrue(str.contains("count"));
    assertEquals(memo, mapper.readValue(str, Memo.class));
  }
}
