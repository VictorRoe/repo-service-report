package co.com.pragma.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

/* Enhanced DynamoDB annotations are incompatible with Lombok #1932
         https://github.com/aws/aws-sdk-java-v2/issues/1932*/
@DynamoDbBean
public class ReportEntity {

    private String metricId;
    private Long totalCount;

    public ReportEntity() {
    }


    public ReportEntity(String metricId, Long totalCount) {
        this.metricId = metricId;
        this.totalCount = totalCount;
    }

    @DynamoDbPartitionKey
    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
