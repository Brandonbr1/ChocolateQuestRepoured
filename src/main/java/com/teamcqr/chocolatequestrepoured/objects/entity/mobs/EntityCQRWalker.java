package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIFireFighter;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAITorchIgniter;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityCQRWalker extends AbstractEntityCQR {

	public EntityCQRWalker(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(14, new EntityAIFireFighter(this));
		this.tasks.addTask(22, new EntityAITorchIgniter(this));
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.WALKER.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.WALKERS;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WITHER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDERMEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesNormal.ENTITY_WALKER.getLootTable();
	}

	@Override
	public int getTextureCount() {
		return 1;
	}

	@Override
	public boolean canRide() {
		return true;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	@Override
	public boolean canOpenDoors() {
		return true;
	}

}
