package hide92795.bukkit.plugin.chestrestrictions;

import hide92795.bukkit.plugin.chestrestrictions.listener.ChestListener;
import hide92795.bukkit.plugin.corelib.Localize;
import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.mcbansdetector.MCBansDetectorAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestRestrictions extends JavaPlugin {
	public List<?> blockList;
	private Logger logger;
	public Localize localize;
	public boolean onlyWarnPlayer;
	public MCBansDetectorAPI mcbansdetector;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
		logger = getLogger();
		localize = new Localize(this);
		try {
			reload();
		} catch (Exception e1) {
			logger.severe("Error has occured on loading config.");
		}
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new ChestListener(this), this);
		// hook MCBansDetector
		try {
			MCBansDetector mcbans = (MCBansDetector) pm.getPlugin("MCBansDetector");
			mcbansdetector = mcbans.getAPI();
			logger.info("Hook into MCBansDetector.");
		} catch (Exception e) {
			mcbansdetector = null;
		}
	}

	private void reload() throws Exception {
		reloadConfig();
		try {
			localize.reload(getConfig().getString("Language"));
		} catch (Exception e1) {
			logger.severe("Can't load language file.");
			try {
				localize.reload("jp");
				logger.severe("Loaded default language file.");
			} catch (Exception e) {
				throw e;
			}
		}
		blockList = getConfig().getList("Blocks");
		if (blockList == null) {
			saveDefaultConfig();
			blockList = new ArrayList<Object>();
		}
		onlyWarnPlayer = getConfig().getBoolean("OnlyWarnPlayer");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName().toLowerCase()) {
		case "chestrestrictions-reload":
			try {
				reload();
				sender.sendMessage(localize.getString(Type.RELOADED_SETTING));
				logger.info("Reloaded successfully.");
			} catch (Exception e) {
				sender.sendMessage(localize.getString(Type.ERROR_RELOAD_SETTING));
			}
			break;
		default:
			break;
		}
		return true;
	}
}
