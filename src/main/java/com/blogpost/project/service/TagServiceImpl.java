package com.blogpost.project.service;

import com.blogpost.project.model.Tags;
import com.blogpost.project.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Override
    public void saveTag(Tags tag) {
        tagRepository.save(tag);
    }
}