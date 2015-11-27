package de.lurch.challenger.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.lurch.challenger.plugin.ChallengeInviter;
import de.lurch.challenger.plugin.ChallengeInviter.Invite;
import de.lurch.challenger.plugin.ChallengeInviter.Message;
import de.lurch.challenger.plugin.Challenger;

public class EntityDamageByEntityListener implements Listener {

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player player = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if (damager.getItemInHand().equals(Challenger.getInstance().getChallengeItem())) {
				damager.getItemInHand().setDurability(damager.getItemInHand().getType().getMaxDurability());
				Invite inv = ChallengeInviter.getInviteOf(player, damager);
				if (inv != null) {
					successInvite(inv);
				} else {
					if (ChallengeInviter.getInvites(damager).isEmpty()) {
						inv = ChallengeInviter.setInvited(damager, player);
						newInvite(inv);
					}
				}
			}
		} else if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			if (damager.getItemInHand().equals(Challenger.getInstance().getChallengeItem())) {
				damager.setItemInHand(Challenger.getInstance().getChallengeItem());
			}
		}
	}

	public void newInvite(Invite inv) {
		Challenger.sendMessage(inv.getInvited(), Message.INVITED, inv.getInviter());
		Challenger.sendMessage(inv.getInviter(), Message.INVITER, inv.getInvited());
	}

	public void successInvite(final Invite inv) {
		Challenger.sendMessage(inv.getInvited(), Message.INVITED_ACCEPTED, inv.getInviter());
		Challenger.sendMessage(inv.getInviter(), Message.INVITER_ACCEPTED, inv.getInvited());
		Challenger.sendMessage(inv.getInvited(), Message.TELEPORTATION, null);
		Challenger.sendMessage(inv.getInviter(), Message.TELEPORTATION, null);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Challenger.getInstance(), new Runnable() {

			@Override
			public void run() {
				String server = Challenger.getInstance().getConfig().getString("Server");
				Challenger.connect(inv.getInvited(), server);
				Challenger.connect(inv.getInviter(), server);
				inv.destroy();
			}
		}, 20);
	}
}