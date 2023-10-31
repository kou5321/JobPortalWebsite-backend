package com.kou5321.jobPortalWebsite.job.controller;

import com.kou5321.jobPortalWebsite.job.model.Post;
import com.kou5321.jobPortalWebsite.job.repository.PostRepository;
import com.kou5321.jobPortalWebsite.job.repository.SearchRepository;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class PostController {

    PostRepository postRepository;

    SearchRepository searchRepository;

    @RequestMapping("/")
    @Hidden
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    @GetMapping("/getAllPosts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @PostMapping("/post")
    public Post addPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    @GetMapping("/post/search/text={text}")
    public List<Post> searchPost(@PathVariable String text) {
        return searchRepository.findByText(text);
    }
}
