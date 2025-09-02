package org.dis.gamedata.service.mapper;

import org.dis.gamedata.model.Job;
import org.dis.gamedata.service.dto.JobDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {
   Job fromDto(JobDTO jobDTO);
   JobDTO toDto(Job job);
}