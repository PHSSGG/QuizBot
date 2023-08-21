package phss.quizbot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import phss.quizbot.data.repository.impl.QuizRepository;
import phss.quizbot.data.repository.impl.SessionRepository;
import phss.quizbot.data.repository.impl.UserRepository;
import phss.quizbot.discord.commands.InfoCommand;
import phss.quizbot.discord.commands.ListCommand;
import phss.quizbot.discord.commands.PlayCommand;
import phss.quizbot.discord.listeners.QuizAnswerListener;
import phss.quizbot.manager.SessionManager;

public class DiscordBot {

    final private UserRepository userRepository;
    final private QuizRepository quizRepository;
    final private SessionRepository sessionRepository;
    final private SessionManager sessionManager;

    public DiscordBot(UserRepository userRepository, QuizRepository quizRepository, SessionRepository sessionRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.sessionRepository = sessionRepository;
        this.sessionManager = sessionManager;
    }

    JDA jda;

    public void start(String token) throws InterruptedException {
        jda = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("quiz"))
                .addEventListeners(
                        new InfoCommand(userRepository, quizRepository, sessionRepository),
                        new ListCommand(userRepository, quizRepository, sessionRepository),
                        new PlayCommand(userRepository, quizRepository, sessionManager),
                        new QuizAnswerListener(userRepository, sessionManager)
                )
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .build()
                .awaitReady();
        registerCommands();
    }

    private void registerCommands() {
        jda.upsertCommand("info", "Get information about you")
                .queue();
        jda.upsertCommand("list", "List all quizzes")
                .queue();
        jda.upsertCommand("play", "Play a quiz")
                .addOption(OptionType.STRING, "quiz", "The quiz name", true)
                .queue();
    }

}
