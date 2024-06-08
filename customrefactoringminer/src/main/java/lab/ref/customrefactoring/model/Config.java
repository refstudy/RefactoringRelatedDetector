package lab.ref.customrefactoring.model;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;

public class Config {

    private String tempFoldPath;
    private String outputFoldPath;
    private List<RepositoryStatus> repositories;
  

    private Config() {

    }

    public static Config loadConfig(String path) throws IOException {
        Gson gson = new Gson();
        System.out.println(Paths.get(path).toAbsolutePath());
        Reader reader = Files.newBufferedReader(Paths.get(path).toAbsolutePath());
        return gson.fromJson(reader, Config.class);
    }

    public String getTempFoldPath() {
        return this.tempFoldPath;
    }


    public String getOutputFoldPath() {
        return this.outputFoldPath;
    }


    public List<RepositoryStatus> getRepositories() {
        return this.repositories;
    }

}
