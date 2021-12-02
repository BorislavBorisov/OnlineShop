package project.onlinestore.domain.binding;

import javax.validation.constraints.Size;

public class QuestionBindingModel {

    private String email;
    private String phoneNumber;
    private String question;

    public QuestionBindingModel() {
    }

    @Size(max = 55)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(max = 20)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Size(max = 150)
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
