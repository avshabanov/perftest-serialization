Serialization Benchmark
=======================

## Libraries benchmarked

* Google GSON
* Jackson
* Google Protobuf

## Cold Start Measurements

Machine: Mac Book Pro, i5, Mac OS X 10.8
Java: Oracle JRE 1.7.0_40

```
Serializer: GSON
Fixture Size: 10000, Serialization Time: 413 msec, Deserialization Time: 1351 msec

Serializer: GSON2 (no reflection, hand-written serializers/deserializers)
Fixture Size: 10000, Serialization Time: 257 msec, Deserialization Time: 182 msec

Serializer: JACKSON
Fixture Size: 10000, Serialization Time: 262 msec, Deserialization Time: 310 msec

Serializer: PROTOBUF
Fixture Size: 10000, Serialization Time: 510 msec, Deserialization Time: 176 msec
```

## Multiple Runs

Machine: Mac Book Pro MC373, i7, Mac OS X 10.9
Java: Oracle JRE 1.7.0_45

Fixture Size = 10000
```
Serializer: GSON
Run #0, Fixture Size: 10000, Serialization Time: 474 msec, Deserialization Time: 870 msec
Run #1, Fixture Size: 10000, Serialization Time: 69 msec, Deserialization Time: 170 msec
Run #2, Fixture Size: 10000, Serialization Time: 86 msec, Deserialization Time: 180 msec
Run #3, Fixture Size: 10000, Serialization Time: 56 msec, Deserialization Time: 149 msec
Run #4, Fixture Size: 10000, Serialization Time: 64 msec, Deserialization Time: 130 msec

Serializer: GSON2
Run #0, Fixture Size: 10000, Serialization Time: 249 msec, Deserialization Time: 201 msec
Run #1, Fixture Size: 10000, Serialization Time: 71 msec, Deserialization Time: 31 msec
Run #2, Fixture Size: 10000, Serialization Time: 74 msec, Deserialization Time: 33 msec
Run #3, Fixture Size: 10000, Serialization Time: 57 msec, Deserialization Time: 37 msec
Run #4, Fixture Size: 10000, Serialization Time: 128 msec, Deserialization Time: 27 msec

Serializer: JACKSON
Run #0, Fixture Size: 10000, Serialization Time: 217 msec, Deserialization Time: 281 msec
Run #1, Fixture Size: 10000, Serialization Time: 59 msec, Deserialization Time: 44 msec
Run #2, Fixture Size: 10000, Serialization Time: 14 msec, Deserialization Time: 22 msec
Run #3, Fixture Size: 10000, Serialization Time: 15 msec, Deserialization Time: 23 msec
Run #4, Fixture Size: 10000, Serialization Time: 15 msec, Deserialization Time: 17 msec

Serializer: PROTOBUF
Run #0, Fixture Size: 10000, Serialization Time: 389 msec, Deserialization Time: 383 msec
Run #1, Fixture Size: 10000, Serialization Time: 27 msec, Deserialization Time: 18 msec
Run #2, Fixture Size: 10000, Serialization Time: 41 msec, Deserialization Time: 10 msec
Run #3, Fixture Size: 10000, Serialization Time: 14 msec, Deserialization Time: 9 msec
Run #4, Fixture Size: 10000, Serialization Time: 25 msec, Deserialization Time: 38 msec
```

Fixture Size = 100000
```
Serializer: GSON
Run #0, Fixture Size: 100000, Serialization Time: 957 msec, Deserialization Time: 5501 msec
Run #1, Fixture Size: 100000, Serialization Time: 1223 msec, Deserialization Time: 3311 msec
Run #2, Fixture Size: 100000, Serialization Time: 809 msec, Deserialization Time: 4466 msec
Run #3, Fixture Size: 100000, Serialization Time: 811 msec, Deserialization Time: 4917 msec
Run #4, Fixture Size: 100000, Serialization Time: 963 msec, Deserialization Time: 5486 msec

Serializer: GSON2
Run #0, Fixture Size: 100000, Serialization Time: 1316 msec, Deserialization Time: 727 msec
Run #1, Fixture Size: 100000, Serialization Time: 569 msec, Deserialization Time: 575 msec
Run #2, Fixture Size: 100000, Serialization Time: 534 msec, Deserialization Time: 205 msec
Run #3, Fixture Size: 100000, Serialization Time: 870 msec, Deserialization Time: 277 msec
Run #4, Fixture Size: 100000, Serialization Time: 448 msec, Deserialization Time: 226 msec

Serializer: JACKSON
Run #0, Fixture Size: 100000, Serialization Time: 359 msec, Deserialization Time: 469 msec
Run #1, Fixture Size: 100000, Serialization Time: 164 msec, Deserialization Time: 189 msec
Run #2, Fixture Size: 100000, Serialization Time: 149 msec, Deserialization Time: 168 msec
Run #3, Fixture Size: 100000, Serialization Time: 124 msec, Deserialization Time: 166 msec
Run #4, Fixture Size: 100000, Serialization Time: 141 msec, Deserialization Time: 160 msec

Serializer: PROTOBUF
Run #0, Fixture Size: 100000, Serialization Time: 684 msec, Deserialization Time: 316 msec
Run #1, Fixture Size: 100000, Serialization Time: 173 msec, Deserialization Time: 119 msec
Run #2, Fixture Size: 100000, Serialization Time: 173 msec, Deserialization Time: 848 msec
Run #3, Fixture Size: 100000, Serialization Time: 142 msec, Deserialization Time: 139 msec
Run #4, Fixture Size: 100000, Serialization Time: 667 msec, Deserialization Time: 121 msec
```

## Conclusions

Given that protocol is convenient enough (e.g. no binary data like large byte arrays):

* Jackson has the best speed/convenience ratio.
* Protobuf is the fastest, but less convenient.
* Straightforward usage of gson can give very good speed, but not at a cold start.
Also gson is not very inconvenient for immutable domain model.
* Gson can be very fast if serialization/deserialization is customized by using low-level streaming API.
