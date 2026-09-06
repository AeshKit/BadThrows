package aesh.kai.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public class Configs {
    public static ServerConfig syncedConfig = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);

    public static void init() {}
}
