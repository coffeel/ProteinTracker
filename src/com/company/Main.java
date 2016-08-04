package com.company;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        // write your code here
        //saveData();
        //getAndUpdateData();
        populateSampleData();
        //testQuery();
        testNativeSQL();

    }

    private static void testNativeSQL() {
        System.out.println("================================================");
        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createSQLQuery("SELECT * FROM Users").addEntity(User.class);
        List<User> users = query.list();
        users.forEach(u->System.out.println(u.getName()));

        User u= session.load(User.class, 1);
        System.out.println(u.getName());

        session.getTransaction().commit();
        session.close();

        HibernateUtilities.getSessionFactory().close();
    }

    private static void testQuery() {
        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery(
                "select user.goalAlerts " +
                        "from User user " +
                        "where user.goalAlerts is not empty");
        List<GoalAlert> goalAlerts = query.list();
        goalAlerts.forEach(System.out::println);

        Query query2 = session.createQuery(
                "select user from User user where user.name=:name");
        query2.setString("name", "Joe");
        List<User> users = query2.list();
        users.forEach(System.out::println);


        Query query3 = session.createQuery("from GoalAlert ")
                .setFirstResult(2)
                .setMaxResults(10);//paging
        List<GoalAlert> alerts = query3.list();
        alerts.forEach(a -> System.out.println(a.getMessage()));

        Query query4 = session.getNamedQuery("AllGoalAlerts");
        List<GoalAlert> alerts2 = query4.list();
        alerts2.forEach(a -> System.out.println(a.getMessage()));//use named query

        Query query5 = session.createQuery("select user from User user " +
                "join user.proteinData pd " +
                "where pd.total > 0");

        Query query6 = session.createQuery("select user from User user, GoalAlert ga " +
                "where user.id = ga.id");

        Query query7 = session.createQuery(
                "select new com.company.UserTotal(user.name, user.proteinData.total) " +
                        "from User user");
        List<UserTotal> userTotals = query7.list();
        userTotals.forEach(System.out::println);

        //criteria
        Criteria criteria = session.createCriteria(User.class)
                .add(Restrictions.eq("name", "Joe"))
                .add(Restrictions.eq("id", 1));
        List<User> users2 = criteria.list();
        users2.forEach(System.out::println);

        Criteria criteria2 = session.createCriteria(User.class)
                .add(Restrictions.or(
                        Restrictions.eq("name", "Joe"),
                        Restrictions.eq("name", "Bob")
                ));
        List<User> users3 = criteria2.list();
        users3.forEach(System.out::println);

        //Criteria+Restrictions+Projections
        Criteria criteria3 = session.createCriteria(User.class)
                .add(Restrictions.or(
                        Restrictions.eq("name", "Joe"),
                        Restrictions.eq("name", "Bob")
                ))
                .setProjection(Projections.projectionList()
                        .add(Projections.property("id"))
                        .add(Projections.property("name"))
                );
        List<Object[]> results = criteria3.list();
        for (Object[] result : results) {
            for (Object r : result) {
                System.out.print(r);
            }
            System.out.println();
        }

        //Criteria Joins
        Criteria criteria4 = session.createCriteria(User.class)
                .createAlias("proteinData", "pd")
                .add(Restrictions.or(
                        Restrictions.eq("name", "Joe"),
                        Restrictions.eq("name", "Bob")
                ))
                .setProjection(Projections.property("pd.total"));
        List<Object> results2 = criteria4.list();
        for (Object result : results2) {
            System.out.print(result);
            System.out.println();
        }

        //query by example
        User user = new User();
        user.setName("joe");
        Example e = Example.create(user).ignoreCase();
        List<User> users4 = session.createCriteria(User.class)
                .add(e).list();
        users4.forEach(System.out::println);

        session.getTransaction().commit();
        session.close();

        HibernateUtilities.getSessionFactory().close();
    }

    private static void populateSampleData() {

        Session session = HibernateUtilities.getSessionFactory().openSession();
        session.beginTransaction();

        User joe = createUser("Joe", 500, 50, "Good Job", "You made it!");
        session.save(joe);
        User bob = createUser("Bob", 300, 20, "Taco Time!");
        session.save(bob);
        User amy = createUser("Amy", 200, 200, "Yes!!");
        session.save(amy);

        session.getTransaction().commit();
    }

    private static User createUser(String name, int goal, int total, String... alerts) {
        User user = new User();
        user.setName(name);
        user.getProteinData().setGoal(goal);
        user.addHistory(new UserHistory(new Date(), "Set goal to " + goal));
        user.getProteinData().setTotal(total);
        user.addHistory(new UserHistory(new Date(), "Set total to " + total));
        for (String alert : alerts) {
            user.getGoalAlerts().add(new GoalAlert(alert));
        }

        return user;
    }

    private static void getAndUpdateData() {
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

    private static void saveData() {
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
