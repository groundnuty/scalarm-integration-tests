package scalarm;

import org.apache.http.MethodNotSupportedException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ScalarmIntegration {
    static Logger logger = Logger.getLogger("scalarmScalarm.Integration");

    public static void main(String args[]) {
        logger.setLevel(Level.OFF);

        DataBean bean = null;
        try {
        if(args.length > 0) {
            if(args[0].equals("FLEXIANT")) {
                    bean = CloudProviders.getFlexiantData();
            } else if (args[0].equals("GWDG")) {
                    bean = CloudProviders.getGWDGData();
            } else if (args[0].equals("AGH")) {
                bean = CloudProviders.getAGHData();
            } else {
                System.err.println("Unsupported argument. First argument has to be: FLEXIANT or GWDG or AGH");
                System.exit(1);
            }
        } else {
            System.err.println("Too few arguments. First argument has to be: FLEXIANT or GWDG or AGH");
            System.exit(1);
        }
        } catch (MethodNotSupportedException e) {
            e.printStackTrace();
        }

        String experimentManagerIp = bean.experimentManager.getPublicAddresses().iterator().next();
        String simulationManagerIp = bean.simulationManager.getPublicAddresses().iterator().next();
        String informationServiceIp =  bean.informationService.getPublicAddresses().iterator().next();
        String storageManagerIp =  bean.storageManager.getPublicAddresses().iterator().next();

        logger.log(Level.INFO,bean.experimentManager.getPublicAddresses().toString());
        logger.log(Level.INFO, bean.simulationManager.getPublicAddresses().toString());
        logger.log(Level.INFO, bean.informationService.getPublicAddresses().toString());
        logger.log(Level.INFO, bean.storageManager.getPublicAddresses().toString());

/*      String experimentManagerIp =  "141.5.102.194";//gwgd.bean.experimentManager.getPublicAddresses().iterator().next();
        String simulationManagerIp =  "141.5.101.186" ;//gwgd.bean.simulationManager.getPublicAddresses().iterator().next();
        String informationServiceIp =  "141.5.102.188" ;//gwgd.bean.informationService.getPublicAddresses().iterator().next();
        String storageManagerIp =  "141.5.102.151" ;//gwgd.bean.storageManager.getPublicAddresses().iterator().next();
*/
        JUnitCore junit = new JUnitCore();

        System.setProperty("storageManagerIp","https://"+
                storageManagerIp
                +":20001/status") ;
        System.setProperty("informationServiceIp", "https://"+
                informationServiceIp
                +":11300/experiment_managers") ;
        System.setProperty("simulationManagerIp", experimentManagerIp) ;
        System.setProperty("experimentManagerIp", "https://"+
                experimentManagerIp
                +":443/status") ;

        Result result = junit.run(scalarm.ScalarmResponsesTest.class);

        if(result.getFailureCount()==0) {
            System.exit(0);
        } else {
            for(Failure f: result.getFailures()){
                System.err.println(f.getTestHeader());
                System.err.println(f.getMessage());
                System.err.println(f.getException());
                System.err.println(f.getTrace());
            }
            System.exit(1);
        }
    }
}
