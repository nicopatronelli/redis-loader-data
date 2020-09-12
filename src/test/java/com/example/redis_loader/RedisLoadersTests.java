package com.example.redis_loader;

import com.opencsv.exceptions.CsvValidationException;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

public class RedisLoadersTests {

    static Jedis jedis;
    String operation;
    String csvFilePath;
    String[] args;
    RedisLoader redisLoader;

    @BeforeAll // TODO: Clean DataBase after each test
    static void setUp(){
        jedis = new Jedis("localhost");
        int databaseNumber = 1;
        jedis.select(databaseNumber); // I am using database number 1 of 16 to testing purposes
    }

    @AfterAll
    static void tierDown(){
        jedis.flushDB();
        jedis.close();
    }

    void persistData(String operation, String csvFilePath, String sortedSetKey) throws IOException, URISyntaxException, CsvValidationException {
        args = new String[]{operation, csvFilePath, sortedSetKey};
        redisLoader = RedisLoaderFactory.getRedisLoader(args);
        redisLoader.execute(jedis);
    }

    @Test
    void HMSETLoaderTest() throws IOException, URISyntaxException, CsvValidationException {
        persistData("HMSET", "./src/test/java/com/example/redis_loader/data_hmset.csv", "");

        String pikachuName = jedis.hmget("pokemon:25", "name").get(0);
        assertThat(pikachuName).isEqualTo("pikachu");

        String charmeleonEvolution = jedis.hmget("pokemon:5", "evolution").get(0);
        assertThat(charmeleonEvolution).isEqualTo("charizard");
    }

    @Test
    void ZADDLoaderTest() throws IOException, URISyntaxException, CsvValidationException {
        persistData("ZADD", "./src/test/java/com/example/redis_loader/data_zadd.csv", "fire_pokemons");

        Long charmanderRank = jedis.zrevrank("fire_pokemons", "charmander");
        assertThat(charmanderRank).isEqualTo(2L);

        Set<String> result = jedis.zrangeByScore("fire_pokemons", 8, 10);
        assertThat(result).containsOnly("charmeleon", "charizard");
    }

}
