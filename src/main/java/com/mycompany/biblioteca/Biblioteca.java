/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.biblioteca;

import Data.LibrarianData;
import Model.Librarian;

import java.util.List;

/**
 * @author franc
 */
public class Biblioteca {

    public static void main(String[] args) {
        // CreateLibrarianView createUserView = new CreateLibrarianView();
        // LibrarianData librarianData = new LibrarianData();
        // UserController userController = new UserController(createUserView, librarianData);
//
        // Librarian librarian = userController.createLibrarian();


        List<Librarian> librarians = LibrarianData.load();

        for (Librarian librarian : librarians) {
            System.out.println(librarian);
            System.out.println("------------------------------");
        }

    }
}

