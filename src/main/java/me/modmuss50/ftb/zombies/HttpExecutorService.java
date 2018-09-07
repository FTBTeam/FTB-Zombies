package me.modmuss50.ftb.zombies;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpExecutorService {

	//2 threads, if things start to queue up add more
	private final static ExecutorService executor = Executors.newFixedThreadPool(2);

	public static void queue(Runnable runnable) {
		executor.submit(runnable);
	}

}
