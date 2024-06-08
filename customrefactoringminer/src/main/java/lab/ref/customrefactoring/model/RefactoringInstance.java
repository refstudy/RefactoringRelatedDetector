package lab.ref.customrefactoring.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lab.ref.customrefactoring.minerhandler.tool.refminer.refactoringtypes.RefactoringParser;
import lab.ref.customrefactoring.model.RelatedChange.RelationType;

public class RefactoringInstance {

    public enum Type {

        PULL_UP,
        PUSH_DOWN,
        MOVE_RENAME_CLASS,
        MOVE_RENAME_METHOD,
        MOVE_METHOD,
        MOVE_CLASS,
        RENAME_METHOD,
        RENAME_CLASS,
        EXTRACT_SUPERCLASS,
        EXTRACT_SUPERINTERFACE,
        EXTRACT_METHOD,
        INLINE_METHOD,
        NOT_USED,
        ERROR;

    }

    private String commit;
    private String project;
    private RefactoringParser parser;
    private ArrayList<String> tools = new ArrayList<>();
    private ArrayList<TreeASTNode<? extends ASTNode>> sourceElements = new ArrayList<>();
    private ArrayList<TreeASTNode<? extends ASTNode>> targetElements = new ArrayList<>();
    private Type type;
    private ArrayList<RelatedChange> relatedChanges = new ArrayList<>();
    private String metadata = "";

    public RefactoringInstance(
            List<TreeASTNode<? extends ASTNode>> sourceElements,
            List<TreeASTNode<? extends ASTNode>> targetElements,
            List<RelatedChange> relatedChanges,
            RefactoringParser parser) {
        this.parser = parser;
        this.sourceElements = new ArrayList<>(sourceElements);
        this.targetElements = new ArrayList<>(targetElements);
        this.relatedChanges = new ArrayList<>(relatedChanges);
        this.type = this.parser.getRefactoringType();
    }

    public Type getType() {
        return this.type;
    }

    public String getCommit() {
        return this.commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public ArrayList<String> getTools() {
        return this.tools;
    }

    public void addTools(String tool) {
        this.tools.add(tool);
    }

    public ArrayList<TreeASTNode<? extends ASTNode>> getSourceElements() {
        return this.sourceElements;
    }

    public ArrayList<TreeASTNode<? extends ASTNode>> getTargetElements() {
        return this.targetElements;
    }

    public boolean shouldMerge() {
        return this.parser.hasToMerge();
    }

    public boolean merge(RefactoringInstance refactoringInstance) {
        return this.parser.mergeRefactoring(this, refactoringInstance);
    }

    public ArrayList<RelatedChange> getRelatedChanges() {
        return this.relatedChanges;
    }

    public void setRelatedChanges(ArrayList<RelatedChange> relatedChanges) {
        this.relatedChanges = relatedChanges;
    }

    // @Override
    // public String toString() {
    // return "{" +
    // " commit='" + getCommit() + "'" +
    // ", project='" + getProject() + "'" +
    // ", tools='" + getTools() + "'" +
    // ", type='" + getType() + "'" +
    // ", affectedElements='" + getAffectedElements() + "'" +
    // ", sourceElements='" + getSourceElements() + "'" +
    // ", targetElements='" + getTargetElements() + "'" +
    // "}";
    // }

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("commit", getCommit());
        jsonObject.addProperty("sourceElements", getSourceElements().toString());
        jsonObject.addProperty("targetElements", getTargetElements().toString());
        jsonObject.addProperty("type", getType().toString());
        JsonArray relatedArray = new JsonArray();
        JsonArray notRelatedArray = new JsonArray();

        this.relatedChanges.stream()
                .forEach(rc -> {
                    if (rc.getRelationType().equals(RelationType.NOT_RELATED)) {
                        notRelatedArray.add(rc.toJSON());
                    } else {
                        relatedArray.add(rc.toJSON());
                    }
                });

        jsonObject.add("related", relatedArray);
        jsonObject.add("notRelated", notRelatedArray);
        jsonObject.add("metadata", JsonParser.parseString(this.metadata));
        return jsonObject.toString();
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
