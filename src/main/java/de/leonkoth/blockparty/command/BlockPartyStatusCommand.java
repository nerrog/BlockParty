package de.leonkoth.blockparty.command;

import de.leonkoth.blockparty.BlockParty;
import de.leonkoth.blockparty.arena.Arena;
import de.leonkoth.blockparty.locale.Locale;
import de.leonkoth.blockparty.locale.Messenger;
import org.bukkit.command.CommandSender;

public class BlockPartyStatusCommand extends SubCommand {

    public BlockPartyStatusCommand(BlockParty blockParty) {
        super(false, 2, "status", "blockparty.admin.status", blockParty);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (!super.onCommand(sender, args)) {
            return false;
        }

        Arena arena;
        try {
            arena = Arena.getByName(args[1]);
        } catch (NullPointerException e) {
            Messenger.message(true, sender, Locale.ARENA_DOESNT_EXIST, "%ARENA%", args[1]);
            return false;
        }

        if(arena == null)
        {
            Messenger.message(true, sender, Locale.ARENA_DOESNT_EXIST, "%ARENA%", args[1]);
            return false;
        }

        if (!arena.isEnabled()) {
            Messenger.message(true, sender, Locale.ARENA_DISABLED, "%ARENA%", arena.getName());
            return false;
        }

        Messenger.message(true, sender, Locale.LOBBY_STATUS, "%ARENA%", args[1], "%STATUS%", arena.getArenaState().name());

        return true;
    }

}
