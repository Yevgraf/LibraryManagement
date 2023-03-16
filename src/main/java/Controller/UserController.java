package Controller;

import Data.UserData;
import Model.User;
import View.CreateUserView;

import java.util.List;

public class UserController {
    private CreateUserView createUserView;
    private UserData userData;

    public UserController(CreateUserView createUserView, UserData userData) {
        this.createUserView = createUserView;
        this.userData = userData;
    }

    public User createUser() {
        User user = createUserView.createUser();
        List<User> userList = userData.load();
        userList.add(user);
        userData.save(userList);
        createUserView.displayUser(user);
        System.out.println("Utilizador criado e guardado com sucesso.");
    return user;
    }


    public void listUsers() {
        List<User> userList = userData.load();
        if (userList.isEmpty()) {
            System.out.println("Não existem utilizadores guardados.");
        } else {
            for (User user : userList) {
                System.out.println(user);
            }
        }
    }
}
