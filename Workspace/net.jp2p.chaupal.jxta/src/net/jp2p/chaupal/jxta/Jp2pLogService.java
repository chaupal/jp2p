package net.jp2p.chaupal.jxta;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * <p>
 * Wrapper class which listens for all framework services of
 * class ImoduleFactory. Each such service is picked
 * up and installed into the module container
 * </p>
 * <p>
 * <p>
 * The alias used for the servlet/resource is taken from the 
 * PROP_ALIAS service property.
 * </p>
 * <p>
 * The resource dir used for contexts is taken from the 
 * PROP_DIR service property.
 * </p>
 */
public class Jp2pLogService {

	private BundleContext  bc;

	private ServiceTracker<BundleContext,LogService> logServiceTracker;
	private static LogService logService;

	Jp2pLogService(BundleContext bc) {
		this.bc = bc;
	}

	/**
	 * Open the service
	 */
	void open() {
		// create a tracker and track the log service
		logServiceTracker = 
				new ServiceTracker<BundleContext,LogService>( bc, LogService.class.getName(), null);
		logServiceTracker.open();

		// grab the service
		logService = logServiceTracker.getService();

		if(logService != null)
			logService.log(LogService.LOG_INFO, "Logging service started");
	}

	void close() {
		if(logService != null)
			logService.log(LogService.LOG_INFO, "Logging service Stopped");
		
		// close the service tracker
		logServiceTracker.close();
		logServiceTracker = null;
	}

	/**
	 * Get the log service
	 * @return
	 */
	LogService getLogService(){
		return logService;
	}
}
