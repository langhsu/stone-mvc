package stone;

/**
 * 
 * @author langhsu
 *
 */
public class StoneVersion {

    /**
     * Return the full version string, or
     * <code>null</code> if it cannot be determined.
     * 
     * @see java.lang.Package#getImplementationVersion()
     */
    public static String getVersion() {
        Package pkg = StoneVersion.class.getPackage();
        return (pkg != null ? pkg.getImplementationVersion() : null);
    }

}
