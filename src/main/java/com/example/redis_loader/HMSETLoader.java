package com.example.redis_loader;

import com.opencsv.exceptions.CsvValidationException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class HMSETLoader extends RedisLoader {

    public HMSETLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
    }

    @Override
    public void insert(Jedis jedis) {
        Map<String, String> values = new HashMap<>();

        BiConsumer<Integer, CSVLine> actionPerLine = (index, line) -> {
            jedis.hmset(line.valueAt(index), values); // insert
            values.clear();
        };

        BiConsumer<Integer, CSVLine> actionPerValue = (index, line) -> values.put(
                csvFile.headerColumnAt(index),
                line.valueAt(index)
        );

        this.csvFile
                .forEachLine(actionPerLine)
                .andForEachValueDo(actionPerValue)
                .execute();
    }
}
