package me.tsblock.Blinky.Command;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class Command {
    public String getName() {
        return null;
    }
    public String getDescription() {
        return "Not available";
    }
    public String getUsage() { return null; }
    public List<String> getAliases() {
        return Collections.emptyList();
    }
    public boolean enabled() { return false; }
    public boolean ownerOnly() { return false; }
    public boolean needArgs() { return false; }
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {}
}
