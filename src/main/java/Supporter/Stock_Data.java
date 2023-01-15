package Supporter;

import Chart.Chart;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import Main.main;
import SugoData.SugoData;

import static Supporter.Stock_Data.Key.*;

public class Stock_Data {

    String id;
    String path = "Stock/";
    String inform = "/Inform.txt";
    String Update = "/Update.txt";

    Chart chart;

    class Key{
        static String Id = "Id";
        static String Name = "name";
        static String Color = "color";
        static String Start_All_Price = "Start_All_Money";
        static String All_Price = "Now_All_Money";
        static String Start_Num = "Start_Num";
        static String Num = "Now_Num";
        static String Selling_Num ="Selling_Num";
        static String last_Price = "last_Money";
        static String Week_Selling_Num = "Week_Selling_Num";
        static String update_Type = "update_Type";
        static String Time ="Time";
        static String seed ="seed";
        static String smooth ="smooth";
        static String noise_x ="x";
    }

    static public List<String> list(){
        File file = new File("Stock");
        //System.out.println(file.list().length);
        ArrayList<String> list = new ArrayList<>(List.of(file.list()));
        list.remove(list.indexOf("Common_Update.txt"));
        return list;
    }

    public Stock_Data(String id){
        this.id = id;
        path += this.id;
        inform = path+inform;
        Update = path+Update;
        chart = new Chart(path+"/Chart");
        //System.out.println(id);
    }

    public BigInteger getStart_Num(){
        String sn = SaveNLoad.loadP(inform,Start_Num);
        return new BigInteger(sn);
    }

    public void setStart_Num(BigInteger bi){
        SaveNLoad.addP(inform,Start_Num,bi.toString());
    }

    public void addStart_Price(BigInteger bi){
        BigInteger sp = getStart_Num().add(bi);
        setStart_Num(sp);
    }

    public BigInteger getStart_All_Price(){
        String p = SaveNLoad.loadP(inform,Start_All_Price);
        return new BigInteger(p);
    }

    public void setStart_All_Price(BigInteger bi){
        SaveNLoad.addP(inform,Start_All_Price,bi.toString());
    }

    public void addStart_All_Price(BigInteger bi){
        BigInteger ap = getStart_All_Price().add(bi);
        setStart_All_Price(ap);
    }


    public BigInteger getPrice(){
        BigInteger all = getAll_Price();
        BigInteger num = getALl_Num();
        //System.out.println(id+" "+getALl_Num()+" "+all);
        return all.divide(num);
    }

    public void setPrice(BigInteger bi){
        setAll_Price(bi.multiply(getALl_Num()));
    }

    public void addPrice(BigInteger bi){
        setPrice(getPrice().add(bi));
    }

    public BigInteger getLast_Price(){
        String lp = SaveNLoad.loadP(inform,last_Price);
        return new BigInteger(lp);
    }

    public void setLast_Price(BigInteger bi){
        SaveNLoad.addP(inform,last_Price,bi.toString());
    }

    public BigInteger getAll_Price(){
        String p = SaveNLoad.loadP(inform,All_Price);
        BigInteger rt;
        try {
            rt = new BigInteger(p);
        }catch (Exception e){
            System.out.println("주식총가격 오류 "+p+" "+id);
            return getAll_Price();
        }
        return new BigInteger(p);
    }

    public void setAll_Price(BigInteger bi){
        SaveNLoad.addP(inform,All_Price,bi.toString());
    }

    public void addAll_Price(BigInteger bi){
        BigInteger ap = getAll_Price().add(bi);
        setAll_Price(ap);
    }

    public BigInteger getALl_Num(){
        String an = SaveNLoad.loadP(inform,Num);
        BigInteger rt;
        try {
            rt = new BigInteger(an);
        }catch (Exception e){
            System.out.println("주식총갯수 오류 "+an+" "+id);
            return getALl_Num();
        }
        //System.out.println(id+ " "+an);
        return new BigInteger(an);
    }
    public void setALl_Num(BigInteger num){
        SaveNLoad.addP(inform,Num,num.toString());
    }

    public BigInteger getSelling_Num(){
        String sn = SaveNLoad.loadP(inform,Selling_Num);
        return new BigInteger(sn);
    }

    public void setSelling_Num(BigInteger bi){
        SaveNLoad.addP(inform,Selling_Num,bi.toString());
    }

    public void addSelling_Num(BigInteger bi){
        BigInteger sp = getSelling_Num().add(bi);
        setSelling_Num(sp);
    }

    public BigInteger getWeek_Selling_Num(){
        String sn = SaveNLoad.loadP(inform,Week_Selling_Num);
        return new BigInteger(sn);
    }

    public void setWeek_Selling_Num(BigInteger bi){
        SaveNLoad.addP(inform,Week_Selling_Num,bi.toString());
    }

    public void addWeek_Selling_Price(BigInteger bi){
        BigInteger sp = getWeek_Selling_Num().add(bi);
        setWeek_Selling_Num(sp);
    }

    public String getUpdate_Type(){
        return SaveNLoad.loadP(inform,update_Type);
    }

    public int getTime(){
        return Integer.parseInt(getTimes()[0]);
    }

    public void setTime(int m){
        setTimes(m,getAll_Time());
    }

    public void addTime(int m){
        setTime(getTime()+m);
    }

