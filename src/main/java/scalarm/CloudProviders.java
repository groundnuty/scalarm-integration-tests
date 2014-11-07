package scalarm;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.Module;
import org.apache.http.MethodNotSupportedException;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.NovaApiMetadata;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import scalarm.DataBean;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;


public class CloudProviders  {



    public static DataBean getGWDGData() {

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

        computeServiceContext.close();


        //lets asume for now that we have one of each
        DataBean bean = new DataBean();
        bean.experimentManager = experimentManagers.iterator().next();
        bean.storageManager = storageManagers.iterator().next();
        bean.informationService = informationServices.iterator().next();
        bean.simulationManager = simulationManagers.iterator().next();


        return bean ;

    }

    public static DataBean getFlexiantData() throws MethodNotSupportedException {

        throw new MethodNotSupportedException("Flexiant not yet supported");

    }

    public static DataBean getAGHData() throws MethodNotSupportedException {

        throw new MethodNotSupportedException("AGH not yet supported");

    }

}