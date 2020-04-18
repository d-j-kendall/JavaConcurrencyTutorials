package com.dustinkendall;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    static class PlcPayLoadData{
        PlcPayLoadData(String data){ this.data = data;}
        public String data;
    }

    static class ModbusPayloadHelper extends Thread{
        LinkedBlockingQueue<PlcPayLoadData> receivedQueue;
        ModbusPayloadHelper(LinkedBlockingQueue<PlcPayLoadData> queue){
            this.setName("Modbus_Payload_Helper_Thread");
            receivedQueue = queue;}
        @Override
        public void run(){
            while(true){

                Random rng = new Random();
                Integer fourBytes = rng.nextInt();
                String data = new String(new byte[]{(ByteBuffer.allocate(4).putInt(fourBytes).array()[0])});
                try {
                    PlcPayLoadData plcData;
                    receivedQueue.put(plcData = new PlcPayLoadData(data));
                    System.out.println("Data incoming = "+plcData.data);
                    Thread.sleep(200);
                }catch (InterruptedException e){

                }
            }
        }
    }

    static class FilterDuplicates extends Thread{
        LinkedBlockingQueue<PlcPayLoadData> inputQueue;
        LinkedBlockingQueue<PlcPayLoadData> ouputQueue;
        PlcPayLoadData previousData;
        private PlcPayLoadData currentData;
        FilterDuplicates(LinkedBlockingQueue<PlcPayLoadData> input, LinkedBlockingQueue<PlcPayLoadData> output){
            inputQueue = input;
            ouputQueue = output;
            previousData = null;
            this.setName("Filter_Duplicated_Thread");
        }

        @Override
        public void run(){
            while(true) {
                try {
                    Thread.sleep(200);
                    currentData = inputQueue.take();
                } catch (InterruptedException e) {

                }
                if (previousData != null) {
                    if (!previousData.equals(currentData)) {
                        try {
                            ouputQueue.put(currentData);
                            System.out.println("Data outgoing = "+currentData.data+"\n\n");
                        } catch (InterruptedException e) {

                        }
                    }
                }
                previousData = currentData;
            }


        }
    }

    public static void main(String[] args) {
        LinkedBlockingQueue<PlcPayLoadData> receiving = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<PlcPayLoadData> outgoing = new LinkedBlockingQueue<>();
        ModbusPayloadHelper modbusPayloadHelper = new ModbusPayloadHelper(receiving);
        FilterDuplicates filterDuplicates = new FilterDuplicates(receiving,outgoing);

        modbusPayloadHelper.start();
        filterDuplicates.start();
    }
}
