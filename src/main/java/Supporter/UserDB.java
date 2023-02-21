package Supporter;

import Chart.Chart;
import School.School_Data;
import SugoData.SugoData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static Supporter.UserDB.Name.*;
public class UserDB {


        final static String UserDBPath = "UserDB";
        String path;
        String HNO;
        String Property;
        String Stocks;
        String Items;
        String Stat;
        String Charts;
        String ChartsImages;
        String Stock;

    class Name {

        static String Name = "Name";
        static String School = "School";
        static String Dead_Num = "Dead_Num";
        static String Failure_Num = "Failure_NUm";
        static String Check_Num = "Check_NUm";

        static String isHealth = "isHealth";
        static String isStopHealth = "isStopHealth";
        static String Health = "Health";
        static String SleepTime = "SleepTime";
        static String Tired = "Tired";

        static String TopProperty ="TopProperty";
        static String TopSP ="TopSP";
        static String Economy_Alarm = "Economy_Alarm";
        static String Money = "Money";
        static String Tax = "Tax";
        static String Auto_Tax = "Auto_Tax";
        static String Debt = "Debt";
        static String Debt_Rate = "Debt_Rate";
        static String Exp = "Exp";

        static String Luck = "Luck";
        static String Stealing = "Stealing";
        static String Defense = "Defense";
        static String Resilience = "Resilience";

        static String Chart_Path = "Charts";
        static String Chart_P = "Chart_P";
        static String Chart_H = "Chart_H";
        static String Chart_SP = "Chart_SP";

    }
    public String userid;
    String name;
    public UserDB(User user){
        userid = user.getId();
        name = user.getName();
        path =UserDBPath+ "/"+userid;
        HNO = path+"/HealthNOthers.txt";
        Property = path+"/Properties.txt";
        Stat = path+"/Stats.txt";
        Stocks = path+"/Stocks.txt";
        Items = path+"/Items.txt";
        Charts = path+"/Charts";
        ChartsImages = Charts+"/Images";
        Stock = path+"/Stock.txt";
    }
    public UserDB(String uid){
        userid = uid;
        name = "미정";
        path =UserDBPath+ "/"+userid;
        HNO = path+"/HealthNOthers.txt";
        Property = path+"/Properties.txt";
        Stat = path+"/Stats.txt";
        Stocks = path+"/Stocks.txt";
        Items = path+"/Items.txt";
        Charts = path+"/Charts";
        ChartsImages = Charts+"/Images";
        Stock = path+"/Stock.txt";
    }
    public static String[] getUserList(){
        File file = new File(UserDBPath);
        String[] list = file.list();
        file.exists();
        return list;
    }

    public static Boolean isUser(String userid){
        for(String id:getUserList()){
            if(id.equalsIgnoreCase(userid)) return true;
        }
        return false;
    }

    public void makeUser(){

        //기타
        setName(name);
        setDead_Num(0);
        setFailure_Num(0);
        setAttend_Num(0);

        //건강
        setIsHealth(false);
        setIsStopHealth(false);
        setHealth(100);
        SaveNLoad.addP(HNO,SleepTime,"0/0");
        setTired(0);

        //자산
        setTopProperty(0);
        setEconomy_Alarm(false);
        setMoney(10000);
        SugoData.addMoney(-10000);
        setTax(0);
        setAuto_TAx(false);
        setDebt(0);
        setDebt_Rate(true);
        setExp(0);
        setItem_NUm(Item_ID.Tree,0);

        //차트
        SaveNLoad.makeFolder(Charts);
        SaveNLoad.makeFolder(ChartsImages);

        //스탯
        setLuck(0);
        setStealing(0);
        setDefense(0);
        setResilience(0);

        setStock(Stock_Data.list().get(0),0,BigInteger.ZERO);

    }

    public String getPath(){
        return path;
    }

    public BigInteger getAll_Property(){
        BigInteger ap = getMoney().add(getExp().divide(new BigInteger(String.valueOf(getAM_Exp_Change())))).subtract(getTax().add(getDebt()));
        BigInteger stock = getStock_All_Money();
        //System.out.println(getName()+" "+stock);
        return ap.add(stock);
    }

    public BigInteger getStock_All_Money(){
        BigInteger stock = BigInteger.ZERO;
        for(String id:Stock_Data.list()){
            Stock_Data stock_data = new Stock_Data(id);
            //System.out.println(getStock_Num(id)+userid);
            BigDecimal bd = new BigDecimal(String.valueOf(getStock_Num(id)));
            stock = stock.add(stock_data.getPrice().multiply(bd.toBigInteger()));
        }
        return stock;
    }

