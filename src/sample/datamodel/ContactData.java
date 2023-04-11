package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContactData {
    private static ContactData instance = new ContactData();
    private static String filename = "ContactItems.txt";
    private ObservableList<Contact> contacts;

    public static ContactData getInstance(){
        return instance;
    }

    private ContactData(){}

    public ObservableList<Contact> getContacts(){
        return contacts;
    }

    public void addContact(Contact contact){
        contacts.add(contact);
    }


    public void updateContact(Contact oldContact, Contact newContact){
        contacts.set(contacts.indexOf(oldContact),newContact);
    }

    public void deleteContact(Contact contact){
        contacts.remove(contact);
    }

    public void loadContacts() throws IOException{
        contacts = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader bufferedReader = Files.newBufferedReader(path);
        String input;

        try{
            while((input=bufferedReader.readLine())!=null){
                String[] contactPieces = input.split("\t");

                String firstName = contactPieces[0];
                String lastName = contactPieces[1];
                String phoneNumber = contactPieces[2];
                String notes = contactPieces[3];

                Contact contact = new Contact(firstName,lastName,phoneNumber,notes);
                contacts.add(contact);
            }
        }finally{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
        }
    }

    public void storeContacts() throws IOException{
        Path path = Paths.get(filename);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
        try {
            for (int i = 0; i < contacts.size(); i++) {
                String contactData = contacts.get(i).getFirstName() + "\t"
                        + contacts.get(i).getLastName() + "\t"
                        + contacts.get(i).getPhoneNumber() + "\t"
                        + contacts.get(i).getNotes();

                bufferedWriter.write(contactData);
                bufferedWriter.newLine();
            }
        }finally {
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
        }
    }
}
