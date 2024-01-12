package me.code.server.controller;

import me.code.server.dto.response.SuccessDto;
import me.code.server.service.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    private final BlogServiceImpl blogService;

    @Autowired
    public AdminController(BlogServiceImpl blogService) {
        this.blogService = blogService;
    }

    @DeleteMapping("/blogs/delete/all")
    public ResponseEntity<SuccessDto> deleteAllBlogs() {
        var result = blogService.deleteAllBlogs();
        return result.toResponseEntity();
    }

    @DeleteMapping("/blogs/delete/{blogId}")
    public ResponseEntity<SuccessDto> deleteAllBlogs(@PathVariable String blogId) {
        var result = blogService.deleteBlog(blogId);
        return result.toResponseEntity();
    }

}
