package com.mycompany.biblioteca;

import java.util.ArrayList;
import java.util.List;

import Model.Book;
import Model.Member;
import View.CreateBookView;
import View.CreateMemberView;
import View.CreateRequestBookView;
import View.ReturnBookView;

public class Library {
    private static List<Book> books = new ArrayList<Book>();
    private static List<Member> members = new ArrayList<Member>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to the Library!");
            System.out.println("1. Register book");
            System.out.println("2. Register member");
            System.out.println("3. View book list");
            System.out.println("4. Request book");
            System.out.println("5. Return book");
            System.out.println("6. Quit");

            String choiceStr = System.console().readLine();
            int choice;

            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please choose again.");
                continue;
            }

            switch (choice) {
                case 1:
                    CreateBookView createBookView = new CreateBookView();
                    Book book = createBookView.createBook();
                    books.add(book);
                    System.out.println("Book registered successfully!");
                    break;
                case 2:
                    CreateMemberView createMemberView = new CreateMemberView();
                    Member member = createMemberView.getMemberInfo();
                    members.add(member);
                    System.out.println("Member registered successfully!");
                    break;
                case 3:
                    System.out.println("Book List:");
                    for (Book b : books) {
                        System.out.println(b.toString());
                    }
                    break;
                case 4:
                    CreateRequestBookView createRequestBookView = new CreateRequestBookView();
                    createRequestBookView.getRequestBookInfo(books, members);
                    System.out.println("Book request registered successfully!");
                    break;
                case 5:
                    ReturnBookView returnBookView = new ReturnBookView();
                    returnBookView.getReturnBookInfo(members);
                    System.out.println("Book returned successfully!");
                    break;
                case 6:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    break;
            }
        }
    }
}
