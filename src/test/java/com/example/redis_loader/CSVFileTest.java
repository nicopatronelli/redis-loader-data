package com.example.redis_loader;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class CSVFileTest {
    private String csvFilePath;
    private Reader reader;
    private CSVReader csvReader;

    @BeforeEach
    void setUp() throws IOException {
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        csvFilePath = "./src/test/java/com/example/redis_loader/data_hmset.csv";
        reader = Files.newBufferedReader(Paths.get(csvFilePath));
        csvReader = new CSVReader(reader);
    }

    @Test
    void hmsetTest() {
        Map<String, String> values = new HashMap<>();
        CSVFile csvFile = new CSVFile(csvReader);
        Jedis jedis = new Jedis("localhost");
        csvFile.forEachLine(
                (index, line) -> {
                    jedis.hmset(line.valueAt(index), values); // insert
                    values.clear();
                }
        ).andForEachValue(
                (index, line) -> values.put(
                    csvFile.headerColumnAt(index),
                    line.valueAt(index)
                )
        ).execute();
    }

    @Test
    void zaddTest() {
        CSVFile csvFile = new CSVFile(csvReader);
        Jedis jedis = new Jedis("localhost");
        csvFile.forEachLine(
                (index, line) -> {
                    jedis.zadd("fire_pokemons", Double.parseDouble(line.valueAt(0)), line.valueAt(1)); // insert
                }
        ).execute();
    }

}