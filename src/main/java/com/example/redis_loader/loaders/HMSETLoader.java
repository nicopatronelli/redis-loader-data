package com.example.redis_loader.loaders;

import com.example.redis_loader.RedisLoader;
import com.example.redis_loader.csv.ActionPerCell;
import com.example.redis_loader.csv.ActionPerLine;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HMSETLoader extends RedisLoader {

    public HMSETLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
    }

    @Override
    public void insert(Jedis jedis) {
        Map<String, String> values = new HashMap<>();

        ActionPerLine actionPerLine = line -> {
            jedis.hmset(line.valueAtFirstColumn(), values); // insert
            values.clear();
        };

        ActionPerCell actionPerCell = (index, line) -> values.put(
            csvFile.headerColumnAt(index),
            line.valueAt(index)
        );

        this.csvFile
                .forEachLine(actionPerLine)
                .andForEachValue(actionPerCell)
                .execute();
    }
}
