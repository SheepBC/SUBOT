import SugoData.SugoData;
import Supporter.SaveNLoad;
import Supporter.Stock_Data;
import Supporter.UserDB;

import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.util.HashMap;
//5375665143525415
public class test {

    public static void main(String[] args) {
        for (String id: Stock_Data.list()){
            Stock_Data stock_data = new Stock_Data(id);
            System.out.println(id);
            stock_data.getAll_Price();
        }

    }
}
