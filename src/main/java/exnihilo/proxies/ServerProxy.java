package exnihilo.proxies;

import exnihilo.ExNihilo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class ServerProxy extends Proxy {

    public ServerProxy() {
        Proxy.setInstance(this);
    }

    @Override
    public World getWorldObj() {
        World world = null;
        try {
            world = (MinecraftServer.getServer()).worldServers[0];
        } catch (Exception ex) {
            ExNihilo.log.error("Error while getting server side world reference");
        }
        return world;
    }
}
