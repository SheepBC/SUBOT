package SugoData;

import Chart.Chart;
import Main.main;
import Supporter.SaveNLoad;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class SugoData {

    public static String DataPath = "Sub/Data.txt";
    public static String ChartPath = "Sub/Charts/";
    public static String AttendPath = "Sub/Attend.txt";
    public static String SleepPath = "Sub/Sleep.txt";
    public static String sp_m = "SP_Price";
    public static String sg_m = "SG_Money";
    public static String sg_sp = "SG_SP";
    public static String am_h = "AM_Health";
    public static String version = "version";
    public static String money = "money";
    public static String tax = "tax";
    public static String last_SP = "last_SP";
    public static String now_SP = "now_SP";

    public static String getAttend(String id){
        String s = SaveNLoad.loadP(AttendPath,id);
        if(s == null) return "null";
        return s;
    }

    public static void setAttend(String id){
        SaveNLoad.addP(AttendPath,id, LocalDate.now().toString());
    }

    public static String getSleep(String id){
        String s = SaveNLoad.loadP(AttendPath,id);
        if(s == null) return "null";
        return s;
    }

    public static void setSleep(String id){
        SaveNLoad.addP(AttendPath,id, LocalDate.now().toString());
    }

    public static BigInteger getLast_SP(){
        try{
            return new  BigInteger(SaveNLoad.loadP(DataPath,last_SP));
        }catch (Exception e){
            return BigInteger.ZERO;
        }
    }
    public static void setLast_SP(BigInteger bInt){
        SaveNLoad.addP(DataPath,last_SP,bInt.toString());
    }

    public static BigInteger getSP(){
        try{
            return new  BigInteger(SaveNLoad.loadP(DataPath,now_SP));
        }catch (Exception e){
            return new BigInteger("33000");
        }
    }
    public static void setSP(){
        SaveNLoad.addP(DataPath,now_SP,getNow_SP().toString());
    }

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

    public static double getTax(){
        return Double.parseDouble(SaveNLoad.loadP(DataPath,tax));
    }

    public static void setTax(double d){
        int end = Math.min(String.valueOf(d).length(), 6);
        String s = String.valueOf(d).substring(0,end);
        SaveNLoad.addP(DataPath,tax,s);
    }

    public static void addTax(double d){
        double tax = getTax()+d;
        if(tax < 0) setTax(0);
        if(tax >100) setTax(100);
        else setTax(tax);
    }

    private static BigInteger getNow_SP(){
        ArrayList<BigDecimal> list = new ArrayList<>();
        for(String id: UserDB.getUserList()){
            UserDB userDB = new UserDB(id);
            BigDecimal p =new BigDecimal(userDB.getAll_Property());
            list.add(p);
        }
        BigDecimal am = Support.getAM_Num(list.toArray(new BigDecimal[0]));
        BigInteger sp = am.toBigInteger().divide(new BigInteger("20000")).add(new BigInteger("3300"));
        if(sp.min(new BigInteger("1000")).equals(sp)) sp = new BigInteger("1000");
        return sp;
    }

    public static void Update_Chart_SP(){
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
                setSP();
                System.out.println("수고차트 업데이트 완료");
            }
        },date,15*min);
    }

    public static double getMRP(){
        BigDecimal sp = new BigDecimal(getSP());
        BigDecimal spm = new BigDecimal(getLast_SP());
        boolean bool = sp.min(spm.add(new BigDecimal("0.00000000000001"))).equals(sp);
        int b = bool ? 1:-1;
        BigDecimal mrp = sp.divide(spm, 2,RoundingMode.HALF_EVEN );
        String size = "8";
        if(bool){
            mrp = spm.divide(sp.add(new BigDecimal("0.00000000000001")),2,RoundingMode.HALF_EVEN);
            size = "7";
        }
        mrp = mrp.subtract(BigDecimal.ONE).abs(MathContext.DECIMAL32);
        mrp = mrp.divide(mrp.add(new BigDecimal(size)),3,RoundingMode.HALF_EVEN);
        return mrp.doubleValue()*b;
    }

    public static BigInteger getTax_Price(String userid){
        String mo = "2500";
        UserDB userDB = new UserDB(userid);
        BigDecimal price = new BigDecimal(userDB.getAll_Property());
        price = price.divide(new BigDecimal("1200000000"),10,RoundingMode.HALF_EVEN);
        BigDecimal tax = new BigDecimal(String.valueOf(getTax()));
        BigDecimal max = new BigDecimal("25.5");
        BigDecimal re = max.subtract(tax)
                .multiply(price)
                .divide(price.add(new BigDecimal(mo)),10,RoundingMode.HALF_EVEN)
                .add(tax)
                .divide(new BigDecimal("100"),10,RoundingMode.HALF_EVEN)
                .multiply(new BigDecimal(userDB.getAll_Property()));
        if(re.min(BigDecimal.ZERO).equals(re)) return BigInteger.ZERO;
        return re.toBigInteger();

    }

    public static void charge_Tax(){
        //System.out.println("----------------------------------------------");
        setLast_SP(getSP());
        for(String userid:UserDB.getUserList()){
            UserDB userDB = new UserDB(userid);
            BigDecimal tax = new BigDecimal(SugoData.getTax_Price(userid));
            BigDecimal price = new BigDecimal(userDB.getAll_Property());
            BigDecimal p = tax.divide(price,4, RoundingMode.HALF_EVEN).multiply(new BigDecimal("100"));
            //System.out.println(userDB.getName()+" "+Support.unit(tax)+" "+p+"%");
            userDB.addMoney(tax.toBigInteger().multiply(new BigInteger("-1")));
            addMoney(tax.toBigInteger());
            EmbedBuilder embed = new EmbedBuilder();
            //embed.setTitle("세금");
            embed.setColor(Support.getRandomColor());
            embed.addField("세금",Support.unit(tax)+"베리("+p.setScale(2)+"%)",true);
            //System.out.println(p+" "+p.setScale(2));
            try {
                main.jda.retrieveUserById(userid).flatMap(User::openPrivateChannel)
                        .queue(privateChannel -> privateChannel.sendMessageEmbeds(embed.build()).queue());
                //System.out.println(embed.getFields().get(0).getValue()+" "+userid);
            }catch (Exception e){

            }
        }
        double size = -13.23;
        if(getMRP()<0) size = -2.3;
        addTax(getMRP()*size);
        main.alarm("기본 세율 변경 "+getTax()+"%");
    }

    public static BigInteger getAttend_Price(UserDB userDB){
        BigDecimal m = getAttend_Num(userDB);
        int d = 300*UserDB.getUserList().length;
        BigDecimal price = new BigDecimal(getMoney()).divide(new BigDecimal(String.valueOf(d)),10,RoundingMode.HALF_EVEN);
        //System.out.println("1:"+m);
        price = price.multiply(m);
        String s = "0.45";
        if(getUp(userDB)) s = "1.3";
        BigInteger maxI = userDB.getTopSP().multiply(SugoData.getSP()).multiply(new BigInteger("4"))
                .add(userDB.getTopProperty())
                .add(userDB.getAll_Property())
                .divide(new BigInteger("6"));
        BigDecimal max = new BigDecimal(maxI)
                .multiply(new BigDecimal(s));
        if(max.min(BigDecimal.ZERO).equals(max)){
            max = new BigDecimal(getSP().multiply(BigInteger.TWO));
        }
        if(price.min(BigDecimal.ZERO).equals(price)){
            price = new BigDecimal(getSP().multiply(BigInteger.TWO));
        }
        //System.out.println("2:"+Support.unit(price)+" "+Support.unit(max));
        if(max.min(price).equals(max)) return max.toBigInteger();
        if(getUp(userDB)) price = price.multiply(new BigDecimal("5"));
        return price.toBigInteger();
    }

    public static BigInteger getAttend_Price_UP(UserDB userDB){
        BigDecimal m = getAttend_Num(userDB);
        int d = 300*UserDB.getUserList().length;
        BigDecimal price = new BigDecimal(getMoney()).divide(new BigDecimal(String.valueOf(d)),10,RoundingMode.HALF_EVEN);
        //System.out.println(Support.unit(price));
        price = price.multiply(m);
        //System.out.println(Support.unit(price));
        String s = "0.8";
        BigDecimal max = new BigDecimal(userDB.getTopSP()).multiply(new BigDecimal(getSP())).multiply(new BigDecimal(s));
        if(max.min(price).equals(max)) return max.toBigInteger();
        return price.toBigInteger();
    }

    public static boolean getUp(UserDB userDB){
        BigInteger i = SugoData.getTax_Price(userDB.userid);
        BigInteger a = SugoData.getAttend_Price_UP(userDB);
        BigInteger s = i.subtract(a.multiply(new BigInteger("15")));
        return s.max(new BigInteger("250000000").multiply(getSP())).equals(s);
    }

    public static BigDecimal getAttend_Num(UserDB userDB){
        BigDecimal an = new BigDecimal(String.valueOf(userDB.getAttend_Num()));
        BigDecimal m = new BigDecimal(String.valueOf(userDB.get_AP_Rank()));
        BigDecimal A = new BigDecimal(String.valueOf(UserDB.getUserList().length));
        BigDecimal f = m.divide(A,10,RoundingMode.HALF_EVEN);
        f = f.sqrt(MathContext.DECIMAL32);
        f = f.sqrt(MathContext.DECIMAL32);
        f = f.pow(3);
        an =an.sqrt(MathContext.DECIMAL32);
        an = an.sqrt(MathContext.DECIMAL32);
        an = an.pow(3);
        BigDecimal t = an.divide(new BigDecimal("25"),10,RoundingMode.HALF_EVEN).add(BigDecimal.ONE);
        return f.add(t).add(BigDecimal.valueOf(getMRP()*3.5));
    }
}
