/**
 * 
 */
package stone;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import stone.mvc.route.ClassScanner;

/**
 * @author langhsu
 *
 */
public class ClassScannerTest extends TestCase {
	
	public void testFind() {
		 List<String> classFilters = new ArrayList<String>();
		 classFilters.add("*Controller");
		 ClassScanner scanner = new ClassScanner(true, classFilters);
		 Set<Class<?>> classes = scanner.findPkgClassAll("demo", true);
		 System.out.println(classes.size());
		 for(Class<?> cls : classes){
			 System.out.println(cls.getName());
		 }
	}
}
