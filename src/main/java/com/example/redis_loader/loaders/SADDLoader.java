package com.example.redis_loader.loaders;

import com.example.redis_loader.RedisLoader;
import com.example.redis_loader.csv.ActionPerCell;
import com.example.redis_loader.csv.ActionPerLine;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SADDLoader extends RedisLoader {

    public SADDLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
    }

    @Override
    public void insert(Jedis jedis) {
        Set<String> values = new HashSet<>();

        ActionPerCell actionPerValue = (index, line) ->
            values.add(line.valueAt(index));

        ActionPerLine actionPerLine = line -> {
            jedis.sadd(
                    line.valueAtFirstColumn(),
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
