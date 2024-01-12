package me.code.server.service;

import me.code.server.dto.response.Result;
import me.code.server.model.Blog;
import me.code.server.model.User;

import java.util.List;

public interface BlogService {

    Result publishBlog(User user, String title, String text);

    List<Blog> getAllBlogs();

    Blog getBlog(String blogId);

    List<Blog> getSearchedBlogs(String query, String filter);

    Blog loadBlogById(String blogId);

}
