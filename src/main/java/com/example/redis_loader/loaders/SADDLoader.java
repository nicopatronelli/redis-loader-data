package com.example.redis_loader;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class SADDLoader extends RedisLoader {

    public SADDLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
    }

    @Override
    public void insert(Jedis jedis) {
        Set<String> values = new HashSet<>();

        BiConsumer<Integer, CSVLine> actionPerValue = (index, line) ->
            values.add(line.valueAt(index));

        BiConsumer<Integer, CSVLine> actionPerLine = (index, line) -> {
            jedis.sadd(
                    line.valueAt(0),
                    values.stream().toArray(String[]::new)
            );
            values.clear();
        };

        this.csvFile
                .forEachLine(actionPerLine)
                .andForEachValue(actionPerValue)
                .execute();
    }
}
