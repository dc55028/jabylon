package de.jutzig.jabylon.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import de.jutzig.jabylon.properties.Project;
import de.jutzig.jabylon.ui.team.TeamProvider;

public class Activator extends Plugin {

	private static BundleContext context;
	private static Activator activator;
	public static final String PLUGIN_ID = "de.jutzig.jabylon.ui";

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		Activator.context = bundleContext;
		activator = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		Activator.context = null;
		activator = null;
	}
	
	public static Activator getDefault()
	{
		return activator;
	}


	public static void error(String message, Throwable cause)
	{
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message,cause));
	}

	public IConfigurationElement[] getReviewParticipants()
	{
		IConfigurationElement[] elements = RegistryFactory.getRegistry().getConfigurationElementsFor("de.jutzig.jabylon.ui.reviewParticipant");
		return elements;
	}
	

	public IConfigurationElement[] getTeamProviders()
	{
		IConfigurationElement[] elements = RegistryFactory.getRegistry().getConfigurationElementsFor("de.jutzig.jabylon.ui.teamProvider");
		return elements;
	}

	public TeamProvider getTeamProviderFor(Project domainObject) {
		String teamProvider = domainObject.getTeamProvider();
		IConfigurationElement[] teamProviders = getTeamProviders();
		for (IConfigurationElement element : teamProviders) {
			if(element.getAttribute("name").equals(teamProvider))
			{
				try {
					return (TeamProvider) element.createExecutableExtension("class");
				} catch (CoreException e) {
					error("Failed to create instance for team provider "+teamProvider,e);
				}				
			}
		}
		return null;
	}
	
}
