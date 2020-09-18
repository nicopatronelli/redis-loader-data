package com.example.redis_loader.loaders;

import com.example.redis_loader.RedisLoader;
import com.example.redis_loader.csv.ActionPerLine;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class LPUSHLoader extends RedisLoader {
    private String key;

    public LPUSHLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
        this.key = this.csvFile.headerColumnAt(0);
    }

    @Override
    public void insert(Jedis jedis) {
        ActionPerLine actionPerLine = line -> {
            jedis.lpush(this.key, line.valueAtFirstColumn()); // insert
        };

        this.csvFile
                .forEachLine(actionPerLine)
                .execute();
    }
}
