package team.cqr.cqrepoured.item.staff;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.ISupportWeapon;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.item.sword.ItemFakeSwordHealingStaff;

public class ItemStaffHealing extends ItemLore implements ISupportWeapon<ItemFakeSwordHealingStaff> {

	public static final float HEAL_AMOUNT_ENTITIES = 4.0F;

	public ItemStaffHealing() {
		this.setMaxDamage(128);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (!player.world.isRemote && entity instanceof LivingEntity && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
			((LivingEntity) entity).heal(HEAL_AMOUNT_ENTITIES);
			entity.setFire(0);
			((ServerWorld) player.world).spawnParticle(ParticleTypes.HEART, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			player.world.playSound(null, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ, CQRSounds.MAGIC, SoundCategory.MASTER, 0.6F, 0.6F + itemRand.nextFloat() * 0.2F);
			stack.damageItem(1, player);
			player.getCooldownTracker().setCooldown(stack.getItem(), 30);
		}
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.0D, 0));
		}

		return multimap;
	}

	@Override
	public ItemFakeSwordHealingStaff getFakeWeapon() {
		return CQRItems.DIAMOND_SWORD_FAKE_HEALING_STAFF;
	}

}
