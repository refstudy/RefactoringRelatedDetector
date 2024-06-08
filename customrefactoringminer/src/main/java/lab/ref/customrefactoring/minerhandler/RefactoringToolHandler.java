package lab.ref.customrefactoring.minerhandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitService;
import org.refactoringminer.util.GitServiceImpl;

import com.google.gson.Gson;

import lab.ref.customrefactoring.model.Config;
import lab.ref.customrefactoring.model.RepositoryStatus;
import lab.ref.customrefactoring.util.ExceptionLogger;
import lab.ref.customrefactoring.util.GitUtils;
import lab.ref.customrefactoring.util.Utils;

public class RefactoringToolHandler {

    private List<RepositoryStatus> repositories;
    private Config config;
    private String configpath;
    private List<RefactoringCollectorTool> refactoringToolMiners = new ArrayList<>();
    private int countProjects = 0;
    private int countCommits = 0;
    public static Map<String, Integer> refsCount = new ConcurrentHashMap<>();
    private Set<String> processedCommits = ConcurrentHashMap.newKeySet();
    private Path processedCommitPath;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Gson gson = new Gson();

    public RefactoringToolHandler(Config config, String configpath) {
        this.repositories = config.getRepositories();
        this.config = config;
        this.configpath = configpath;
    }

    public static RefactoringToolHandler build(String configPath) throws Exception {

        Config config = Config.loadConfig(configPath);

        config.getRepositories().stream().forEach(repository -> {
            repository.loadPathes(config);
        });

        return new RefactoringToolHandler(config, configPath);

    }

    public RefactoringToolHandler addTool(RefactoringCollectorTool refMinerTool) {
        refactoringToolMiners.add(refMinerTool);
        return this;
    }

    private synchronized void saveAndPrint() {

        System.out.println(refsCount);
        try {
            Files.write(processedCommitPath, processedCommits);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProcessedCommits() {
        processedCommits.clear();
        Path parentDir = processedCommitPath.getParent();
        try {

            if (parentDir != null) {
                Files.createDirectories(parentDir); // Create parent directories if they don't exist
            }

            if (!Files.exists(processedCommitPath)) {
                Files.createFile(processedCommitPath); // Create the file if it doesn't exist
            }

            String commitsText = Utils.fileRead(processedCommitPath.toAbsolutePath().toString());
            String[] lines = commitsText.split("\\r?\\n");
            for (String line : lines) {
                processedCommits.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupProgressRoutine() {
        Runnable task = () -> saveAndPrint();

        int initialDelay = 30; // Initial delay in seconds before first execution
        int interval = 30; // Interval in seconds between subsequent executions

        scheduler.scheduleAtFixedRate(task, initialDelay, interval, TimeUnit.SECONDS);
    }

    public RefactoringToolHandler collectAll() {

        final int totalProjects = repositories.size();

        setupProgressRoutine();

        // For each repo
        repositories.stream().sorted((o1, o2) -> o1.getCommits() - o2.getCommits()).forEach(repo -> {
            System.out.println("Repository " + repo.getUrl());
            countProjects++;

            if (repo.isDone()) {
                System.out.println(" Already collected");
                return;
            }

            loadRepo(repo);

            // For Each tool
            refactoringToolMiners.stream().forEach(tool -> {

                List<String> defaultCommits = GitUtils.getCommitsFromRepositoryPath(repo.getFullPath(),
                        repo.getMainBranch());
                repo.setCommits(defaultCommits.size());

                if(defaultCommits.size()<10){
                    return;
                }

                System.out.println("Running tool: " + tool.getName());
                System.out.println("Total of commits " + defaultCommits.size());

                String projectText = repo.getName() + "(" + countProjects + "/"
                        + totalProjects + "): " + tool.getName();

                processedCommitPath = Paths.get(repo.getOutputPath(), "__metadada__", tool.getName() + ".txt");
                loadProcessedCommits();

                defaultCommits.parallelStream().forEach((commit) -> {

                    countCommits++;

                    File outputFold = repo.getCommitFold(commit);
                    File toolCommitInfo = new File(outputFold, tool.getName() + ".json");

                    String commitInfo = projectText +
                            " - Commit (" + countCommits + "/" + defaultCommits.size()
                            + ") " + commit;

                    if (!processedCommits.contains(commit)) {
                        System.out.println(commitInfo + " collecting...");
                        tool.collect(repo, commit, toolCommitInfo.getAbsolutePath());
                        processedCommits.add(commit);
                    } else {
                        System.out.println(commitInfo);
                    }
                });
                countCommits = 0;
                tool.clear();
            });

            // Update the config indicating that the repository is done
            repo.setDone();
            saveAndPrint();
            Utils.fileWrite(configpath, gson.toJson(config));
        });
        scheduler.shutdownNow();
        return this;
    }

    private void loadRepo(RepositoryStatus repository) {
        GitService gitService = new GitServiceImpl();
        Repository repo;
        try {
            System.out.println("clonning " + repository.getPath());
            repo = gitService.cloneIfNotExists(
                    repository.getPath(),
                    repository.getUrl());
            repository.setRepository(repo);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionLogger.log(e, "Failed to clone project " + repository.getUrl());
        }

    }

}
