package phss.quizbot.discord.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import phss.quizbot.data.repository.impl.UserRepository;
import phss.quizbot.user.UserAccount;

public abstract class BaseCommand extends ListenerAdapter {

    protected User author;
    protected UserAccount userAccount;

    String name;
    UserRepository userRepository;

    public BaseCommand(String name, UserRepository userRepository) {
        this.name = name;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase(name)) {
            author = event.getUser();
            userAccount = userRepository.get(author.getIdLong()).orElse(userRepository.createAccount(author.getIdLong()));

            onExecute(event);
        }
    }

    public abstract void onExecute(SlashCommandInteractionEvent event);

}
