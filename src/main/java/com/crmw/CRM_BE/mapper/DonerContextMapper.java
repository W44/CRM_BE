package com.crmw.CRM_BE.mapper;

import com.crmw.CRM_BE.dto.DonerContextResponseDto;
import com.crmw.CRM_BE.entity.DonerContext;

public class DonerContextMapper {

    public static DonerContextResponseDto mapToDto(DonerContext context) {
        DonerContextResponseDto dto = new DonerContextResponseDto();
        dto.setId(context.getId());
        dto.setDonerId(context.getDoner().getId());
        dto.setTextContent(context.getTextContent());
        dto.setNotes(context.getNotes());
        dto.setCreatedAt(context.getCreatedAt());
        return dto;
    }
}
