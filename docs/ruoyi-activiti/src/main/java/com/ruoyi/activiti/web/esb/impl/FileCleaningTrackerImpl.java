package com.ruoyi.activiti.web.esb.impl;

import org.apache.commons.io.FileCleaningTracker;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileCleaningTrackerImpl extends FileCleaningTracker {
    @Override
    public void track(File file, Object marker) {
        super.track(file, marker);
    }
}
