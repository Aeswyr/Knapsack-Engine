package gui;

/**
 * interface for determining click events
 * 
 * @author Pascal
 *
 */
public interface ClickListener {

	/**
	 * Method to execute upon clicking on a UI object
	 * 
	 * @param source - the UI object that generates this onClick call
	 */
	public void onClick(UIObject source);

}
