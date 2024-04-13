package com.fastcampus.boardserver.service.impl;

import com.fastcampus.boardserver.dto.CommentDTO;
import com.fastcampus.boardserver.dto.PostDTO;
import com.fastcampus.boardserver.dto.TagDTO;
import com.fastcampus.boardserver.dto.UserDTO;
import com.fastcampus.boardserver.mapper.CommentMapper;
import com.fastcampus.boardserver.mapper.PostMapper;
import com.fastcampus.boardserver.mapper.TagMapper;
import com.fastcampus.boardserver.mapper.UserProfileMapper;
import com.fastcampus.boardserver.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;

    private final UserProfileMapper userProfileMapper;

    private final CommentMapper commentMapper;

    private final TagMapper tagMapper;

    public PostServiceImpl(
            PostMapper postMapper,
            UserProfileMapper userProfileMapper,
            CommentMapper commentMapper,
            TagMapper tagMapper
    ) {
        this.postMapper = postMapper;
        this.userProfileMapper = userProfileMapper;
        this.commentMapper = commentMapper;
        this.tagMapper = tagMapper;
    }

    /**
     * @Cacheable
     * - 메서드의 결과를 캐시에 저장
     * - getUserById 메서드에 @Cacheable 적용되어 있기 때문에 결과가 캐시에 저장된다.
     *
     * @CacheEvict
     * - 캐시에서 데이터를 제거
     * - evicUserCache 메서드에는 @CacheEvict 어노테이션이 적용되어 해당 key(id)에 대한 캐시가 제거된다.
     */
    @CacheEvict(value="getProducts", allEntries = true)
    @Override
    public void register(String userId, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(userId);

        if (memberInfo != null) {
            postDTO.setUserId(memberInfo.getId());
            postDTO.setCreateTime(new Date());
            postMapper.register(postDTO);

            Integer postId = postDTO.getId();
            // 생성된 post 객체 에서 태그 리스트 생성
            for(int i = 0; i < postDTO.getTagDTOList().size(); i++){
                TagDTO tagDTO = postDTO.getTagDTOList().get(i);
                tagMapper.register(tagDTO);

                // M:N 관계 테이블 생성
                int tagId = tagDTO.getId();
                tagMapper.createPostTag(tagId, postId);
            }
        } else {
            log.error("register ERROR! {}", postDTO);
            throw new RuntimeException("register ERROR! 상품 등록 메서드를 확인해주세요\n" + "Params : " + postDTO);
        }
    }

    @Override
    public List<PostDTO> getMyProducts(int accountId) {
        return postMapper.selectMyProducts(accountId);
    }

    @CacheEvict(value="getProducts", allEntries = true)
    @Override
    public void updateProducts(PostDTO postDTO) {
        if (postDTO != null && postDTO.getId() != 0 && postDTO.getUserId() != 0) {
            postMapper.updateProducts(postDTO);
        } else {
            log.error("updateProducts ERROR! {}", postDTO);
            throw new RuntimeException("updateProducts ERROR! 물품 변경 메서드를 확인해주세요\n" + "Params : " + postDTO);
        }
    }

    @Override
    public void deleteProduct(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            postMapper.deleteProduct(productId);
        } else {
            log.error("deleteProudct ERROR! {}", productId);
            throw new RuntimeException("updateProducts ERROR! 물품 삭제 메서드를 확인해주세요\n" + "Params : " + productId);
        }
    }

    @Override
    public void registerComment(CommentDTO commentDTO) {
        if (commentDTO.getPostId() != 0) {
            commentMapper.register(commentDTO);
        } else {
            log.error("registerComment ERROR!");
            throw new RuntimeException("registerComment ERROR! 댓글 추가 메서드를 확인해주세요");
        }
    }

    @Override
    public void updateComment(CommentDTO commentDTO) {
        if (commentDTO != null) {
            commentMapper.updateComments(commentDTO);
        } else {
            log.error("updateComment ERROR~!");
            throw new RuntimeException("updateComment ERROR! 댓글 변경 메서드를 확인해주세요");
        }
    }

    @Override
    public void deletePostComment(int userId, int commentId) {
        if (userId != 0 && commentId != 0) {
            commentMapper.deletePostComment(commentId);
        } else {
            log.error("deletePostComment ERROR!");
            throw new RuntimeException("deletePostComment ERROR! 댓글 삭제 메서드를 확인해주세요");
        }
    }

    @Override
    public void registerTag(TagDTO tagDTO) {
        if (tagDTO.getPostId() != 0) {
            tagMapper.register(tagDTO);
        } else {
            log.error("registerTag ERROR");
            throw new RuntimeException("registerTag ERROR! 태그 추가 메서드를 확인해주세요");
        }
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        if (tagDTO != null) {
            tagMapper.updateTags(tagDTO);
        } else {
            log.error("updateTag ERROR");
            throw new RuntimeException("updateTag ERROR! 태그 변경 메서드를 확인해주세요");
        }
    }

    @Override
    public void deletePostTag(int userId, int tagId) {
        if (userId != 0 && tagId != 0) {
            tagMapper.deletePostTag(tagId);
        } else {
            log.error("deletePostTag ERROR");
            throw new RuntimeException("deletePostTag ERROR! 태그 삭제 메서드를 확인해주세요");
        }
    }
}
