package de.lurch.challenger.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import de.lurch.challenger.plugin.ChallengeInviter;
import de.lurch.challenger.plugin.ChallengeInviter.Invite;
import de.lurch.challenger.plugin.ChallengeInviter.Message;
import de.lurch.challenger.plugin.Challenger;

public class PlayerInteractEntityListener implements Listener {

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player) {
			Player player = event.getPlayer();
			Player target = (Player) event.getRightClicked();
			if (player.getItemInHand().equals(Challenger.getInstance().getChallengeItem())) {
				event.setCancelled(true);
				player.setItemInHand(Challenger.getInstance().getChallengeItem());
				Invite inv = ChallengeInviter.getInviteOf(target, player);
				if (inv != null) {
					if (inv.getInvited().equals(player)) {
						declineInvite(inv);
					} else {
						revokeInvite(inv);
					}
					inv.destroy();
				}
			}
		}
	}

	public void revokeInvite(Invite inv) {
		Challenger.sendMessage(inv.getInvited(), Message.INVITED_REVOKE, inv.getInviter());
		Challenger.sendMessage(inv.getInviter(), Message.INVITER_REVOKE, inv.getInvited());
	}

	public void declineInvite(Invite inv) {
		Challenger.sendMessage(inv.getInvited(), Message.INVITED_DECLINE, inv.getInviter());
		Challenger.sendMessage(inv.getInviter(), Message.INVITER_DECLINE, inv.getInvited());
	}
}