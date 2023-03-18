/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.biblioteca;

import Controller.LibrarianController;
import Data.LibrarianData;
import View.CreateLibrarianView;
import View.LibrarianMenu;

/**
 * @author franc
 */
public class Biblioteca {

    public static void main(String[] args) {
        LibrarianMenu librarianMenu = new LibrarianMenu();
        librarianMenu.start();

    }
}

