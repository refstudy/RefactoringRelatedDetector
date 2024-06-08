package lab.ref.customrefactoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GitHubRepositorySearch {

    public static void main(String[] args) {

        String keyword = "language:java";
        int minimumStars = 3500;
        int minimumForks = 500;
        int numberOfProjects = 250;
        int minimumCommits = 500;

        // Last 6 months
        LocalDate currentDate = LocalDate.now();
        LocalDate sixMonthsAgo = currentDate.minusMonths(6);

        // Date limit formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = sixMonthsAgo.format(formatter);

        try {

            int pageNumber = 6; 
            String url = "https://api.github.com/search/repositories?q=" + keyword +
                    "+stars:>=" + minimumStars +
                    "+forks:>=" + minimumForks +
                    "+pushed:>" + formattedDate +
                    "&sort=stars&order=desc&per_page=" + numberOfProjects +
                    "&page=" + pageNumber;

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonParser parser = new JsonParser();
            JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();
            JsonArray items = jsonResponse.getAsJsonArray("items");

            System.out.println("[");

            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                String urlValue = item.get("html_url").getAsString() + ".git";
                String mainBranch = item.get("default_branch").getAsString();
                int stars = item.get("stargazers_count").getAsInt();
                int forks = item.get("forks_count").getAsInt();

                int commits = countCommit(urlValue);

                if (commits < 500) {
                   continue;
                }

                System.out.print("{");
                System.out.print("\"url\": \"" + urlValue + "\",");
                System.out.print("\"mainBranch\": \"" + mainBranch + "\",");
                System.out.print("\"stars\": " + stars + ",");
                System.out.print("\"forks\": " + forks + ",");
                System.out.print("\"commits\": " + commits);
                System.out.println("},");

            }

            System.out.println("]");

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countCommit(String gitUrl) {

        try {
            URL url = new URL(gitUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }
            reader.close();

            Pattern pattern = Pattern.compile("<strong>([0-9,]+)</strong>[\n ]*<span aria-label=\"Commits on");
            Matcher matcher = pattern.matcher(htmlContent.toString());
            if (matcher.find()) {
                String commitCount = matcher.group(1).replace(",", "");
                return Integer.parseInt(commitCount);
            } else {
                System.out.println("Commit count not found");
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

}