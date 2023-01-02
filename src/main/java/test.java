import Supporter.SaveNLoad;
import Supporter.UserDB;

import java.util.HashMap;

public class test {

    public static void main(String[] args) {
        UserDB userDB = new UserDB("336828850358976512");
        UserDB userDB1 = new UserDB("353350451607109633");
        System.out.println(userDB.getName()+" "+userDB.getPath());
        System.out.println(userDB1.getName()+" "+userDB1.getPath());
    }
}
