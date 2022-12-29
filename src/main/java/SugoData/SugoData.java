package SugoData;

import Supporter.SaveNLoad;

import java.math.BigInteger;
public class SugoData {

    public static String DataPath = "Sub/Data.txt";
    public String LogPath = "Sub/subLog.log";

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


}