    public BigDecimal getAM_Luck(){
        ArrayList<BigDecimal> exp_list = new ArrayList<>();
        for(String id:getUserList()){
            UserDB userDB = new UserDB(id);
            exp_list.add(new BigDecimal(String.valueOf(userDB.getLuck())));
        }
        return Support.getAM_Num(exp_list.toArray(new BigDecimal[0]));
    }

    public BigDecimal getAM_Exp_Change(){
        int amExp = getAM_Luck().intValue();
        int i = 5*(10+amExp)+10*(24+amExp)+15*(10+amExp)+20*3;
        i = i/(50+amExp*3);
        return new BigDecimal(String.valueOf(i));
    }

    public String getName(){
        return SaveNLoad.loadP(HNO,Name);
    }

    public void setName(String name){
        SaveNLoad.addP(HNO,Name,name);
    }

    public School_Data getSchool(){
        String s = SaveNLoad.loadP(HNO,School);
        if(s == null) return null;
        return new School_Data((String) Support.toObject(s));
    }

    public void setSchool(School_Data school){
        SaveNLoad.addP(HNO,School,Support.toSerialize(school.toString()));
    }

    public int getDead_Num(){
        return Integer.parseInt(SaveNLoad.loadP(HNO,Dead_Num));
    }

    public void setDead_Num(int i){
        SaveNLoad.addP(HNO,Dead_Num,String.valueOf(i));
    }

    public void addDead_Num(int i){
        int ii = getDead_Num()+i;
        setDead_Num(ii);
    }

    public int getFailure_Num(){
        return Integer.parseInt(SaveNLoad.loadP(HNO,Failure_Num));
    }

    public void setFailure_Num(int i){
        SaveNLoad.addP(HNO,Failure_Num,String.valueOf(i));
    }

    public void addFailure_NUm(int i){
        int ii = getFailure_Num()+i;
        SaveNLoad.addP(HNO,Failure_Num,String.valueOf(ii));
    }

    public int getAttend_Num(){
        return Integer.parseInt(SaveNLoad.loadP(HNO,Check_Num));
    }

    public void setAttend_Num(int i){
        SaveNLoad.addP(HNO,Check_Num,String.valueOf(i));
    }

    public void addAttend_Num(int i){
        int ii = getAttend_Num()+i;
        setAttend_Num(ii);
    }

    public boolean isHealth(){
        return Boolean.parseBoolean(SaveNLoad.loadP(HNO,isHealth));
    }

    public void setIsHealth(Boolean b){
        SaveNLoad.addP(HNO,isHealth,String.valueOf(b));
    }

    public boolean getIsStopHealth(){
        return Boolean.parseBoolean(SaveNLoad.loadP(HNO,isStopHealth));
    }

    public void setIsStopHealth(Boolean b){
        SaveNLoad.addP(HNO,isStopHealth,String.valueOf(b));
    }

    public long getHealth(){
        return Long.parseLong(SaveNLoad.loadP(HNO,Health));
    }

    public void setHealth(long l){
        SaveNLoad.addP(HNO,Health,String.valueOf(l));
    }

    public void addHealth(long l){
        long ll = getHealth()+l;
        setHealth(ll);
    }

    public boolean isSleeping(){
        return getAll_SleepTime() > 0;
    }

    public long getSleepTime(){
        String slt = SaveNLoad.loadP(HNO,SleepTime);
        String[] list = slt.split("/");
        return Long.parseLong(list[0]);
    }

    public void setSleepTime(long l){
        SaveNLoad.addP(HNO,SleepTime,l+"/"+getAll_SleepTime());
    }

    public void addSleepTime(long l){
        long ll = getSleepTime()+l;
        setSleepTime(ll);
    }

    public long getAll_SleepTime(){
        String slt = SaveNLoad.loadP(HNO,SleepTime);
        String[] list = slt.split("/");
        return Long.parseLong(list[1]);
    }

    public void setAll_SleepTime(long l){
        SaveNLoad.addP(HNO,SleepTime,getSleepTime()+"/"+l);
    }

    public void addAll_SleepTime(long l){
        long ll = getAll_SleepTime()+l;
        setAll_SleepTime(ll);
    }

