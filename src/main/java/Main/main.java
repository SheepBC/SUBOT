package Main;

import Command.Basic;
import SheepBC.BaseN;
import Supporter.SaveNLoad;
import Supporter.UserDB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;
import SheepBC.BaseN.*;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class main extends ListenerAdapter {

    public static boolean isBackup = false;

    public static JDA jda;

    public static ArrayList<String> users = new ArrayList<>();
    public static long use = 0;
    public static PrintStream ps;
    public static PrintStream console;
    public static boolean start = true;

    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault(Setting.getBasic("BotToken")).build();
        jda.getPresence().setActivity(Activity.playing("\"숩명령어\"를 쳐보라고 말하는중 "));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.addEventListener(new main());

    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        change();
        Runnable runnable = new Ready(event);
        Thread thread = new Thread(runnable);
        thread.start();

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if(event.getAuthor().isBot()){
            String s = event.getAuthor().getId();
            String u = event.getJDA().getSelfUser().getId();
            if(s.equalsIgnoreCase(u)) return;
        };

        Runnable runnable = new MessageReceived(event);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {

        Runnable runnable = new SelectMenu(event);
        Thread thread = new Thread(runnable);
        thread.start();

    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Runnable runnable = new SelectButton(event);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        Runnable runnable = new Modal_Event(event);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static void alarm(String msg, String... withID){
        for(String id: UserDB.getUserList()){
            UserDB userDB = new UserDB(id);
            //System.out.println(List.of(withID).contains(id));
            if(userDB.getEconomy_Alarm() || List.of(withID).contains(id)){
                jda.retrieveUserById(id).flatMap(User::openPrivateChannel).queue(privateChannel -> privateChannel.sendMessage(msg).queue());
            }
        }
    }

    private static void start_change(){
        if(start){
            console = System.out;
            try {
                ps = new PrintStream(new FileOutputStream("output.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            start = false;
            change();
            System.out.println("**--------------------------------------------**");
            System.out.println(":white_check_mark: **봇이 시작되었습니다.**");
        }
    }

    public static void change(){
        start_change();
        System.setOut(ps);
        System.setErr(ps);

    }


}
