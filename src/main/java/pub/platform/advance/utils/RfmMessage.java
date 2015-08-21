package pub.platform.advance.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Thinkpad on 2015/8/21.
 */
public class RfmMessage {
    private static RfmMessage manager = null;
    private static Object managerLock = new Object();
    private static String propsName = "/rfmMessages.properties";
    private Properties properties = null;
    private Object propertiesLock = new Object();
    private String resourceURI;
    private long propTime;

    /**
     *  Singleton access only.
     *
     *@param  resourceURI  Description of the Parameter
     */
    private RfmMessage(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    /**
     *  Returns a Jive property
     *
     *@param  name  the name of the property to return.
     *@return       The property value
     *@returns      the property value specified by name.
     */
    public static String getProperty(String name) {
        if(manager == null) {
            synchronized(managerLock) {
                if(manager == null) {
                    manager = new RfmMessage(propsName);
                }
            }
        }
        String props = manager.getProp(name);
        if(props == null)
            return null;
        try {
            props = new String(props.getBytes("GBK"));
        } catch(Exception e) {

        }
        return props;
    }

    /**
     *  Gets a Jive property. Jive properties are stored in jive.properties. The
     *  properties file should be accesible from the classpath. Additionally, it
     *  should have a path field that gives the full path to where the file is
     *  located. Getting properties is a fast operation.
     *
     *@param  name  Description of the Parameter
     *@return       The prop value
     */
    public String getProp(String name) {
        //If properties aren't loaded yet. We also need to make this thread
        //safe, so synchronize...
        if(properties == null) {
            synchronized(propertiesLock) {
                //Need an additional check
                if(properties == null) {
                    loadProps();
                }
            }
        }

        long curPropTime = (new File(propsName)).lastModified();
        if ( curPropTime != this.propTime ) {
            loadProps();
        }
        return properties.getProperty(name);
    }

    /**
     *  Loads Jive properties from the disk.
     */
    private void loadProps() {
        properties = new Properties();
        InputStream in = null;
        try {
            propTime = (new File(propsName)).lastModified();
            in = getClass().getResourceAsStream(resourceURI);
            properties.load(in);
        } catch(IOException ioe) {
            System.err.println(
                    "Error reading Jive properties in DbForumFactory.loadProperties() " +
                            ioe);
            ioe.printStackTrace();
        } finally {
            try {
                in.close();
            } catch(Exception e) {}
        }
    }
}
