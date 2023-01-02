package Change_File;

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
            System.out.println(id);
        }
    }

    static public void stock(){
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
