package me.tsblock.Blinky;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import me.tsblock.Blinky.Command.Administrator.evalCommand;
import me.tsblock.Blinky.Command.Administrator.forceCreateWalletCommand;
import me.tsblock.Blinky.Command.Administrator.sayCommand;
import me.tsblock.Blinky.Command.Administrator.shutdownCommand;
import me.tsblock.Blinky.Command.Core.levelCommand;
import me.tsblock.Blinky.Command.Core.pingCommand;
import me.tsblock.Blinky.Command.Economy.balanceCommand;
import me.tsblock.Blinky.Command.Economy.hackdollarsCommand;
import me.tsblock.Blinky.Command.Economy.payCommand;
import me.tsblock.Blinky.Command.Fun.eightballCommand;
import me.tsblock.Blinky.Command.Fun.catCommand;
import me.tsblock.Blinky.Database.MongoConnect;
import me.tsblock.Blinky.Handler.CommandHandler;
import me.tsblock.Blinky.Handler.EventHandler;
import me.tsblock.Blinky.Settings.Settings;
import me.tsblock.Blinky.Settings.SettingsManager;
import me.tsblock.Blinky.utils.ExitCodes;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    //set logger's level to error only
    static Logger root = (Logger) LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME);

    static {
        root.setLevel(Level.ERROR);
    }

    private static CommandHandler commandHandler = new CommandHandler();
    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }
    public static MongoConnect mongoConnect = new MongoConnect();
    public static JDA jda;
    private static Settings settings = SettingsManager.getInstance().getSettings();
    public static void main(String[] args) {
        if (System.getenv("TRAVIS_CI") == "TRUE") {
            System.exit(ExitCodes.TRAVIS_CI_TEST);
            return;
        }
        mongoConnect.connect();
        try {
            registerCommands();
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(settings.getBotSecret())
                    .setGame(Game.of(GameType.WATCHING, "tsb#3160"))
                    .addEventListener(new EventHandler())
                    .buildBlocking();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static JDA getJDA() { return jda; }
    private static void registerCommands() {
        commandHandler.register(new pingCommand());
        commandHandler.register(new evalCommand());
        commandHandler.register(new shutdownCommand());
        commandHandler.register(new balanceCommand());
        commandHandler.register(new hackdollarsCommand());
        commandHandler.register(new payCommand());
        commandHandler.register(new forceCreateWalletCommand());
        commandHandler.register(new sayCommand());
        commandHandler.register(new catCommand());
        commandHandler.register(new levelCommand());
        commandHandler.register(new eightballCommand());
    }
}
