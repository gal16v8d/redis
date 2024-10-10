package com.gsdd.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gsdd.docker.config.util.DockerEnvLoader;
import com.gsdd.redis.dto.LocationDto;
import com.gsdd.redis.dto.UserDto;
import java.io.ByteArrayOutputStream;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
public class RedisSample {

  private static final int REDIS_PORT = 6379;
  private static final String KEY = "sample-name";
  private static final String KEY_LIST = "sample-list";

  public static void main(String[] args) {
    jedisPoolConnection();
  }

  private static void jedisPoolConnection() {
    try (JedisPool jedisPool = new JedisPool(DockerEnvLoader.getDockerServiceIp(), REDIS_PORT);
        Jedis jedis = jedisPool.getResource()) {
      log.info("Connection established");
      log.info("Server is running: {}", jedis.ping());
      jedis.set(KEY, "Redis sample");
      log.info("Stored value on Redis:: {}", jedis.get(KEY));
      jedis.lpush(KEY_LIST, "Redis");
      jedis.lpush(KEY_LIST, "Mongodb");
      jedis.lpush(KEY_LIST, "Mysql");
      // Get the stored data and print it
      jedis
          .lrange(KEY_LIST, 0, 5)
          .forEach(value -> log.info("List values stored on Redis:: {}", value));
      jedis.keys("*").forEach(value -> log.info("Key values stored on Redis:: {}", value));
      jedis.del(KEY);
      jedis.del(KEY_LIST);
      log.info("Current store on Redis:: {}", jedis.keys("*").size());
      cypherStore(jedis);
    }
  }

  private static void cypherStore(Jedis jedis) {
    Kryo k = new Kryo();
    k.register(UserDto.class);
    k.register(LocationDto.class);
    k.register(String.class);
    var location = LocationDto.builder().city("Medellín").address("Fake street 123").build();
    UserDto user = UserDto.builder().name("King").lastName("Bradley").location(location).build();
    byte[] byteArray = encode(k, user);
    byte[] keyArray = encode(k, KEY_LIST);
    byte[] name = "Kryo".getBytes();
    // put
    jedis.hset(name, keyArray, byteArray);
    // get
    byte[] jedisArray = jedis.hget(name, keyArray);
    UserDto decodedUser = decode(k, jedisArray);
    log.info("{}", decodedUser);
  }

  private static <T> byte[] encode(Kryo kryo, T obj) {
    ByteArrayOutputStream objStream = new ByteArrayOutputStream();
    try (Output objOutput = new Output(objStream)) {
      kryo.writeClassAndObject(objOutput, obj);
    }
    return objStream.toByteArray();
  }

  @SuppressWarnings("unchecked")
  private static <T> T decode(Kryo kryo, byte[] bytes) {
    return (T) kryo.readClassAndObject(new Input(bytes));
  }
}
