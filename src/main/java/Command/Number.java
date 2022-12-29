package Command;

import Main.Setting;
import Supporter.Python;
import Supporter.Support;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

public class Number {

    MessageReceivedEvent event;
    MessageChannel channel;
    String[] args;

    public Number(MessageReceivedEvent event, String[] args){
        this.event = event;
        channel = event.getChannel();
        this.args = args;
    }

    public void prime(){
        String command = "숩소수 [원하는 숫자]";
        EmbedBuilder embed = new EmbedBuilder();
        if(args.length <2){
            channel.sendMessage(command).queue();
            return;
        }
        if(!Support.isLong(args[1])){
            channel.sendMessage(command).queue();
            return;
        }
        long num = Long.parseLong(args[1]);
        if(isPrime(num)){
            embed.setTitle("소수⭕");
        }
        else {
            embed.setTitle("소수❌");
        }
        embed.setColor(Support.getRandomColor());
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    boolean isPrime(Long l){
        long n = 1;
        while (true){
            n++;
            if(l==n) return true;
            if(l%n == 0){
                return false;
            }
        }
    }

    public void factor(){
        String command = "숩인수분해 [인수 분해 할 식]";
        if(args.length <2){
            channel.sendMessage(command).queue();
            return;
        }
        List<String> list = numPython("factor",change(args[1]));
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(list.get(0));
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void expend(){
        String command = "숩전개 [전개 할 식]";
        if(args.length <2){
            channel.sendMessage(command).queue();
            return;
        }
        List<String> list = numPython("expend",change(args[1]));
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(list.get(0));
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void solve(){
        String command = "숩근 [근을 구 할 식]";
        if(args.length <2){
            channel.sendMessage(command).queue();
            return;
        }
        List<String> list = numPython("solve",change(args[1]));
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        try {
            embed.setTitle(list.get(0));
        }catch (IllegalArgumentException e){
            embed.addField(args[1]+"의 근","```"+list.get(1)+"```",true);
        }
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void coalition(){
        String command = "숩연립 [연립 할 식1] [연립 할 식2] [연립 할 식3] ...";
        if(args.length <2){
            channel.sendMessage(command).queue();
            return;
        }

        String[] ways = new String[args.length-1];
        for(int i = 0;i<ways.length;i++){
            ways[i] = change(args[i+1]);
        }

        List<String> list = numPython("coalition",ways);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        try {
            embed.setTitle(list.get(0));
        }catch (IllegalArgumentException e){
            embed.addField(args[1]+"의 해","```"+list.get(1)+"```",true);
        }
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public List<String> numPython(String type, String... arg){
        String[] command = Support.addStrings(new String[]{type},arg);
        return Python.getPython(Setting.getPythonPath("Number"),command);
    }

    public String change(String way){
        String returnWay = way.replace("^","**")
                .replace(")(",")*(");
        if(returnWay.contains("=")){
            String[] sp = returnWay.split("=");
            String[] sp1 = sp[1].split("");

            for(int i =0;i<sp1.length;i++){
                if(sp1[i].equalsIgnoreCase("-")) sp1[i] ="+";
                else if(sp1[i].equalsIgnoreCase("+")) sp1[i] ="-";
            }

            returnWay = sp[0]+"-";
            for (String s:sp1){
                returnWay +=s;
            }

        }

        return returnWay;
    }

}
