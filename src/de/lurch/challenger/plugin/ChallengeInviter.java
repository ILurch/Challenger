package de.lurch.challenger.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public final class ChallengeInviter {

	private final static List<Invite> invites = new ArrayList<Invite>();

	public static Invite getInviteOf(Player inviter, Player invited) {
		for (Invite inv : invites) {
			if (inv.invited.equals(invited) && inv.inviter.equals(inviter))
				return inv;
		}
		return null;
	}

	public static Invite setInvited(Player inviter, Player invited) {
		Invite inv = new Invite(inviter, invited);
		invites.add(inv);
		return inv;
	}

	public static boolean isInvited(Player inviter, Player invited) {
		return (getInviteOf(inviter, invited) != null);
	}

	public static void removeInvite(Invite inv) {
		if (inv != null)
			invites.remove(inv);
	}

	public static List<Invite> getInvites(Player player) {
		List<Invite> invs = new ArrayList<Invite>();
		for (Invite inv : invites) {
			if (inv.getInvited().equals(player) || inv.getInviter().equals(player))
				invs.add(inv);
		}
		return invs;
	}

	public static boolean isInviter(Player damager, Player player) {
		for (Invite inv : invites) {
			if (inv.getInviter().equals(damager) && inv.getInvited().equals(player))
				return true;
		}
		return false;
	}

	public static Invite getEqualInvite(List<Invite> player, List<Invite> player1) {
		for (Invite inv : player) {
			for (Invite inv1 : player1) {
				if (inv.equals(inv1))
					return inv;
			}
		}
		return null;
	}

	public static class Invite {

		private Player inviter;
		private Player invited;

		public Invite(Player inviter, Player invited) {
			this.invited = invited;
			this.inviter = inviter;
		}

		public Player getInvited() {
			return invited;
		}

		public Player getInviter() {
			return inviter;
		}

		public void destroy() {
			invites.remove(this);
		}
	}

	public static enum Message {

		INVITER("Du hast §6%player% §7erfolgreich eingeladen!"), INVITED("Du wurdest von §6%player% §7eingeladen!"), INVITER_REVOKE("Du hast die Einladung für §6%player% §7zurückgezogen!"), INVITED_REVOKE("§6%player% §7hat die Einladung zurückgezogen!"), INVITED_ACCEPTED("Du hast die Einladung von §6%player% §7akzeptiert!"), INVITER_ACCEPTED("§6%player% §7hat deine Einladung angenommen!"), INVITED_DECLINE("Du hast die Einladung von §6%player% §7abgelehnt!"), INVITER_DECLINE("§6%player% §7hat deine Einladung abgelehnt!"), TELEPORTATION("Du wirst gleich zu einem freien Server teleportiert!"), TELEPORTATION_FAILED("Die Teleporation ist fehlgeschlagen!");

		public static String prefix = "§8[§6Challenger§8] §7";
		private String message;

		private Message(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}