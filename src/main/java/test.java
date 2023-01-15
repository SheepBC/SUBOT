import SugoData.SugoData;
import Supporter.SaveNLoad;
import Supporter.Stock_Data;
import Supporter.Support;
import Supporter.UserDB;

import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
//5375665143525415
public class test {

    public static void main(String[] args) {
        double d = 1000000000000d;
        //d *= 5000;
        System.out.println(Support.unit(d));
        System.out.println(Support.unit(new BigDecimal(String.valueOf(d)).divide(new BigDecimal(SugoData.getSP()),0,RoundingMode.HALF_EVEN)));
        for (String id:UserDB.getUserList()){
            UserDB userDB = new UserDB(id);
            System.out.println(userDB.getName()+" "+Support.unit(userDB.getAll_Property().divide(SugoData.getSP())));
        }
    }
}
