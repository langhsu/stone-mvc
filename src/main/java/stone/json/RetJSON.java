/**
 * 
 */
package stone.json;

/**
 * @author langhsu
 *
 */
public class RetJSON implements JSONEntry {
	
	private Object data;
	
	public RetJSON(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
