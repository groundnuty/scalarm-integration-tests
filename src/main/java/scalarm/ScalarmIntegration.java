package scalarm;

import org.apache.http.MethodNotSupportedException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ScalarmIntegration {
    static Logger logger = Logger.getLogger("scalarmScalarm.Integration");

    public static void main(String args[]) {
        logger.setLevel(Level.WARNING);

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
            System.exit(1);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            System.exit(1);
        }


        logger.log(Level.INFO, bean.experimentManagerUrl);
        logger.log(Level.INFO, bean.simulationManagerUrl);
        logger.log(Level.INFO, bean.informationServiceUrl);
        logger.log(Level.INFO, bean.storageManagerUrl);

/*      String experimentManagerIp =  "141.5.102.194";//gwgd.bean.experimentManagerUrl.getPublicAddresses().iterator().next();
        String simulationManagerIp =  "141.5.101.186" ;//gwgd.bean.simulationManagerUrl.getPublicAddresses().iterator().next();
        String informationServiceIp =  "141.5.102.188" ;//gwgd.bean.informationServiceUrl.getPublicAddresses().iterator().next();
        String storageManagerIp =  "141.5.102.151" ;//gwgd.bean.storageManagerUrl.getPublicAddresses().iterator().next();
*/
        JUnitCore junit = new JUnitCore();

        System.setProperty("storageManagerIp",bean.storageManagerUrl) ;
        System.setProperty("informationServiceIp", bean.informationServiceUrl) ;
        System.setProperty("simulationManagerIp", bean.simulationManagerUrl) ;
        System.setProperty("simulationManagerPortAlive", Integer.toString(bean.simulationManagerPortAlive)) ;
        System.setProperty("simulationManagerPortRunning", Integer.toString(bean.simulationManagerPortRunning)) ;
        System.setProperty("experimentManagerIp", bean.experimentManagerUrl) ;

        Result result = junit.run(scalarm.ScalarmResponsesTest.class);

        if(result.getFailureCount()==0) {
            System.exit(0);
        } else {
            for(Failure f: result.getFailures()){
                System.err.println(f.getTestHeader());
                System.err.println(f.getException());
                System.err.println(f.getTrace());
            }
            System.exit(1);
        }
    }
}
