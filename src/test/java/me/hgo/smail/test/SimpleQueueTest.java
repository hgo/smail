package me.hgo.smail.test;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

public class SimpleQueueTest {

    class Data extends Observable {
        final AtomicInteger a = new AtomicInteger(1);

        public synchronized int getAndIncrement() {
            int r = a.getAndIncrement();
            setChanged();
            notifyObservers(r);
            return r;
        }

    }

    class Obs implements Observer {

        public void update(Observable o, Object arg) {
            final Data d = (Data) o;
            System.err.println("Observer " + Thread.currentThread().getName() + " = > " + d.a.get());
        }
    }

    @Before
    public void before() {
    }

    @Test
    public void test_add_to_queue() throws InterruptedException {
        final AtomicInteger a = new AtomicInteger(1);
        ExecutorService s = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            s.submit(new Runnable() {
                public void run() {
                    System.err.println(Thread.currentThread().getName() + " " + a.getAndIncrement());
                }
            });
        }
        s.shutdown();
        while (!s.isTerminated()) {

        }
    }

    @Test
    public void test_observer() throws InterruptedException {
        ExecutorService s = Executors.newFixedThreadPool(4);
        final Data d = new Data();
        Obs obs = new Obs();
        d.addObserver(obs);
        for (int i = 0; i < 100; i++) {
            s.submit(new Runnable() {
                public void run() {
                    System.err.println("d increment =>" + Thread.currentThread().getName() + " " + d.getAndIncrement());
                }
            });
        }
        s.shutdown();
        while (!s.isTerminated()) {

        }
    }

}
