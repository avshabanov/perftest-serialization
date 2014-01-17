package com.alexshabanov.perftest.serialization;

import com.alexshabanov.perftest.serialization.model.Description;
import com.alexshabanov.perftest.serialization.model.Memo;
import com.alexshabanov.perftest.serialization.model.gson.AnimalGsonDeserializer;
import com.alexshabanov.perftest.serialization.model.gson.DescriptionGsonDeserializer;
import com.alexshabanov.perftest.serialization.model.gson.MemoGsonDeserializer;
import com.alexshabanov.perftest.serialization.model.gson.ZooGsonDeserializer;

import com.alexshabanov.perftest.serialization.model.Animal;
import com.alexshabanov.perftest.serialization.model.Zoo;
import com.alexshabanov.perftest.serialization.model.gson2.AnimalTypeAdapter;
import com.alexshabanov.perftest.serialization.model.gson2.DescriptionTypeAdapter;
import com.alexshabanov.perftest.serialization.model.gson2.MemoTypeAdapter;
import com.alexshabanov.perftest.serialization.model.gson2.ZooTypeAdapter;
import com.alexshabanov.perftest.serialization.model.protobuf.Pb;
import com.alexshabanov.perftest.serialization.model.protobuf.PbAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entry point.
 */
public final class CliApp {
  private static final String CHARSET = "UTF-8";

  // gson
  private final Gson gson;
  private final Gson gson2;

  // jackson
  private final ObjectWriter jsonWriter;
  private final ObjectReader jsonReader;

  private int estimatedRawSize;

  private Zoo zoo;

  public CliApp(int fixtureSize) {
    zoo = createTestFixture(fixtureSize);
    estimatedRawSize = fixtureSize * 2000;

    final GsonBuilder gsonBuilder =  new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Animal.class, new AnimalGsonDeserializer());
    gsonBuilder.registerTypeAdapter(Description.class, new DescriptionGsonDeserializer());
    gsonBuilder.registerTypeAdapter(Memo.class, new MemoGsonDeserializer());
    gsonBuilder.registerTypeAdapter(Zoo.class, new ZooGsonDeserializer());
    gson = gsonBuilder.create();

    final GsonBuilder gsonBuilder2 = new GsonBuilder();
    gsonBuilder2.registerTypeAdapter(Animal.class, new AnimalTypeAdapter());
    gsonBuilder2.registerTypeAdapter(Description.class, new DescriptionTypeAdapter());
    gsonBuilder2.registerTypeAdapter(Memo.class, new MemoTypeAdapter());
    gsonBuilder2.registerTypeAdapter(Zoo.class, new ZooTypeAdapter());
    gson2 = gsonBuilder2.create();

