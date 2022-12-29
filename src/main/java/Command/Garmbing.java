package Command;

import Supporter.UserDB;

import java.math.BigInteger;

public class Garmbing {

    private static BigInteger Common(UserDB userDB, BigInteger bi){

        double r = Math.random()*100;
        double b = 0;
        double H = 0;

        if( r < (H += 0.0001)) b = 100000000;
        else if( r < (H += 5)) b = 0;
        else if( r < (H += 18)) b = 0.5;
        else if( r < (H += 19.9999)) b = 1;
        else if( r < (H += 37)) b = 1.2;
        else if( r < (H += 15)) b = 1.5;
        else if( r < (H += 5)) b = 2;

        return null;
    }

}
