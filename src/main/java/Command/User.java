package Command;

import Chart.Chart;
import Chart.Chart_Data;
import Main.SelectButton;
import SugoData.SugoData;
import Supporter.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static Chart.Make_Chart.makeLine_Chart;

public class User {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public User(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public User(MessageChannel channel){
        this.channel = channel;
    }

    //------------------------------------------

    public void addExp(){
        if(!UserDB.isUser(userid)) return;
        UserDB userDB = new UserDB(user);
        String[] sList = event.getMessage().getContentRaw().split("");
        Set<String> list = new HashSet<>(List.of(sList));
        int exp = (int) Math.sqrt(list.size()*50);
        userDB.addExp(new BigInteger(String.valueOf(exp)));
    }

    public void addTired(){
        if(!UserDB.isUser(userid)) return;
        UserDB userDB = new UserDB(user);
        int tired = (int) Math.sqrt(event.getMessage().getContentRaw().length()*8.8);
        userDB.addTired(tired);

        if(userDB.getTired() >= 100){
            userDB.addHealth(-1*userDB.getTired()/100);
            userDB.setTired(userDB.getTired()%100);
        }
    }

    //------------------------------------------
    public void join(){
        if(UserDB.isUser(userid)){
            channel.sendMessage("이미 회원가입한 사람입니다.").queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        userDB.makeUser();
        channel.sendMessage("회원가입이 완료되었습니다.").queue();
    }

    public void inform(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        EmbedBuilder embed = new EmbedBuilder();
        UserDB userDB = new UserDB(user);

        embed.setColor(Support.getRandomColor());
        embed.setTitle(userDB.getName()+"님의 정보");
        embed.addField("**이름**",userDB.getName(),true);
        //embed.addField("**학교**",userDB.getSchool().equals("지정 안됨") ? "지정 안됨":"지정 됨",false);
        BigInteger sp = userDB.getAll_Property().divide(SugoData.getSP());
        embed.addField("**재산**",
                "총자산: "+Support.unit(userDB.getAll_Property())+"\n"+
                "SP: "+Support.unit(sp)+"SP",false);

        String sleep = "\n잠: "+Support.time_Unit(userDB.getSleepTime())+"/"+Support.time_Unit(userDB.getAll_SleepTime());

        embed.addField("**건강**",
                "건강: "+Support.unit(userDB.getHealth())
                        +"\n피로도: "+userDB.getTired()
                        +(userDB.isHealth() ? "\n회복중":"")
                        +(userDB.isSleeping() ? sleep:"")
                ,false);
        /*
        embed.addField("**스탯**",
                /*
                "도둑질: "+userDB.getStealing()+"강"
                        +"\n방어력: "+userDB.getDefense()+"강"


                        "\n행운: "+userDB.getLuck()+"강"
                        +"\n회복력: "+userDB.getResilience()+"강"
                ,true);

         */

        channel.sendMessageEmbeds(embed.build()).queue();
    }
    /*
    public void change(){
        String command = "숩교환 [바꿀 경험치(10경험치 = 1베리,Long 타입)]";

        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(args.length == 1){
            channel.sendMessage(command).queue();
            return;
        }
        if(!Support.isLong(args[1])){
            channel.sendMessage(command).queue();
            return;
        }
        Long changeExp = Long.parseLong(args[1]);
        Boolean isBot = user.isBot();
        UserDB userDB = new UserDB(user);
        Long changMoney = changeExp/10;
        changeExp = changMoney*10;
        if(!userDB.getExp().max(new BigInteger(String.valueOf(changeExp))).equals(userDB.getExp())){
            channel.sendMessage("경험치가 부족합니다.").queue();
            return;
        }
        if(userDB.getMoney().add(new BigInteger(String.valueOf(changMoney))).max(BigInteger.ZERO).equals(BigInteger.ZERO)){
            channel.sendMessage("돈이 부족합니다.").queue();
            return;
        }

        userDB.addExp(changeExp*-1);
        userDB.addMoney(changMoney);

        channel.sendMessage("**"+changeExp+"경험치**가 **"+changMoney+"베리**로 교환되었습니다.").queue();


    }
     */ // 교환

    public void inform_Money(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        BigInteger sp = userDB.getAll_Property().divide(SugoData.getSP());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(user.getName()+"님의 재산");
        embed.addField("**재산**",
                "돈: "+Support.unit(userDB.getMoney())
                        + "\n빚: "+Support.unit(userDB.getDebt())
                        + "\n경험치: "+Support.unit(userDB.getExp())
                        + "\n-----------------"
                        + "\n총자산: "+Support.unit(userDB.getAll_Property())
                        +"\nSP: "+Support.unit(sp)+"SP",false);

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void inform_Health(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(user.getName()+"님의 건강");
        String sleep = "\n잠: "+Support.time_Unit(userDB.getSleepTime())+"/"+Support.time_Unit(userDB.getAll_SleepTime());
        embed.addField("**건강**",
                "건강: "+Support.unit(userDB.getHealth())
                        +"\n피로도: "+userDB.getTired()
                        +(userDB.isHealth() ? "\n회복중":"")
                        +(userDB.isSleeping() ? sleep:"")
                ,false);

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void inform_Stat(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(user.getName()+"님의 스탯");

        embed.addField("**스탯**",
                "도둑질: "+userDB.getStealing()+"강"
                        +"\n방어력: "+userDB.getDefense()+"강"
                        +"\n행운: "+userDB.getLuck()+"강"
                        +"\n회복력: "+userDB.getResilience()+"강"
                ,true);

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void inform_Item(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(user.getName()+"님의 아이템");

        if(userDB.getItem_List() == null){
            channel.sendMessage("아이템이 없습니다.").queue();
            return;
        }

        for(Item item :userDB.getItem_List()){
            embed.addField("**"+item.Korean_Name+"**",Support.unit(userDB.getItem_NUm(item))+" 개",true);
        }

        if(embed.getFields().size() == 0){
            channel.sendMessage("아이템이 없습니다.").queue();
            return;
        }
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void Rank() {

        SelectMenu.Builder menu = SelectMenu.create("UserRank")
                .addOption("돈", "돈")
                .addOption("자산(재산)", "재산")
                .addOption("건강", "건강")
                .addOption("경험치", "경험치")
                .addOption("빚", "빚");

        if (arg.length == 1) {

                    //.addOption("스탯", "스탯")
                    //.addOption("미납세", "미납세")
                    //.addOption("죽거나 파산힌 횟수", "죽거나파산한횟수");

            channel.sendMessage("**숩순위 (돈/자산(재산)/건강/경험치/빚)**").setActionRow(menu.build()).queue();
            return;
        }
        String[] list = {"돈","자산","재산","건강","경험치","빚"};
        if(!List.of(list).contains(arg[1])){
            channel.sendMessage("**숩순위 (돈/자산(재산)/건강/경험치/빚)**").setActionRow(menu.build()).queue();
            return;
        }

        User_Rank_System(arg[1]);
    }



    public void User_Rank_System(String s) {
        System.out.println(s);
        String path = "";
        String key = "";
        boolean isOne = true;

        switch (s) {
            case "돈":
                path = "Properties.txt";
                key = "Money";
                break;
            case "재산":
            case "자산":
                isOne = false;
                break;
            case "건강":
                path = "HealthNOthers.txt";
                key = "Health";
                break;
            case "경험치":
                path = "Properties.txt";
                key = "Exp";
                break;
            case "빚":
                path = "Properties.txt";
                key = "Debt";
                break;
            default:
                return;
        }
        if (isOne) {
            ArrayList<Rank> list = getRank(path, key);
            EmbedBuilder embed = new EmbedBuilder();

            String title = s;
            embed.setTitle(title);
            embed.setColor(Support.getRandomColor());
            int i = 1;
            for (Rank rank : list) {
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i + "위 "+userDB.getName() ,Support.unit(rank.getNumber()), true);
                i++;
                if (i > 5) {
                    Button button = Button.primary("UserRank" + s, "전체순위보기");
                    ArrayList<Button> buttons = new ArrayList<>();
                    buttons.add(button);
                    channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
                    return;
                }
            }
            Button button = Button.primary("UserRank" + s, "전체순위보기");
            ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(button);
            channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
        } else {
            ArrayList<Rank> list = new ArrayList<>();

            switch (s) {
                case "재산":
                case "자산":
                    for (String userid : UserDB.getUserList()) {
                        UserDB userDB = new UserDB(userid);
                        BigInteger pro = userDB.getMoney().add(userDB.getExp()).subtract(userDB.getTax().add(userDB.getDebt()));
                        list.add(new Rank(userid, pro));
                    }
                    break;
                case "스탯":
                    for (String userid : UserDB.getUserList()) {
                        UserDB userDB = new UserDB(userid);
                        int sta = userDB.getLuck() + userDB.getResilience() + userDB.getStealing() + userDB.getDefense();
                        list.add(new Rank(userid, sta));
                    }
                    break;
                case "죽거나파산한횟수":
                    for (String userid : UserDB.getUserList()) {
                        UserDB userDB = new UserDB(userid);
                        int die = userDB.getDead_Num() + userDB.getFailure_Num();
                        list.add(new Rank(userid, die));
                    }
                    break;
            }
            Collections.sort(list, new Comparator<Rank>() {
                @Override
                public int compare(Rank o1, Rank o2) {
                    if (o1.getNumber().max(o2.getNumber()).equals(o1.getNumber())) {
                        return -1;
                    } else if (o1.getNumber().min(o2.getNumber()).equals(o1.getNumber())) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            EmbedBuilder embed = new EmbedBuilder();
            String title = s;
            //String title = arg[1] == null ? "":arg[1];
            embed.setTitle(title);
            embed.setColor(Support.getRandomColor());
            int i = 1;
            for (Rank rank : list) {
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i + "위 "+userDB.getName(), Support.unit(rank.getNumber()), true);
                i++;
                if (i > 5) {
                    Button button = Button.primary("UserRank" + s, "전체순위보기");
                    ArrayList<Button> buttons = new ArrayList<>();
                    buttons.add(button);
                    channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
                    return;
                }
            }
            Button button = Button.primary("UserRank" + s, "전체순위보기");
            ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(button);
            channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
        }
    }


    public void otherUser_inform(JDA jda){
        if(arg.length == 1){
            SelectMenu.Builder menu = SelectMenu.create("otherUser-Inform");
            for(String id: UserDB.getUserList()){
                UserDB userDB = new UserDB(id);
                menu.addOption(userDB.getName(),id,"유저ID: "+id);
            }
            channel.sendMessage("여기서 고르시오.").setActionRow(menu.build()).queue();
        }
        else {
            String userid = arg[1]
                    .replace("<@","")
                    .replace(">","");
            if(UserDB.isUser(userid)){
                otherUser_inform_System(userid);
            }
            else if (!Support.isLong(userid)){
                channel.sendMessage("ㅅ유저정보 (유저)").queue();
            }
            else {
                channel.sendMessage("회원가입된 유저가 아닙니다.").queue();
            }
        }
    }

    public void otherUser_inform_System(String id){
        UserDB userDB = new UserDB(id);
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(Support.getRandomColor());
        embed.setTitle(userDB.getName()+"님의 정보");

        embed.addField("**총자산**",Support.unit(userDB.getAll_Property()),true);
        embed.addField("**건강**",Support.unit(userDB.getHealth()),true);

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static void FullRank(ButtonInteractionEvent event, String arg1){

        String path = "";
        String key = "";
        boolean isOne = true;

        switch (arg1){
            case "돈":
                path = "Properties.txt";
                key = "Money";
                break;
            case "재산":
            case "자산":
            case "스탯":
            case "죽거나파산한횟수":
                isOne = false;
                break;
            case "건강":
                path = "HealthNOthers.txt";
                key =  "Health";
                break;
            case "경험치":
                path = "Properties.txt";
                key = "Exp";
                break;
            case "빚":
                path = "Properties.txt";
                key = "Debt";
                break;
            case "미납세":
                path = "Properties.txt";
                key = "Tax";
                break;
            default:
                return;
        }
        if(isOne){
            ArrayList<Rank> list = getRank(path,key);
            EmbedBuilder embed = new EmbedBuilder();

            String title = arg1;
            embed.setTitle(title);
            embed.setColor(Support.getRandomColor());
            int i = 1;
            for(Rank rank:list){
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i+"위 "+userDB.getName(),Support.unit(rank.getNumber()),true);
                i++;
            }
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
        else {
            ArrayList<Rank> list = new ArrayList<>();

            switch (arg1){
                case "재산":
                case "자산":
                    for(String userid:UserDB.getUserList()){
                        UserDB userDB = new UserDB(userid);
                        BigInteger pro = userDB.getMoney().add(userDB.getExp()).subtract(userDB.getTax().add(userDB.getDebt()));
                        list.add(new Rank(userid,pro));
                    }
                    break;
                case "스탯":
                    for(String userid:UserDB.getUserList()){
                        UserDB userDB = new UserDB(userid);
                        int sta = userDB.getLuck()+userDB.getResilience()+userDB.getStealing()+userDB.getDefense();
                        list.add(new Rank(userid,sta));
                    }
                    break;
                case "죽거나파산한횟수":
                    for(String userid:UserDB.getUserList()){
                        UserDB userDB = new UserDB(userid);
                        int die = userDB.getDead_Num()+userDB.getFailure_Num();
                        list.add(new Rank(userid,die));
                    }
                    break;
            }
            Collections.sort(list, new Comparator<Rank>() {
                @Override
                public int compare(Rank o1, Rank o2) {
                    if(o1.getNumber().max(o2.getNumber()).equals(o1.getNumber())){
                        return -1;
                    }else if(o1.getNumber().min(o2.getNumber()).equals(o1.getNumber())){
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });

            EmbedBuilder embed = new EmbedBuilder();

            String title = arg1;
            embed.setTitle(title);
            embed.setColor(Support.getRandomColor());
            int i = 1;
            for(Rank rank:list){
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i+"위",userDB.getName()+": "+Support.unit(rank.getNumber()),true);
                i++;
            }
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();

        }

    }

    public static ArrayList<Rank> getRank(String path,String key){
        ArrayList<Rank> RankList = new ArrayList<>();
        for(String userid:UserDB.getUserList()){
            RankList.add(new Rank(userid, new BigDecimal(SaveNLoad.loadP("UserDB/"+userid+"/"+path,key))));
        }

        Collections.sort(RankList, new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                if(o1.getNumber().max(o2.getNumber()).equals(o1.getNumber())){
                    return -1;
                }else if(o1.getNumber().min(o2.getNumber()).equals(o1.getNumber())){
                    return 1;
                }else {
                    return 0;
                }
            }
        });

        return RankList;
    }

    public void send(){
        String command = "숩송금 [받는사람] [금액]";
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 3){
            channel.sendMessage(command).queue();
            return;
        }

        String sentId = arg[1].replace("<@","").replace(">","");
        String price = arg[2];
        UserDB userDB = new UserDB(user);
        UserDB sent = new UserDB(sentId);

        if(!UserDB.isUser(sentId)){
            channel.sendMessage("받는 사람이 회원가입되어있지 않습니다.").queue();
            return;
        }
        if(!Support.isBigI(price)){
            channel.sendMessage(command).queue();
            return;
        }

        BigInteger sent_price = new BigInteger(price);
        if(sent_price.min(userDB.getMoney()).equals(userDB.getMoney())){
            channel.sendMessage("돈이 부족합니다.").queue();
            return;
        }
        if(sent_price.min(BigInteger.ZERO).equals(sent_price)){
            channel.sendMessage("어디서 돈을 훔쳐갈라고!!").queue();
            return;
        }
        userDB = new UserDB(user);
        userDB.addMoney(sent_price.multiply(new BigInteger("-1")));
        userDB = new UserDB(sentId);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("송금");
        embed.setColor(Support.getRandomColor());
        embed.addField("받는 사람",sent.getName(),true);

        if(user.isBot()){
            sent = new UserDB(sentId);
            sent.addMoney(sent_price.divide(BigInteger.TWO));
            SugoData.addMoney(sent_price.divide(BigInteger.TWO));
            embed.addField("금액",Support.unit(sent_price.divide(BigInteger.TWO)),true);
        }
        else {
            sent = new UserDB(sentId);
            sent.addMoney(sent_price);
            embed.addField("금액",Support.unit(sent_price),true);
        }
        channel.sendMessage(arg[1]).queue();
        channel.sendMessageEmbeds(embed.build()).queue();

    }

    public void chart(){
        String command = "숩유저그래프 (건강/자산/SP) (오늘/일주일/한달/일년/전체)";
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        String days = "전체";
        String[] day_list = {"일주일","한달","일년","전체","오늘"};
        String type = "자산";
        String[] type_list = {"건강","자산","SP"};
        if(arg.length == 2){
            if(List.of(day_list).contains(arg[1])) days = arg[1];
            else if(List.of(type_list).contains(arg[1].toUpperCase(Locale.ROOT))) type = arg[1].toUpperCase(Locale.ROOT);
            else {
                channel.sendMessage(command).queue();
                return;
            }
        }else if(arg.length >= 3){
            if(List.of(type_list).contains(arg[1].toUpperCase(Locale.ROOT))) type = arg[1].toUpperCase(Locale.ROOT);
            else {
                channel.sendMessage(command).queue();
                return;
            }
            if(List.of(day_list).contains(arg[2])) days = arg[2];
            else {
                channel.sendMessage(command).queue();
                return;
            }
        }

        String path = makeChart(days,type);
        File file = new File(path);
        channel.sendFile(file).queue();
    }

    public String makeChart(String days,String type){
        String danwe = "berry";
        String type_E = "";
        switch (type){
            case "건강":
                type_E = "Chart_H";
                danwe = "health";
                break;
            case "자산":
                type_E = "Chart_P";
                break;
            case "SP":
                type_E = "Chart_SP";
                break;
        }

        long plus = 0;
        if(days.equalsIgnoreCase("일주일")){
            plus = -7;
        }
        if(days.equalsIgnoreCase("한달")){
            plus = -30;
        }
        if(days.equalsIgnoreCase("일년")){
            plus = -365;
        }
        if(days.equalsIgnoreCase("전체")){
            plus = -12000;
        }

        ArrayList<String> check_times = new  ArrayList<>();

        String now = LocalDate.now().toString().replace("-","/");
        String old = LocalDate.now().plusDays(plus).toString().replace("-","/");
        String id = user.getId();
        String colors = "122:122:12";
        String start_time  = LocalDate.now().plusDays(plus).toString();

        Chart chart = new Chart("UserDB/"+userid+"/Charts/"+type_E);
        ArrayList<Chart_Data> cds = chart.getChart_Data_list(old,now);
        check_times.add(cds.get(0).time.substring(0,10).replace("/","-"));
        String path = "UserDB/"+userid+"/Charts/Images/"+id.replace("/","-")+"_"+days+".png";

        String[] ctList = check_times.toArray(new String[0]);
        Arrays.sort(ctList);
        String check_time = ctList[0];

        if(checkStartTime(start_time,check_time)){
            start_time = check_time;
        }

        makeLine_Chart(path,id.replace("/","-")+"("+start_time+"~"+LocalDate.now()+")/Day/"+danwe,id,colors, cds);
        return path;
    }

    static private boolean checkStartTime(String startTime,String checkTime){
        if(checkTime.equalsIgnoreCase(""))return false;
        long s = Long.parseLong(startTime.replace("-",""));
        long c = Long.parseLong(checkTime.replace("-",""));
        if(s < c) return true;
        return false;
    }

    public void setAlarm(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        if(userDB.getEconomy_Alarm()){
            userDB.setEconomy_Alarm(false);
            channel.sendMessage("경제 알람이 꺼져습니다.").queue();
        }
        else {
            userDB.setEconomy_Alarm(true);
            channel.sendMessage("경제 알람이 켜져습니다.").queue();
        }
    }

    public void loan(){
        String command = "숩대출 [금액]";
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        String sp = arg[1];
        if(!Support.isBigI(sp)){
            channel.sendMessage(command).queue();
            return;
        }
        BigInteger price = new BigInteger(sp);
        if(price.min(BigInteger.ZERO).equals(price)){
            channel.sendMessage("돈을 갚으 시려면 갚기를 쳐주세요").queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        userDB.addMoney(price);
        userDB.addDebt(price);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName()+"님의 대출");
        embed.addField("대출한 금액",Support.unit(price)+"베리",true);
        embed.setFooter("일이자율 0.5%");
        channel.sendMessageEmbeds(embed.build()).queue();

    }

    public void pay_back(){
        String command = "숩갚기 [금액]";
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        String sp = arg[1];
        if(!Support.isBigI(sp)){
            channel.sendMessage(command).queue();
            return;
        }
        BigInteger price = new BigInteger(sp);
        if(price.min(BigInteger.ZERO).equals(price)){
            channel.sendMessage("대출하실려면 **숩대출**로").queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        if(price.max(userDB.getMoney()).equals(price)){
            channel.sendMessage("돈이 부족합니다.").queue();
            return;
        }
        if(price.max(userDB.getDebt()).equals(price)){
            price = userDB.getDebt();
        }
        price = price.multiply(new BigInteger("-1"));
        userDB.addMoney(price);
        userDB.addDebt(price);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName()+"님의 대출 갚기");
        embed.addField("갚은 금액",Support.unit(sp)+"베리",true);
        embed.addField("남은 금액",Support.unit(userDB.getDebt())+"베리",true);
        embed.setFooter("일이자율 0.5%");
        channel.sendMessageEmbeds(embed.build()).queue();

    }

    public void bankruptcy(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        Button button = Button.primary("UserBankruptcy"+userid, "파산신청하기");
        event.getMessage().reply("파산을 신청하시려면 밑에 버튼을 눌려주세요!!").setActionRow(button).queue();
    }

    public static void Button_bankruptcy(ButtonInteractionEvent event){
        String button_userid = event.getButton().getId().replace("UserBankruptcy","");
        String userid = event.getUser().getId();
        if(!button_userid.equalsIgnoreCase(userid)){
            event.getChannel().sendMessage("파산 신청을 하시려면 숩파산을 눌려주세요.").queue();
            return;
        }
        UserDB userDB = new UserDB(event.getUser());
        if(userDB.getAll_Property().min(BigInteger.ZERO).equals(BigInteger.ZERO)){
            SugoData.addMoney(userDB.getAll_Property());
        }
        userDB.setMoney("0");
        for (String id:Stock_Data.list()){
            Stock_Data stock_data = new Stock_Data(id);
            stock_data.addSelling_Num(new BigInteger(String.valueOf(userDB.getStock_Num(id)*-1)));
            userDB.setStock(id,0,new BigInteger("0"));
        }
        userDB.setDebt(0);
        userDB.setExp(0);
        event.getMessage().reply("파산하였습니다.").queue();

    }




}
