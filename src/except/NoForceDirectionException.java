package except;

/**
 * An exception class that defines an exception that is to be raised when force has no direction.
 * (For example when two bonded atoms occupy the same position).
 * @author u4672869 (Emmanuel Malikides)
 *
 */
public class NoForceDirectionException extends Exception{
	/**
	 * Default Serial version Id.
	 */
	private static final long serialVersionUID = 1L;
	String error;
	
	public NoForceDirectionException(String err) {
		super(err);
		error = err;
	}


}
