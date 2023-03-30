
package Controller;

import java.util.List;

import Data.PublisherData;
import Model.Publisher;

public class PublisherController {
    private PublisherData publisherData;


    public PublisherController(PublisherData publisherData) {
        this.publisherData = publisherData;
    }

    public void createPublisher(String name, String address) {
        List<Publisher> publisherList = listPublishers();
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