package me.code.server.service;

import me.code.server.dto.response.SuccessDto;
import me.code.server.exception.CustomRuntimeException;
import me.code.server.model.Blog;
import me.code.server.model.User;
import me.code.server.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public SuccessDto publishBlog(User user, String title, String text) {
        try {
            var blog = new Blog(user, title, text);
            blogRepository.save(blog);

            return new SuccessDto(
                    HttpStatus.CREATED,
                    "Published blog with title: " + title);

        } catch (Exception exception) {
            throw new CustomRuntimeException(
                    HttpStatus.BAD_REQUEST,
                    "Failed to publish blog",
                    exception.getMessage());
        }
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public Blog getBlog(String blogId) {
        return loadBlogById(blogId);
    }

    @Override
    public Blog loadBlogById(String blogId) {
        return blogRepository.findById(blogId).orElseThrow(
                () -> new CustomRuntimeException(
                        HttpStatus.NOT_FOUND,
                        "Blog not found",
                        "Could not find blog with id: " + blogId
                ));
    }

    @Override
    public List<Blog> getSearchedBlogs(String search, String filter) {
        if (search.isBlank()) {
            return loadAllBlogsWithFilter(filter);
        } else if (filter.isBlank()) {
            return loadSearchedBlogs(search);
        } else {
            return loadSearchedBlogs(search, filter);
        }
    }

    private List<Blog> loadAllBlogsWithFilter(String filter) {
        return switch (filter) {
            case "newest" -> blogRepository.findAllAndOrderByTimeStamp("ASC");
            case "oldest" -> blogRepository.findAllAndOrderByTimeStamp("DESC");
            default -> getAllBlogs();
        };
    }

    private List<Blog> loadSearchedBlogs(String search) {
        return blogRepository.findBySearch(search);
    }

    private List<Blog> loadSearchedBlogs(String search, String filter) {
        return switch (filter) {
            case "newest" -> blogRepository.findBySearchAndOrderByTimeStamp(search, "ASC");
            case "oldest" -> blogRepository.findBySearchAndOrderByTimeStamp(search, "DESC");
            default -> getAllBlogs();
        };
    }
}
