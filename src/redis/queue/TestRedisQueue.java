package redis.queue;

import java.io.IOException;

import redis.RedisUtil;

public class TestRedisQueue {
    public static byte[] redisKey = "key546356365".getBytes();
    static {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void init() throws IOException {
        for (int i = 0; i < 100; i++) {
            Message message = new Message(i, "这是第" + i + "个内容");
            RedisUtil.getJedis().lpush(redisKey, ObjectUtil.object2Bytes(message));
        }

    }

    public static void main(String[] args) {
        try {
//            for (int i = 0; i < 100; i++) {
                pop();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pop() throws Exception {
        byte[] bytes = RedisUtil.getJedis().rpop(redisKey);
        Message msg = (Message) ObjectUtil.bytes2Object(bytes);
        if (msg != null) {
            System.out.println(msg.getId() + "----" + msg.getContent());
        }
    }

}
