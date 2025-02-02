package team.cqr.cqrepoured.entity.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.ISummoner;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;

public class EntitySummoningCircle extends Entity implements IEntityAdditionalSpawnData, IDontRenderFire {

	protected static final int BORDER_WHEN_TO_SPAWN_IN_TICKS = 60;

	protected ResourceLocation entityToSpawn;
	protected float timeMultiplierForSummon;
	protected ECircleTexture texture;
	protected ISummoner summoner;
	protected LivingEntity summonerLiving;
	protected Vector3d velForSummon = null;

	public enum ECircleTexture {
		ZOMBIE(), SKELETON(), FLYING_SKULL(), FLYING_SWORD(), METEOR();

		static {
			values();
		}
	}

	public EntitySummoningCircle(World worldIn) {
		this(worldIn, new ResourceLocation("minecraft", "zombie"), 1F, ECircleTexture.ZOMBIE, null);
	}

	public EntitySummoningCircle(World worldIn, ResourceLocation entityToSpawn, float timeMultiplier, ECircleTexture texture, ISummoner summoner) {
		this(worldIn, entityToSpawn, timeMultiplier, texture, summoner, null);
	}

	public EntitySummoningCircle(World worldIn, ResourceLocation entityToSpawn, float timeMultiplier, ECircleTexture texture, ISummoner isummoner, LivingEntity summoner) {
		super(worldIn);
		this.setSize(2.0F, 0.005F);
		this.entityToSpawn = entityToSpawn;
		this.timeMultiplierForSummon = timeMultiplier;
		this.texture = texture;
		this.summoner = isummoner;
		this.summonerLiving = summoner;
	}

	@Override
	protected void entityInit() {

	}

	@Override
	public void tick() {
		super.tick();

		if (!this.world.isRemote) {
			if (this.ticksExisted >= EntitySummoningCircle.BORDER_WHEN_TO_SPAWN_IN_TICKS * this.timeMultiplierForSummon) {
				Entity summon = EntityList.createEntityByIDFromName(this.entityToSpawn, this.world);

				if (summon != null) {
					summon.setPosition(this.posX, this.posY + 0.5D, this.posZ);

					if (this.velForSummon != null) {
						summon.motionX = this.velForSummon.x;
						summon.motionY = this.velForSummon.y;
						summon.motionZ = this.velForSummon.z;
						summon.velocityChanged = true;
					}

					this.world.spawnEntity(summon);

					if (this.summonerLiving != null && summon instanceof AbstractEntityCQR) {
						((AbstractEntityCQR) summon).setLeader(this.summonerLiving);
						Faction faction = FactionRegistry.instance(this).getFactionOf(this.summonerLiving);
						if (faction != null) {
							((AbstractEntityCQR) summon).setFaction(faction.getName());
						}
					}

					if (this.summoner != null && !this.summoner.getSummoner().isDead) {
						this.summoner.setSummonedEntityFaction(summon);
						this.summoner.tryEquipSummon(summon, this.world.rand);
						this.summoner.addSummonedEntityToList(summon);
					}
				}

				this.setDead();
			}
		} else {
			if (this.ticksExisted >= EntitySummoningCircle.BORDER_WHEN_TO_SPAWN_IN_TICKS * this.timeMultiplierForSummon * 0.8F) {
				for (int i = 0; i < 4; i++) {
					this.world.spawnParticle(ParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, this.rand.nextDouble(), this.rand.nextDouble(), this.rand.nextDouble());
				}

				if (!this.world.isRemote) {
					Faction faction = this.summoner != null ? this.summoner.getSummonerFaction() : null;
					for (Entity ent : this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(this.getPosition().add(this.width / 2, 0, this.width / 2), this.getPosition().add(-this.width / 2, 3, -this.width / 2)), faction != null ? TargetUtil.createPredicateNonAlly(faction) : TargetUtil.PREDICATE_LIVING)) {
						if (ent != null && ent.isEntityAlive() && ent instanceof LivingEntity) {
							((LivingEntity) ent).addPotionEffect(new EffectInstance(Effects.WITHER, 80, 0));
						}
					}
				}
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, 1.0F, 0.0F, 0.0F,
				// 20);
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, 0.5F, 0.0F, 0.5F,
				// 1);
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, 0.5F, 0.0F, -0.5F,
				// 1);
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, -0.5F, 0.0F, 0.5F,
				// 1);
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, -0.5F, 0.0F, -0.5F,
				// 1);
			}
		}
	}

	@Override
	public void readEntityFromNBT(CompoundNBT compound) {
		this.timeMultiplierForSummon = compound.getFloat("cqrdata.timeMultiplier");
		String resD = compound.getString("cqrdata.entityToSpawn.Domain");
		String resP = compound.getString("cqrdata.entityToSpawn.Path");
		this.entityToSpawn = new ResourceLocation(resD, resP);
		this.ticksExisted = compound.getInteger("ticksExisted");
	}

	@Override
	public void writeEntityToNBT(CompoundNBT compound) {
		compound.setFloat("cqrdata.timeMultiplier", this.timeMultiplierForSummon);
		compound.setString("cqrdata.entityToSpawn.Domain", this.entityToSpawn.getNamespace());
		compound.setString("cqrdata.entityToSpawn.Path", this.entityToSpawn.getPath());
		compound.setInteger("ticksExisted", this.ticksExisted);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeByte(this.texture.ordinal());
		buffer.writeFloat(this.timeMultiplierForSummon);
		buffer.writeInt(this.ticksExisted);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.texture = ECircleTexture.values()[additionalData.readByte()];
		this.timeMultiplierForSummon = additionalData.readFloat();
		this.ticksExisted = additionalData.readInt();
	}

	@OnlyIn(Dist.CLIENT)
	public int getTextureID() {
		return this.texture.ordinal();
	}

	public void setVelocityForSummon(Vector3d v) {
		this.velForSummon = v;
	}

	public void setSummon(ResourceLocation summon) {
		if (summon != null) {
			this.entityToSpawn = summon;
		}
	}

}
