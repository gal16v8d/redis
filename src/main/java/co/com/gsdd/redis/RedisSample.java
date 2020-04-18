package co.com.gsdd.redis;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import co.com.gsdd.redis.dto.LocationDTO;
import co.com.gsdd.redis.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
public class RedisSample {

    private static final String REDIS_HOST = "192.168.99.100";
    private static final int REDIS_PORT = 6379;
    private static final String KEY = "sample-name";
    private static final String KEY_LIST = "sample-list";

    public static void main(String[] args) {
        jedisPoolConnection();
    }

    private static void jedisPoolConnection() {
        try (JedisPool jedisPool = new JedisPool(REDIS_HOST, REDIS_PORT); Jedis jedis = jedisPool.getResource();) {
            log.info("Connection established");
            log.info("Server is running: {}", jedis.ping());
            jedis.set(KEY, "Redis sample");
            log.info("Stored value on Redis:: {}", jedis.get(KEY));
            jedis.lpush(KEY_LIST, "Redis");
            jedis.lpush(KEY_LIST, "Mongodb");
            jedis.lpush(KEY_LIST, "Mysql");
            // Get the stored data and print it
            jedis.lrange(KEY_LIST, 0, 5).stream().forEach(value -> log.info("List values stored on Redis:: {}", value));
            jedis.keys("*").stream().forEach(value -> log.info("Key values stored on Redis:: {}", value));
            jedis.del(KEY);
            jedis.del(KEY_LIST);
            log.info("Current store on Redis:: {}", jedis.keys("*").size());
            cypherStore(jedis);
        }
    }

    private static void cypherStore(Jedis jedis) {
        Kryo k = new Kryo();
        k.register(UserDTO.class);
        k.register(String.class);
        LocationDTO location = new LocationDTO();
        location.setCity("Medellín");
        location.setAddress("Fake street 123");
        UserDTO usuario = new UserDTO();
        usuario.setName("King");
        usuario.setLastName("Bradley");
        usuario.setLocation(location);
        byte[] byteArray = encode(k, usuario);
        byte[] keyArray = encode(k, KEY_LIST);
        byte[] nombre = "Kryo".getBytes();
        // put
        jedis.hset(nombre, keyArray, byteArray);
        // get
        byte[] arrayMapeado = jedis.hget(nombre, keyArray);
        UserDTO usuarioD = decode(k, arrayMapeado);
        log.info("{}", usuarioD);
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
