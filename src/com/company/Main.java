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

        for (UserHistory history : loadedUser.getHistory()) {
            System.out.println(history.getEntryTime().toString() + ":" + history);
        }

        //Update
        loadedUser.getProteinData().setTotal(loadedUser.getProteinData().getTotal() + 50);
        loadedUser.addHistory(new UserHistory(new Date(), "Add 50 protein"));

        loadedUser.setProteinData(new ProteinData());

        session.getTransaction().commit();
        session.close();
    }

    private static void SaveData() {
        System.out.println("hello world");

        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();

        User user = new User();
        user.setName("Jim");
        user.addHistory(new UserHistory(new Date(), "Set Name to Jim"));
        user.getProteinData().setGoal(250);
        user.addHistory(new UserHistory(new Date(), "Set the goal to 250"));

        user.getGoalAlerts().add(new GoalAlert("Congratulations!"));
        user.getGoalAlerts().add(new GoalAlert("You did it!"));

        session.save(user);
        session.getTransaction().commit();
        session.close();
    }
}
