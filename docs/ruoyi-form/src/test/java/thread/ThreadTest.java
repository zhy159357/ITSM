package thread;

import javax.sound.midi.Soundbank;
import java.util.concurrent.*;

public class ThreadTest {

//    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    private static ExecutorService executor = new ThreadPoolExecutor(5, 20, 1000, TimeUnit.SECONDS,
        new LinkedBlockingDeque<>(50), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程执行了...." + Thread.currentThread().getId());
//        Thread thread01 = new Thread(new Thread01());
//        thread01.start();
//        Future<?> future = executor.submit(new Thread01());
//        Future<Integer> future = executor.submit(new Thread02());
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("T1:洗水壶......");
            sleep(1, TimeUnit.SECONDS);
            System.out.println("T1:烧开水......");
            sleep(15, TimeUnit.SECONDS);
        }, executor);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("T2:洗茶壶......");
            sleep(1, TimeUnit.SECONDS);
            System.out.println("T2:洗茶杯......");
            sleep(2, TimeUnit.SECONDS);
            System.out.println("T2:拿茶叶......");
            sleep(1, TimeUnit.SECONDS);
            return "龙井";
        }, executor);
        // 任务1和任务2完成后泡茶，执行任务3
        CompletableFuture<String> future3 = future1.thenCombineAsync(future2, (t1, t2) -> {
            System.out.println("T1:拿到茶叶：" + t2);
            System.out.println("T1:泡茶......");
            return "上茶:" + t2;
        }, executor);
        System.out.println(future3.get());
    }

    public static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class Thread00 extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }

    public static class Thread01 implements Runnable{
        @Override
        public void run() {
            System.out.println("线程执行了...." + Thread.currentThread().getId());
        }
    }

    public static class Thread02 implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            System.out.println("线程执行了...." + Thread.currentThread().getId());
            int n = 10 / 2;
            return n;
        }
    }
}
