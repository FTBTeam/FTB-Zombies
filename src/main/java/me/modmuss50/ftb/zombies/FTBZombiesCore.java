package me.modmuss50.ftb.zombies;

import javassist.LoaderClassPath;
import me.modmuss50.ftb.zombies.mixin.ZombieMixinProvider;
import me.modmuss50.fusion.MixinManager;
import me.modmuss50.fusion.transformer.MixinTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.classloading.FMLForgePlugin;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * To run this in dev you need to add the following to the *VM* Options in the run config
 *
 * -Dfml.coreMods.load=me.modmuss50.ftb.zombies.FTBZombiesCore
 */
@IFMLLoadingPlugin.SortingIndex(1001)//Ensures that the target classes have been mapped to srg names
@IFMLLoadingPlugin.Name("FTBZombiesCore")
@IFMLLoadingPlugin.TransformerExclusions({"me.modmuss50.fusion", "javassist", "me.modmuss50.ftb.zombies.mixin"})
public class FTBZombiesCore implements IFMLLoadingPlugin {

    private static boolean setup;

    @Override
    public String[] getASMTransformerClass() {
        if(!setup){
            setup = true;
            MixinManager.logger.info("Setting up fusion environment");
            MixinManager.registerMixinProvicer(ZombieMixinProvider.class);
            MixinManager.RUNTIME_DEOBF = FMLForgePlugin.RUNTIME_DEOBF;
            MixinManager.methodRemapper = FMLDeobfuscatingRemapper.INSTANCE::mapMethodName;
            MixinTransformer.cp.appendClassPath(new LoaderClassPath(Launch.classLoader));
        }
        return new String[]{"me.modmuss50.fusion.transformer.MixinTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
