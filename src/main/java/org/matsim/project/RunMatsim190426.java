package org.matsim.project;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.drt.analysis.afterSimAnalysis.DrtVehicleStoppingTaskWriter;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.config.groups.ReplanningConfigGroup;
import org.matsim.core.config.groups.RoutingConfigGroup;
import org.matsim.core.config.groups.ScoringConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;
import scala.util.parsing.combinator.testing.Str;

import java.nio.file.Path;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists;
import static org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles;

class RunMatsim190426{

    public static  void main(String[] args){

        Config config = ConfigUtils.loadConfig("scenarios/equil/config-2026.xml");

        config.controller().setOverwriteFileSetting(deleteDirectoryIfExists);
        config.controller().setLastIteration(15);

        config.qsim().setLinkDynamics(QSimConfigGroup.LinkDynamics.PassingQ);

//        config.routing().clearTeleportedModeParams();
//
        {
            Collection<String> modes = new ArrayList<>();
            modes.add(TransportMode.car);
            modes.add("pedelec");
            config.routing().setNetworkModes(modes);
        }
        {
            ReplanningConfigGroup.StrategySettings settings = new ReplanningConfigGroup.StrategySettings();
            settings.setStrategyName(DefaultPlanStrategiesModule.DefaultStrategy.SubtourModeChoice);
            settings.setWeight(0.3);
            config.replanning().addStrategySettings(settings);
        }
        {
            String[]modes = {"car", "pedelec"};
            config.subtourModeChoice().setChainBasedModes(modes);
            config.subtourModeChoice().setModes(modes);
        }

//        {
//            RoutingConfigGroup.TeleportedModeParams params = new RoutingConfigGroup.TeleportedModeParams("walk");
//            params.setTeleportedModeSpeed(25*3.6);
//            params.setBeelineDistanceFactor(1.3);
//            config.routing().addTeleportedModeParams(params);
//        }
//        {
//            RoutingConfigGroup.TeleportedModeParams params = new RoutingConfigGroup.TeleportedModeParams("pedelec");
//            params.setTeleportedModeSpeed(25*3.6);
//            params.setBeelineDistanceFactor(1.3);
//            config.routing().addTeleportedModeParams(params);
//        }
        {
            Collection<String> modes= new ArrayList<>();
            modes.add(TransportMode.car);
            modes.add("pedelec");
            config.qsim().setMainModes(modes);
        }
        {
            ScoringConfigGroup.ModeParams params = new ScoringConfigGroup.ModeParams("pedelec");
            config.scoring().addModeParams(params);
        }

//        {
//            RoutingConfigGroup.TeleportedModeParams params = new RoutingConfigGroup.TeleportedModeParams("cars");
//            params.setTeleportedModeSpeed(25*3.6);
//            params.setBeelineDistanceFactor(1.3);
//            config.routing().addTeleportedModeParams(params);
//        }


        /*{
            ScoringConfigGroup.ActivityParams params = new ScoringConfigGroup.ActivityParams("w");
            config.scoring().addActivityParams(params);
        }*/

        {
            config.qsim().setVehiclesSource(QSimConfigGroup.VehiclesSource.modeVehicleTypesFromVehiclesData);
        }
//
        Scenario scenario = ScenarioUtils.loadScenario(config);

        {
            Id<VehicleType>vehicleId = Id.create("pedelec", VehicleType.class);
            VehicleType pedelecType = VehicleUtils.createVehicleType(vehicleId);
            pedelecType.setMaximumVelocity(25.*3.6);
            scenario.getVehicles().addVehicleType(pedelecType);
        }
        {
            Id<VehicleType>vehicleId = Id.create("car", VehicleType.class);
            VehicleType carType = VehicleUtils.createVehicleType(vehicleId);
            carType.setMaximumVelocity(25./3.6);
            scenario.getVehicles().addVehicleType(carType);
        }
        for (Link link: scenario.getNetwork().getLinks().values()) {
            Set<String> modes = new HashSet<>();
            modes.add("car");
            modes.add("pedelec");
            link.setAllowedModes(modes);
        }

        //--

        Controler controler = new Controler(scenario);

        controler.run();

    }
}
