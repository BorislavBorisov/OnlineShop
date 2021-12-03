package project.onlinestore.service;

import project.onlinestore.domain.binding.QuestionBindingModel;
import project.onlinestore.domain.view.QuestionViewModel;

import java.util.List;

public interface QuestionService {
    void sendQuestion(QuestionBindingModel question);

    List<QuestionViewModel> getAllQuestions();
}
