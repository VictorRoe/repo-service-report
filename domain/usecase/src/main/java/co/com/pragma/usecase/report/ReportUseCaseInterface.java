package co.com.pragma.usecase.report;

import co.com.pragma.model.report.Report;
import reactor.core.publisher.Mono;

public interface ReportUseCaseInterface {

    Mono<Report> getReport();

    Mono<Void> processNewApprovedLoan();
}
