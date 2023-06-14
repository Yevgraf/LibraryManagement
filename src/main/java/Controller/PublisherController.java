
package Controller;

import java.util.List;
import java.util.regex.Pattern;
import Data.PublisherData;
import Model.Publisher;

public class PublisherController {
    private PublisherData publisherData;

    public PublisherController(PublisherData publisherData) {
        this.publisherData = publisherData;

    }

    public void createPublisher(String name, String address) throws IllegalArgumentException {
        List<Publisher> publisherList = listPublishers();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome da editora não pode ser nulo ou vazio");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("O endereço da editora não pode ser nulo ou vazio");
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


}