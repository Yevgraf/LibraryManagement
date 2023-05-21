/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.biblioteca;

import Controller.LibrarianController;
import Data.*;
import View.CreateLibrarianView;
import View.LibrarianMenu;
import View.LoginView;
import View.MainMenu;

/**
 * @author franc
 */
public class Biblioteca {

    public static void main(String[] args) {

        BookData.load();
        AgeRangeData.load();
        AuthorData.load();
        CardData.load();
        CategoryData.load();
        MemberData.load();
        PublisherData.load();
        ReservationData.load();

        LibrarianData librarianData = new LibrarianData();
        LibrarianController librarianController = new LibrarianController(librarianData);
        LoginView loginView = new LoginView(librarianController);
        loginView.login();
//       MainMenu mainMenu = new MainMenu();
//       mainMenu.displayMenu();

    }
}



