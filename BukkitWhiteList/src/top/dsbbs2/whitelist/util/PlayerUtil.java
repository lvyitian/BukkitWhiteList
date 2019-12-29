package top.dsbbs2.whitelist.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import top.dsbbs2.whitelist.WhiteListPlugin;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig.WLPlayer;

public class PlayerUtil {
	public static void setInv(Player p,boolean inv) throws Throwable
	{
		Method getHandle=p.getClass().getDeclaredMethod("getHandle", new Class<?>[0]);
		getHandle.setAccessible(true);
		Object NMSPlayer=getHandle.invoke(p, new Object[0]);
		Field abilities=NMSPlayer.getClass().getSuperclass().getDeclaredField("abilities");
		abilities.setAccessible(true);
		Object abiObj=abilities.get(NMSPlayer);
		Class<?> abiClass=abiObj.getClass();
		Field isInvulnerable=abiClass.getDeclaredField("isInvulnerable");
		isInvulnerable.setAccessible(true);
		isInvulnerable.set(abiObj, inv);
		abilities.set(NMSPlayer, abiObj);
		Method upd=NMSPlayer.getClass().getDeclaredMethod("updateAbilities", new Class<?>[0]);
		upd.setAccessible(true);
		upd.invoke(NMSPlayer, new Object[0]);
	}
	public static boolean isInWhiteList(UUID uuid)
	{
		for(WhiteListConfig.WLPlayer i : WhiteListPlugin.instance.whitelist.con.players)
		{
			if(i.uuid!=null && i.uuid.equals(uuid))
				return true;
		}
		return false;
	}
	public static boolean isInWhiteList(Player p)
	{
		if(p==null)
			return false;
		return isInWhiteList(p.getUniqueId());
	}
	public static boolean isInWhiteList(OfflinePlayer p)
	{
		if(p==null)
			return false;
		return isInWhiteList(p.getUniqueId());
	}
	public static void addToWhiteListAndSave(WLPlayer p) throws IOException
	{
		try {
		Player p2=Bukkit.getPlayer(p.uuid);
		if(p2!=null)
		  PlayerUtil.setInv(p2,false);
		}catch(Throwable e) {throw new RuntimeException(e);}
		WhiteListPlugin.instance.whitelist.con.players.add(p);
		WhiteListPlugin.instance.whitelist.saveConfig();
	}
	public static void removeFromWhiteListAndSave(UUID uuid) throws IOException
	{
		try {
		Player p2=Bukkit.getPlayer(uuid);
		if(p2!=null)
		  PlayerUtil.setInv(p2,true);
		}catch(Throwable e) {throw new RuntimeException(e);}
		WhiteListPlugin.instance.whitelist.con.players.removeIf(i->((WLPlayer)i).uuid.equals(uuid));
		WhiteListPlugin.instance.whitelist.saveConfig();
	}
	public static ArrayList<String> playerListToNameList(Vector<Player> v)
	{
		ArrayList<String> ret=new ArrayList<>();
		for(Player i : v)
			ret.add(i.getName());
		return ret;
	}
	public static ArrayList<String> whiteListPlayerListToNameList(Vector<WLPlayer> v)
	{
		ArrayList<String> ret=new ArrayList<>();
		for(WLPlayer i : v)
		{
			OfflinePlayer t=i.toOfflinePlayer();
			if(t!=null)
			  ret.add(t.getName());
		}
		return ret;
	}
	public static ArrayList<String> offlinePlayerListToNameList(Vector<OfflinePlayer> v)
	{
		ArrayList<String> ret=new ArrayList<>();
		for(OfflinePlayer i : v)
			ret.add(i.getName());
		return ret;
	}
	public static boolean informPlayer(Player p)
	{
		if(p==null || !p.isOnline())
			return false;
		p.sendMessage("ÄúÎ´ÔÚ°×Ãûµ¥ÖÐ!");
		return true;
	}
	public static boolean informPlayer(OfflinePlayer p)
	{
		if(p==null || !p.isOnline())
			return false;
		return informPlayer(p.getPlayer());
	}
	public static void massivelyInformPlayer(Player p,long count)
	{
		for(long i=0;i<count;i++)
			informPlayer(p);
	}
	public static ArrayList<String> getOfflinePlayersNameList()
	{
		 return PlayerUtil.offlinePlayerListToNameList(VectorUtil.toVector(Bukkit.getOfflinePlayers()));
	}
	public static ArrayList<String> getUnwhitelistedOfflinePlayersNameList()
	{
		Vector<OfflinePlayer> temp=VectorUtil.toVector(Bukkit.getOfflinePlayers());
		temp.removeIf(i->isInWhiteList(i));
		return offlinePlayerListToNameList(temp);
	}
}
