package io.github.mrsdarth.skirt;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.google.gson.JsonElement;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Skirt extends JavaPlugin implements Listener {


    private void tryLoad(SkriptAddon skirt, String name, String... subPackages) {
        getLogger().info("Loading " + name + " . . .");
        try {
            skirt.loadClasses("io.github.mrsdarth.skirt", subPackages);
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().info("Failed to load " + name);
        }
    }


    @Override
    public void onEnable() {

        SkriptAddon skirt = Skript.registerAddon(this);

        if (Skirtness.hasPaper())
            tryLoad(skirt, "Paper elements", "paper");

        if (Skirtness.hasNBT())
            tryLoad(skirt, "NBT api", "nbt");

        if (Skirtness.hasProtocolLib())
            tryLoad(skirt, "Protocol Lib", "protocolLib");

        if (Skirtness.isSkriptv2_6())
            tryLoad(skirt, "skript 2.6 elements", "skriptv2_6");

        tryLoad(skirt, "skirt", "elements");


        if (getServer().getOnlineMode()) {
            String skirtGithub = "MrsDarth/Skirt/releases/latest";
            HttpUtils.simpleGetRequest("https://api.github.com/repos/" + skirtGithub)
                    .map(HttpUtils::parseJson)
                    .map(JsonElement::getAsJsonObject)
                    .ifPresent(body -> {
                        String latestVersion = body.get("tag_name").getAsString();
                        if (getDescription().getVersion().equals(latestVersion))
                            Skirtness.registerEvent(PlayerJoinEvent.class, event -> {
                                if (event.getPlayer().isOp())
                                    event.getPlayer().sendMessage(
                                            Component.text("Go get", NamedTextColor.LIGHT_PURPLE)
                                                    .append(Component.text("Skirt " + latestVersion, NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                                                    .append(Component.text("here", NamedTextColor.AQUA)
                                                            .hoverEvent(HoverEvent.showText(Component.text("Click to go to latest Skirt version", NamedTextColor.GREEN)))
                                                            .clickEvent(ClickEvent.openUrl("https://github.com/" + skirtGithub)))
                                    );
                            });
                    });
        }

        new Metrics(this, 11834);

    }


    @Override
    public void onDisable() {

    }


}