    final ObjectMapper mapper = new ObjectMapper();
    jsonWriter = mapper.writerWithType(Zoo.class);
    jsonReader = mapper.reader(Zoo.class);
  }

  private static void usage() {
    System.out.println("Usage: console-runner -k [GSON|JACKSON|PROTOBUF] -s [Number: Fixture Size]");
  }

  private static void errorAndUsage(String str) {
    System.err.println("Error: " + str);
    usage();
  }

  public static void main(String[] args) {
    if (System.currentTimeMillis() != 1) {
      // cheat compiler - test args for debug
      args = new String[] { "-k", "PROTOBUF", "-s", "10000" };
    }

    SerializerKind serializerKind = null;
    int fixtureSize = -1;
    for (int i = 0; i < args.length;++i) {
      if ("-k".equals(args[i])) {
        ++i;
        if (i >= args.length) {
          errorAndUsage("-k switch expects an argument");
          return;
        }
        serializerKind = SerializerKind.valueOf(args[i]);
      } else if ("-s".equals(args[i])) {
        ++i;
        if (i >= args.length) {
          errorAndUsage("-s switch expects a numeric argument");
          return;
        }
        fixtureSize = Integer.parseInt(args[i]);
      } else {
        errorAndUsage("Unknown switch: " + args[i]);
      }
    }

    if (serializerKind == null || fixtureSize < 0) {
      errorAndUsage("At least one of the mandatory parameters is missing");
    }

    // Do the actual work
    final CliApp app = new CliApp(fixtureSize);

    String metricsMessage;
    try {
      final Metrics metrics = app.runTest(serializerKind);
      metricsMessage = "Fixture Size: " + fixtureSize + ", " +
          "Serialization Time: " + metrics.serializationTime + " msec, " +
          "Deserialization Time: " + metrics.deserializationTime + " msec";
    } catch (Exception e) {
      metricsMessage = "Unable to fetch time delta";
      System.err.println(metricsMessage + "\nError:\n" + e);
    }

    System.out.println("Serializer: " + serializerKind);
    System.out.println(metricsMessage);
  }

  private Metrics runTest(SerializerKind kind) throws IOException {
    switch (kind) {
      case GSON:
        return runGsonTest();

      case GSON2:
        return runGson2Test();

      case JACKSON:
        return runJacksonTest();

      case PROTOBUF:
        return runProtobufTest();

      default:
        throw new UnsupportedOperationException("Unsupported metric: " + kind);
    }
  }

  private Metrics runGsonTest() throws IOException {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream(estimatedRawSize);
    final OutputStreamWriter writer = new OutputStreamWriter(bos, CHARSET);

    long start = System.currentTimeMillis();
    gson.toJson(zoo, writer);
    final long serializationTime = System.currentTimeMillis() - start;
    writer.close();

    final ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    final InputStreamReader reader = new InputStreamReader(bis);

    start = System.currentTimeMillis();
    final Zoo other = gson.fromJson(reader, Zoo.class);
    final long deserializationTime = System.currentTimeMillis() - start;
    reader.close();

    assert zoo.equals(other);

    return new Metrics(serializationTime, deserializationTime);
  }

  private Metrics runGson2Test() throws IOException {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream(estimatedRawSize);
    final OutputStreamWriter writer = new OutputStreamWriter(bos, CHARSET);

    long start = System.currentTimeMillis();
    gson2.toJson(zoo, writer);
    final long serializationTime = System.currentTimeMillis() - start;
    writer.close();

    final ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    final InputStreamReader reader = new InputStreamReader(bis);

    start = System.currentTimeMillis();
    final Zoo other = gson2.fromJson(reader, Zoo.class);
    final long deserializationTime = System.currentTimeMillis() - start;
    reader.close();

    assert zoo.equals(other);

    return new Metrics(serializationTime, deserializationTime);
  }

  private Metrics runJacksonTest() throws IOException {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream(estimatedRawSize);
    final OutputStreamWriter writer = new OutputStreamWriter(bos, CHARSET);

    long start = System.currentTimeMillis();
    jsonWriter.writeValue(writer, zoo);
    final long serializationTime = System.currentTimeMillis() - start;
    writer.close();

    final ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

    start = System.currentTimeMillis();
    final Zoo other = jsonReader.readValue(bis);
    final long deserializationTime = System.currentTimeMillis() - start;
    bis.close();

    assert zoo.equals(other);

    return new Metrics(serializationTime, deserializationTime);
  }

  private Metrics runProtobufTest() throws IOException {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream(estimatedRawSize);

    long start = System.currentTimeMillis();
    PbAdapter.convert(zoo).writeTo(bos);
    final long serializationTime = System.currentTimeMillis() - start;
    bos.close();

    final ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

    start = System.currentTimeMillis();
    final Zoo other = PbAdapter.convert(Pb.Zoo.parseFrom(bis));
    final long deserializationTime = System.currentTimeMillis() - start;
    bis.close();

    assert zoo.equals(other);

    return new Metrics(serializationTime, deserializationTime);
  }



  private static Zoo createTestFixture(int count) {
    final List<Animal> animals = new ArrayList<Animal>(count);
    for (int i = 0; i < count; ++i) {
      final Memo memo = new Memo(Arrays.asList("noteline#1," + i,
          "noteline#2," + i, "noteline#3," + i), i);
      final Description description = new Description("note" + i, 1000000000L + i, memo);
      final Animal animal = new Animal(900000000L + i, "animal" + i, description, i % 5);
      animals.add(animal);
    }

    return new Zoo("test", animals);
  }

  private static final class Metrics {
    public final long serializationTime;
    public final long deserializationTime;

    private Metrics(long serializationTime, long deserializationTime) {
      this.serializationTime = serializationTime;
      this.deserializationTime = deserializationTime;
    }
  }
}