    public int getAll_Time(){
        return Integer.parseInt(getTimes()[1]);
    }


    public void setTimes(int m,int am){
        String s = m+"/"+am;
        SaveNLoad.addP(inform,Time,s);
    }

    private String[] getTimes(){
        return SaveNLoad.loadP(inform,Time).split("/");
    }

    public void update_Price(){
        int r1 = (int)(Math.random()*10);
        setLast_Price(getPrice());
        if(r1<4){
            //+
            update_Price_A();
        }
        else {
            //*
            if(getUpdate_Type().equalsIgnoreCase("P")){
                update_Price_P_P();
            }
            else {
                update_Price_C_P();
            }
        }
        if(getPrice().min(new BigInteger("1000")).equals(getPrice())){
            addPrice(new BigInteger("600"));
        }if(getPrice().min(new BigInteger("0")).equals(getPrice())){
            ArrayList<String> list = new ArrayList<>();
            for(String userid:UserDB.getUserList()){
                UserDB userDB = new UserDB(userid);
                if(userDB.getStock_Num(id) != 0){
                    list.add(userid);
                }
            }
            main.alarm(getName()+"이 상장폐지 하였습니다.",list.toArray(new String[0]));
            reset();
        }

        chart.add(getPrice().toString());
    }

    private void update_Price_A() {
        int aP = (int) SaveNLoad.getRandom("Stock/Common_Update.txt");
        double mrp = 2.5+SugoData.getMRP();
        if((int) (Math.random()*mrp) == 0) aP *= -1;
        if(getUpdate_Type().equalsIgnoreCase("C") & Math.random()*10 <3) aP += 1500;
        addPrice(new BigInteger(String.valueOf(aP)));
        if(getPrice().max(BigInteger.ZERO).equals(BigInteger.ZERO)){
            setPrice(new BigInteger("1000"));
        }
    }

    private void update_Price_C_P() {
        double aPP = SaveNLoad.getRandom(Update)/100;
        //System.out.println(id+" "+aPP);
        double mrp = 2.011175+SugoData.getMRP();
        if((int) (Math.random()*mrp) == 0) aPP *= -1;
        if(Math.random()*100 < 0.5) aPP+= 1;
        if(Math.random()*100 < 0.1) aPP+= 2;
        if(Math.random()*100 < 0.3) aPP = Math.abs(aPP)*-1;
        aPP += SugoData.getMRP()/2;
        BigDecimal nP = new BigDecimal(getPrice());
        nP = nP.multiply(BigDecimal.ONE.add(new BigDecimal(String.valueOf(aPP))));
        setPrice(nP.toBigInteger());
    }

    private void update_Price_P_P(){
        double mrp = 5.19-SugoData.getMRP();
        double d = Support.noise(getSeed(),getSmooth(),getNoise_X())/mrp;
        addNoise_X(1);
        double p = 1+d;
        p += SugoData.getMRP()/2;
        BigDecimal nP = new BigDecimal(getPrice());
        nP = nP.multiply(new BigDecimal(String.valueOf(p)));
        setPrice(nP.toBigInteger());
        //System.out.println(getPrice()+" "+p);
    }

    public int getSeed(){
        String sn = SaveNLoad.loadP(inform,seed);
        return Integer.parseInt(sn);
    }

    public void setSeed(int seed){
        SaveNLoad.addP(inform,Key.seed,String.valueOf(seed));
    }

    public int getSmooth(){
        String sn = SaveNLoad.loadP(inform,smooth);
        return (int) Double.parseDouble(sn);
    }

    public void setSmooth(int smooth){
        SaveNLoad.addP(inform, Key.smooth,String.valueOf(smooth));
    }

    public int getNoise_X(){
        String sn = SaveNLoad.loadP(inform,noise_x);
        return (int) Double.parseDouble(sn);
    }

    public void setNoise_x(double x){
        SaveNLoad.addP(inform, noise_x,String.valueOf(x));
    }

    public void addNoise_X(int x){
        setNoise_x(x+getNoise_X());
    }

    public Chart getChart(){
        Chart chart = new Chart("Stock/"+id+"/Chart");
        return chart;
    }

    public String getName(){
        return SaveNLoad.loadP(inform,Name);
    }
    public void setName(String name){
        SaveNLoad.addP(inform,Name,name);
    }

    public String getColor(){
        return SaveNLoad.loadP(inform,Color);
    }

    public double getLeftNum(){
        return getALl_Num().subtract(getSelling_Num()).doubleValue();
    }

    public void reset(){
        setALl_Num(getStart_Num());
        setSelling_Num(BigInteger.ZERO);
        setWeek_Selling_Num(BigInteger.ZERO);
        setAll_Price(getStart_All_Price());
        for(String id:UserDB.getUserList()){
            UserDB userDB = new UserDB(id);
            if(userDB.getStock_Num(this.id) != 0){
                //System.out.println(userDB.getName()+" "+this.id);
                userDB.setStock(this.id,0,BigInteger.ZERO);
            }
        }
        setSeed((int) (Math.random() * 1000000));
        setSmooth((int) (Math.random() * 255+1));
        setNoise_x(0);
        setLast_Price(getPrice());
        File file = new File(getChart().getPath());
        Support.deleteFilesRecursively(file);
        //System.out.println(id+" "+getPrice()+" "+b);
    }


}
