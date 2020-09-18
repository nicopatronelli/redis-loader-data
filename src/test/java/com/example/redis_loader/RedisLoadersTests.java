package com.example.redis_loader;

import com.opencsv.exceptions.CsvValidationException;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public class RedisLoadersTests {

    static Jedis jedis;
    String[] args;
    RedisLoader redisLoader;

    @BeforeAll
    static void setUp(){
        jedis = new Jedis("localhost");
        int databaseNumber = 1;
        jedis.select(databaseNumber); // I am using database number 1 of 16 to testing purposes
        jedis.flushDB();
    }

    @AfterAll
    static void tierDown(){
        jedis.flushDB();
        jedis.close();
    }

    void persistData(String operation, String csvFilePath) throws IOException, CsvValidationException {
        args = new String[]{operation, csvFilePath};
        redisLoader = RedisLoaderFactory.getRedisLoader(args);
        redisLoader.execute(jedis);
    }

    @Test @Disabled
    void mockTest() throws IOException, CsvValidationException {
        // Mocking
        Jedis jedisMock = mock(Jedis.class);
        when(jedisMock.lpush(anyString(), anyString())).thenReturn(5L);
        when(jedisMock.lpop("electric_pokemons")).thenReturn("electrode");

        // persistData
        args = new String[]{"LPUSH", "./src/test/java/com/example/redis_loader/data/data_lpush.csv"};
        redisLoader = RedisLoaderFactory.getRedisLoader(args);
        redisLoader.execute(jedisMock);

        // testing
        String shouldBeElectrode = jedisMock.lpop("electric_pokemons");
        assertThat(shouldBeElectrode).isEqualTo("electrode");
    }

    @Test
    void SetStringTest() throws IOException, CsvValidationException {
        persistData("SET", "./src/test/java/com/example/redis_loader/data/data_set_string.csv");

        String shoulBePikachu = jedis.get("pokemons:25");
        assertThat(shoulBePikachu).isEqualTo("pikachu");

        String shoulBeCharizard = jedis.get("pokemons:6");
        assertThat(shoulBeCharizard).isEqualTo("charizard");
    }

    @Test
    void LPUSHLoaderTest() throws IOException, URISyntaxException, CsvValidationException {
        persistData("LPUSH", "./src/test/java/com/example/redis_loader/data/data_lpush.csv");
        List<String> electricPokemons = jedis.lrange("electric_pokemons", 0L, -1L);
        // containsExactly checks for elements and ORDER -> This is what we want!
        assertThat(electricPokemons).containsExactly(
                "electrode",
                "mareep",
                "jolteon",
                "raichu",
                "electabuzz"
        );

        String shouldBeElectrode = jedis.lpop("electric_pokemons");
        assertThat(shouldBeElectrode).isEqualTo("electrode");

        String shoulBeElectabuzz = jedis.rpop("electric_pokemons");
        assertThat(shoulBeElectabuzz).isEqualTo("electabuzz");
    }

    @Test
    void HMSETLoaderTest() throws IOException, URISyntaxException, CsvValidationException {
        persistData("HMSET", "./src/test/java/com/example/redis_loader/data/data_hmset.csv");

        String pikachuName = jedis.hmget("pokemon:25", "name").get(0);
        assertThat(pikachuName).isEqualTo("pikachu");

        String charmeleonEvolution = jedis.hmget("pokemon:5", "evolution").get(0);
        assertThat(charmeleonEvolution).isEqualTo("charizard");
    }

    @Test
    void SADDLoaderTest() throws IOException, URISyntaxException, CsvValidationException {
        persistData("SADD", "./src/test/java/com/example/redis_loader/data/data_sadd.csv");

        Set<String> pokemonsFire = jedis.smembers("pokemons:fire");
        assertThat(pokemonsFire).containsOnly("charmander", "charmeleon", "charizard");

        Set<String> pokemonsElectric = jedis.smembers("pokemons:electric");
        assertThat(pokemonsElectric).containsOnly("pikachu", "raichu", "electabuzz");

        boolean pokemonsWaterIncludesWartortle = jedis.sismember("pokemons:water", "wartortle");
        assertThat(pokemonsWaterIncludesWartortle).isTrue();

        jedis.sinterstore("pokemons:fire_and_dragon", "pokemons:fire", "pokemons:dragon");
        Set<String> pokemonsFireAndDragon = jedis.smembers("pokemons:fire_and_dragon");
        assertThat(pokemonsFireAndDragon).containsOnly("charizard");

    }

    @Test
    void ZADDLoaderTest() throws IOException, URISyntaxException, CsvValidationException {
        persistData("ZADD", "./src/test/java/com/example/redis_loader/data/data_zadd.csv");

        Long charmanderRank = jedis.zrevrank("fire_pokemons", "charmander");
        assertThat(charmanderRank).isEqualTo(2L);

        Set<String> result = jedis.zrangeByScore("fire_pokemons", 8, 10);
        assertThat(result).containsOnly("charmeleon", "charizard");
    }

    @Test
    void PFADDLoaderTest() throws IOException, URISyntaxException, CsvValidationException {
        persistData("PFADD", "./src/test/java/com/example/redis_loader/data/data_pfadd.csv");

        Long differentElements = jedis.pfcount("pokemons");
        assertThat(differentElements).isEqualTo(5);
    }
}
