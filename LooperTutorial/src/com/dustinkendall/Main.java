package com.dustinkendall;


import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    static class LooperThread extends Thread{
        private ArrayBlockingQueue<Runnable> jobQueue = new ArrayBlockingQueue<>(30);

        @Override
        public void run() {
                for (; ; ) {
                    if (jobQueue.isEmpty()) {
                        synchronized (this) {
                            try {
                                wait();
                                continue;
                            } catch (InterruptedException e) {
                                continue;
                            }

                        }
                    }else {
                        jobQueue.poll().run();
                    }
            }
        }

        synchronized public void post(Runnable job){
                jobQueue.add(job);
                notify();

        }
    }
    private static void sleep(long ms){
        try{
            Thread.sleep(ms);
        }catch (InterruptedException e){

        }
    }

    private static void sleep(){
        sleep(4000);
    }


    public static void main(String[] args) {
        LooperThread looperThread = new LooperThread();
        looperThread.start();
            Runnable job1 = ()->{
                System.out.println("Running job 1...");
                sleep();
            };

            Runnable job2 = ()->{
                System.out.println("Running job 2...");
                sleep();
            };

            Runnable job3 = () ->{
                System.out.println("Running job 3...");
                sleep();
            };

            //Posting jobs
            looperThread.post(job1);
            looperThread.post(job2);
            looperThread.post(job3);

            System.out.println("Main Function Completed");

    }
}
