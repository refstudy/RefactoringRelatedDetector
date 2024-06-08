package lab.ref.customrefactoring.model;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;

import org.eclipse.jgit.lib.Repository;

import lab.ref.customrefactoring.util.ExceptionLogger;

public class RepositoryStatus {

    private String url;
    private String name;
    private String path;
    private String outputPath;
    private String mainBranch;
    private int stars;
    private int forks;
    private int commits;
    private boolean done;
    private transient Repository repository;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMainBranch() {
        return this.mainBranch;
    }

    public void setMainBranch(String mainBranch) {
        this.mainBranch = mainBranch;
    }

    public int getStars() {
        return this.stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return this.forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getCommits() {
        return this.commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }

    public Repository getRepository() {
        return this.repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void print() {
        System.out.print("{");
        System.out.print("\"url\": \"" + url + "\",");
        System.out.print("\"mainBranch\": \"" + mainBranch + "\",");
        System.out.print("\"stars\": " + stars + ",");
        System.out.print("\"forks\": " + forks + ",");
        System.out.print("\"commits\": " + commits);
        System.out.println("},");
    }

    public String getPath() {
        return this.path;
    }

    public String getFullPath() {
        return Paths.get(getPath()).toAbsolutePath().toString();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOutputPath() {
        return this.outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public File getCommitFold(String commit) {
        return Paths.get(getOutputPath(), commit).toFile();
    }

    public void loadPathes(Config config) {
        URI repoUrl;
        try {
            repoUrl = new URL(getUrl()).toURI();
            String name = repoUrl.getPath().substring(1).replaceAll("/", "-").replaceAll(".git", "");
            String path = config.getTempFoldPath() + "/" + name;
            String outPath = config.getOutputFoldPath() + "/" + name;
            setPath(path);
            setName(name);
            setOutputPath(outPath);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionLogger.log(e, "Failed to load paths");
        }

    }

    public boolean isDone(){
        return done; 
    }

    public void setDone(){
        this.done = true;
    }

}
