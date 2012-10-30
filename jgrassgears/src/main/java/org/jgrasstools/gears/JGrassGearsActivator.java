package org.jgrasstools.gears;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class JGrassGearsActivator implements BundleActivator {

  /**
    * Implements BundleActivator.start().
    * @param bundleContext - the framework context for the bundle.
  **/
  public void start(BundleContext bundleContext) {
    System.out.println("Hello World");
  }

  /**
    * Implements BundleActivator.stop() 
    * @param bundleContext - the framework context for the bundle.
  **/
  public void stop(BundleContext bundleContext) {
    System.out.println("Goodbye World");
  }
}