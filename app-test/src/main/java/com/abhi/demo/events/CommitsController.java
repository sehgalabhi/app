package com.abhi.demo.events;

import com.abhi.demo.github.GithubClient;
import com.abhi.demo.github.RepositoryEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommitsController {

    private final GithubProjectRepository repository;
    private final GithubClient githubClient;

    public CommitsController(GithubProjectRepository repository, GithubClient githubClient) {
        this.repository = repository;
        this.githubClient = githubClient;
    }

    @GetMapping("/commits/{repoName}")
    @ResponseBody
    public RepositoryEvent[] fetchEvents(@PathVariable String repoName){
        GithubProject project = this.repository.findByRepoName(repoName);
        return githubClient.fetchEvents(project.getOrgName(), project.getRepoName()).getBody();
    }
}
