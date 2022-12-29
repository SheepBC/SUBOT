import Chart.Chart;
import Chart.Chart_Data;
import Chart.Make_Chart;
import Supporter.SaveNLoad;
import Supporter.Stock_Data;
import Supporter.Support;

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
        String s1 = "2022/12/03";
        String s2 = "2019/12/31";

        String[] list = {s1, s2};
        Arrays.sort(list);
        System.out.println(list[0]);
    }
}
