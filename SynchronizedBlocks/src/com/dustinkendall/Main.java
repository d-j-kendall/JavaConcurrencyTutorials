package com.dustinkendall;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static class Bar{
        synchronized public void foo1(){
            System.out.println("Method 1");
            try{
                System.out.println("Sleeping...");
                Thread.sleep(6000);
                return;
            }catch (InterruptedException e){

            }finally {
                System.out.println("Foo1 lock released");
            }
        }

        synchronized public void foo2(){
            System.out.println("Method 2");
        }
    }

    public static void main(String[] args) {
        Bar bar = new Bar();
        ExecutorService exeService = Executors.newFixedThreadPool(10);
        exeService.submit(bar::foo1);
        exeService.submit(()->{
            System.out.println("Executing Task 2 before synchronized block");
            synchronized (bar){
                System.out.println("Now in Synchronized block");
                bar.foo2();
            }

        });
    }
}
