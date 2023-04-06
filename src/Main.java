import UI.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//color palette
//445a75
//88889e
//93bad2
//b39d71
//f5f8f9
//d90429

//admin login credentials id: 000000001, password: admin12345
//encryption key: Za9lu6QWB9UWG4/OnifEkw==

public class Main {
    static int APP_WIDTH = 800, APP_HEIGHT = 600;
    public static void main(String[] args){
        App app = new App("Peace", APP_WIDTH, APP_HEIGHT, Color.decode("#445a75"));
        app.handler.setAntiAliasing(true);

        File f = new File("database.db");
        if (!f.exists()){
            //create database file
            DatabaseHandler.makeDatabase();

            //create an admin for initial usage
            Admin admin = new Admin("00000001", "Admin", "", "QeVlP8euuJQCbWu3stt/ZZArRUlF+o/WWmMnBHtSd/s=");
            admin.addToDatabase();
        }else{
            //load database
            DatabaseHandler.getDatabaseData();
        }

        //handle the GUI
        GUIHandler.loginPage(app);
    }

    public static String getEncryptionKey(){
        //get the encryption key from the file
        try{
            File file = new File("Encryption Key.txt");
            Scanner scanner = new Scanner(file);
            return scanner.nextLine();
        }catch (FileNotFoundException e){
            System.err.println(e);
            return "";
        }
    }
}
