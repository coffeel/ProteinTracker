package com.company;

import java.util.*;

/**
 * Created by yonglinx on 7/31/16.
 */
public class User {
    private int id;
    private String name;
    private ProteinData proteinData = new ProteinData();
    private List<UserHistory> history = new ArrayList<>();
    private Set<GoalAlert> goalAlerts = new HashSet<>();

    public User() {
        setProteinData(new ProteinData());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProteinData getProteinData() {
        return proteinData;
    }

    public void setProteinData(ProteinData proteinData) {
        this.proteinData = proteinData;
        proteinData.setUser(this);
    }

    public List<UserHistory> getHistory() {
        return history;
    }

    public void setHistory(List<UserHistory> history) {
        this.history = history;
    }

    public void addHistory(UserHistory historyItem) {
        historyItem.setUser(this);
        this.history.add(historyItem);
    }

    public Set<GoalAlert> getGoalAlerts() {
        return goalAlerts;
    }

    public void setGoalAlerts(Set<GoalAlert> goalAlerts) {
        this.goalAlerts = goalAlerts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", proteinData=" + proteinData +
                ", history=" + history +
                ", goalAlerts=" + goalAlerts +
                '}';
    }
}
