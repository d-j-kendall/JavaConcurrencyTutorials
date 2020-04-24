package com.dustinkendall;

import com.lichen.TestAnnotation;
import java.lang.reflect.*;

public class Main {
    static public class Ming {
        @TestAnnotation("Testing")
        private String test;
        Ming(String val){
            test = val;
        }
    }

    public static void main(String[] args) {
        try {
            Ming m = new Ming("Hello Reflection!");
            Field f = m.getClass().getDeclaredField("test");
            f.setAccessible(true);
                System.out.println(f.getName() + " : " + f.get(m));
                f.set(m,"f.get(m)");
                System.out.println(f.getName() + " : " + f.get(m));
        }catch (Exception e){
            System.out.println("Exception thrown : "+e.toString());
        }

    }
}
