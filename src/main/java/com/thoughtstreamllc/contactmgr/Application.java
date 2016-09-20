package com.thoughtstreamllc.contactmgr;


import com.thoughtstreamllc.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    // Hold a reusable reference to a SessionFactory (since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new Contact.ContactBuilder("JT","Keller")
                .withEmail("jt.keller@branddesk.com")
                .withPhone(7735556666L)
                .build();
        int id = save(contact);


        // Display a list of all contacts before the update
        System.out.printf("%n%nBefore The Update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        // Get the persisted contact
        Contact c = findContactById(id);

        // Update the contact
        c.setFirstName("James");

        // Persist the changes
        System.out.printf("%n%nUpdating the contact...%n%n");
        update(c);
        System.out.printf("%n%nUpdate complete!%n%n");

        // Display a list of all contacts after the update
        System.out.printf("%n%nAfter The Update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        // Delete a contact
        c = findContactById(1);
        System.out.printf("%n%nDeleting the contact...%n%n");
        delete(c);
        System.out.printf("%n%nDeleted!%n%n");

        System.out.printf("%n%nAfter The Delete%n%n");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id) {
        // Open session
        Session session = sessionFactory.openSession();

        // Retrieve the persistent contact object (null if not found)
        Contact contact = session.get(Contact.class, id);

        // Close session
        session.close();

        // Return the object
        return contact;
    }

    private static void update(Contact contact) {
        // Open session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the contact
        session.update(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close session
        session.close();
    }

    private static void delete(Contact contact) {
        // Open session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to delete the contact
        session.delete(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close session
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create Criteria
        Criteria criteria = session.createCriteria(Contact.class);

        // Get a list of contacts based on the criteria
        List<Contact> contacts = criteria.list();

        //Close the session
        session.close();

        return contacts;
    }

    private static int save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        int id = (int)session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

        return id;
    }
}
