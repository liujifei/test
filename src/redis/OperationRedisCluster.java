package redis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterConnectionHandler;
import redis.clients.jedis.JedisClusterInfoCache;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;

/**
 * 对RedisCluster的部分操作：包括获得JedisCluster中connectionHandler、clusterInfoCache成员，刷新集群信息
 * @author fjy
 *
 */
public class OperationRedisCluster {
	private static Logger logger = LoggerFactory.getLogger(OperationRedisCluster.class);
	// 部分字段没有对应的获取方法，只能采用反射来做
	private static final Field FIELD_CONNECTION_HANDLER;
	private static final Field FIELD_CACHE; 
	private static JedisSlotBasedConnectionHandler connectionHandler;
	private static JedisClusterInfoCache clusterInfoCache;
	private static JedisCluster jedisCluster = null;
	private static OperationRedisCluster operationRedisCluster =null;
	
	static {
		FIELD_CONNECTION_HANDLER = getField(BinaryJedisCluster.class, "connectionHandler");
		FIELD_CACHE = getField(JedisClusterConnectionHandler.class, "cache");
	}
	
	private OperationRedisCluster() {}
	private static class SingletonHolder {
		private static final OperationRedisCluster INSTANCE = new OperationRedisCluster();
		static {
			if(jedisCluster == null) {
				jedisCluster =  RedisUtil.getJedis();
				connectionHandler = getValue(jedisCluster, FIELD_CONNECTION_HANDLER);
				clusterInfoCache = getValue(connectionHandler, FIELD_CACHE);
			}
		}
	}
	public static OperationRedisCluster getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public JedisSlotBasedConnectionHandler getConnectionHandler() {
		if(connectionHandler==null)
			throw new NullPointerException() ;
		return connectionHandler;
	}

	public JedisClusterInfoCache getClusterInfoCache() {
		if(clusterInfoCache ==null){
			throw new NullPointerException() ;
		}
		return clusterInfoCache;
	}

	public JedisCluster getJedisCluster(){
		return jedisCluster;
	}
	
	public void close() throws IOException{
		jedisCluster.close();
		jedisCluster =null;
		connectionHandler = null;
		clusterInfoCache = null;
	}

	/**
	 * 刷新集群信息，当集群信息发生变更时调用
	 * @param 
	 * @return
	 */
	public void refreshCluster() {
		if(connectionHandler==null)
			throw new NullPointerException() ;
		connectionHandler.renewSlotCache();
	}
	
	private static Field getField(Class<?> cls, String fieldName) {
		try {
			Field field = cls.getDeclaredField(fieldName);
			field.setAccessible(true);
			
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException("cannot find or access field '" + fieldName + "' from " + cls.getName(), e);
		}
	}
	
	@SuppressWarnings({"unchecked" })
	private static <T> T getValue(Object obj, Field field) {
		try {
			return (T)field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("get value fail", e);
			
			throw new RuntimeException(e);
		}
	}
}
