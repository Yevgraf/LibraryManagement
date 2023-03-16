/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.biblioteca;

import Controller.UserController;
import Data.UserData;
import Model.User;
import View.CreateUserView;

/**
 *
 * @author franc
 */
public class Biblioteca {

    public static void main(String[] args) {
        CreateUserView createUserView = new CreateUserView();
        UserData userData = new UserData();
        UserController userController = new UserController(createUserView, userData);

        User user = userController.createUser();

    }
}

