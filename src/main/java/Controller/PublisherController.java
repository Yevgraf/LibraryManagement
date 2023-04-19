
package Controller;

import java.util.List;
import java.util.regex.Pattern;
import Data.PublisherData;
import Model.Publisher;

public class PublisherController {
    private PublisherData publisherData;
    private Pattern validAddressPattern;

    public PublisherController(PublisherData publisherData) {
        this.publisherData = publisherData;
        this.validAddressPattern = Pattern.compile("^\\d+\\s+([A-Za-z]+\\s?)+,\\s+[A-Za-z]{2}$");
    }

    public void createPublisher(String name, String address) throws IllegalArgumentException {
        List<Publisher> publisherList = listPublishers();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome da editora não pode ser nulo ou vazio");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("O endereço da editora não pode ser nulo ou vazio");
        }
        if (!validAddressPattern.matcher(address).matches()) {
            throw new IllegalArgumentException("O endereço deve estar no formato 'número rua, cidade' (Exemplo: '123 Main St, New York, NY')");
        }
        Publisher publisher = new Publisher(name, address);
        publisherList.add(publisher);
        publisherData.save(publisherList);
    }

    public List<Publisher> listPublishers() {
        return publisherData.load();
    }
    public Publisher findByName(String name) {
        List<Publisher> publishers = publisherData.load();
        for (Publisher publisher : publishers) {
            if (publisher.getName().equalsIgnoreCase(name)) {
                return publisher;
            }
        }
        return null;
    }

    public void listPublishersView() {
        List<Publisher> publisherList = listPublishers();
        System.out.println("Lista de editoras:");
        for (int i = 0; i < publisherList.size(); i++) {
            Publisher publisher = publisherList.get(i);
            System.out.println((i+1) + ". " + publisher.getName() + " - " + publisher.getAddress());
        }
    }

}