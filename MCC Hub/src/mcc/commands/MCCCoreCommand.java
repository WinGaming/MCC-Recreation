package mcc.commands;

import mcc.core.BukkitConnector;
import mcc.core.MCCEvent;
import mcc.core.event.EventChapter;
import mcc.core.event.EventChapterState;
import mcc.core.event.minigame.ChapterMinigame;
import mcc.core.event.preevent.ChapterPreEvent;
import mcc.core.event.preevent.ChapterPreEventWaiting;
import mcc.core.players.EventPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCCCoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
			sender.sendMessage("Command can only be executed as a player");
			return true;
		}

        if (args.length == 0) {

        } else if (args[0].equalsIgnoreCase("init")) {
            MCCEvent.getInstance().setCurrentChapter(new ChapterPreEvent()); // TODO: Check if already initialized
            sender.sendMessage("Initialized event");
        } else if (args[0].equalsIgnoreCase("start")) {
            EventChapter<?> currentChapter = MCCEvent.getInstance().getCurrentChapter();
            EventChapterState<?> currentChapterState = currentChapter.getCurrentStateInstance();
            if (currentChapterState instanceof ChapterPreEventWaiting) {
                ((ChapterPreEventWaiting) currentChapterState).markShouldStart();
                sender.sendMessage("Scheduled event start");
            } else {
                sender.sendMessage("Can't start the event currently, is it already running?");
            }
        } else if (args[0].equalsIgnoreCase("ready")) {
            if (sender instanceof Player) {
                EventPlayer player = new BukkitConnector().getEventPlayer((Player) sender);
                player.markReady();
            }
        } else if (args[0].equalsIgnoreCase("game")) {
            MCCEvent.getInstance().setCurrentChapter(new ChapterMinigame());
        }

        return true;
    }
}
