package net.blockcade.Arcade.games.BedBattles.Events;

import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.games.BedBattles.Main;
import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class PotionEvent implements Listener {

    public static HashMap<Player, ArmorStand> invis_players = new HashMap<>();
    private int invisible_time = 30;

    @EventHandler
    public void PotionEvent(EntityPotionEffectEvent e) {
        if (e.getCause().equals(EntityPotionEffectEvent.Cause.POTION_DRINK)) {
            if (e.getNewEffect().getType().equals(PotionEffectType.INVISIBILITY)) {
                e.setCancelled(true);
                ArmorStand as = (ArmorStand) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.ARMOR_STAND);
                as.setVisible(false);
                as.setItemInHand(((Player) e.getEntity()).getItemOnCursor());
                invis_players.put((Player) e.getEntity(), as);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(Main.getPlugin(Main.class), (Player) e.getEntity());
                }
                new BukkitRunnable() {
                    int counter = 1;

                    @Override
                    public void run() {
                        counter = counter + 1;
                        CraftPlayer player = (CraftPlayer) e.getEntity();
                        char chatColor = 'a';
                        if (counter / 20 > (invisible_time / 3)) {
                            chatColor = '6';
                        }
                        if (counter / 20 > (invisible_time / 2)) {
                            chatColor = 'c';
                        }
                        if (counter / 20 > (invisible_time)) {
                            chatColor = '4';
                        }
                        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Text.format(String.format("&eInvisible for &" + chatColor + "%s&as.", 30 - (counter / 20))) + "\"}");
                        PacketPlayOutChat packet = new PacketPlayOutChat(comp, ChatMessageType.GAME_INFO);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                        if ((30 - (counter / 20)) <= 0 || (!invis_players.containsKey(player))) {
                            cancel();
                        }
                    }
                }.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (invis_players.containsKey((Player) e.getEntity())) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.showPlayer(Main.getPlugin(Main.class), (Player) e.getEntity());
                                invis_players.get((Player) e.getEntity()).remove();
                            }
                            invis_players.remove((Player) e.getEntity());
                        }
                    }
                }.runTaskLater(Main.getPlugin(Main.class), (30 * 20));
            }
        }
    }
}
