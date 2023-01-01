package SugoData;

import Supporter.SaveNLoad;
import Supporter.Support;
import Supporter.UserDB;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

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

}
