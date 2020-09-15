package com.example.redis_loader;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.function.BiConsumer;

public class ZADDLoader extends RedisLoader {
    String sortedSetKey;

    public ZADDLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
        this.sortedSetKey = this.csvFile.headerColumnAt(0);
    }

    @Override
    public void insert(Jedis jedis) {
        BiConsumer<Integer, CSVLine> actionPerLine = (index, line) -> {
            jedis.zadd(this.sortedSetKey, Double.parseDouble(line.valueAt(0)), line.valueAt(1)); // insert
        };

        this.csvFile
                .forEachLine(actionPerLine)
                .execute();
    }
}
