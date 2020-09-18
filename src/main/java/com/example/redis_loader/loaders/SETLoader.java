package com.example.redis_loader.loaders;

import com.example.redis_loader.RedisLoader;
import com.example.redis_loader.csv.ActionPerLine;
import com.example.redis_loader.csv.CSVLine;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SETLoader extends RedisLoader {

    public SETLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
    }

    @Override
    public void insert(Jedis jedis) {
        ActionPerLine actionPerLine = line -> {
            jedis.set(line.valueAtFirstColumn(), line.valueAtSecondColumn()); // insert
        };

        this.csvFile
                .forEachLine(actionPerLine)
                .execute();
    }
}
