package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.binding.QuestionBindingModel;
import project.onlinestore.domain.entities.QuestionEntity;
import project.onlinestore.domain.view.QuestionViewModel;
import project.onlinestore.repository.QuestionRepository;
import project.onlinestore.service.QuestionService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, ModelMapper modelMapper) {
        this.questionRepository = questionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void sendQuestion(QuestionBindingModel question) {
        QuestionEntity map = this.modelMapper.map(question, QuestionEntity.class);
        map.setRegistered(Instant.now());
        this.questionRepository.save(map);
    }

    @Override
    public List<QuestionViewModel> getAllQuestions() {
        return this.questionRepository.findAll()
                .stream()
                .map(q -> this.modelMapper.map(q, QuestionViewModel.class))
                .collect(Collectors.toList());
    }
}
