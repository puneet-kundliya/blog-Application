package com.blogpost.project.service;

import com.blogpost.project.model.Tags;
import com.blogpost.project.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}