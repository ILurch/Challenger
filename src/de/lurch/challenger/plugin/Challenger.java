package de.lurch.challenger.plugin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.lurch.challenger.handler.EntityDamageByEntityListener;
import de.lurch.challenger.handler.PlayerInteractEntityListener;
import de.lurch.challenger.plugin.ChallengeInviter.Message;

public class Challenger extends JavaPlugin implements Listener {

	private static Challenger instance;
	private ItemStack challengeItem;

	@Override
	public void onEnable() {
		instance = this;
		registerHandlers();
		challengeItem = createItemStack(Material.IRON_SWORD, 1, (byte) 0, "§6§lChallenger", Arrays.asList("§aMit diesem Schwert forderst", "§adu andere Spieler heraus."));
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	private void registerHandlers() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new EntityDamageByEntityListener(), this);
		pm.registerEvents(new PlayerInteractEntityListener(), this);
	}

	public ItemStack getChallengeItem() {
		return challengeItem;
	}

	public static ItemStack createItemStack(Material mat, int amount, byte data, String displayName, List<String> lore) {
		ItemStack out = new ItemStack(mat, amount, data);
		ItemMeta meta = out.getItemMeta();
		meta.setDisplayName(displayName);
		out.setItemMeta(meta);
		return out;
	}

	public static void sendMessage(Player player, Message msg, Player target) {
		if (target != null)
			player.sendMessage(Message.prefix + msg.getMessage().replace("%player%", target.getDisplayName()));
		else
			player.sendMessage(Message.prefix + msg.getMessage());

	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if (event.getItemDrop().getItemStack().equals(challengeItem)) {
			event.getItemDrop().remove();
			event.getPlayer().setItemInHand(challengeItem);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (player.getGameMode() != GameMode.CREATIVE && event.getCurrentItem().equals(challengeItem)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.PHYSICAL && event.getPlayer().getItemInHand().isSimilar(challengeItem))
			event.getPlayer().getItemInHand().setDurability((short) 0);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().getInventory().setItem(0, challengeItem);
	}

	public static Challenger getInstance() {
		return instance;
	}

	public static void connect(Player player, String direction) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(direction);
		player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
	}
}