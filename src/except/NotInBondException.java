/**
 * 
 */
package except;

/**
 * @author u4685222
 *
 */
public class NotInBondException extends Exception {

	String error;
	
	public NotInBondException(String err) {
		super(err);
		error = err;
	}
	
}
