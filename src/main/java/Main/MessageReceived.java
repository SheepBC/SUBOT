package Main;

import Command.*;
import SugoData.SugoData;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;

public class MessageReceived implements Runnable {

    MessageReceivedEvent event;
    String[] args;
    MessageChannel channel;

    String[] prefixes = {"숩","ㅅ","t"};

    public MessageReceived(MessageReceivedEvent event){
        this.event = event;
        args = event.getMessage().getContentRaw().split(" ");
        channel = event.getChannel();
    }

    @Override
    public void run() {

        onMessageEvent();

        if(CommandCheck("")) return;
        BasicCommand();
        Recommend();
        Number();
        User();
        User_Health();
        Stock();
        Enhance();

        Test();

   }

    void onMessageEvent(){
        User user = new User(event,args);

        user.addExp();
        user.addTired();
    }
    void BasicCommand(){
       Basic basic = new Basic(event);
       if(CommandCheck("도움말", "명령어")){
           basic.Help();
       }
       if(CommandCheck("숩")){
           String[] list = {"숩숩!!","수박고기!!"};
           basic.randomMessage(list);
       }
       if(CommandCheck("잘가")){
           String[] list = {"싫어!","좋아^^","Nooooooo!!"};
           basic.randomMessage(list);
       }
       if(CommandCheck("안녕")){
           String[] list = {"안녕!","싫어!","안녕하세요!!"};
           basic.randomMessage(list);
       }
       if(CommandCheck("멍청이")){
           String[] list = {"저 멍청이 아닌데요??","아 당신이 멍청이구나!!!!","||저를 만든사람이 멍청이 입네다.||"};
           basic.randomMessage(list);
       }
       if(CommandCheck("봇","수박고기")){
           basic.botInform(event.getJDA());
       }
       if(CommandCheck("링크","초대링크")){
           basic.linK();
       }
       if(CommandCheck("프로필")){
           event.getChannel().sendMessage(event.getAuthor().getAvatarUrl()).queue();
       }
       if(CommandCheck("tmi","설명서")){
           event.getChannel().sendFile(new File("others/tmi/SUGO-Tmi-v"+ SugoData.getVersion+".hwp")).queue();
       }
   }
    void Recommend(){
        Recommend recommend = new Recommend(event,args);
        if(CommandCheck("게임추천","겜추")){
            String[] list = {"마인크래프트","끄투","래프트","갱비스트","얼치홀","그타","캐치마인드",
                    "옵치","폴가이즈","로블록스","MUCK","갈틱폰","포탈","유료트럭 시물레이션","배그",
                    "발로란트","롤","히오스","원신","마인크래프트 던전스","직접 만들어서 하기","휴폴플",
                    "워킹 시물레이션 2020","애니팡","술래잡기","얼음땡","BattleBlock Theater","지뢰 찾기"};
            String title = "게임추천";
            recommend.recommend(list,title);
        }
        if(CommandCheck("산추천","산추")){
            String[] list = {"무등산","백두산","||금강산||","지리산","한라산","에베레스트 산"};
            String title = "산추천";
            recommend.recommend(list,title);
        }
        if(CommandCheck("주사위")){
            String[] list = {"\uD83C\uDFB2**1**","\uD83C\uDFB2**2**","\uD83C\uDFB2**3**",
                    "\uD83C\uDFB2**4**","\uD83C\uDFB2**5**","\uD83C\uDFB2**6**"};
            String title = "주사위";
            recommend.recommend(list,title);
        }
        if(CommandCheck("추천")){
            recommend.customRecommend();
        }
   }
    void Number(){
        Command.Number num = new Command.Number(event,args);
        if(CommandCheck("소수")){
            num.prime();
        }
        if(CommandCheck("인수분해")){
            num.factor();
        }
        if(CommandCheck("전개")){
            num.expend();
        }
        if(CommandCheck("근")){
            num.solve();
        }
        if(CommandCheck("연립방정식","연립","해")){
            num.coalition();
        }
    }
    void User(){
        User user = new User(event,args);
        if (CommandCheck("회원가입","가입")){
            user.join();
        }
        if(CommandCheck("내정보")){
            user.inform();
        }
        if(CommandCheck("자산","재산")){
            user.inform_Money();
        }
        if(CommandCheck("건강")){
            user.inform_Health();
        }
        if(CommandCheck("스탯")){
            user.inform_Stat();
        }
        if(CommandCheck("아이템")){
            user.inform_Item();
        }
        if(CommandCheck("순위")){
            user.Rank();
        }
        if(CommandCheck("유저정보")){
            user.otherUser_inform(event.getJDA());
        }
    }

    void User_Health(){
        User_Health user_health = new User_Health(event,args);
        if(CommandCheck("회복")){
            user_health.health();
        }
        if(CommandCheck("그만회복","그만회복하기")){
            user_health.stop_health();
        }
        if(CommandCheck("잠")){
            user_health.sleep();
        }
        if(CommandCheck("그만잠","그만잠자기")){
            user_health.stop_Sleep();
        }
    }

    void Enhance(){
        Enhance enhance = new Enhance(event,args);
        if(CommandCheck("강화석도박")){
            enhance.Enhance_Stone_Gambling();
        }
    }

    void Stock(){
        Stock stock = new Stock(event,args);
        if(CommandCheck("주식")){
            stock.inform();
        }
        if(CommandCheck("그래프","주식그래프")){
            stock.Chart();
        }
        Stock_User stock_user = new Stock_User(event,args);
        if(CommandCheck("내주식")){
            stock_user.user();
        }
        if(CommandCheck("매수")){
            stock_user.Stock_Buy();
        }
        if(CommandCheck("풀매수")){
            stock_user.Stock_Buy_All();
        }
        if(CommandCheck("매도")){
            stock_user.Stock_Sell();
        }
        if(CommandCheck("풀매도")){
            stock_user.Stock_Sell_All();
        }
    }

    void Test(){
        Test test = new Test(event,args);
        if(CommandCheck("테스트","xptmxm")){
            test.test1();
        }
    }
    boolean CommandCheck(String... commands){
        for(String command:commands){
            for(String prefix:prefixes){
                if(args[0].equalsIgnoreCase(prefix+command)) return  true;
            }
        }
        return false;
    }

}
