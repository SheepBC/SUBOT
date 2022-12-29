package Command;

import SheepBC.BaseN;
import Supporter.Rank;
import Supporter.Support;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recommend {

    MessageReceivedEvent event;
    MessageChannel channel;
    String[] args;

    public Recommend(MessageReceivedEvent event, String[] args){
        this.event = event;
        channel = event.getChannel();
        this.args = args;
    }

    public void recommend(String[] list,String title){
        EmbedBuilder embed = new EmbedBuilder();
        String commend = "숩"+title+" [원하는 횟수]";

        if(args.length == 1){
            embed.setTitle(list[(int)(Math.random()*list.length)]).setColor(Support.getRandomColor());
            channel.sendMessageEmbeds(embed.build()).queue();
        }

        if(args.length > 1){
            if(!Support.isLong(args[1])){
                channel.sendMessage(commend).queue();
                return;
            }
            Long num = Long.parseLong(args[1]);
            if(num < 1){
                channel.sendMessage("최소 횟수는 1회 입니다.").queue();
                return;
            }

            embed.setColor(Support.getRandomColor());
            embed.setTitle(title);

            ArrayList<Rank> rankList = Rank.getRankList(list,num);

            for(int i = 1;i<=5;i++){
                Rank rank = rankList.get(i-1);
                if(!rank.getNumber().equals(BigDecimal.ZERO)){
                    embed.addField(i+"위 ",rank.getname()+":"+ rank.getNumber(),true);
                }
            }

            if(Rank.getRankSize(rankList) > 6){
                String s = Support.toSerialize(toRank(rankList));
                List<Button> buttons = new ArrayList<>();
                String[] sList = Support.division(s,5);
                String[] seeList = {"전체","순위","보기","\uD83E\uDD14"};
                int i = 0;
                for(String s1:sList){
                    if(!s1.equalsIgnoreCase(sList[sList.length-1])){
                        Button button = Button.primary("Rank"+s1,seeList[i]);
                        i++;
                        buttons.add(button);
                    }
                }
                embed.setFooter(sList[sList.length-1]);

                channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
            }else {
                channel.sendMessageEmbeds(embed.build()).queue();
            }
            /*
            Long n = 1L;
            for (Rank rank:rankList){
                if(rank.getNumber() != 0){
                        embed.addField(n+"위 ",rank.getname()+":"+ rank.getNumber(),true);
                    n++;
                }
                if(n == 7){
                    pri = true;
                    channel.sendMessageEmbeds(embed.build()).queue();
                }
            }

            if(pri){
                event.getAuthor().openPrivateChannel().
                        flatMap(channel -> channel.sendMessageEmbeds(embed.build())).queue();
            }else {
                channel.sendMessageEmbeds(embed.build()).queue();
            }

             */

        }

    }

    public void customRecommend(){
        String command = "숩추천 [원하는 목록1/목록2...] ([원하는 횟수])";
        String title ="추천";
        if(args.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        String listString ="";
        int num = 1;
        for (int i =1;i<args.length;i++){
            if(i == (args.length-1) & args[i].startsWith("[") & args[i].endsWith("]")){
                String numS = args[i];
                numS = numS.replace("[","")
                        .replace("]","");
                try {
                    num = Integer.parseInt(numS);
                }catch (Exception e){
                    listString += args[i]+" ";
                }
            }
            else {
                listString += args[i]+" ";
            }
        }

        String[] list = listString.split("/");
        EmbedBuilder embed = new EmbedBuilder();

        ArrayList<Rank> rankList = Rank.getRankList(list,num);

        embed.setColor(Support.getRandomColor());
        if(num == 1){
            embed.setTitle(rankList.get(0).getname());
            channel.sendMessageEmbeds(embed.build()).queue();
        }
        else {
            embed.setTitle(title);

            for(int i = 1;i<=5;i++){
                Rank rank = rankList.get(i-1);
                if(!rank.getNumber().equals(BigDecimal.ZERO)){
                    embed.addField(i+"위 ",rank.getname()+":"+ rank.getNumber(),true);
                }
            }

            if(Rank.getRankSize(rankList) > 6){
                String s = Support.toSerialize(toRank(rankList));
                List<Button> buttons = new ArrayList<>();
                String[] sList = Support.division(s,5);
                String[] seeList = {"전체","순위","보기","\uD83E\uDD14"};
                int i = 0;
                for(String s1:sList){
                    if(!s1.equalsIgnoreCase(sList[sList.length-1])){
                        Button button = Button.primary("Rank"+s1,seeList[i]);
                        i++;
                        buttons.add(button);
                    }
                }
                embed.setFooter(sList[sList.length-1]);

                channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
            }else {
                channel.sendMessageEmbeds(embed.build()).queue();
            }
            /*
            boolean pri = false;
            Long n = 1L;
            for (Rank rank:rankList){
                if(rank.getNumber() != 0){
                    embed.addField(n+"위 ",rank.getname()+":"+ rank.getNumber(),true);
                    n++;
                }
                if(n == 7){
                    pri = true;
                    channel.sendMessageEmbeds(embed.build()).queue();
                }
            }
            if(pri){
                event.getAuthor().openPrivateChannel().
                        flatMap(channel -> channel.sendMessageEmbeds(embed.build())).queue();
            }else {
                channel.sendMessageEmbeds(embed.build()).queue();
            }

             */
        }

    }

    public static String[] toRank(ArrayList<Rank> list){
        String[] ranks = new String[list.size()];
        BaseN baseM = new BaseN(Support.getListM());

        for(int l = 0;l<list.size();l++){
            Rank rank = list.get(l);
            if(!rank.getNumber().equals(BigDecimal.ZERO)){
                String num =    baseM.DecimalToBaseN(rank.getNumber().toString().replace(".0","")) ;
                String name = rank.getname();
                //System.out.println(rank.getname()+" "+name);
                ranks[l] = name+"/."+num;
            }
        }

        return ranks;
    }

}

