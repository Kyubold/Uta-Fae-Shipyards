package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class utafae_UtanayPatternArmor extends BaseHullMod {

	public static float ARMOR_MULT = 1.1f;
	static int armor_bonus = 0;

	public static float ENERGY_DAMAGE_MULT = 1.25f;
	static float supplyMult = 1.5f;


	int addArmorBonus(int size){
		switch (size) {
			case 0:
				return 100;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
				
		
			case 1:
				return 200;
			    
			case 2:
				return 300;
				
			case 3:
				return 400;
				

			default:
				return -69;
				
		}
	}


	int size(HullSize hullSize){
		int size;

		if(hullSize == HullSize.FRIGATE){
			size = 0;
		}else if(hullSize == HullSize.DESTROYER){
			size = 1;
		}else if(hullSize == HullSize.CRUISER){
			size = 2;
		}else{
			size = 3;
		}

		return size;
	}

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

		int size = size(hullSize);

		
		stats.getEnergyDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_MULT);
		armor_bonus = addArmorBonus(size);

		stats.getArmorBonus().modifyFlat(id, armor_bonus);
		stats.getEffectiveArmorBonus().modifyMult(id, ARMOR_MULT);

		stats.getCRPerDeploymentPercent().modifyMult(id, supplyMult);
		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) Math.round((ARMOR_MULT - 1 )* 100f) + "%";
		if (index == 1) return "" + addArmorBonus(0);
		if (index == 2) return "" + addArmorBonus(1);
		if (index == 3) return "" + addArmorBonus( 2);
		if (index == 4) return "" + addArmorBonus(3);
		if (index == 5) return "" + (int) Math.round((ENERGY_DAMAGE_MULT - 1 )* 100f) + "%";
		return null;
	}


}








