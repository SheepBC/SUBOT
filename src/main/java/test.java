import Chart.Chart;
import Chart.Chart_Data;
import Chart.Make_Chart;
import SugoData.SugoData;
import Supporter.SaveNLoad;
import Supporter.Stock_Data;
import Supporter.Support;
import Supporter.UserDB;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class test {

    public static void main(String[] args) {
        Chart chart = new Chart("UserDB/353350451607109633/Charts/Chart_P");
        ArrayList<Chart_Data> list = chart.getChart_Data_list("2022/12/23","2023/12/31");
        System.out.println(chart.getPath());
        for(Chart_Data cd :list){
            System.out.println(cd.time+" "+cd.value);
        }

        Make_Chart.makeLine_Chart("1.png","t/t/t","D","122:122:1", list);
    }
}
