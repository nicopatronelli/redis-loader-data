package com.example.redis_loader.loaders;

import com.example.redis_loader.RedisLoader;
import com.example.redis_loader.csv.ActionPerLine;
import redis.clients.jedis.Jedis;
import java.io.IOException;

public class ZADDLoader extends RedisLoader {
    String sortedSetKey;

    public ZADDLoader(String csvFilePath) throws IOException {
        super(csvFilePath);
        this.sortedSetKey = this.csvFile.headerValueAtFirstColumn();
    }

    @Override
    public void insert(Jedis jedis) {
        ActionPerLine actionPerLine = line -> {
            jedis.zadd(this.sortedSetKey, Double.parseDouble(line.valueAtFirstColumn()), line.valueAtSecondColumn()); // insert
        };

        this.csvFile
                .forEachLine(actionPerLine)
                .execute();
    }
}
