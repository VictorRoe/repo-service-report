package co.com.pragma.sqs.listener;

import co.com.pragma.usecase.report.ReportUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final ReportUseCase useCase;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Mensaje de SQS recibido para actualizar reporte: {}", message.body());

        return useCase.processNewApprovedLoan()
                .doOnSuccess(v -> log.info("Contador de reportes actualizado con éxito para el mensaje: {}", message.messageId()))
                .doOnError(err -> log.error("Error procesando el mensaje SQS: {}", message.messageId(),err));
    }
}
