package Change_File;

import Supporter.SaveNLoad;
import Supporter.Stock_Data;
import Supporter.UserDB;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;


public class main {

    public static void main(String[] args) {
        make_User();
        set_User();
        stock();
    }

    static public void make_User(){
        for(String id:UDS.getUserList()){
            UserDB userDB = new UserDB(id);
            userDB.makeUser();
        }
    }

    static public void set_User(){
        for(String id:UDS.getUserList()){
            System.out.println(id);
            UserDB userDB = new UserDB(id);
            BigDecimal bdm = new BigDecimal(String.valueOf(UDS.getMoney(id)));
            BigDecimal bde = new BigDecimal(String.valueOf(UDS.getExp(id)*10));
            long h = Long.parseLong(String.valueOf(UDS.getHealth(id)).replace(".0",""));
            userDB.setHealth(h);
            userDB.setMoney(bdm.toBigInteger());
            userDB.setName(UDS.getName(id));
            userDB.addMoney(bde.toBigInteger());
            try {
                set_Stock_user(id);
                System.out.println(id);
            }catch (Exception e){
                System.out.println(id+" x");
            }
        }
    }

    static public void set_Stock_user(String userid){
        String[] list = {"BC","JH","IH","HM"};
        UserDB userDB = new UserDB(userid);
        for (String jid:list){
            userDB.setStock(jid,getJSNum(jid,userid),getJSAM(jid,userid));
        }
    }

    public static double getJSNum(String jsId,String userid){
        String path = "user/"+userid+".txt";
        String[] ss = SaveNLoad.loadP(path,jsId).split("/");
        return Double.parseDouble(ss[0]);

    }

    public static BigInteger getJSAM(String jsId,String userid){
        String path = "user/"+userid+".txt";
        String[] ss = SaveNLoad.loadP(path,jsId).split("/");
        return new BigInteger(ss[1]);
    }

    static public void stock(){
        String[] list = {"BC","JH","IH","HM"};
        String[] bc = "(주)BellCircle,1500000,2180,20".split(",");
        String[] jh = "(주)중현공업,2000000,1000,10".split(",");
        String[] ih = "(주)IsolatedHun,2500000,2000,3".split(",");
        String[] hm = "(주)효민휴턴,1200000,5000,30".split(",");
        String[] inform = new String[4];
        for(String id:list){
            switch (id){
                case "BC":
                    inform = bc;
                    break;
                case "JH":
                    inform = jh;
                    break;
                case "IH":
                    inform = ih;
                    break;
                case "HM":
                    inform = hm;
                    break;
            }
            Stock_Data stock_data = new Stock_Data(id);
            stock_data.setName(inform[0]);
            stock_data.setStart_Num(new BigInteger(inform[1]).multiply(BigInteger.TEN));
            BigInteger sap = new BigInteger(inform[2]);
            stock_data.setStart_All_Price(sap.multiply(stock_data.getStart_Num()).multiply(BigInteger.TEN));
            System.out.println(id+" "+stock_data.getPrice()+" "+stock_data.getALl_Num());
            stock_data.setTimes(0,Integer.parseInt(inform[3]));
            stock_data.setSelling_Num(sellingNum(id));
            stock_data.setPrice(nowPrice(id));
            stock_data.setLast_Price(nowPrice(id));
        }

    }
    static String path = "inform/";
    public static BigInteger sellingNum(String id){
        String s = path+id+".txt";
        BigInteger an = new BigInteger(SaveNLoad.loadP(s,"StartNum"));
        BigInteger nn = new BigInteger(SaveNLoad.loadP(s,"Num"));
        return an.subtract(nn);
    }

    public static BigInteger nowPrice(String id){
        String s = path+id+".txt";
        BigInteger an = new BigInteger(SaveNLoad.loadP(s,"Money"));;
        return an;
    }


}
