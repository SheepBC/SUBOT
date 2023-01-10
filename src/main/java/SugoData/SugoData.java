package SugoData;

import Chart.Chart;
import Supporter.SaveNLoad;
import Supporter.Support;
import Supporter.UserDB;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class SugoData {

    public static String DataPath = "Sub/Data.txt";
    public static String ChartPath = "Sub/Charts/";
    public static String sp_m = "SP_Price";
    public static String sg_m = "SG_Money";
    public static String sg_sp = "SG_SP";
    public static String am_h = "AM_Health";
    public static String version = "version";
    public static String money = "money";
    public static String tax = "tax";

    public static String getVersion = SaveNLoad.loadP(DataPath,version);

    public static BigInteger getMoney(){
        try{
            return new  BigInteger(SaveNLoad.loadP(DataPath,money));
        }catch (Exception e){
            return BigInteger.ZERO;
        }
    }
    public static void setMoney(BigInteger bInt){
        SaveNLoad.addP(DataPath,money,bInt.toString());
    }

    public static void setMoney(int i){
        setMoney(new BigInteger(String.valueOf(i)));
    }

    public static void setMoney(Long l){
        setMoney(new BigInteger(String.valueOf(l)));
    }

    public static void setMoney(String s){
        setMoney(new BigInteger(s));
    }

    public static void addMoney(BigInteger bInt){
        BigInteger bi = getMoney().add(bInt);
        setMoney(bi);
    }

    public static void addMoney(int i){
        addMoney(new BigInteger(String.valueOf(i)));
    }

    public static void addMoney(String s){
        addMoney(new BigInteger(s));
    }

    public static void addMoney(long l){
        addMoney(new BigInteger(String.valueOf(l)));
    }

    public static float getTax(){
        return Float.parseFloat(SaveNLoad.loadP(DataPath,tax));
    }

    public static void setTax(float f){
        SaveNLoad.addP(DataPath,tax,String.valueOf(f));
    }

    public static BigInteger getSP(){
        ArrayList<BigDecimal> list = new ArrayList<>();
        for(String id: UserDB.getUserList()){
            UserDB userDB = new UserDB(id);
            BigDecimal p =new BigDecimal(userDB.getAll_Property());
            UserDB userDB1 = new UserDB(id);
            String n = userDB1.getName();
            list.add(p);
        }
        BigDecimal am = Support.getAM_Num(list.toArray(new BigDecimal[0]));
        return am.toBigInteger().divide(new BigInteger("2598810490"));
    }

    public static void UpdateChart(){
        int second = 1000;
        int min = 60*second;

        Chart chart_sp_m = new Chart(ChartPath+sp_m);
        Chart chart_sg_m = new Chart(ChartPath+sg_m);
        Chart chart_sg_sp = new Chart(ChartPath+sg_sp);
        Chart chart_am_h = new Chart(ChartPath+am_h);


        Date date = new Date();
        if(date.getMinutes() < 30){
            date.setMinutes(30);
        }
        else {
            date.setMinutes(0);
            date.setHours(date.getHours()+1);
        }

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                chart_sp_m.add(getSP().toString());
                chart_sg_m.add(getMoney().toString());
                chart_sg_sp.add(getMoney().divide(getSP()).toString());
                ArrayList<BigDecimal> list = new ArrayList<BigDecimal>();
                for(String id:UserDB.getUserList()){
                    UserDB userDB = new UserDB(id);
                    list.add(new BigDecimal(String.valueOf(userDB.getHealth())));
                }
                chart_am_h.add(Support.getAM_Num(list.toArray(new BigDecimal[0])).toBigInteger().toString());
            }
        },date,30*min);
    }

    public static double addMRP(){
        BigDecimal sp = new BigDecimal(getSP());
        BigDecimal spm = new BigDecimal("330000");
        int b = sp.min(spm).equals(sp) ? 1:-1;
        BigDecimal mrp = sp.divide(spm, 2,RoundingMode.HALF_EVEN );
        mrp = mrp.subtract(BigDecimal.ONE).abs(MathContext.DECIMAL32);
        mrp = mrp.divide(mrp.add(new BigDecimal("25")),3,RoundingMode.HALF_EVEN);
        return mrp.doubleValue()*b;
    }
}
