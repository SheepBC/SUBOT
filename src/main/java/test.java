import SugoData.*;
import Supporter.SaveNLoad;
import Supporter.Stock_Data;
import Supporter.Support;
import Supporter.UserDB;
import com.fasterxml.jackson.databind.introspect.Annotated;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

//5375665143525415
public class test {

    static Map<String,Object> list = new HashMap<>();
/*
    public static void main(String[] args) {
        Class[]classes = new Class[]{class_test.class};
        class_test classTest = new class_test(1,1);
        list.put("class_test",classTest);
        for(Class c : classes){

            Method[] methods = c.getDeclaredMethods();
            for(Method m : methods){
                System.out.println(m.getName());
                for(Annotation a : m.getAnnotations()){
                    if(a instanceof A_test){
                        try {
                            m.invoke(list.get("class_test"));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        }

    }

 */

    public static void main(String[] args) {
        SugoData.updateMoney();

        BigInteger start =SugoData.getMoney();

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    SugoData.addMoney(new BigInteger("1"), SugoAddMoney.Work);
                }
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    SugoData.addMoney(new BigInteger("-1"), SugoAddMoney.Work);
                }
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            e.printStackTrace();

        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                BigInteger end =SugoData.getMoney();
                System.out.println(start.subtract(end));
            }
        },12000);


    }
}
