package Change_File;

import Supporter.Stock_Data;
import Supporter.UserDB;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;


public class main {

    public static void main(String[] args) {
        //make_User();
        //set_User();
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
            System.out.println(id);
        }
    }

    static public void stock(){
        /*
        for(String id:UDS.getUserList()){
            UserDB userDB = new UserDB(id);
            try {
                String path = "user/"+id;
                String[] list = {"BC","JH","IH","HM"};
                for (String Id:list){
                    userDB.setStock(Id,getJSNum(path,Id),new BigInteger(String.valueOf(getJSAM(path,Id))));
                }
            }catch (Exception e){
                System.out.println("dsad");
            }
        }

         */
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
            stock_data.setStart_Num(new BigInteger(inform[1]));
            BigInteger sap = new BigInteger(inform[2]);
            stock_data.setStart_All_Price(sap.multiply(stock_data.getStart_Num()));
            System.out.println(id+" "+stock_data.getPrice()+" "+stock_data.getALl_Num());
            stock_data.setTimes(0,Integer.parseInt(inform[3]));
        }

    }

    public static int getJSNum(String path,String jsId){
        try {
            String[] s = YDS.Loadp(path,jsId).split("/");
            return Integer.parseInt(s[0]);
        }catch (Exception e){
            return 0;
        }
    }

    public static long getJSAM(String path,String jsId){
        try {
            String[] s = YDS.Loadp(path,jsId).split("/");
            return Long.parseLong(s[1]);
        }catch (Exception e){
            return 0;
        }
    }

}
