package com.blogpost.project.repository;


import com.blogpost.project.model.Posts;
import com.blogpost.project.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tags, Integer> {
     Tags getTagByName(String name);
}
