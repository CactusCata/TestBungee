package com.gmail.cactuscata.testbungee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Sets;

public class TestBungee extends JavaPlugin implements PluginMessageListener {

	public static HashMap<String, Integer> map = new HashMap<>();
	private static final Set<String> channels = Sets.newHashSet();

	@Override
	public void onEnable() {

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) 
			return;
		
		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
			String subchannel = in.readUTF();

			if (subchannel.equals("PlayerCount")) {
				if (channels.size() > 1 && channel.equalsIgnoreCase("BungeeCord"))
					return;

				String server = in.readUTF();
				int playerCount = in.readInt();
				map.put(server, playerCount);

			}
		} catch (IOException e) {

		}
	}

	public void getPlayerCount(Player player, String targetServer) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("PlayerCount");
			out.writeUTF(targetServer);
			out.close();
		} catch (IOException localIOException) {
		}
		player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
	}

}
