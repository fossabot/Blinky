package me.tsblock.Blinky.Command.Economy;

import com.mongodb.client.MongoCollection;
import me.tsblock.Blinky.Command.Command;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.utils.Embed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class balanceCommand extends Command {
    public MongoConnect mongoConnect = new MongoConnect();
    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Check your wallet\'s balance";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("bal", "money");
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean needArgs() {
        return false;
    }

    @Override
    public void onExecute(GuildMessageReceivedEvent event, Message msg, User user, Guild guild, String... args) {
        String toCheckId = user.getId();
        if (!msg.getMentionedUsers().isEmpty()) {
            toCheckId = msg.getMentionedUsers().get(0).getId();
        }
        Embed embed = new Embed();
        MongoCollection<Document> userStatsCollection = mongoConnect.getUserStats();
        Document userStats = new Document("id", toCheckId);
        Document found = userStatsCollection.find(userStats).first();
        if (found == null) {
            if (guild.getMemberById(toCheckId).getUser().isBot()) {
                embed.sendEmbed("Bots are not allowed to use economy.", event.getChannel());
                return;
            }
            if (!toCheckId.equals(user.getId())) {
                    embed.sendEmbed("The targeted user did not create a wallet. Use b.balance to create a wallet.", event.getChannel());
                return;
            }
            mongoConnect.initUserStats(toCheckId);
            embed.sendEmbed("Successfully created your wallet, please type this command again to check your balance.", event.getChannel());
        } else {
            MessageEmbed messageEmbed = new EmbedBuilder()
                    .setAuthor(guild.getMemberById(toCheckId).getUser().getName() + "\'s wallet", null, guild.getMemberById(toCheckId).getUser().getAvatarUrl())
                    .addField("\uD83D\uDCB0 Balance \uD83D\uDCB0", "$" + String.format("%,d", found.getLong("balance")), false) //ez format ez trick
                    .setColor(Color.GREEN)
                    .build();
            event.getChannel().sendMessage(messageEmbed).queue();
        }
    }
}
