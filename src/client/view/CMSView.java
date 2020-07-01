package client.view;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;

/**
 * Provides the GUI and event listeners for the Customer Management System.
 * @author Jason Paul
 * @version 1.0
 * @since Nov 15, 2019
 */
@SuppressWarnings("serial")
public class CMSView extends JFrame{
	
	private JRadioButton cId;
	private JRadioButton cLName;
	private JRadioButton cType;
	private JTextField searchField;
	private JButton searchButton;
	private JButton clearSearchButton;
	private DefaultListModel<String> listModel;
	private JList<String> resultList;

	private JTextField idField;
	private JTextField fNameField;
	private JTextField lNameField;
	private JTextField addressField;
	private JTextField pcField;
	private JTextField phField;
	private JComboBox<String> typeCombo;
	private JButton newCustomerButton;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton clearButton;
	private JTextArea messageArea; // for displaying a messages.
	
	/**
	 * Constructs an instance of the GUI.
	 * @param title The title to display in the window.
	 */
	public CMSView(String title) {
		super(title);

		setLayout (new FlowLayout());
		Container c = getContentPane();
		
		// Create Search Panel (left hand side panel)
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
		TitledBorder t1 = BorderFactory.createTitledBorder("Search Customers");
		t1.setTitleJustification(TitledBorder.CENTER);
		searchPanel.setBorder(t1);	

		JPanel selectTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel selectTypeLabel = new JLabel("Select Type of Search To Be Performed:");
		selectTypePanel.add(selectTypeLabel);
		searchPanel.add(selectTypePanel);

		JPanel radioButtonPanel = new JPanel();
		ButtonGroup bg = new ButtonGroup();
		cId = new JRadioButton("Customer ID", true);
		bg.add(cId);
		radioButtonPanel.add(cId);
		cLName = new JRadioButton("Last Name", false);
		bg.add(cLName);
		radioButtonPanel.add(cLName);
		cType = new JRadioButton("Customer type", false);
		bg.add(cType);
		radioButtonPanel.add(cType);
		searchPanel.add(radioButtonPanel);

		JPanel searchParamsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel searchParamsLabel = new JLabel("Enter The Search Parameter Below:");
		searchParamsPanel.add(searchParamsLabel);
		searchPanel.add(searchParamsPanel);
		
		JPanel searchButtonPanel = new JPanel();
		searchField = new JTextField(15);
		searchButton = new JButton("Search");
		clearSearchButton = new JButton("Clear Search");
		searchButtonPanel.add(searchField);
		searchButtonPanel.add(searchButton);
		searchButtonPanel.add(clearSearchButton);
		searchPanel.add(searchButtonPanel);
		
		JPanel searchResultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel searchResultsLabel = new JLabel("Search Rresults:");
		searchResultsPanel.add(searchResultsLabel);
		searchPanel.add(searchResultsPanel);
		
		listModel = new DefaultListModel<String>();
		resultList = new JList<String>(listModel);
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane resultScrollPane = new JScrollPane(resultList);
		searchPanel.add(resultScrollPane);
		
		// Create Customer Info Panel (right hand side panel)
		JPanel customerInfoPanel = new JPanel();
		customerInfoPanel.setLayout(new BoxLayout(customerInfoPanel, BoxLayout.Y_AXIS));
		TitledBorder t2 = BorderFactory.createTitledBorder("Customer Information");
		t2.setTitleJustification(TitledBorder.CENTER);
		customerInfoPanel.setBorder(t2);
		
		JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel idLabel = new JLabel("Customer ID:");
		idField = new JTextField(3);
		idField.setEditable(false);
		idPanel.add(idLabel);
		idPanel.add(idField);
		customerInfoPanel.add(idPanel);	
		
		JPanel fNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel fNameLabel = new JLabel("First Name:");
		fNameField = new JTextField(10);
		fNamePanel.add(fNameLabel);
		fNamePanel.add(fNameField);
		customerInfoPanel.add(fNamePanel);
		
		JPanel lNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lNameLabel = new JLabel("Last Name:");
		lNameField = new JTextField(10);
		lNamePanel.add(lNameLabel);
		lNamePanel.add(lNameField);
		customerInfoPanel.add(lNamePanel);
		
		JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel addressLabel = new JLabel("Address:");
		addressField = new JTextField(20);
		addressPanel.add(addressLabel);
		addressPanel.add(addressField);
		customerInfoPanel.add(addressPanel);	
		
		JPanel pcPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel pcLabel = new JLabel("Postal Code:");
		pcField = new JTextField(9);
		pcPanel.add(pcLabel);
		pcPanel.add(pcField);
		customerInfoPanel.add(pcPanel);
		
		JPanel phPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel phLabel = new JLabel("Phone #:");
		phField = new JTextField(14);
		phPanel.add(phLabel);
		phPanel.add(phField);
		customerInfoPanel.add(phPanel);
		
		JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel typeLabel = new JLabel("Customer Type:");
		typeCombo = new JComboBox<String>();
		typeCombo.addItem("R");
		typeCombo.addItem("C");
		typePanel.add(typeLabel);
		typePanel.add(typeCombo);
		customerInfoPanel.add(typePanel);
		
		JPanel infoButtonPanel = new JPanel();
		newCustomerButton = new JButton("Start New Customer");
		saveButton = new JButton("Save");
		deleteButton = new JButton("Delete Customer");
		clearButton = new JButton("Clear");
		infoButtonPanel.add(newCustomerButton);
		infoButtonPanel.add(saveButton);
		infoButtonPanel.add(deleteButton);
		infoButtonPanel.add(clearButton);
		customerInfoPanel.add(infoButtonPanel);
		
		JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		messageArea = new JTextArea(6, 35);
		messageArea.setForeground(Color.RED);
		messageArea.setEditable(false);
		messagePanel.add(messageArea);
		customerInfoPanel.add(messagePanel);
		
		// Add Search and Customer Info Panels to the frame
		c.add(searchPanel);
		c.add(customerInfoPanel);
		
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Displays a dialog box to the user.
	 * @param errorMessage The message to display.
	 */
	public void displayErrorMessage(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage);
	}
	
