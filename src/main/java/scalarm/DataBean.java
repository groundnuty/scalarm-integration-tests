package scalarm;

import org.jclouds.compute.domain.NodeMetadata;


public class DataBean {

    NodeMetadata _storageManager ;
    NodeMetadata _informationService ;
    NodeMetadata _simulationManager ;
    NodeMetadata _experimentManager ;

    String storageManagerUrl;
    String informationServiceUrl;
    String simulationManagerUrl;
    String experimentManagerUrl;

    int simulationManagerPortAlive;
    int simulationManagerPortRunning;

}
