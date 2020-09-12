package com.example.redis_loader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class RedisLoader {
    protected Reader reader;
    protected CSVReader csvReader;
    protected CSVFile csvFile;

    public RedisLoader(String csvFilePath) throws URISyntaxException, IOException {
        this.reader = Files.newBufferedReader(Paths.get(csvFilePath));
        this.csvReader = new CSVReader(this.reader);
        this.csvFile = new CSVFile(this.csvReader);
    }

    public void execute(Jedis jedis) throws IOException, CsvValidationException {
        this.insert(jedis);
        this.closeReaders();
    }

    abstract void insert(Jedis jedis) throws IOException, CsvValidationException;

    protected void closeReaders() throws IOException {
        reader.close();
        csvReader.close();
    }
}
