package redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import utils.Configuration;

public final class RedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	private static final String DEFAULT_CLUSTER = "default,172.22.5.20:7000,172.22.5.21:7000,172.22.5.22:7000,172.22.5.23:7000,172.22.5.24:7000";
	// 访问密码
	// private static String AUTH = "admin";
	// 可用连接实例的最大数目，默认值为8；
	private static int MAX_CONN = 200;

	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	// private static int MAX_ACTIVE = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 100;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 60000;

	// JedisCluster的timeout
	private static int TIMEOUT = 60000;
	// JedisCluster的maxRedirections
	private static int MAX_REDIRECT = 10;

	// redis相关配置（从配置文件读取）
	private static Configuration conf = null;
	
	// 存储所有redis集群
	private static final Map<String, JedisCluster> jcList = new HashMap<String, JedisCluster>();
	
	// 用来存储状态信息的default集群
	private static JedisCluster defaultCluster;
	
	private static final String OLD_TASK_SET = "OldTaskInstances";

	static {
		conf = new Configuration("redis.properties");
		String clusterConf = conf.get("redis.cluster", DEFAULT_CLUSTER);
		String[] clusterArray = clusterConf.split(";");
		for (String clusterStr : clusterArray) {
			logger.info("初始化redis集群：" + clusterStr);
			initCluster(clusterStr);
		}
		defaultCluster = getJedis();
	}
	
	// 私有构造函数，避免被类被实例化
	private RedisUtil() {}

	private static void initCluster(String cluster) {
		String[] clusterNameAndNodes = cluster.split(",");
		String clusterName = clusterNameAndNodes[0];
		JedisCluster jc = jcList.get(clusterName);
		if (jc != null) {
			try {
				jc.close();
			} catch (IOException e) {
				logger.error("关闭jedisCluster失败：" + e.getMessage(), e);
				return;
			}
		}
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		for (int i = 1; i < clusterNameAndNodes.length; i++) {
			String clusterNode = clusterNameAndNodes[i];
			// Jedis Cluster will attempt to discover cluster nodes
			// automatically
			String host = clusterNode.substring(0, clusterNode.indexOf(":"));
			int port = Integer.valueOf(clusterNode.substring(1 + clusterNode.indexOf(":")));
			jedisClusterNodes.add(new HostAndPort(host, port));
		}
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(MAX_CONN); // 最大连接数
		poolConfig.setMaxIdle(MAX_IDLE); // 最大空闲数
		poolConfig.setMaxWaitMillis(MAX_WAIT);

		jc = new JedisCluster(jedisClusterNodes, TIMEOUT, MAX_REDIRECT, poolConfig);
		jcList.put(clusterName, jc);
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized static JedisCluster getJedis() {
		return getJedis("default");
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized static JedisCluster getJedis(String clusterName) {
		JedisCluster jc = jcList.get(clusterName);
		if (jc == null) {
			logger.error("不存在该redis集群：" + clusterName);
		}
		return jc;
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized static JedisCluster getJedis(Long taskId, String instanceId) {
		if(defaultCluster.sismember(OLD_TASK_SET, taskId +":"+instanceId)){
			return getJedis();
		}else{
			return getJedis("new");
		}
		
	}
	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 * @throws IOException
	 */
	public static void close(String clusterName) throws IOException {
		JedisCluster jc = jcList.remove(clusterName);
		if (jc != null) {
			jc.close();
		}
	}

	public static TreeSet<String> keys(JedisCluster cluster, String pattern) {
		logger.debug("Start getting keys...");
		TreeSet<String> keys = new TreeSet<>();

		Map<String, JedisPool> clusterNodes = cluster.getClusterNodes();
		String nodes = null;
		for (String k : clusterNodes.keySet()) {
			JedisPool jp = clusterNodes.get(k);
			// Jedis implements Closable. Hence, the jedis instance will be
			// auto-closed after the last statement.
			try (Jedis connection = jp.getResource()) {
				if (nodes == null) {
					nodes = connection.clusterNodes();
				}
				if (nodes.contains(k + " master") || nodes.contains(k + " myself,master")) {
					logger.debug("Getting keys from: {}", k);
					keys.addAll(connection.keys(pattern));
				} else{
					logger.debug("Ignore slave node: {}", k);
				}
			} catch (Exception e) {
				logger.error("Can't connect redis server: " + k);
				logger.error(e.getMessage(), e);
			}
		}
		logger.debug("Keys gotten!");
		return keys;
	}
	
	public static void putOldTaskInstance(Long taskId, String instanceId) {
		defaultCluster.sadd(OLD_TASK_SET, taskId+":"+instanceId);
		logger.info("放入old集合："+taskId+":"+instanceId);
	}

	public static void main(String[] args) {
		System.out.println(defaultCluster.smembers(OLD_TASK_SET));
		System.out.println("===================================");
		System.out.println(defaultCluster.get("HelloRedis"));
		defaultCluster.set("HelloRedis","HelLo");
		System.out.println(defaultCluster.get("HelloRedis"));
		System.out.println(defaultCluster.del("HelloRedis"));
		defaultCluster.expire("HelloRedis", 3);
		try {
            Thread.sleep(3001);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		System.out.println(defaultCluster.get("HelloRedis"));
		//defaultCluster.del(OLD_TASK_SET);
	}
}