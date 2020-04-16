package com.dustinkendall;

public class Main {
    static public class ThreadB extends Thread{
        @Override
        public void run() {
            int i = 0;
            while(true) {
                synchronized (this) {
                    System.out.println("ThreadB is now waiting");
                    try {
                        wait();
                        System.out.println("ThreadB has been notified");
                    }catch (InterruptedException e){
                        System.out.println("ThreadB was interrupted, continuing...");
                        continue;
                    }finally {
                        if(i>40){
                            System.out.println("Ending ThreadB");
                            break;
                        }
                    }
                }
                i++;
            }

        }
    }
    public static void main(String[] args) {
        ThreadB testThread = new ThreadB();
        testThread.start();
        int i = 0;
        while(true) {

            synchronized (testThread){
                System.out.println("MainThread is notifying ThreadB");
                if(i%5 == 4){
                    testThread.interrupt();
                }
                testThread.notify();

            }
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                System.out.println("MainThread was interrupted");
                break;
            }
            finally {
                if(!testThread.isAlive()){
                    System.out.println("ThreadB is dead, ending MainThread");
                    break;
                }
            }
            i++;
        }

    }
}
