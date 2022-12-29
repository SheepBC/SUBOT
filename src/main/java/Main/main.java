package Main;

import SheepBC.BaseN;
import Supporter.SaveNLoad;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import SheepBC.BaseN.*;

import javax.security.auth.login.LoginException;

public class main extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault(Setting.getBasic("BotToken")).build();
        jda.getPresence().setActivity(Activity.playing("\"숩명령어\"를 쳐보라고 말하는중 "));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.addEventListener(new main());

    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
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
}
