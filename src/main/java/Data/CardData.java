package Data;

import Model.Book;
import Model.Card;
import Model.Member;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardData {


    public static void save(Card card) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Card (memberId, cardNumber) VALUES (?, ?)")) {

            int memberId = card.getMember().getId();


            if (!isMemberExists(connection, memberId)) {
                System.err.println("Erro ao guardar o cartão: O membro referenciado não existe.");
                return; // Skip
            }

            statement.setInt(1, memberId);
            statement.setString(2, card.getCardNumber());
            statement.executeUpdate();

            System.out.println("Card criado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao guardar cartão: " + e.getMessage());
        }
    }



    private static boolean isMemberExists(Connection connection, int memberId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Member WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public static List<Card> load() {
        List<Card> cards = new ArrayList<>();

        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Card")) {

            while (resultSet.next()) {
                int cardId = resultSet.getInt("id");
                int memberId = resultSet.getInt("memberId");
                String cardNumber = resultSet.getString("cardNumber");

                // Ir buscar membro associado
                Member member = MemberData.getById(memberId);

                // Criar  novo objecto cartão
                Card card = new Card(member, cardNumber);
                card.setId(cardId);

                // adicionar cartão a lista
                cards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar cartões: " + e.getMessage());
        }

        return cards;
    }


}
