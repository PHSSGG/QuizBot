package phss.quizbot.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import phss.quizbot.data.repository.impl.QuizRepository;
import phss.quizbot.data.repository.impl.SessionRepository;
import phss.quizbot.data.repository.impl.UserRepository;
import phss.quizbot.user.UserAccount;

import java.util.UUID;

public class InfoCommand extends BaseCommand {

    QuizRepository quizRepository;
    SessionRepository sessionRepository;

    public InfoCommand(UserRepository userRepository, QuizRepository quizRepository, SessionRepository sessionRepository) {
        super("info", userRepository);
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void onExecute(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Quizzes")
                .setDescription(buildInfoDescription());
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    private String buildInfoDescription() {
        StringBuilder description = new StringBuilder();
        description.append("Your quizzes: \n");
        if (!userAccount.getQuizzes().isEmpty()) {
            for (UUID quizId : userAccount.getQuizzes()) {
                quizRepository.get(quizId).ifPresent(quiz -> description.append(quiz.getName()).append("\n"));
            }
        } else {
            description.append("None");
        }

        description.append("\n Completed: \n");
        if (!userAccount.getCompletedQuizzes().isEmpty()) {
            for (UUID sessionId : userAccount.getCompletedQuizzes()) {
                sessionRepository.get(sessionId).ifPresent(session -> description.append(session.getQuiz().getName()).append("\n"));
            }
        } else {
            description.append("None");
        }

        return description.toString();
    }

}
