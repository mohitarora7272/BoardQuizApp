package com.board.quiz;

public class ScoreData {

    int id;
    String name, score;

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ScoreData(String name, String score) {
        super();

        this.name = name;
        this.score = score;
    }

    public ScoreData() {
        super();
    }
}