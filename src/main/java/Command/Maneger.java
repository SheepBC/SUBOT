package Command;

import Main.main;
import SugoData.*;
import Supporter.SaveNLoad;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.awt.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Maneger {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;
    JDA jda;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public Maneger(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        jda = event.getJDA();
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public void BackUp(){
        LocalDate dat = LocalDate.now();
        LocalTime time = LocalTime.now();
        String ts = time.toString().replace(":","");
        int i = ts.indexOf(".");
        ts = ts.substring(0,i);
        main.isBackup = true;
        SugoData.Update_Chart_SP();
        jda.getPresence().setActivity(Activity.playing("백업중"));
        channel.sendMessage("백업시작_userDB").queue();
        SaveNLoad.zip("BackUP/"+dat+"_"+ts+"_userDB.zip","UserDB");
        channel.sendMessage("백업시작_Stock").queue();
        SaveNLoad.zip("BackUP/"+dat+"_"+ts+"_Stock.zip","Stock");
        channel.sendMessage("백업시작_Sub").queue();
        SaveNLoad.zip("BackUP/"+dat+"_"+ts+"_Sub.zip","Sub");
        channel.sendMessage("백업끝").queue();
        main.isBackup = false;
    }

    public void Stop(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BackUp();
                channel.sendMessage("봇이 꺼집니다.").queue();
            }
        };
        Thread thread = new Thread(runnable);
        try {
            thread.start();
            thread.join();
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void Button_Inquiry_Reply(ButtonInteractionEvent event){
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);
        String text = embed.getFields().get(0).getValue().replaceAll("```","");
        Modal.Builder modal = Modal.create("Inquiry_Reply"+embed.getFooter().getText(),"문의답장");

        String[] list = di(text,4);
        int i = 0;
        for (String s:list){
            if(s == null) break;
            String[] list2 =di2(s);
            TextInput textInput = TextInput.create("I"+i,list2[0], TextInputStyle.PARAGRAPH)
                    .setPlaceholder(list2[1] == null ? "...":list2[1])
                    .setMaxLength(1000)
                    .build();
            i++;
            modal.addActionRow(textInput);
        }

        TextInput textInput2 = TextInput.create("text","답장 내용(답장 후 첫 칸에 문의 내용 요약)", TextInputStyle.PARAGRAPH)
                .setPlaceholder("몰라!!")
                .setMaxLength(1000)
                .build();

        modal.addActionRow(textInput2);
        event.replyModal(modal.build()).queue();
    }

    public static void Modal_Inquiry_Reply(ModalInteractionEvent event){
        String userid = event.getModalId().replace("Inquiry_Reply","");
        JDA jda = event.getJDA();
        EmbedBuilder embed = new  EmbedBuilder();
        embed.setTitle("문의 답장");
        embed.setColor(Support.getRandomColor());
        embed.addField("문의내용",event.getValue("I0").getAsString(),true);
        embed.addField("답장",event.getValue("text").getAsString(),true);
        jda.retrieveUserById(userid).flatMap(User::openPrivateChannel).queue(privateChannel -> privateChannel.sendMessageEmbeds(embed.build()).queue());
        event.replyEmbeds(embed.build()).queue();
    }

    private static String[] di2(String s){
        String[] list = new String[2];
        if(s.length() <= 45){
            list[0] = s;
        }
        else {
            list[0] = s.substring(0,45);
            list[1] = s.substring(46);
        }
        return list;
    }

    private static String[] di(String s,int n){
        if(n == 1) return new String[]{s};
        int num = 145;
        String[] list = new String[n];
        int index = 0;
        int beginIndex = s.length() - s.length() % (n-1);
        for (int i = 0; i< beginIndex;){
            if(i+num > s.length()){
                list[index] = s.substring(i,s.length());
            }
            else {
                list[index] = s.substring(i,i+num);
            }
            i+= num;
            index++;

        }
        return list;
    }

    public void use_num(){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("사용 횟수");
        embed.setColor(Support.getRandomColor());
        embed.addField("사용한 유저수",main.users.size()+"명",true);
        embed.addField("사용한 횟수",main.use+"번",true);
        embed.setFooter("평균 사용횟수 "+(double)(main.use/main.users.size())+"번");
        channel.sendMessageEmbeds(embed.build()).queue();
        for(BigInteger bi :SugoData.amr){
            int i = SugoData.amr.indexOf(bi);
            String name = SugoAddMoney.values()[i].name();
            channel.sendMessage(name+": "+bi.toString()).queue();
        }
    }

    public void notice(){
        Button button = Button.primary("notice","공지하기");
        channel.sendMessage("공지하기").setActionRow(button).queue();
    }

    public static void notice_Button(ButtonInteractionEvent event){
        TextInput text = TextInput.create("text","공지내용", TextInputStyle.PARAGRAPH)
                .setPlaceholder("죄송합니다.....?")
                .setMaxLength(1000)
                .build();

        Modal modal = Modal.create("notice","공지")
                .addActionRow(text)
                .build();
        event.replyModal(modal).queue();
    }

    public static void notice_Modal(ModalInteractionEvent event){
        String text = event.getValue("text").getAsString();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(4, 101, 4));
        embed.addField("**공지**","```"+text+"```",true);
        embed.setFooter("🍉SUBACKGOGI(공지가 이상한대로 온다면 -숩봇)");

        for(Guild server:event.getJDA().getGuilds()){
            TextChannel channel = checkChannel(new ArrayList<>(server.getTextChannels()));
            if(channel == null){
                send_notice(server.getTextChannels(),embed);
            }
            else {
                try {
                    channel.sendMessageEmbeds(embed.build()).queue();
                }catch (Exception e){
                    send_notice(server.getTextChannels(),embed);
                }
            }
        }
        event.reply("공지 전송이 완료 되었습니다.").queue();
    }
    private static void send_notice(List<TextChannel> list, EmbedBuilder embed){
        for(TextChannel channel1:list){
            try {
                channel1.sendMessageEmbeds(embed.build()).queue();
                break;
            }catch (Exception e){

            }
        }
    }

    private static TextChannel checkChannel(ArrayList<TextChannel> list){
        for (TextChannel channel:list){
            String topic = channel.getTopic();
            if(topic == null) topic="null";
                if(topic.contains("-숩봇")){
                return channel;
            }
        }
        return null;
    }

    public void give(){
        for(String id: UserDB.getUserList()){
            UserDB userDB = new UserDB(id);
            userDB.addExp(60000);
            channel.sendMessage(userDB.getName()+"("+id+")").queue();
        }
        channel.sendMessage("Rmx").queue();
    }


}
