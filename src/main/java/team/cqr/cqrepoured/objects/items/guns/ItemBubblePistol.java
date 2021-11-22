package team.cqr.cqrepoured.objects.items.guns;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.objects.items.INonEnchantable;
import team.cqr.cqrepoured.objects.items.ItemLore;
import team.cqr.cqrepoured.util.IRangedWeapon;

public class ItemBubblePistol extends ItemLore implements IRangedWeapon, INonEnchantable {

	private final Random rng = new Random();

	public ItemBubblePistol() {
		super();
		this.setMaxDamage(this.getMaxUses());
		this.setMaxStackSize(1);
	}

	public int getMaxUses() {
		return 200;
	}

	public double getInaccurary() {
		return 0.5D;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, this.getCooldown());
		}
		stack.damageItem(1, entityLiving);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		stack.damageItem(1, entityLiving);
		if (entityLiving instanceof EntityPlayer) {
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, this.getCooldown());
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof EntityLivingBase
				&& ((EntityLivingBase) entityIn).isHandActive()
				&& ((EntityLivingBase) entityIn).getActiveItemStack() == stack) {
			this.shootBubbles((EntityLivingBase) entityIn);
		}
	}

	private void shootBubbles(EntityLivingBase entity) {
		double x = -Math.sin(Math.toRadians(entity.rotationYaw));
		double z = Math.cos(Math.toRadians(entity.rotationYaw));
		double y = -Math.sin(Math.toRadians(entity.rotationPitch));
		this.shootBubbles(new Vec3d(x, y, z), entity);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	private void shootBubbles(Vec3d velocity, EntityLivingBase shooter) {
		Vec3d v = new Vec3d(-this.getInaccurary() + velocity.x + (2 * this.getInaccurary() * this.rng.nextDouble()),
				-this.getInaccurary() + velocity.y + (2 * this.getInaccurary() * this.rng.nextDouble()),
				-this.getInaccurary() + velocity.z + (2 * this.getInaccurary() * this.rng.nextDouble()));
		v = v.normalize();
		v = v.scale(1.4);

		shooter.playSound(CQRSounds.BUBBLE_BUBBLE, 1, 0.75F + (0.5F * shooter.getRNG().nextFloat()));

		ProjectileBubble bubble = new ProjectileBubble(shooter.world, shooter);
		bubble.motionX = v.x;
		bubble.motionY = v.y;
		bubble.motionZ = v.z;
		bubble.velocityChanged = true;
		shooter.world.spawnEntity(bubble);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public void shoot(World world, EntityLivingBase shooter, Entity target, EnumHand hand) {
		this.shootBubbles(shooter);
	}

	@Override
	public SoundEvent getShootSound() {
		// TODO: return bubble sound
		return SoundEvents.ENTITY_BOBBER_THROW;
	}

	@Override
	public double getRange() {
		return 32.0D;
	}

	@Override
	public int getCooldown() {
		return 80;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

	// INonEnchantable stuff
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return INonEnchantable.super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return INonEnchantable.super.isEnchantable(stack);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return INonEnchantable.super.isBookEnchantable(stack, book);
	}

}
