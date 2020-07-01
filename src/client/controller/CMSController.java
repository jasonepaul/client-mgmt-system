package client.controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.view.CMSView;
import server.model.Customer;

/**
 * Controls communication between the GUI and the client controller.
 * @author Jason Paul
 * @version 1.0
 * @since Feb 13, 2020
 */
public class CMSController {
	
	private CMSView theView;
	private ClientController clientCtr;
	private ArrayList<Customer> customerSearchResult;
	
	/**
	 * Constructs a controller by assigning an view (GUI) and client controller.
	 * Also sends action listener instances to the view for event handlers.
	 * @param theView The instance of a CMS GUI.
	 * @param clientCtr the client controller.
	 */
	public CMSController(CMSView theView, ClientController clientCtr) {
		this.theView = theView;
		this.clientCtr = clientCtr;
		
		theView.addSearchListener(new SearchListener());
		theView.addClearSearchListener(new ClearSearchListener());
		theView.addSelectedListItemListener(new ListSelectionHandler());
		theView.addNewCustomerListener(new NewCustomerListener());
		theView.addSaveListener(new SaveListener() );
		theView.addDeleteListener(new DeleteListener());
		theView.addClearListener(new ClearListener());
	}
	
	/**
	 * Inner class that implements the actionPerformed method for the Search 
	 * button, calling one of the search methods.
	 * @author Jason Paul
	 *
	 */
	class SearchListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(theView.getSearchCriteria().equals("")) {
				return;
			}
			if(theView.isSelectedCustomerId()) {
					int id = theView.getIntSearchCriteria();
					customerSearchResult = clientCtr.searchCustomer(id);
					buildListModel();
			}
			else if(theView.isSelectedCustomerLName()) {
					String lName = theView.getSearchCriteria();
					customerSearchResult = clientCtr.searchCustomer(lName);
					buildListModel();
			}
			else if(theView.isSelectedCustomerType()) {
					char cType = theView.getSearchCriteria().charAt(0);
					customerSearchResult = clientCtr.searchCustomerByType(cType);
					buildListModel();
			}
		}
		
	}
	
	/**
	 * Builds the list model based on the list of customers in the search result.
	 * @param theViewListModel The GUI list model.
	 */
	private void buildListModel() {
		theView.getListModel().clear();
		for(Customer c: customerSearchResult) {
			theView.getListModel().addElement(c.toShortString());
		}
	}
	
	/**
	 * Inner class that implements the actionPerformed method for the Clear 
	 * Search button.
	 * @author Jason Paul
	 */
	class ClearSearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			theView.getListModel().clear();
			theView.setSearchCriteria("");
		}
	}
	
	/**
	 * Inner class that implements the valueChanged method for the search list.
	 * When a customer in the search pane is selected, a method is called to 
	 * populate the customer info text boxes.
	 * @author Jason Paul
	 */
	class ListSelectionHandler implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			if(e.getValueIsAdjusting() == false) {
				JList<String> list = theView.getResultList();
			    int index = list.getSelectedIndex();
			    if(index == -1) {
			    	return;
			    }
			    populateCustomerInfoFields(index);
			}
		}
		
		/**
		 * Populates the customer info text boxes based on the customer selected in the 
		 * search list.
		 * @param index The index for the selected customer.
		 */
		private void populateCustomerInfoFields(int index) {
			Customer selectedCustomer = clientCtr.getCustomerSearchResult(index);
			theView.setIdAsString(selectedCustomer.getId());
			theView.setfName(selectedCustomer.getFirstName());
			theView.setlName(selectedCustomer.getLastName());
			theView.setAddress(selectedCustomer.getAddress());
			theView.setPc(selectedCustomer.getPostalCode());
			theView.setPh(selectedCustomer.getPhoneNumber());
			theView.setCType(selectedCustomer.getCustomerType());
		}
	}
	
	/**
	 * Inner class that implements the actionPerformed method for the Start New
	 * Customer Button. It auto generates a new and unique customer ID. 
	 * @author Jason Paul
	 */
	class NewCustomerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			theView.setMessage("");
			theView.clearInfoFields();
			int id = clientCtr.getNewId();
			theView.setIdAsString(id);
		}
	}
	
	/**
	 * Inner class that implements the actionPerformed method for the Save Button.
	 * Rules for text fields are first checked and error message displayed as 
	 * appropriate, then either the new customer is added or the existing customer 
	 * is modified in the database.
	 * @author Jason Paul
	 */
	class SaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int id = 0;
			// First check that there is a valid ID in the customer info.
			try {
				id = theView.getIdAsInt();				
			}
			catch(NumberFormatException e1) {
				theView.displayErrorMessage("First press Start New Customer " +
						"button or search and make a selection.");
				return;
			}
			String fName = theView.getfName();
			String lName = theView.getlName();
			String address = theView.getAddress();
			String pc = theView.getPc().toUpperCase();
			String ph = theView.getPh();
			char cType = theView.getCType();
			
			//check inputs against rules
			String message = "";
			boolean isViolated = false;
			
			if(! lengthOK(fName, 20)) {
				message += "Ensure first name is between 1 and 20 characters.\n";
				isViolated = true;
			}
			if(! lengthOK(lName, 20)) {
				message += "Ensure last name is between 1 and 20 characters.\n";
				isViolated = true;
			}
			if(! lengthOK(address, 50)) {
				message += "Ensure address is between 1 and 50 characters.\n";
				isViolated = true;
			}
			if(! pc.matches("[a-zA-Z]\\d[a-zA-Z] \\d[a-zA-Z]\\d")) {
				message += "Postal code must be in the format A1A 1A1.\n";
				isViolated = true;
			}
			if(! ph.matches("\\d{3}-\\d{3}-\\d{4}")) {
				message += "Phone number must be in the format 111-111-1111.\n";
				isViolated = true;
			}
			if(isViolated) {
				theView.setMessage(message);
				return;
			}
			theView.setMessage("");
			Customer c = new Customer( id, fName, lName, address, pc, ph, cType);
			clientCtr.addCustomer(c);
		}
		
		/**
		 * Checks whether a string has an acceptable length.
		 * @param s The string to check.
		 * @param maxLength The maximum allowable length.
		 * @return true if length is fine otherwise false.
		 */
		private boolean lengthOK(String s, int maxLength) {
			if(s.length() > maxLength || s.length() == 0) {
				return false;
			}
			else {
				return true;
			}
		}
	}
	
	
	/**
	 * Inner class that implements the actionPerformed method for the Delete 
	 * Button. If customer exists in the database, they are deleted.
	 * @author Jason Paul
	 */
	class DeleteListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			theView.setMessage("");
			try {
				customerSearchResult = clientCtr.deleteCustomer(theView.getIdAsInt());
				theView.clearInfoFields();			
			}
			catch(NumberFormatException e1) {
				theView.displayErrorMessage("First search and make a selection.");
			}
			buildListModel();
		}
	}
	
	/**
	 * Inner class that implements the actionPerformed method for the Clear 
	 * Button. Information is cleared from the customer info text boxes.
	 * @author Jason Paul
	 */
	class ClearListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			theView.clearInfoFields();
			theView.setMessage("");
		}
	}
	
}
