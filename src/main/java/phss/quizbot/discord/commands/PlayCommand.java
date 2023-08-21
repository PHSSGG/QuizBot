package phss.quizbot.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import phss.quizbot.data.repository.impl.QuizRepository;
import phss.quizbot.data.repository.impl.UserRepository;
import phss.quizbot.discord.view.SessionView;
import phss.quizbot.manager.SessionManager;
import phss.quizbot.quiz.Quiz;
import phss.quizbot.quiz.session.QuizActiveSession;

import java.util.Optional;

public class PlayCommand extends BaseCommand {

    QuizRepository quizRepository;
    SessionManager sessionManager;

    public PlayCommand(UserRepository userRepository, QuizRepository quizRepository, SessionManager sessionManager) {
        super("play", userRepository);
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onExecute(SlashCommandInteractionEvent event) {
        if (event.getOption("quiz") == null) {
            return;
        }
        if (sessionManager.isUserInAnySession(userAccount.getUserId())) {
            event.reply(":x: | You're already playing a quiz").setEphemeral(true).queue();
            return;
        }
        String quizName = event.getOption("quiz").getAsString();
        Optional<Quiz> foundQuiz = quizRepository.getByName(quizName);
        if (foundQuiz.isPresent()) {
            event.reply("Quiz started").setEphemeral(true).queue();

            SessionView view = new SessionView(event.getJDA(), event.getMessageChannel());
            QuizActiveSession session = sessionManager.createActiveSession(userAccount, foundQuiz.get(), view);

            view.attachSession(session);
        } else {
            event.reply(":x: | Cannot found quiz with the provided name").setEphemeral(true).queue();
        }
    }

}
