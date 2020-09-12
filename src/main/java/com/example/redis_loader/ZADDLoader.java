package com.example.redis_loader;

import com.opencsv.exceptions.CsvValidationException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

public class ZADDLoader extends RedisLoader {
    String sortedSetKey;

    public ZADDLoader(String csvFile, String keySortedSet) throws IOException, URISyntaxException {
        super(csvFile);
        this.sortedSetKey = keySortedSet;
    }

    @Override
    public void insert(Jedis jedis) throws IOException, CsvValidationException {
        Iterator<String[]> lines = csvReader.iterator();
        String[] line;
        while (lines.hasNext()) {
            line = lines.next();
            for (int i = 0; i < line.length; i++) {
                jedis.zadd(sortedSetKey, Double.parseDouble(line[0]), line[1]);
            }
        }
    }
}
