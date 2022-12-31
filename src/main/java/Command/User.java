package Command;

import Main.SelectButton;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.List;

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
        embed.addField("**재산**",
                "돈: "+"총자산: "+Support.unit(userDB.getAll_Property()),false);

        String sleep = "\n잠: "+Support.time_Unit(userDB.getSleepTime())+"/"+Support.time_Unit(userDB.getAll_SleepTime());

        embed.addField("**건강**",
                "건강: "+Support.unit(userDB.getHealth())
                        +"\n피로도: "+userDB.getTired()
                        +(userDB.isHealth() ? "\n회복중":"")
                        +(userDB.isSleeping() ? sleep:"")
                ,false);

        embed.addField("**스탯**",
                /*
                "도둑질: "+userDB.getStealing()+"강"
                        +"\n방어력: "+userDB.getDefense()+"강"

                 */
                        "\n행운: "+userDB.getLuck()+"강"
                        +"\n회복력: "+userDB.getResilience()+"강"
                ,true);

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
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(user.getName()+"님의 재산");
        embed.addField("**재산**",
                "돈: "+Support.unit(userDB.getMoney())
                        + "\n빚: "+Support.unit(userDB.getDebt())
                        + "\n미납세: "+Support.unit(userDB.getTax())
                        + "\n경험치: "+Support.unit(userDB.getExp())
                        + "\n-----------------"
                        + "\n총자산: "+Support.unit(userDB.getAll_Property()),false);

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

        if (arg.length == 1) {

            SelectMenu.Builder menu = SelectMenu.create("UserRank")
                    .addOption("돈", "돈")
                    .addOption("자산(재산)", "재산")
                    .addOption("건강", "간강")
                    .addOption("경험치", "경험치")
                    .addOption("빚", "빚")
                    .addOption("스탯", "스탯")
                    .addOption("미납세", "미납세")
                    .addOption("죽거나 파산힌 횟수", "죽거나파산한횟수");

            channel.sendMessage("**숩순위 [돈/자산(재산)/건강/경험치/빚/스탯/미납세/죽거나파산한횟수]**").setActionRow(menu.build()).queue();
            return;
        }

        User_Rank_System(arg[1]);
    }



    public void User_Rank_System(String s) {
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
            case "스탯":
            case "죽거나파산한횟수":
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
            case "미납세":
                path = "Properties.txt";
                key = "Tax";
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
                embed.addField(i + "위", userDB.getName() + ": " + Support.unit(rank.getNumber()), true);
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

            String title = arg[1] == null ? "":arg[1];
            embed.setTitle(title);
            embed.setColor(Support.getRandomColor());
            int i = 1;
            for (Rank rank : list) {
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i + "위", userDB.getName() + ": " + Support.unit(rank.getNumber()), true);
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
                embed.addField(i+"위",userDB.getName()+": "+Support.unit(rank.getNumber()),true);
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


}
