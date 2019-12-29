package top.dsbbs2.whitelist.config.struct;

import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class WhiteListConfig {
	public static class WLPlayer{
		public UUID uuid;
		public long QQ;
		public WLPlayer(UUID uuid,long QQ)
		{
			this.uuid=uuid;
			this.QQ=QQ;
		}
		public Player toPlayer()
		{
			return Bukkit.getPlayer(this.uuid);
		}
		public OfflinePlayer toOfflinePlayer()
		{
			return Bukkit.getOfflinePlayer(this.uuid);
		}
		public static WLPlayer fromPlayer(Player p,long QQ)
		{
			return new WLPlayer(p.getUniqueId(),QQ);
		}
		public static WLPlayer fromOfflinePlayer(OfflinePlayer p,long QQ)
		{
			return new WLPlayer(p.getUniqueId(),QQ);
		}
	}
	public Vector<WLPlayer> players=new Vector<>();
}
