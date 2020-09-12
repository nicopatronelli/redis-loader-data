package com.example.redis_loader;

import java.io.IOException;
import java.net.URISyntaxException;

public class RedisLoaderFactory {
    public static RedisLoader getRedisLoader(String[] args) throws IOException, URISyntaxException {
        String operation = args[0]; // HMSET
        String csvFilePath = args[1]; // DATASET
        switch(operation) {
            case "HMSET":
                return new HMSETLoader(csvFilePath);
            case "ZADD":
                String sortedSetKey = args[2];
                return new ZADDLoader(csvFilePath, sortedSetKey);
            default:
                return null; // TODO
        }
    }
}
