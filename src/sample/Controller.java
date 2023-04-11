package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import sample.datamodel.Contact;
import sample.datamodel.ContactData;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

public class Controller {

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn firstNameColumn;

    @FXML
    private TableColumn lastNameColumn;

    @FXML
    private TableColumn phoneNumberColumn;

    @FXML
    private TableColumn notesColumn;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu contextMenu;

    public void initialize(){
        contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Contact contact = (Contact) tableView.getSelectionModel().getSelectedItem();
                deleteContact(contact);
            }
        });
        MenuItem editMenuItem = new MenuItem("Edit");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Contact contact = (Contact) tableView.getSelectionModel().getSelectedItem();
                editContact(contact);
            }
        });

        contextMenu.getItems().addAll(deleteMenuItem,editMenuItem);

        ObservableList<Contact> contacts = ContactData.getInstance().getContacts();
        SortedList<Contact> sortedContacts = new SortedList<>(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getFirstName().compareTo(o2.getFirstName());
            }
        });

        tableView.setItems(sortedContacts);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.getSelectionModel().selectFirst();

        tableView.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView tableView) {
                TableRow<Contact> row = new TableRow<Contact>(){
                    @Override
                    protected void updateItem(Contact contact, boolean b) {
                        super.updateItem(contact, b);
                    }
                };
                row.emptyProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasEmpty, Boolean isNowEmpty) {
                        if(isNowEmpty){
                            row.setContextMenu(null);
                        }else{
                            row.setContextMenu(contextMenu);
                        }
                    }
                });
                return row;
            }
        });



    }

    public void deleteContact(Contact contact){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete contact");
        alert.setHeaderText("Delete contact: " + contact.getFirstName());
        alert.setContentText("Are you sure you want to delete this contact?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            ContactData.getInstance().deleteContact(contact);

        }

    }

    public void editContact(Contact contact){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit contact");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("NewContactDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
            NewContactDialogController controller = fxmlLoader.getController();
            controller.populateFieldsWithContactInfo(contact);
        }catch(IOException e){
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            NewContactDialogController controller = fxmlLoader.getController();
            Contact newContact = controller.updateContact(contact);
            tableView.getSelectionModel().select(newContact);
            tableView.refresh();
        }
    }


    @FXML
    public void showNewContactDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add a new contact");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("NewContactDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            NewContactDialogController controller = fxmlLoader.getController();
            Contact contact = controller.processResults();
            tableView.getSelectionModel().select(contact);

        }



    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }


}