    public int getTired(){
        return Integer.parseInt(SaveNLoad.loadP(HNO,Tired));
    }

    public void setTired(int i){
        SaveNLoad.addP(HNO,Tired,String.valueOf(i));
    }

    public void addTired(int i){
        int ii = getTired()+i;
        setTired(ii);
    }

    public BigInteger getTopProperty(){
        return new BigInteger(SaveNLoad.loadP(Property,TopProperty));
    }

    public void setTopProperty(BigInteger bInt){
        SaveNLoad.addP(Property,TopProperty,bInt.toString());
    }

    public void setTopProperty(int i){
        setTopProperty(new BigInteger(String.valueOf(i)));
    }

    public void setTopProperty(Long l){
        setTopProperty(new BigInteger(String.valueOf(l)));
    }

    public void setTopProperty(String s){
        setTopProperty(new BigInteger(s));
    }

    public void addTopProperty(BigInteger bInt){
        BigInteger bi = getTopProperty().add(bInt);
        setTopProperty(bi);
    }

    public void addTopProperty(int i){
        addTopProperty(new BigInteger(String.valueOf(i)));
    }

    public void addTopProperty(String s){
        addTopProperty(new BigInteger(s));
    }

    public void addTopProperty(long l){
        addTopProperty(new BigInteger(String.valueOf(l)));
    }

    public BigInteger getTopSP(){
        String s = SaveNLoad.loadP(Property,TopSP);
        if(s == null){
            return BigInteger.ZERO;
        }
        return new BigInteger(s);
    }

    public void setTopSP(BigInteger bInt){
        SaveNLoad.addP(Property,TopSP,bInt.toString());
    }

    public boolean getEconomy_Alarm(){
        return Boolean.parseBoolean(SaveNLoad.loadP(Property,Economy_Alarm));
    }

    public void setEconomy_Alarm(Boolean b){
        SaveNLoad.addP(Property,Economy_Alarm,String.valueOf(b));
    }

    public BigInteger getMoney(){
        return new BigInteger(SaveNLoad.loadP(Property,Money));
    }

    public void setMoney(BigInteger bInt){
        if(bInt.min(BigInteger.ZERO).equals(bInt)){
            addDebt(bInt.multiply(new BigInteger("-1")));
            SaveNLoad.addP(Property,Money,BigInteger.ZERO.toString());
        }
        else {
            SaveNLoad.addP(Property,Money,bInt.toString());
        }
    }

    public void setMoney(int i){
        setMoney(new BigInteger(String.valueOf(i)));
    }

    public void setMoney(Long l){
        setMoney(new BigInteger(String.valueOf(l)));
    }

    public void setMoney(String s){
        setMoney(new BigInteger(s));
    }

    public void addMoney(BigInteger bInt){
        BigInteger bi = getMoney().add(bInt);
        setMoney(bi);
    }

    public void addMoney(int i){
        addMoney(new BigInteger(String.valueOf(i)));
    }

    public void addMoney(String s){
        addMoney(new BigInteger(s));
    }

    public void addMoney(long l){
        addMoney(new BigInteger(String.valueOf(l)));
    }

    public BigInteger getTax(){
        return new BigInteger(SaveNLoad.loadP(Property,Tax));
    }

    public void setTax(BigInteger bInt){
        SaveNLoad.addP(Property,Tax,bInt.toString());
    }

    public void setTax(int i){
        setTax(new BigInteger(String.valueOf(i)));
    }

    public void setTax(Long l){
        setTax(new BigInteger(String.valueOf(l)));
    }

    public void setTax(String s){
        setTax(new BigInteger(s));
    }

    public void addTax(BigInteger bInt){
        BigInteger bi = getTax().add(bInt);
        setTax(bi);
    }

    public void addTax(int i){
        addTax(new BigInteger(String.valueOf(i)));
    }

    public void addTax(String s){
        addTax(new BigInteger(s));
    }

    public void addTax(long l){
        addTax(new BigInteger(String.valueOf(l)));
    }

    public boolean getAuto_Tax(){
        return Boolean.parseBoolean(SaveNLoad.loadP(Property,Auto_Tax));
    }

    public void setAuto_TAx(Boolean b){
        SaveNLoad.addP(Property,Auto_Tax,String.valueOf(b));
    }

    public BigInteger getDebt(){
        try {
            return new BigInteger(SaveNLoad.loadP(Property,Debt));
        }catch (Exception e){
            return getDebt();
        }

    }

