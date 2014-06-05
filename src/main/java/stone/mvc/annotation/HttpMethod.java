/**
 * 
 */
package stone.mvc.annotation;

/**
 * @author langhsu
 *
 */
public enum HttpMethod {
	GET(0), POST(1), PUT(2), DELETE(3), HEAD(4);
	
	private int index;
	
	private HttpMethod(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
