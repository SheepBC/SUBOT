import SugoData.SugoData;
import Supporter.SaveNLoad;
import Supporter.Stock_Data;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//5375665143525415
public class test {

    public static void main(String[] args) {
        UserDB userDB = new UserDB("336828850358976512");
        SugoData.getAttend_Price(userDB);
    }
}
