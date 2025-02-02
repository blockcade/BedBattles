package net.blockcade.Arcade.games.BedBattles.Misc;

import net.blockcade.Arcade.Managers.GamePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class BedPlayer extends GamePlayer {

    private static HashMap<GamePlayer, BedPlayer> players = new HashMap<>();

    private ArrayList<Item> quickbuy = new ArrayList<>();
    private int BedDestroys = 0;

    public BedPlayer(Player player) {
        super(player);
        players.put(getGamePlayer(player),this);
    }

    public void getQuickbuy() {

    }

    public int getBedDestroys() {
        return BedDestroys;
    }
    public void setBedDestroys(int destroys) {
        this.BedDestroys=destroys;
    }

    public static BedPlayer getBedPlayer(GamePlayer player){
        if(!players.containsKey(player))new BedPlayer(player.getPlayer());
        return players.get(player);
    }
}
