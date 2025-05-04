package data.missions.utafaederelict;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.BattleObjectives;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "ISS", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "DSS", FleetGoal.ATTACK, true);

//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.COORDINATED_MANEUVERS, 3);
//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.ELECTRONIC_WARFARE, 3);
		
		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "'The Intrepid Hero and their Peaceful Employers.'");
		api.setFleetTagline(FleetSide.ENEMY, "Unholy Abominations of Moloch. Not That I'm A Luddite.");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Protect the ISS Longest Burn");
		api.addBriefingItem("Don't Die");
		
		boolean testMode = false;
		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters
		//api.addToFleet(FleetSide.PLAYER, "station_small_Standard", FleetMemberType.SHIP, "Test Station", false);
		if (true) {
			api.addToFleet(FleetSide.PLAYER, "utafae_oxylo_standard", FleetMemberType.SHIP, "Intrepid", true);
			api.addToFleet(FleetSide.PLAYER, "venture_Balanced", FleetMemberType.SHIP, "Longest Burn", false);
			api.addToFleet(FleetSide.PLAYER, "utafae_tuber_standard", FleetMemberType.SHIP, "By Moloch!", false);
			api.addToFleet(FleetSide.PLAYER, "mule_Standard", FleetMemberType.SHIP, "Salveration", false);
			api.addToFleet(FleetSide.PLAYER, "shepherd_Frontier", FleetMemberType.SHIP, "Miner Problem", false);
			api.addToFleet(FleetSide.PLAYER, "shepherd_Frontier", FleetMemberType.SHIP, "Hyperspace Topographer", false);
			
			// Set up the enemy fleet.
			//cruisers
			api.addToFleet(FleetSide.ENEMY, "utafae_derelict_ranger_standard", FleetMemberType.SHIP, "Xi-Qi", false);
			//api.addToFleet(FleetSide.ENEMY, "rampart_Standard", FleetMemberType.SHIP, "Rho-Rover", false);

			//destroyers
			api.addToFleet(FleetSide.ENEMY, "utafae_derelict_curator_standard", FleetMemberType.SHIP, "David-Rex", false);
			//api.addToFleet(FleetSide.ENEMY, "utafae_derelict_curator_assault", FleetMemberType.SHIP, "Martel-Whiskey", false);
			//api.addToFleet(FleetSide.ENEMY, "berserker_Assault", FleetMemberType.SHIP, "Samuel-Prissy", false);

			//frigates
			api.addToFleet(FleetSide.ENEMY, "utafae_derelict_freeholder_standard", FleetMemberType.SHIP, "Daleth-Tucker", false);
			api.addToFleet(FleetSide.ENEMY, "utafae_derelict_freeholder_picket", FleetMemberType.SHIP, "Sade-Toby", false);
			api.addToFleet(FleetSide.ENEMY, "picket_Assault", FleetMemberType.SHIP, "Aladdin-San", false);
			api.addToFleet(FleetSide.ENEMY, "picket_Assault", FleetMemberType.SHIP, "Rikard-Ba", false);
			api.addToFleet(FleetSide.ENEMY, "picket_Assault", FleetMemberType.SHIP, "Aleph-Angel", false);
			api.addToFleet(FleetSide.ENEMY, "warden_Defense", FleetMemberType.SHIP, "Beth-Buddy", false);
			api.addToFleet(FleetSide.ENEMY, "warden_Defense", FleetMemberType.SHIP, "Taylor-Queen", false);

			
			api.defeatOnShipLoss("Longest Burn");
			api.defeatOnShipLoss("Intrepid");
		}
		
		
		
		// Set up the map.
		float width = 12000f;
		float height = 12000f;
		
		if (testMode) {
			width += 4000;
			height += 8000;
		}
		
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 90, 4000f,
							 20f, 120f, 200);
		
		api.addPlanet(0, 0, 100f, StarTypes.ORANGE, 250f, true);
		
	}

}
