package except;

/**
 * An exception class that defines an exception that is to be raised when a particular coordinate is
 * is not in the world.
 * @author u4672869 (Emmanuel Malikides)
 *
 */
public class OutsideWorldException extends Exception {
	
	String error;
	
	public OutsideWorldException(String err) {
		super(err);
		error = err;
	}

}
