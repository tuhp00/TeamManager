package com.tuhp00.teammanager.training;

public class Training {

    private String id;
    private String dateForOrderTraining;
    private String dateTraining;
    private String timeTraining;
    private String noteTraining;

    public Training() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateForOrderTraining() {
        return dateForOrderTraining;
    }

    public void setDateForOrderTraining(String dateForOrderTraining) {
        this.dateForOrderTraining = dateForOrderTraining;
    }

    public String getDateTraining() {
        return dateTraining;
    }

    public void setDateTraining(String dateTraining) {
        this.dateTraining = dateTraining;
    }

    public String getTimeTraining() {
        return timeTraining;
    }

    public void setTimeTraining(String timeTraining) {
        this.timeTraining = timeTraining;
    }

    public String getNoteTraining() {
        return noteTraining;
    }

    public void setNoteTraining(String noteTraining) {
        this.noteTraining = noteTraining;
    }
}

