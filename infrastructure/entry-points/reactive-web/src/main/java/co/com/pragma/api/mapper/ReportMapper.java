package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.ReportDTO;
import co.com.pragma.model.report.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "approvedLoanCount", target = "totalApprovedLoans")
    ReportDTO toDTO(Report report);
}