	/**
	 * Clears the info from the customer info text boxes.
	 */
	public void clearInfoFields() {
		idField.setText("");
		fNameField.setText("");
		lNameField.setText("");
		addressField.setText("");
		pcField.setText("");
		phField.setText("");
	}
	
	
	// *********** ADD LISTENER METHODS *****************
	
	/**
	 * Adds a listener to the Search Button.
	 * @param searchListener The search listener.
	 */
	public void addSearchListener(ActionListener searchListener) {
		searchButton.addActionListener(searchListener);
	}
	
	/**
	 * Adds a listener to the Clear Search Button.
	 * @param searchListener The Clear Search listener.
	 */
	public void addClearSearchListener(ActionListener clearSearchListener) {
		clearSearchButton.addActionListener(clearSearchListener);
	}
	
	/**
	 * Adds a listener to the Search List.
	 * @param searchListener The Search List listener.
	 */
	public void addSelectedListItemListener(ListSelectionListener selectedListItemListener) {
		resultList.addListSelectionListener(selectedListItemListener);
	}

	/**
	 * Adds a listener to the Start New Customer Button.
	 * @param searchListener The Start New Customer listener.
	 */
	public void addNewCustomerListener(ActionListener newCustomerListener) {
		newCustomerButton.addActionListener(newCustomerListener);
	}
	
	/**
	 * Adds a listener to the Save Button.
	 * @param searchListener The Save listener.
	 */
	public void addSaveListener(ActionListener saveListener) {
		saveButton.addActionListener(saveListener);
	}

	/**
	 * Adds a listener to the Delete Customer Button.
	 * @param searchListener The Delete Customer listener.
	 */
	public void addDeleteListener(ActionListener deleteListener) {
		deleteButton.addActionListener(deleteListener);
	}
	
