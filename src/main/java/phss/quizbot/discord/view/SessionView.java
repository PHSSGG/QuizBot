package phss.quizbot.discord.view;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import phss.quizbot.quiz.QuizQuestion;
import phss.quizbot.quiz.session.QuizSession;

import java.util.List;

public class SessionView {

    long messageId = 0L;

    QuizSession session;

    JDA jda;
    MessageChannel channel;

    public SessionView(JDA jda, MessageChannel channel) {
        this.jda = jda;
        this.channel = channel;
    }

    public void attachSession(QuizSession session) {
        this.session = session;
        sendMessage();
    }

    private void sendMessage() {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Quiz")
                .setDescription(getQuizEmbedDescription("{position}: {name} \n"));
        channel.sendMessageEmbeds(embedBuilder.build()).queue(message -> messageId = message.getIdLong());
    }

    public void updateMessage() {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Quiz")
                .setDescription(getQuizEmbedDescription("{position}: {name} \n"));
        channel.editMessageEmbedsById(messageId, embedBuilder.build()).queue();
    }

    public void updateMessageWithCurrentAnswered() {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Quiz")
                .setDescription(getQuizEmbedDescription("{position}: {name} {isCorrect} \n"));
        channel.editMessageEmbedsById(messageId, embedBuilder.build()).queue();
    }

    private String getQuizEmbedDescription(String answersFormat) {
        QuizQuestion currentQuestion = session.getCurrentQuestion();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(session.getQuiz().getName()).append(" \n");
        stringBuilder.append("Creator: ").append(jda.getUserById(session.getQuiz().getCreatorId()).getName()).append(" \n\n");
        stringBuilder.append("**Question ").append(currentQuestion.getPosition() + 1).append("/").append(session.getQuiz().getQuestions().size()).append("** \n\n");
        stringBuilder.append(currentQuestion.getDescription()).append(" \n");
        appendAnswers(stringBuilder, currentQuestion.getAnswers(), answersFormat);
        stringBuilder.append("\n ").append("Type the answer number in the chat to select your answer");

        return stringBuilder.toString();
    }

    private void appendAnswers(StringBuilder stringBuilder, List<QuizQuestion.QuizQuestionAnswer> answers, String format) {
        for (QuizQuestion.QuizQuestionAnswer answer : answers) {
            stringBuilder.append(format
                    .replace("{position}", "" + (answers.indexOf(answer) + 1))
                    .replace("{name}", answer.getName())
                    .replace("{isCorrect}", answer.isCorrect() ? ":white_check_mark:" : ":x:")
            );
        }
    }

    public void deleteMessage() {
        channel.deleteMessageById(messageId).queue();
    }

}
