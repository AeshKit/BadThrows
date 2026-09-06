package aesh.kai.config;

import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.resources.Identifier;

import static aesh.kai.BadThrows.MOD_ID;

public class ServerConfig extends Config {
    public ServerConfig() {
        super(Identifier.fromNamespaceAndPath(MOD_ID, "bad_config"));
    }

    public boolean globalToggle = true;
    public boolean doProjectileLogic = true;
    public boolean doLivingEntityLogic = true;
    public boolean doItemEntityLogic = true;
    public boolean doDropperLogic = true;

    public double directionVariance = 0.1D;
    public double magnitudeVariance = 0.075D;

    @Override
    public int defaultPermLevel() {
        return 2;
    }
}
