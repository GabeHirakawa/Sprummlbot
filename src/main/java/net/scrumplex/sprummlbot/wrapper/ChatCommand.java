package net.scrumplex.sprummlbot.wrapper;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import net.scrumplex.sprummlbot.plugins.CommandHandler;
import net.scrumplex.sprummlbot.plugins.SprummlbotPlugin;
import net.scrumplex.sprummlbot.tools.Exceptions;

public class ChatCommand implements Comparable<ChatCommand> {
    private CommandHandler commandHandler = null;
    private SprummlbotPlugin plugin = null;
    private final String command;
    private final String usage;

    public ChatCommand(String command, String usage) {
        this.command = command;
        this.usage = usage;
    }

    public CommandResponse handle(String message, ClientInfo client) {
        String commandName = message.toLowerCase();
        String[] args = new String[0];
        if (message.contains(" ")) {
            String[] parts = message.split(" ");
            commandName = parts[0].toLowerCase();
            args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, parts.length - 1);
        }
        commandName = commandName.substring(1);
        if (!commandName.equalsIgnoreCase(getCommandName()))
            return CommandResponse.INTERNAL_ERROR;
        try {
            return commandHandler.handleCommand(client, args);
        } catch (Throwable throwable) {
            if (plugin == null)
                Exceptions.handle(throwable, "Internal Error", false);
            else
                Exceptions.handlePluginError(throwable, plugin);
            return CommandResponse.INTERNAL_ERROR;
        }
    }

    public String getCommandName() {
        return command;
    }

    public String getCommandUsage() {
        return this.usage;
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public SprummlbotPlugin getCommandPlugin() {
        return this.plugin;
    }

    public void setCommandHandler(SprummlbotPlugin plugin, CommandHandler commandHandler) {
        this.plugin = plugin;
        this.commandHandler = commandHandler;
    }

    @Override
    public int compareTo(ChatCommand o) {
        return o.getCommandName().compareTo(getCommandName());
    }
}