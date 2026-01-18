package com.xti.bank.mapper;

import com.xti.bank.domain.PixTransaction;
import com.xti.bank.event.PixTransactionCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = ComponentModel.SPRING,          // → becomes Spring @Component
        unmappedTargetPolicy = ReportingPolicy.IGNORE,   // don't fail on unmapped fields
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface PixTransactionMapper {

    PixTransaction toEvent(PixTransactionCreatedEvent event);
}
