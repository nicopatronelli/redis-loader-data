package com.example.redis_loader;

import com.opencsv.exceptions.CsvValidationException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.function.BiConsumer;

public class ZADDLoader extends RedisLoader {
    String sortedSetKey;

    public ZADDLoader(String csvFilePath, String keySortedSet) throws IOException, URISyntaxException {
        super(csvFilePath);
        this.sortedSetKey = keySortedSet;
    }

    @Override
    public void insert(Jedis jedis) {

//        Iterator<String[]> lines = csvReader.iterator();
//        String[] line;
//        while (lines.hasNext()) {
//            line = lines.next();
//            for (int i = 0; i < line.length; i++) { // TODO: and the index i is for...??
//                jedis.zadd(sortedSetKey, Double.parseDouble(line[0]), line[1]);
//            }
//        }
        BiConsumer<Integer, CSVLine> actionPerLine = (index, line) -> {
            jedis.zadd(this.sortedSetKey, Double.parseDouble(line.valueAt(0)), line.valueAt(1)); // insert
        };

        BiConsumer<Integer, CSVLine> actionPerValue = (index, line) -> {};

        this.csvFile.forEachLine(actionPerLine, actionPerValue);
    }
}
