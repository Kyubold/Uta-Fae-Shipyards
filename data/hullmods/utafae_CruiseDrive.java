package data.hullmods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.util.Misc;

public class utafae_CruiseDrive extends BaseHullMod {


	//public static final float MANEUVER_PENALTY_MULT = 0.85f;
	public float ZERO_FLUX_BOOST = 40f;

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//float effect = stats.getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		float mult = 0.85f;
		
		
		
		stats.getAcceleration().modifyMult(id, mult);
		stats.getDeceleration().modifyMult(id, mult);
		stats.getTurnAcceleration().modifyMult(id, mult);
		stats.getMaxTurnRate().modifyMult(id, mult);
		
		stats.getZeroFluxSpeedBoost().modifyFlat(id, ZERO_FLUX_BOOST);


		//CompromisedStructure.modifyCost(hullSize, stats, id);
	}


	public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return Math.round(ZERO_FLUX_BOOST) + "";
        return null;
    }

}
