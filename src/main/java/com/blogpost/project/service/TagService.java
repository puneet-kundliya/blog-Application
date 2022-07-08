package com.blogpost.project.service;

import com.blogpost.project.model.Tags;
import java.util.List;
import java.util.Optional;

public interface TagService {
    Optional<Tags> getTagById(Integer tagId) throws Exception;

     List<Tags> getAllTag();

    String getAllTagName(List<Tags> tagsList);

    void save(Tags newTag);
}
