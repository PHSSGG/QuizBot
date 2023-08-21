package phss.quizbot.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import phss.quizbot.data.repository.impl.QuizRepository;
import phss.quizbot.data.repository.impl.SessionRepository;
import phss.quizbot.data.repository.impl.UserRepository;
import phss.quizbot.quiz.Quiz;
import phss.quizbot.user.UserAccount;

public class ListCommand extends BaseCommand {

    QuizRepository quizRepository;
    SessionRepository sessionRepository;

    public ListCommand(UserRepository userRepository, QuizRepository quizRepository, SessionRepository sessionRepository) {
        super("list", userRepository);
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void onExecute(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Quizzes")
                .setDescription(buildListDescription());
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    private String buildListDescription() {
        StringBuilder description = new StringBuilder();
        description.append("Quizzes with :white_check_mark: means that you have already completed it \n\n");
        for (Quiz quiz : quizRepository.getData()) {
            description.append(quiz.getName());
            sessionRepository.find(session -> {
                        return session.getUserAccount().getUserId() == userAccount.getUserId()
                        && session.getQuiz().getQuizId().equals(quiz.getQuizId());
            }).ifPresent(session -> {
                description.append(" :white_check_mark:");
            });
            description.append(" \n");
        }

        return description.toString();
    }

}
