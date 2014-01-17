package com.alexshabanov.perftest.serialization.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class Memo {
  private final List<String> lines;
  private final int count;

  public Memo(@JsonProperty("lines") List<String> lines, @JsonProperty("count") int count) {
    if (lines == null) {
      throw new IllegalArgumentException("lines");
    }
    this.lines = lines;
    this.count = count;
  }

  public List<String> getLines() {
    return lines;
  }

  public int getCount() {
    return count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Memo memo = (Memo) o;

    if (count != memo.count) return false;
    if (!lines.equals(memo.lines)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = lines.hashCode();
    result = 31 * result + count;
    return result;
  }

  @Override
  public String toString() {
    return "Memo{" +
        "lines=" + lines +
        ", count=" + count +
        '}';
  }
}
