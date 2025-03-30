package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.hullmods.BallisticRangefinder.RangefinderRangeModifier;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.AIHints;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.loading.WeaponSlotAPI;

//import data.hullmods.utafae_siege.utafaeSiegeBaseModifier;

public class utafae_siege extends BaseHullMod {

	float baseRangeBonus = 100f;
	float baseRangePenalty = 100f;

	float rangeMult = 100f;
	float velocityMult = 30f;

	float fluxPenalty = 40f;

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0)
			return "" + baseRangeBonus + " su";
		if (index == 1)
			return "" + rangeMult + "%";
		if (index == 2)
			return "" + velocityMult + "%";
		if (index == 3)
			return "" + baseRangePenalty + " su";
		if (index == 4)
			return "" + fluxPenalty + "%";
		return null;
	}

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getBallisticWeaponRangeBonus().modifyPercent(id, rangeMult);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, rangeMult);
		stats.getProjectileSpeedMult().modifyPercent(id, velocityMult);
		//stats.getBallisticProjectileSpeedMult.modifyPercent(id, velocityMult);

		stats.getFluxDissipation().modifyPercent(id, fluxPenalty);

		
	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id){
		float small = baseRangePenalty;
		float medium = baseRangePenalty;
		float large = baseRangeBonus;

		ship.addListener(new RangefinderRangeModifier(small, medium, 1000));
	}




	public static class RangefinderRangeModifier implements WeaponBaseRangeModifier {
		public float small, medium, max;
		public RangefinderRangeModifier(float small, float medium, float max) {
			this.small = small;
			this.medium = medium;
			this.max = max;
		}
		
		public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			return 0;
		}
		public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			return 1f;
		}
		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
//			if (weapon.getSlot() == null || weapon.getSlot().getWeaponType() != WeaponType.BALLISTIC) {
//				return 0f;
//			}
			if (weapon.getSpec() == null) {
				return 0f;
			}
			if (weapon.getSpec().getMountType() != WeaponType.BALLISTIC && 
					weapon.getSpec().getMountType() != WeaponType.HYBRID &&
					weapon.getSpec().getMountType() != WeaponType.ENERGY) {
				return 0f;
			}
			
			
			float bonus = 0;
			if (weapon.getSize() == WeaponSize.SMALL) {
				bonus = small;
			} else if (weapon.getSize() == WeaponSize.MEDIUM) {
				bonus = medium;
			} else if (weapon.getSize() == WeaponSize.LARGE){
				bonus = medium * -1;
			}
			
			if (bonus == 0f) return 0f;
			
			float base = weapon.getSpec().getMaxRange();
			if (base + bonus > max) {
				bonus = max - base;
			}
			if (bonus < 0) bonus = 0;
			return bonus;
		}
	}


	/* 
	 * public static class utafaeSiegeBaseModifier implements WeaponBaseRangeModifier {
		public float small, medium, large;

		public utafaeSiegeBaseModifier(float small, float medium, float large) {
			this.small = small;
			this.medium = medium;
			this.large = large;
		}

		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {

			if (weapon.getSpec() == null) {
				return 0f;
			}
			if (weapon.getSpec().getMountType() != WeaponType.BALLISTIC &&
					weapon.getSpec().getMountType() != WeaponType.HYBRID &&
					weapon.getSpec().getMountType() != WeaponType.ENERGY) {
				return 0f;
			}

			float bonus = 0;
			if (weapon.getSize() == WeaponSize.SMALL) {
				bonus = small;
			} else if (weapon.getSize() == WeaponSize.MEDIUM) {
				bonus =  medium;
			} else {
				bonus = large;
			}

			bonus += weapon.getSpec().getMaxRange();
			return bonus;

		}
		public float getWeaponBaseRangeMultMod(ShipAPI arg0, WeaponAPI arg1) {

			return 0;
		}


		public float getWeaponBaseRangePercentMod(ShipAPI arg0, WeaponAPI arg1) {
			return 1f;
		}
	}
	 * 
	*/
	

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return (ship.getHullSize() == HullSize.CAPITAL_SHIP || ship.getHullSize() == HullSize.CRUISER) &&
				!ship.getVariant().getHullMods().contains("targetingunit") &&
				!ship.getVariant().getHullMods().contains(HullMods.DISTRIBUTED_FIRE_CONTROL) &&
				!ship.getVariant().getHullMods().contains("dedicated_targeting_core") &&
				!ship.getVariant().getHullMods().contains("ballistic_rangefinder") &&
				!ship.getVariant().getHullMods().contains("advancedcore");
	}

	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.getHullSize() != HullSize.CAPITAL_SHIP && ship.getHullSize() != HullSize.CRUISER) {
			return "Can only be installed on cruisers and capital ships";
		}
		if (ship.getVariant().getHullMods().contains("targetingunit")) {
			return "Incompatible with Integrated Targeting Unit";
		}
		if (ship.getVariant().getHullMods().contains("advancedcore")) {
			return "Incompatible with Advanced Targeting Core";
		}
		if (ship.getVariant().getHullMods().contains("dedicated_targeting_core")) {
			return "Incompatible with Dedicated Targeting Core";
		}
		if (ship.getVariant().getHullMods().contains("ballistic_rangefinder")) {
			return "Incompatible with Ballistic Rangefinder";
		}
		if (ship.getVariant().getHullMods().contains(HullMods.DISTRIBUTED_FIRE_CONTROL)) {
			return "Incompatible with Distributed Fire Control";
		}

		return null;
	}

}
