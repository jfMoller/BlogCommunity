package me.code.server.controller;

import me.code.server.dto.request.PublishBlogDto;
import me.code.server.dto.response.SuccessDto;
import me.code.server.model.Blog;
import me.code.server.model.User;
import me.code.server.security.CurrentUser;
import me.code.server.service.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/blogs")
public class BlogController {

    private final BlogServiceImpl blogService;

    @Autowired
    public BlogController(BlogServiceImpl blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/all")
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/{blogId}")
    public Blog getBlog(@PathVariable String blogId) {
        return blogService.getBlog(blogId);
    }

    @GetMapping("/search")
    public List<Blog> getSearchedBlogs(@RequestParam String query, @RequestParam String filter) {
        return blogService.getSearchedBlogs(query, filter);
    }

    @PostMapping("/publish")
    public ResponseEntity<SuccessDto> publishBlog(@CurrentUser User user, @RequestBody PublishBlogDto dto) {
        var result = blogService.publishBlog(user, dto.title(), dto.text());
        return result.toResponseEntity();
    }

}
