package com.example.redis_loader;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.function.BiConsumer;

public class PFADDLoader extends RedisLoader {

    public PFADDLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
    }

    @Override
    public void insert(Jedis jedis) {
        BiConsumer<Integer, CSVLine> actionPerLine = (index, line) -> {
            jedis.pfadd(this.csvFile.headerColumnAt(0), line.valueAt(0)); // insert
        };

        this.csvFile
                .forEachLine(actionPerLine)
                .execute();
    }
}
