package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;

class RunMatsim2{

    public static void main(String[]args){
        
        Config config = ConfigUtils.loadConfig (    "matsim-example-project/scenarios/equil/config.xml");

        config.controller().setLastIteration(5);
        config.controller().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);

        config.controller().setWritePlansInterval(1);

        Scenario scenario = ScenarioUtils.loadScenario(config); 

        Controler controler = new Controler (scenario); 

        controler.run();
    }

}
