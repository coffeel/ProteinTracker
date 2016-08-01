package com.company;

/**
 * Created by yonglinx on 8/1/16.
 */
public class GoalAlert {
    private int id;
    private String message;

    public GoalAlert(String message) {
        this.message = message;
    }

    public GoalAlert() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