    public void setDebt(BigInteger bInt){
        SaveNLoad.addP(Property,Debt,bInt.toString());
    }

    public void setDebt(int i){
        setDebt(new BigInteger(String.valueOf(i)));
    }

    public void setDebt(Long l){
        setDebt(new BigInteger(String.valueOf(l)));
    }

    public void setDebt(String s){
        setDebt(new BigInteger(s));
    }

    public void addDebt(BigInteger bInt){
        BigInteger bi = getDebt().add(bInt);
        setDebt(bi);
    }

    public void addDebt(int i){
        addDebt(new BigInteger(String.valueOf(i)));
    }

    public void addDebt(String s){
        addDebt(new BigInteger(s));
    }

    public void addDebt(long l){
        addDebt(new BigInteger(String.valueOf(l)));
    }

    public float getDebt_Rate(){
        String rate = SaveNLoad.loadP(Property,Debt_Rate);
        if(rate.equals("Debt_Flow")){
            return 0.1f; // 나중에 수정
        }
        else {
            return Float.parseFloat(rate);
        }
    }

    public void setDebt_Rate(boolean isFlow){
        if(isFlow){
            SaveNLoad.addP(Property,Debt_Rate,"Debt_Flow");
        }
        else{
            SaveNLoad.addP(Property,Debt_Rate,"현재금리가져오기");
        }
    }

    public BigInteger getExp(){
        return new BigInteger(SaveNLoad.loadP(Property,Exp));
    }

    public void setExp(BigInteger bInt){
        SaveNLoad.addP(Property,Exp,bInt.toString());
    }

    public void setExp(int i){
        setExp(new BigInteger(String.valueOf(i)));
    }

    public void setExp(Long l){
        setExp(new BigInteger(String.valueOf(l)));
    }

    public void setExp(String s){
        setExp(new BigInteger(s));
    }

    public void addExp(BigInteger bInt){
        BigInteger bi = getExp().add(bInt);
        setExp(bi);
    }

    public void addExp(int i){
        addExp(new BigInteger(String.valueOf(i)));
    }

    public void addExp(String s){
        addExp(new BigInteger(s));
    }

    public void addExp(long l){
        addExp(new BigInteger(String.valueOf(l)));
    }

    public int getLuck(){
        return Integer.parseInt(SaveNLoad.loadP(Stat,Luck));
    }

    public void setLuck(int i){
        SaveNLoad.addP(Stat,Luck,String.valueOf(i));
    }

    public void addLuck(int i){
        int ii = getLuck()+i;
        setLuck(ii);
    }

    public int getStealing(){
        return Integer.parseInt(SaveNLoad.loadP(Stat,Stealing));
    }

    public void setStealing(int i){
        SaveNLoad.addP(Stat,Stealing,String.valueOf(i));
    }

    public void addStealing(int i){
        int ii = getStealing()+i;
        setStealing(ii);
    }

    public int getDefense(){
        return Integer.parseInt(SaveNLoad.loadP(Stat,Defense));
    }

    public void setDefense(int i){
        SaveNLoad.addP(Stat,Defense,String.valueOf(i));
    }

    public void addDefense(int i){
        int ii = getDefense()+i;
        setDefense(ii);
    }

    public int getResilience(){
        return Integer.parseInt(SaveNLoad.loadP(Stat,Resilience));
    }

    public void setResilience(int i){
        SaveNLoad.addP(Stat,Resilience,String.valueOf(i));
    }

    public void addResilience(int i){
        int ii = getResilience()+i;
        setResilience(ii);
    }

    public long getItem_NUm(Item item){
        String sn = SaveNLoad.loadP(Items,item.English_Name);
        sn = sn.equalsIgnoreCase("null") ? "0":sn;
        return Long.parseLong(sn);
    }

    public void setItem_NUm(Item item,long l){
        SaveNLoad.addP(Items,item.English_Name,String.valueOf(l));
    }

    public void addItem_Num(Item item,long l){
        long ll = getItem_NUm(item)+l;
        setItem_NUm(item,ll);
    }

