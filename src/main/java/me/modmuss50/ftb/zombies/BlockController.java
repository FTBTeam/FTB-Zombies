package me.modmuss50.ftb.zombies;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;

public class BlockController extends BlockMachineBase {

	public BlockController() {
		setUnlocalizedName("ftbzombies.controller");
		setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileController();
	}
}
