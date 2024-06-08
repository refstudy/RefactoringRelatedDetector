package org.metricsminer.model;

import java.util.ArrayList;

import org.metricsminer.model.astnodes.FileAST;
import org.metricsminer.model.diff.FileDiff;

public class Commit{
    
    private String hash;
    private String previousHash;
    private String comment;
    private String author;
    private String commiter;
    private String email;
    private ArrayList<FileDiff> files = new ArrayList<>();
    
    public Commit(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCommiter() {
        return commiter;
    }
    public void setCommiter(String commiter) {
        this.commiter = commiter;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<FileDiff> getFiles() {
        return files;
    }



    

}
