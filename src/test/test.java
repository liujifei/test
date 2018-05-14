package test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class test {

	private static AtomicInteger count = new AtomicInteger();

	public static void main(String[] args) {
		count.set(0);;
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<Object> task = new Callable<Object>() {
			public Object call() throws Exception {
				Object result = "...";
				System.out.println("run task!!!");
				for(int i = 0; i < 10; i++){
					System.out.println(count.incrementAndGet());
				}
				return result;
			}
		};
		Callable<Object> task1 = new Callable<Object>() {
			public Object call() throws Exception {
				Object result = "...";
				System.out.println("run task!!!11111");
				for(int i = 0; i < 10; i++){
					System.out.println(count.incrementAndGet());
				}
				return result;
			}
		};
		Future<Object> future = executor.submit(task);
		executor.submit(task1);
		try {
			// 等待到任务被执行完毕返回结果
			// 如果任务执行出错，这里会抛ExecutionException
			future.get();
			//等待3秒，超时后会抛TimeoutException
//			future.get(3, TimeUnit.SECONDS);
			System.out.println("finish!!!");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

}
