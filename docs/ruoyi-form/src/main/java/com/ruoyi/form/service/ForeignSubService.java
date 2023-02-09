package com.ruoyi.form.service;

import com.ruoyi.form.domain.IncidentSubEvent;

import java.util.List;

public interface ForeignSubService {

    int insertIncidentSubEvent(IncidentSubEvent subEvent);

    int updateIncidentSubEvent(IncidentSubEvent subEvent);

    int updateIncidentSubEventByNo(IncidentSubEvent subEvent);

    List<IncidentSubEvent> selectIncidentSubEventList(IncidentSubEvent subEvent);

    IncidentSubEvent selectIncidentSubEventById(Long id);

    IncidentSubEvent selectIncidentSubEventByEventNo(String eventNo);

    int updateIncidentSubEvent(String eventNo, String flag);
}

