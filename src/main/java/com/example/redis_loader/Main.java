package com.example.redis_loader;

import com.opencsv.exceptions.CsvValidationException;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {
        Jedis jedis = new Jedis("localhost");
        RedisLoader redisLoader = RedisLoaderFactory.getRedisLoader(args);
        redisLoader.execute(jedis);
        jedis.close();
    }
}