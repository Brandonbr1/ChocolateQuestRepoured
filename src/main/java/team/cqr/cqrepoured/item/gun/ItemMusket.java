package team.cqr.cqrepoured.item.gun;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet;
import team.cqr.cqrepoured.init.CQRPotions;
import team.cqr.cqrepoured.init.CQRSounds;

public class ItemMusket extends ItemRevolver {

	public ItemMusket() {
		this.setMaxDamage(300);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + "7.5 " + I18n.format("description.bullet_damage.name"));
		tooltip.add(TextFormatting.RED + "-60 " + I18n.format("description.fire_rate.name"));
		tooltip.add(TextFormatting.RED + "-10" + "% " + I18n.format("description.accuracy.name"));
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.gun.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	protected float getRecoil() {
		return 0.5F * super.getRecoil();
	}

	@Override
	public void shoot(ItemStack stack, World worldIn, EntityPlayer player) {
		boolean flag = player.capabilities.isCreativeMode;
		ItemStack itemstack = this.findAmmo(player);

		if (!itemstack.isEmpty() || flag) {
			if (!worldIn.isRemote) {
				if (flag && itemstack.isEmpty()) {
					ProjectileBullet bulletE = new ProjectileBullet(worldIn, player, 1);
					bulletE.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 2F);
					player.getCooldownTracker().setCooldown(stack.getItem(), 30);
					worldIn.spawnEntity(bulletE);
				} else {
					ProjectileBullet bulletE = new ProjectileBullet(worldIn, player, this.getBulletType(itemstack));
					bulletE.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 2F);
					player.getCooldownTracker().setCooldown(stack.getItem(), 30);
					worldIn.spawnEntity(bulletE);
					stack.damageItem(1, player);
				}
			}

			worldIn.playSound(player.posX, player.posY + player.getEyeHeight(), player.posZ, this.getShootSound(), SoundCategory.MASTER, 1.0F, 0.9F + itemRand.nextFloat() * 0.2F, false);
			player.rotationPitch -= worldIn.rand.nextFloat() * 10;

			if (!flag) {
				itemstack.shrink(1);

				if (itemstack.isEmpty()) {
					player.inventory.deleteStack(itemstack);
				}
			}
		}
	}

	@Override
	public SoundEvent getShootSound() {
		return CQRSounds.MUSKET_SHOOT;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof EntityLivingBase)) {
			return;
		}
		if (!isSelected) {
			return;
		}
		EntityLivingBase entityLiving = (EntityLivingBase) entityIn;
		ItemStack offhand = entityLiving.getHeldItemOffhand();
		if (!offhand.isEmpty()) {
			entityLiving.addPotionEffect(new PotionEffect(CQRPotions.TWOHANDED, 30, 1));
		}
	}

}
