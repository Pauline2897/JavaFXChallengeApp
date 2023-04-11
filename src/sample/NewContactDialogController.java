package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.datamodel.Contact;
import sample.datamodel.ContactData;

public class NewContactDialogController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField notesField;

    public Contact updateContact(Contact oldContact){
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesField.getText().trim();

        Contact newContact = new Contact(firstName,lastName,phoneNumber,notes);
        ContactData.getInstance().updateContact(oldContact,newContact);
        return newContact;
    }

    public void populateFieldsWithContactInfo(Contact contact){
        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        phoneNumberField.setText(contact.getPhoneNumber());
        notesField.setText(contact.getNotes());
    }

    public Contact processResults(){
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesField.getText().trim();

        Contact contact = new Contact(firstName,lastName,phoneNumber,notes);
        ContactData.getInstance().addContact(contact);
        return contact;
    }
}
