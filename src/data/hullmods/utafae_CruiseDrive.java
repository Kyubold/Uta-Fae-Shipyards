package data.hullmods;



import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;


public class utafae_CruiseDrive extends BaseHullMod {


	//public static final float MANEUVER_PENALTY_MULT = 0.85f;


	private float getZeroFluxBoost(HullSize hullSize){

		switch (hullSize) {
			case FRIGATE:
				return 40f;

			case DESTROYER:
				return 35f;

			case CRUISER:
				return 30f;

			case CAPITAL_SHIP:
				return 25f;
		
			default:
				return 0;

		}
	}

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//float effect = stats.getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		float mult = 0.85f;
		
		
		
		stats.getAcceleration().modifyMult(id, mult);
		stats.getDeceleration().modifyMult(id, mult);
		stats.getTurnAcceleration().modifyMult(id, mult);
		stats.getMaxTurnRate().modifyMult(id, mult);
		

		float boost = getZeroFluxBoost(hullSize);
		stats.getZeroFluxSpeedBoost().modifyFlat(id, boost);


		//CompromisedStructure.modifyCost(hullSize, stats, id);
	}


	public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return Math.round(getZeroFluxBoost(HullSize.FRIGATE)) + "";
		if (index == 1) return Math.round(getZeroFluxBoost(HullSize.DESTROYER)) + "";
		if (index == 2) return Math.round(getZeroFluxBoost(HullSize.CRUISER)) + "";
		if (index == 3) return Math.round(getZeroFluxBoost(HullSize.CAPITAL_SHIP)) + "";
        return null;
    }



		@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return (ship.getHullSize() == HullSize.CAPITAL_SHIP || ship.getHullSize() == HullSize.CRUISER) &&
				!ship.getVariant().getHullMods().contains("unstable_injector") &&
				!ship.getVariant().getHullMods().contains("safetyoverrides");
	}
	
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("unstable_injector")) {
			return "Incompatible with Unstable Injector";
		}
		if (ship.getVariant().getHullMods().contains("safetyoverrides")) {
			return "Incompatible with Safety Overrides";
		}

		
		return null;
	}

}
