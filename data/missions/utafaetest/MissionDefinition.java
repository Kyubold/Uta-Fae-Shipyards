package data.missions.utafaetest;

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
		api.initFleet(FleetSide.PLAYER, "UFS", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "ISS", FleetGoal.ATTACK, true);

//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.COORDINATED_MANEUVERS, 3);
//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.ELECTRONIC_WARFARE, 3);
		
		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "'Our Rugged Designs.'");
		api.setFleetTagline(FleetSide.ENEMY, "Their Buckets Of Junk");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Try out some ships");
		api.addBriefingItem("Don't Die");
		
		boolean testMode = false;
		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters
		//api.addToFleet(FleetSide.PLAYER, "station_small_Standard", FleetMemberType.SHIP, "Test Station", false);
		if (!testMode) {
			api.addToFleet(FleetSide.PLAYER, "utafae_canopy_standard", FleetMemberType.SHIP, "About Face", true);
			api.addToFleet(FleetSide.PLAYER, "utafae_tuber_standard", FleetMemberType.SHIP, "By Moloch!", false);
			api.addToFleet(FleetSide.PLAYER, "utafae_briar_escort", FleetMemberType.SHIP, "Morning Glory", false);
			api.addToFleet(FleetSide.PLAYER, "utafae_frond_standard", FleetMemberType.SHIP, "Fashionably Early", false);
			
			// Set up the enemy fleet.
			api.addToFleet(FleetSide.ENEMY, "mora_Assault", FleetMemberType.SHIP, "Obadiah's Legacy", false);
			api.addToFleet(FleetSide.ENEMY, "venture_Outdated", FleetMemberType.SHIP, "Ignoramous", false);
			api.addToFleet(FleetSide.ENEMY, "shrike_p_Attack", FleetMemberType.SHIP, "Look Behind You", false);
			api.addToFleet(FleetSide.ENEMY, "shrike_p_Attack", FleetMemberType.SHIP, "Flanks For The Memories", false);
			//api.addToFleet(FleetSide.ENEMY, "vanguard_pirates_Attack", FleetMemberType.SHIP, "Visualization Aid", false);
			
			api.defeatOnShipLoss("About Face");
		}
		
		if (testMode) {
//			FleetMemberAPI member = api.addToFleet(FleetSide.PLAYER, "omen_PD", FleetMemberType.SHIP, "Milk Run", true);
//			member.getCaptain().getStats().setSkillLevel(Skills.IMPACT_MITIGATION, 2);
//			member.getCaptain().getStats().setSkillLevel(Skills.SHIELD_MODULATION, 2);
//			member.getCaptain().getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
			
			api.addToFleet(FleetSide.PLAYER, "falcon_Attack", FleetMemberType.SHIP, "Stranger II", true);
	//		PersonAPI person = new AICoreOfficerPluginImpl().createPerson(Commodities.ALPHA_CORE, null, null);
	//		member.setCaptain(person);
			
			api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "Cherenkov Bloom", false);
			api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, null, false);
			api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, null, false);
			
			api.addObjective(0, 4000, BattleObjectives.SENSOR_JAMMER);
			api.addObjective(4000, 0, BattleObjectives.COMM_RELAY);
			api.addObjective(-3000, -2000, BattleObjectives.NAV_BUOY);
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
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
		api.addPlanet(0, 0, 50f, StarTypes.RED_GIANT, 250f, true);
		
	}

}
