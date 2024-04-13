package com.fastcampus.boardserver.mapper;

import com.fastcampus.boardserver.dto.PostDTO;
import com.fastcampus.boardserver.dto.request.PostSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostSearchMapper {
    List<PostDTO> selectPosts(PostSearchRequest postSearchRequest);

    List<PostDTO> getPostByTag(String tagName);
}
