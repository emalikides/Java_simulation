package except;

/**
 * An exception class that defines an exception that is to be raised when the mass of some matter
 * is zero, and an acceleration needs to be calculated.
 * @author u4672869 (Emmanuel Malikides)
 *
 */
public class ZeroMassException extends Exception {
	/**
	 * Default Serial version Id.
	 */
	private static final long serialVersionUID = 1L;
	String error;
	
	public ZeroMassException(String err) {
		super(err);
		error = err;
	}

}
