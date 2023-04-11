package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.datamodel.ContactData;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("My Contacts");
        primaryStage.setScene(new Scene(root, 500, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        try{
            ContactData.getInstance().loadContacts();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        try{
            ContactData.getInstance().storeContacts();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