	/**
	 * Adds a listener to the Clear Button.
	 * @param searchListener The Clear listener.
	 */
	public void addClearListener(ActionListener clearListener) {
		clearButton.addActionListener(clearListener);
	}
	
	
	// *********** GETTERS AND SETTERS ******************
	
	/**
	 * @return True if Customer ID radio button is selected.
	 */
	public boolean isSelectedCustomerId() {
		return cId.isSelected();
	}
	
	/**
	 * @return True if Last Name radio button is selected.
	 */
	public boolean isSelectedCustomerLName() {
		return cLName.isSelected();
	}

	/**
	 * @return True if Customer Type radio button is selected.
	 */
	public boolean isSelectedCustomerType() {
		return cType.isSelected();
	}
	
	/**
	 * @return The search box text.
	 */
	public String getSearchCriteria() {
		return searchField.getText();
	}
	
	/**
	 * @return The search field text.
	 */
	public int getIntSearchCriteria() {
		return Integer.parseInt(searchField.getText());
	}
	
	/**
	 * @param s The search criteria.
	 */
	public void setSearchCriteria(String s) {
		this.searchField.setText(s);
	}
	
	/**
	 * @return The search list model.
	 */
	public DefaultListModel<String> getListModel() {
		return listModel;
	}
	
	/**
	 * 
	 * @return The search result list.
	 */
	public JList<String> getResultList() {
		return resultList;
	}
	
	/**
	 * @return The customer ID.
	 */
	public int getIdAsInt() {
		return getIntFromIdField(idField);
	}

	/**
	 * @param idField The customer ID text field.
	 * @return The ID from the text field.
	 */
	private int getIntFromIdField(JTextField idField) {
		return Integer.parseInt(idField.getText());
	}	
	
	/**
	 * @param id The ID to set.
	 */
	public void setIdAsString(int id) {
		String s = id + "";
		this.idField.setText(s);
	}

	/**
	 * @return The customer's first name.
	 */
	public String getfName() {
		return fNameField.getText();
	}

	/**
	 * @param fName The customer's first name.
	 */
	public void setfName(String fName) {
		this.fNameField.setText(fName);
	}

	/**
	 * @return The customer's last name.
	 */
	public String getlName() {
		return lNameField.getText();
	}

	/**
	 * @param lName The customer's last name.
	 */
	public void setlName(String lName) {
		this.lNameField.setText(lName);
	}

	/**
	 * @return The customer's address.
	 */
	public String getAddress() {
		return addressField.getText();
	}

	/**
	 * @param addressField The customer's address.
	 */
	public void setAddress(String addressField) {
		this.addressField.setText(addressField);
	}

	/**
	 * @return The customer's postal code.
	 */
	public String getPc() {
		return pcField.getText();
	}

	/**
	 * @param pcField The customer's postal code.
	 */
	public void setPc(String pcField) {
		this.pcField.setText(pcField);
	}

	/**
	 * @return The customer's phone number.
	 */
	public String getPh() {
		return phField.getText();
	}

	/**
	 * @param phField The customer's phone number.
	 */
	public void setPh(String phField) {
		this.phField.setText(phField);
	}

	/**
	 * @return The customer's type.
	 */
	public char getCType() {
		char type = typeCombo.getItemAt(typeCombo.getSelectedIndex()).charAt(0);
		return type;
	}

	/**
	 * @param typeCombo The customer's type.
	 */
	public void setCType(char typeCombo) {
		String type = String.valueOf(typeCombo);
		this.typeCombo.setSelectedItem(type);
	}

	/**
	 * @return The New Customer Button.
	 */
	public JButton getNewCustomerButton() {
		return newCustomerButton;
	}
	
	/**
	 * @return The Save Button.
	 */
	public JButton getSaveButton() {
		return saveButton;
	}

	/**
	 * @return The clear button.
	 */
	public JButton getClearButton() {
		return clearButton;
	}

	/**
	 * @return The Delete Button.
	 */
	public JButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * @param s The message text to display.
	 */
	public void setMessage(String s) {
		this.messageArea.setText(s);
	}

}
