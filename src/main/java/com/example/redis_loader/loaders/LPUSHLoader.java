package com.example.redis_loader;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class LPUSHLoader extends RedisLoader {
    String key;

    public LPUSHLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
        this.key = this.csvFile.headerColumnAt(0);
    }

    @Override
    public void insert(Jedis jedis) {

        BiConsumer<Integer, CSVLine> actionPerLine = (index, line) -> {
            jedis.lpush(this.key, line.valueAt(0)); // insert
        };

        this.csvFile
                .forEachLine(actionPerLine)
                .execute();
    }
}
