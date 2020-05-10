package com.tuhp00.teammanager.match;

public class Match {

    private String id;
    private String matchTeams;
    private String opponent;
    private String noteMatch;
    private String dateMatch;
    private String timeMatch;
    private String dateForOrderMatch;
    private String homeMatch;
    private String s1;
    private String s2;
    private String score;

    public Match() {
    }

    public String getId() {
        return id;
    }

    public String getDateForOrderMatch() {
        return dateForOrderMatch;
    }

    public void setDateForOrderMatch(String dateForOrderMatch) {
        this.dateForOrderMatch = dateForOrderMatch;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchTeams() {
        return matchTeams;
    }

    public void setMatchTeams(String matchTeams) {
        this.matchTeams = matchTeams;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getNoteMatch() {
        return noteMatch;
    }

    public void setNoteMatch(String noteMatch) {
        this.noteMatch = noteMatch;
    }

    public String getDateMatch() {
        return dateMatch;
    }

    public void setDateMatch(String dateMatch) {
        this.dateMatch = dateMatch;
    }

    public String getTimeMatch() {
        return timeMatch;
    }

    public void setTimeMatch(String timeMatch) {
        this.timeMatch = timeMatch;
    }

    public String getHomeMatch() {
        return homeMatch;
    }

    public void setHomeMatch(String homeMatch) {
        this.homeMatch = homeMatch;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}