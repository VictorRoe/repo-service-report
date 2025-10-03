package co.com.pragma.usecase.report;

import co.com.pragma.model.report.Report;
import co.com.pragma.model.report.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportUseCase implements ReportUseCaseInterface{

    private final ReportRepository repository;


    @Override
    public Mono<Report> getReport() {
        return repository.getCurrentReport();
    }

    @Override
    public Mono<Void> processNewApprovedLoan() {
        return repository.incrementApprovedLoanCount();
    }
}
