package project.onlinestore.domain.service;

import java.time.Instant;

public class QuestionServiceModel extends BaseServiceModel {

    private String email;
    private String phoneNumber;
    private String question;
    private Instant registered;
    private boolean status;

    public QuestionServiceModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getRegistered() {
        return registered;
    }

    public void setRegistered(Instant registered) {
        this.registered = registered;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