    public Item[] getItem_List(){
        Set<Object> list1 = new HashSet<>();

        Properties prop = new Properties();
        FileInputStream fis;
        InputStreamReader isr;
        String result = "";
        try {
            fis = new FileInputStream(Items);
            isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            prop.load(isr);
            list1 = prop.keySet();
            fis.close(); isr.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        ArrayList<Item> list = new ArrayList<>();
        for (Object o:list1){
            if(getItem_NUm((Item.find_Item((String) o))) != 0){
                list.add((Item.find_Item((String) o)));
            }
        }

        return list.toArray(new Item[0]);
    }

    public double getStock_Num(String id){
        return Double.parseDouble(getStock(id)[0]);
    }

    public void setStock_Num(String id,double num){
        setStock(id,num,getStock_Price(id));
    }

    public void addStock_Num(String id,double num){
        setStock_Num(id,getStock_Num(id)+num);
    }

    public BigInteger getStock_Price(String id){
        return new BigInteger(getStock(id)[1]);
    }

    public void setStock_Price(String id,BigInteger price){
        setStock(id,getStock_Num(id),price);
    }

    private String[] getStock(String id){
         String inform = SaveNLoad.loadP(Stock,id);
        if(inform == null) SaveNLoad.addP(Stock,id,"0.0/0");
         return SaveNLoad.loadP(Stock,id).split("/");
    }

    public void setStock(String id,double num,BigInteger Price){
        String txt = num+"/"+Price;
        SaveNLoad.addP(Stock,id,txt);
    }

    public void update_Chart(){
        int second = 1000;
        int min = 60*second;

        String cp = path+"/"+Chart_Path+"/";

        Chart chart_H = new Chart(cp+Chart_H);
        Chart chart_P = new Chart(cp+Chart_P);
        Chart chart_SP = new Chart(cp+Chart_SP);

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
                //System.out.println(userid);
                TopUpdate();
                TopSPUpdate();
                chart_H.add(String.valueOf(getHealth()));
                chart_P.add(getAll_Property().toString());
                chart_SP.add(getAll_Property().divide(SugoData.getSP()).toString());
                System.out.println("수고차트 업데이트 완료");
            }
        },date,15*min);


    }

    public void Debt_update(UserDB userDB){

        int second = 1000;
        int min = 60*second;
        long hour = 60*min;

        Date date = new Date();
        if(LocalTime.now().isAfter(LocalTime.of(10,0))) date.setDate(date.getDate()+1);
        date.setHours(10);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                BigDecimal bd = new BigDecimal(userDB.getDebt());
                BigDecimal addP = new BigDecimal("0.005");
                BigInteger addPrice = bd.multiply(addP).toBigInteger();
                userDB.addDebt(addPrice);
            }
        },date,24*hour);
    }

    public static void Tax_Update(){

        int second = 1000;
        int min = 60*second;
        long hour = 60*min;

        Date date = new Date();
        date.setHours(10);
        if(LocalTime.now().isAfter(LocalTime.of(10,0))) date.setDate(date.getDate()+1);
        System.out.println(date);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int now = LocalDate.now().getDayOfMonth();
                if(now == 1|| now == 15){
                    SugoData.charge_Tax();
                }
            }
        },date,24*hour);
    }

    public void TopUpdate(){
        if(getAll_Property().max(getTopProperty()).equals(getAll_Property())){
            setTopProperty(getAll_Property());
        }
    }

    public void TopSPUpdate(){
        BigInteger sp = getAll_Property().divide(SugoData.getSP());
        //System.out.println(sp+" "+getTopSP()+" "+sp.max(getTopSP()).equals(sp));
        if(sp.max(getTopSP()).equals(sp)){
            setTopSP(sp);
        }
    }

    public void overwork(JDA jda){
        if(getHealth() < -2500){
            if(getAll_Property().min(BigInteger.ZERO).equals(BigInteger.ZERO)){
                SugoData.addMoney(getAll_Property());
            }
            setMoney("0");
            for (String id:Stock_Data.list()){
                setStock(id,0,new BigInteger("0"));
            }
            setDebt(0);
            setExp(0);
            setHealth(0);
            setTired(0);
            jda.retrieveUserById(userid).flatMap(User::openPrivateChannel).queue(privateChannel ->
                    privateChannel.sendMessage("과로사 하였습니다.").queue());
        }
    }

    public int get_AP_Rank(){
        ArrayList<BigInteger> list = new ArrayList<>();
        for(String id:UserDB.getUserList()){
            UserDB userDB = new UserDB(id);
            list.add(userDB.getAll_Property());
        }
        Collections.sort(list);
        return getUserList().length-(list.indexOf(getAll_Property()));
    }


}
