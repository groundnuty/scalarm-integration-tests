package scalarm;

import com.google.common.base.Predicate;
import org.apache.http.MethodNotSupportedException;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.openstack.nova.v2_0.NovaApiMetadata;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;

public class CloudProviders  {



    public static DataBean getGWDGData() throws NoSuchElementException{

        String endpoint = "https://api.cloud.gwdg.de:5000/v2.0/";
        //ContextBuilder computeServiceContext = ContextBuilder.newBuilder("openstack-nova");
        ContextBuilder b = ContextBuilder.newBuilder(new NovaApiMetadata());
        ContextBuilder bb = b.endpoint(endpoint);
        ContextBuilder bbb = bb.credentials("149671:paasage-wp5@ercim.eu", "paasagewp5ostestbed!");
        ComputeServiceContext computeServiceContext = bbb.buildView(ComputeServiceContext.class);

        ComputeService computeService = computeServiceContext.getComputeService();


        Set<? extends NodeMetadata> informationServices = computeService.listNodesDetailsMatching(new Predicate<ComputeMetadata>() {
            @Override
            public boolean apply(ComputeMetadata computeMetadata) {
                return computeMetadata.getName().contains("informationService");
            }
        });

        Set<? extends NodeMetadata> experimentManagers = computeService.listNodesDetailsMatching(new Predicate<ComputeMetadata>() {
            @Override
            public boolean apply(ComputeMetadata computeMetadata) {
                return computeMetadata.getName().contains("experimentManager");
            }
        });

        Set<? extends NodeMetadata> storageManagers = computeService.listNodesDetailsMatching(new Predicate<ComputeMetadata>() {
            @Override
            public boolean apply(ComputeMetadata computeMetadata) {
                return computeMetadata.getName().contains("storageManager");
            }
        });

        Set<? extends NodeMetadata> simulationManagers = computeService.listNodesDetailsMatching(new Predicate<ComputeMetadata>() {
            @Override
            public boolean apply(ComputeMetadata computeMetadata) {
                return computeMetadata.getName().contains("simulationManager");
            }
        });



        if(informationServices.isEmpty() && experimentManagers.isEmpty() && storageManagers.isEmpty() && simulationManagers.isEmpty()) {
            System.out.println("Services present:");
            for(ComputeMetadata m : computeService.listNodes()) {
                System.out.println(m.getName());
            }
            throw new NoSuchElementException("No scalarm services found at GWDG") ;
        }
        if(informationServices.isEmpty()) {
            throw new NoSuchElementException("No informationServices managers found in GWDG") ;
        }else {
            System.out.println("informationServices");
            for(NodeMetadata m :informationServices) {
                System.out.print(m.getName());
                for(String s :m.getPublicAddresses()) {
                    System.out.print(" "+s+" ");
                }
                System.out.println();
            }}
        if(experimentManagers.isEmpty()) {
            throw new NoSuchElementException("No experimentManagers managers found in GWDG") ;
        }else {
            System.out.println("experimentManagers");
            for(NodeMetadata m :experimentManagers) {
                System.out.print(m.getName());
                for(String s :m.getPublicAddresses()) {
                    System.out.print(" "+s+" ");
                }
                System.out.println();
            }}
        if(storageManagers.isEmpty()) {
            throw new NoSuchElementException("No storageManagers managers found in GWDG") ;
        }else {
            System.out.println("storageManagers");
            for(NodeMetadata m :storageManagers) {
                System.out.print(m.getName());
                for(String s :m.getPublicAddresses()) {
                    System.out.print(" "+s+" ");
                }
                System.out.println();
            }}
        if(simulationManagers.isEmpty()) {
            throw new NoSuchElementException("No simulationManagers found in GWDG") ;
        } else {
           System.out.println("Simulation managers");
           for(NodeMetadata m :simulationManagers) {
               System.out.print(m.getName());
               for(String s :m.getPublicAddresses()) {
               System.out.print(" "+s+" ");
           }
               System.out.println();
        }}
        computeServiceContext.close();


        //lets asume for now that we have one of each
        DataBean bean = new DataBean();



        bean._experimentManager =  experimentManagers.iterator().next() ;
        bean._storageManager = storageManagers.iterator().next();
        bean._informationService = informationServices.iterator().next();
        bean._simulationManager = simulationManagers.iterator().next();

        bean.experimentManagerUrl = "https://" + bean._experimentManager.getPublicAddresses().iterator().next()  + ":443/status" ;
        bean.storageManagerUrl = "https://" + bean._storageManager.getPublicAddresses().iterator().next() + ":20001/status";
        bean.informationServiceUrl = "https://" + bean._informationService.getPublicAddresses().iterator().next() + ":11300/experiment_managers";
        bean.simulationManagerUrl = bean._simulationManager.getPublicAddresses().iterator().next();
        bean.simulationManagerPortAlive = 7101;
        bean.simulationManagerPortRunning = 7102;

        return bean ;

    }

    public static DataBean getFlexiantData() throws MethodNotSupportedException {

        throw new MethodNotSupportedException("Flexiant not yet supported");

    }

    public static DataBean getAGHData() throws MethodNotSupportedException {

        DataBean bean = new DataBean();
        bean.experimentManagerUrl = "https://149.156.10.32:50047/status" ;
        bean.storageManagerUrl = "https://149.156.10.32:16450/status";
        bean.informationServiceUrl = "https://149.156.10.32:31034/experiment_managers" ;
        bean.simulationManagerUrl = "149.156.10.32" ;
        bean.simulationManagerPortAlive = 50047;
        bean.simulationManagerPortRunning = 50047;

        //throw new MethodNotSupportedException("AGH not yet supported");
        return bean;

    }

}