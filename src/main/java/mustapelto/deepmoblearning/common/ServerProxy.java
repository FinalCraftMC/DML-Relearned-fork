package mustapelto.deepmoblearning.common;

import mustapelto.deepmoblearning.client.ClientProxy;
import net.minecraft.world.World;

public class ServerProxy {
    // Client-only methods
    public void registerGuiRenderers() {}
    public void spawnSmokeParticle(World world, double x, double y, double z, double mx, double my, double mz, ClientProxy.SmokeType type) {}
}
