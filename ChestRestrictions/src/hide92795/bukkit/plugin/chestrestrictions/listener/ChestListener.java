package hide92795.bukkit.plugin.chestrestrictions.listener;

import hide92795.bukkit.plugin.chestrestrictions.ChestRestrictions;
import hide92795.bukkit.plugin.chestrestrictions.Type;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener {
	private ChestRestrictions plugin;

	public ChestListener(ChestRestrictions chestRestrictions) {
		this.plugin = chestRestrictions;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		switch (event.getAction()) {
		case LEFT_CLICK_BLOCK:
		case RIGHT_CLICK_BLOCK:
			Block b = event.getClickedBlock();
			Player player = event.getPlayer();
			if (plugin.onlyWarnPlayer && plugin.mcbansdetector != null) {
				if (!plugin.mcbansdetector.isPlayerWarned(player)) {
					break;
				}
			}
			if (plugin.blockList.contains(b.getTypeId())) {
				if (!player.hasPermission("chestrestrictions.open") && !player.isOp()) {
					event.setCancelled(true);
					player.sendMessage(plugin.localize.getString(Type.OPEN_CHEST));
				}
			}
			break;
		default:
			break;
		}
	}

}
