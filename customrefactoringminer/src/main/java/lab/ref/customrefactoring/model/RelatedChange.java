package lab.ref.customrefactoring.model;

import org.metricsminer.metric.changemetricsgenerator.ChangeMetric;

import com.google.gson.JsonObject;

public class RelatedChange {

    public enum RelationType {
        MENTIONED,
        MENTIONED_DECLARATION,
        VAR_RELATED,
        SIGNATURE,
        CALL_SOURCE,
        CALL_TARGET,
        NOT_RELATED
    }

    private ChangeMetric changeMetric;
    private RelationType relationType;
    private int distance;
    private String relationDetail;

    public RelatedChange(ChangeMetric changeMetric, RelationType relationType, int distance) {
        this.changeMetric = changeMetric;
        this.relationType = relationType;
        this.distance = distance;
    }

    public ChangeMetric getChangeMetric() {
        return this.changeMetric;
    }

    public RelationType getRelationType() {
        return this.relationType;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setChangeMetric(ChangeMetric changeMetric) {
        this.changeMetric = changeMetric;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setRelationDetail(String relationDetail) {
        this.relationDetail = relationDetail;
    }

    public String getRelationDetail() {
        return this.relationDetail;
    }

    @Override
    public String toString() {
        return "{" +
                " Metric='" + getChangeMetric().getName() + "'" +
                " Method='" + getChangeMetric().getParent().getFullName() + "'" +
                " Local='" + getChangeMetric().getTextPos() + "'" +
                " Detail='" + getRelationDetail() + "'" +
                ", Code='" + getChangeMetric().getClosestElement().getNode() + "'" +
                ", relationType='" + getRelationType() + "'" +
                ", distance='" + getDistance() + "'" +
                "}";
    }

    public JsonObject toJSON() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("Metric", getChangeMetric().getName());
        if (getChangeMetric().getParent() != null) {
            jsonObject.addProperty("Method", getChangeMetric().getParent().getFullName());
        } else {
            jsonObject.addProperty("Method", getChangeMetric().getClosestElement().getFullName());
        }
        jsonObject.addProperty("Local", getChangeMetric().getTextPos());
        jsonObject.addProperty("Detail", getRelationDetail());
        jsonObject.addProperty("Code", getChangeMetric().getClosestElement().getNode().toString());
        jsonObject.addProperty("relationType", getRelationType().toString());
        jsonObject.addProperty("distance", getDistance());

        return jsonObject;
    }

}
