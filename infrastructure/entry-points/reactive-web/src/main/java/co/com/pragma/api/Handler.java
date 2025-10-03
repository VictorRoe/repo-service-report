package co.com.pragma.api;

import co.com.pragma.api.mapper.ReportMapper;
import co.com.pragma.usecase.report.ReportUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {
    private final ReportUseCase useCase;
    private final ReportMapper mapper;


    public Mono<ServerResponse> getReport(ServerRequest serverRequest) {
        log.info("[getReport] Peticion recibida para obtener reporte de prestamos aprobados.");
        return useCase.getReport()
                .map(mapper::toDTO)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto)
                );
    }
}
