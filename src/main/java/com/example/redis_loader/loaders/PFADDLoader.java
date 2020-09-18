package com.example.redis_loader.loaders;

import com.example.redis_loader.RedisLoader;
import com.example.redis_loader.csv.ActionPerLine;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class PFADDLoader extends RedisLoader {

    public PFADDLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
    }

    @Override
    public void insert(Jedis jedis) {
        ActionPerLine actionPerLine = line -> {
            jedis.pfadd(this.csvFile.headerValueAtFirstColumn(), line.valueAtFirstColumn()); // insert
        };

        this.csvFile
                .forEachLine(actionPerLine)
                .execute();
    }
}
