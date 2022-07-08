package com.blogpost.project.Serviceimplementation;

import com.blogpost.project.model.Tags;
import com.blogpost.project.repository.TagRepository;
import com.blogpost.project.service.TagService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Override
    public Optional<Tags> getTagById(Integer tagId) throws Exception {
        Optional<Tags> tags = tagRepository.findById(tagId);

        if(tags.isPresent()){
            return tags;
        }
        else{
            throw new Exception();
        }
    }

    public List<Tags> getAllTag(){
        List<Tags> tagsList = tagRepository.findAll();
        return tagsList;
    }

    @Override
    public String getAllTagName(List<Tags> tagsList) {
        String allTags = "";
        for (Tags eachTag : tagsList) {
            String name = eachTag.getName() + ",";
            allTags += name;
        }
        return allTags;
    }

    @Override
    public void save(Tags tags) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try{
            tags.setCreatedAt(timestamp);
            tagRepository.save(tags);
        }
        catch (ConstraintViolationException | DataIntegrityViolationException exception){
            exception.printStackTrace();
        }
    }
}