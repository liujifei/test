package redis;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.PipelineBase;
import redis.clients.jedis.exceptions.JedisMovedDataException;
import redis.clients.jedis.exceptions.JedisRedirectionException;
import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.SafeEncoder;

/**
 * 在集群模式下提供批量操作的功能。 <br/>
 * @author fjy
 *
 */
public class JedisClusterPipeline extends PipelineBase implements Closeable{
	private Logger logger = LoggerFactory.getLogger(JedisClusterPipeline.class);
	private Queue<Client> clients = new LinkedList<Client>();	// 根据顺序存储每个命令对应的Client
	private Map<JedisPool, Jedis> jedisMap = new HashMap<>();	// 用于缓存连接
	private boolean hasDataInBuf = false;	// 是否有数据在缓存区
	
	private OperationRedisCluster operation;
	
	public JedisClusterPipeline() {
		this.operation = OperationRedisCluster.getInstance();
	}
	
	@Override
	public void close() throws IOException {
		super.clean();
		clients.clear();
		
		for (Jedis jedis : jedisMap.values()) {
			if (hasDataInBuf) {
				flushCachedData(jedis);
			}
			jedis.close();
		}
		jedisMap.clear();
		
		hasDataInBuf = false;
	}

	/**
	 * 同步读取所有数据. 与syncAndReturnAll()相比，sync()只是没有对数据做反序列化
	 */
	public void sync() {
		innerSync(null);
	}
	
	/**
	 * 同步读取所有数据 并按命令顺序返回一个列表
	 * 
	 * @return 按照命令的顺序返回所有的数据
	 */
	public List<Object> syncAndReturnAll() {
		List<Object> responseList = new ArrayList<Object>();
		
		innerSync(responseList);
		
		return responseList;
	}
	
	private void innerSync(List<Object> formatted) {
		HashSet<Client> clientSet = new HashSet<Client>();
		
		try {
			for (Client client : clients) {
				// 在sync()调用时其实是不需要解析结果数据的，但是如果不调用get方法，发生了JedisMovedDataException这样的错误应用是不知道的，因此需要调用get()来触发错误。
				Object data = generateResponse(client.getOne()).get();
				if (null != formatted) {
					if(data!=null)
						formatted.add(data);
				}
				
				// size相同说明所有的client都已经添加，就不用再调用add方法了
				if (clientSet.size() != jedisMap.size()) {
					clientSet.add(client);
				}
			}
		} catch (JedisRedirectionException jre) {
			if (jre instanceof JedisMovedDataException) {
				// if MOVED redirection occurred, rebuilds cluster's slot cache,
				// recommended by Redis cluster specification
				operation.refreshCluster();
			}

			throw jre;
		} finally {
			if (clientSet.size() != jedisMap.size()) {
				// 所有还没有执行过的client要保证执行(flush)，防止放回连接池后后面的命令被污染
				for (Jedis jedis : jedisMap.values()) {
					if (clientSet.contains(jedis.getClient())) {
						continue;
					}
					flushCachedData(jedis);
				}
			}
			
			hasDataInBuf = false;
			try {
				close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	private void flushCachedData(Jedis jedis) {
		try {
			//其实目的是为了调用flush这个方法
			jedis.getClient().getAll();
		} catch (RuntimeException ex) {
			// 其中一个client出问题，后面出问题的几率较大
		}
	}
	
	@Override
	protected Client getClient(String key) {
		// TODO Auto-generated method stub
		byte[] bKey = SafeEncoder.encode(key);

		return getClient(bKey);
	}

	@Override
	protected Client getClient(byte[] key) {
		// TODO Auto-generated method stub
		Jedis jedis = getJedis(JedisClusterCRC16.getSlot(key));
		
		Client client = jedis.getClient();
		clients.add(client);
		
		return client;
	}
	
	private Jedis getJedis(int slot) {
		JedisPool pool = operation.getClusterInfoCache().getSlotPool(slot);
		// 根据pool从缓存中获取Jedis
		Jedis jedis = jedisMap.get(pool);
		if (null == jedis) {
			jedis = pool.getResource();
			jedisMap.put(pool, jedis);
		}

		hasDataInBuf = true;
		return jedis;
	}
}
