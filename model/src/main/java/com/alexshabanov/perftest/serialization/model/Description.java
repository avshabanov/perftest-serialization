package com.alexshabanov.perftest.serialization.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Description {
    private final String note;
    private final long timestamp;
    private final Memo memo;

    public Description(@JsonProperty("note") String note,
                       @JsonProperty("timestamp") long timestamp,
                       @JsonProperty("memo") Memo memo) {
        this.note = note;
        this.timestamp = timestamp;
        this.memo = memo;
    }

    public String getNote() {
        return note;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Memo getMemo() {
        return memo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Description that = (Description) o;

        if (timestamp != that.timestamp) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = note != null ? note.hashCode() : 0;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Description{" +
                "note='" + note + '\'' +
                ", timestamp=" + timestamp +
                ", memo=" + memo +
                '}';
    }
}
