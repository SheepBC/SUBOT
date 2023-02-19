package Command;

import SIng.GuildMusicManager;
import SIng.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Sing {

    MessageReceivedEvent event;
    String userID;
    MessageChannel channel;
    Member member;
    AudioManager manager;
    TextChannel textChannel;
    String[] args;

    public Sing(MessageReceivedEvent event,String[] args) {
        this.event = event;
        userID = event.getAuthor().getId();
        channel = event.getChannel();
        member = event.getMember();
        manager = event.getGuild().getAudioManager();
        textChannel = event.getTextChannel();
        this.args = args;
    }

    public void play(){

        if(args.length == 1){
            channel.sendMessage("숩노래 [링크]").queue();
            return;
        }
        String url = event.getMessage().getContentRaw().substring(4);

        if(!isUrl(url)){
            url = "ytsearch:"+url;
        }

        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }
        manager.openAudioConnection(voiceState.getChannel());

        PlayerManager.getINSTANCE()
                .loadAndPlayer(event.getTextChannel(),url);
    }

    public void stop(){
        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        manager.closeAudioConnection();
        channel.sendMessage("노래가 멈췄습니다.").queue();

    }

    public void skip(){
        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("노래가 없는데?").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
    }

    public void now(){
        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        channel.sendMessage(audioPlayer.getPlayingTrack().getInfo().title).queue();
    }

    public void list(){
        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("노래가 없는데?").queue();
            return;
        }

        final ArrayList<AudioTrack> list = new ArrayList<>(queue);

        int num1 = 0;
        channel.sendMessage("지금"+audioPlayer.getPlayingTrack().getInfo().title).queue();
        for(AudioTrack track:list){
            channel.sendMessage(track.getInfo().title).queue();
            num1++;
            if(num1 > 10) break;
        }

    }

    public static void menu(SelectMenuInteractionEvent event, String url){

        MessageChannel channel = event.getChannel();
        Member member = event.getMember();
        AudioManager manager = event.getGuild().getAudioManager();

        if(!isUrl(url)){
            url = "ytsearch:"+url;
        }

        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }
        manager.openAudioConnection(voiceState.getChannel());

        PlayerManager.getINSTANCE()
                .loadAndPlayer(event.getTextChannel(),url);
    }

    private static boolean isUrl(String url) {
        try {
        new URL(url);
        return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
