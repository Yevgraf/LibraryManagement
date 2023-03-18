/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.biblioteca;

import Controller.LibrarianController;
import Data.LibrarianData;
import View.CreateLibrarianView;

/**
 * @author franc
 */
public class Biblioteca {

    public static void main(String[] args) {
        CreateLibrarianView createUserView = new CreateLibrarianView();
        LibrarianData librarianData = new LibrarianData();
        LibrarianController librarianController = new LibrarianController(createUserView, librarianData);

        //Librarian librarian = librarianController.createLibrarian();

        librarianController.listLibrarians();


    }
}

