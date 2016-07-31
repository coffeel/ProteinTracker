package com.company;

import org.hibernate.Session;

import java.util.Date;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // write your code here
        SaveData();
        GetAndUpdateData();

        HibernateUtilities.getSessionFactory().close();
    }

    private static void GetAndUpdateData() {
        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();

        //get
        //return null if not found. is use load function, exceptions
        User loadedUser = session.get(User.class, 1);
        System.out.println(loadedUser.getName());
        System.out.println(loadedUser.getProteinData().getGoal());

        for (Map.Entry<String, UserHistory> history : loadedUser.getHistory().entrySet()) {
            System.out.println(history.getKey());
            System.out.println(history.getValue().getEntryTime().toString() + ":" + history.getValue());
        }

        //Update
        loadedUser.getProteinData().setTotal(loadedUser.getProteinData().getTotal() + 50);
        loadedUser.getHistory().put("GPL123", new UserHistory(new Date(), "Add 50 protein"));

        session.getTransaction().commit();
        session.close();
    }

    private static void SaveData() {
        System.out.println("hello world");

        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();

        User user = new User();
        user.setName("Jim");
        user.getHistory().put("GPL122", new UserHistory(new Date(), "Set Name to Jim"));
        user.getProteinData().setGoal(250);
        user.getHistory().put("GPL112",new UserHistory(new Date(), "Set the goal to 250"));

        session.save(user);
        session.getTransaction().commit();
        session.close();
    }
}
