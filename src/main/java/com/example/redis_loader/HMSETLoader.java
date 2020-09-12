package com.example.redis_loader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HMSETLoader extends RedisLoader {

    public HMSETLoader(String csvFile) throws URISyntaxException, IOException {
        super(csvFile);
    }

    @Override
    public void insert(Jedis jedis) throws IOException, CsvValidationException {
        Iterator<String[]> lines = this.csvReader.iterator();
        String[] headers = lines.next();
        String[] line;
        Map<String, String> values = new HashMap<>();

        while (lines.hasNext()) {
            line = lines.next();
            for (int i = 0; i < line.length; i++) {
                values.put(headers[i], line[i]);
            }
            jedis.hmset(line[0], values); // insert
            values.clear();
        }
    }
}
