package com.olxapplication.service;

import com.olxapplication.constants.ReportMessages;
import com.olxapplication.entity.Announcement;
import com.olxapplication.repository.AnnouncementRepository;
import com.olxapplication.strategy.Context;
import com.olxapplication.strategy.CsvGenerator;
import com.olxapplication.strategy.PdfGenerator;
import com.olxapplication.strategy.TxtGenerator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;


@Service
@AllArgsConstructor
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private final AnnouncementRepository announcementRepository;

    public List<Announcement> getTimeSortedAnnounces(){
        List<Announcement> announcements = announcementRepository.findAll();
        announcements.sort(Comparator.comparing(Announcement::getDate));

        return announcements;
    }

    public Map<YearMonth, Integer> getData(List<Announcement> announcements){
        Map<YearMonth, Integer> map = new HashMap<>();
        for (Announcement announcement : announcements) {
            YearMonth yearMonth = YearMonth.of(announcement.getDate().getYear(), announcement.getDate().getMonth());
            if(map.containsKey(yearMonth)){
                map.put(yearMonth, map.get(yearMonth) + 1);
            } else {
                map.put(yearMonth, 1);
            }
        }
        return map;
    }

    public String generateReport(String fileType) {
        Map<YearMonth, Integer> map = getData(getTimeSortedAnnounces());

        if (fileType.equals("csv")) {
            Context context = new Context(new CsvGenerator());
            return context.executeStrategy(map);
        }
        if (fileType.equals("txt")) {
            Context context = new Context(new TxtGenerator());
            return context.executeStrategy(map);
        }
        if (fileType.equals("pdf")) {
            Context context = new Context(new PdfGenerator());
            return context.executeStrategy(map);
        }
        return ReportMessages.REPORT_NOT_GENERATED + "invalid strategy";
    }
}


