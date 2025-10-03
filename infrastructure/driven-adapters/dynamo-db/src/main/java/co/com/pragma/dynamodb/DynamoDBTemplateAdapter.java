package co.com.pragma.dynamodb;

import co.com.pragma.dynamodb.helper.TemplateAdapterOperations;
import co.com.pragma.model.report.Report;
import co.com.pragma.model.report.gateways.ReportRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.Map;

@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Report, String, ReportEntity> implements ReportRepository {

    private static final String REPORT_ID = "APPROVED_LOAN_COUNT";
    private final DynamoDbAsyncTable<ReportEntity> table;
    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper, @Value("${adapters.dynamodb.tableName}") String tableName, DynamoDbAsyncClient dynamoDbAsyncClient) {
        // 3. CORRECCIÓN: Pasamos 'tableName' al super() y ajustamos la función de mapeo
        super(connectionFactory, mapper, entity -> new Report(entity.getTotalCount()),
                tableName);

        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
        // 4. CORRECCIÓN: Usamos ReportEntity.class para crear la tabla
        this.table = connectionFactory.table(tableName, TableSchema.fromBean(ReportEntity.class));
    }

    @Override
    public Mono<Report> getCurrentReport() {
        return getById(REPORT_ID)
                .defaultIfEmpty(new Report(0L));
    }

    @Override
    public Mono<Void> incrementApprovedLoanCount() {
        Map<String, AttributeValue> key = Map.of(
                "metric_id", AttributeValue.builder().s(REPORT_ID).build()
        );

        String updateExpression = "SET totalCount = if_not_exists(totalCount, :start) + :inc";
        Map<String, AttributeValue> expressionAttributeValues = Map.of(
                ":inc", AttributeValue.builder().n("1").build(),
                ":start", AttributeValue.builder().n("0").build()
        );

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(table.tableName())
                .key(key)
                .updateExpression(updateExpression)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        return Mono.fromFuture(dynamoDbAsyncClient.updateItem(request)).then();
    }
}