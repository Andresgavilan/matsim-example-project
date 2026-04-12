 package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.minibus.stats.operatorLogger.PlanElement;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;

import java.net.URL;

import com.jogamp.common.util.IOUtil;

public class RunMatsim202604 {

    public static void main (String[] args){

        URL url = ExamplesUtils.getTestScenarioURL( "equil");
        URL configfile = IOUtils.extendUrl( url, "config.xml");

        Config config = ConfigUtils.loadConfig(configfile);
//      Config config = ConfigUtils.loadConfig ( "scenarios/equil/config.xml");
        config.controller().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
        config.controller().setLastIteration(0);

//      config.plans().setActivityDurationInterpretation();

        Scenario scenario = ScenarioUtils.loadScenario(config);

//       for(Link link: scenario.getNetwork().getLinks().values()){
//           if (link.getId().toString().contains("keiserin"))
//               link.setFreespeed(10.);
//       }

//        for(Person person : scenario.getPopulation().getPersons().values()){
//            for Plan plan : person.getPlans() {
//                for (PlanElement planElement : plan.getPlanElements()){
//                    if(planElement instanceof Leg){
//                        ((Leg) planElement).setMode("pedelec");
//                    }
//                }
//            }
//        }

        Controler controler = new Controler (scenario);
//      controler.addOverridingModule(new OTFVisLiveModule());



        controler.run();
    }
}
