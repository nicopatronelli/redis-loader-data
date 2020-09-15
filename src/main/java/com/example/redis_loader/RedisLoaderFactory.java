package com.example.redis_loader;

import java.io.IOException;

public class RedisLoaderFactory {
    public static RedisLoader getRedisLoader(String[] args) throws IOException {
        String operation = args[0]; // example: HMSET
        String csvFilePath = args[1]; // DATASET
        switch(operation) {
            case "LPUSH":
                return new LPUSHLoader(csvFilePath);
            case "HMSET":
                return new HMSETLoader(csvFilePath);
            case "SADD":
                return new SADDLoader(csvFilePath);
            case "ZADD":
                return new ZADDLoader(csvFilePath);
            case "PFADD":
                return new PFADDLoader(csvFilePath);
            default:
                return null; // TODO
        }
    }
}