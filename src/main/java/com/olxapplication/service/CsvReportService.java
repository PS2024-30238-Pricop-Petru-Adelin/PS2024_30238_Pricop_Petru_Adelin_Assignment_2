package com.olxapplication.service;

import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.Favourite;
import com.olxapplication.repository.AnnouncementRepository;
import com.opencsv.CSVWriter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.util.*;


@Service
@AllArgsConstructor
public class CsvReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReportService.class);
    private final AnnouncementRepository announcementRepository;

    public List<Announcement> getTimeSortedAnnounces(){
        List<Announcement> announcements = announcementRepository.findAll();
        announcements.sort(Comparator.comparing(Announcement::getDate));

        return announcements;
    }

    public Map<YearMonth, Integer> getCSVdata(List<Announcement> announcements){
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

    public String writeCSV(){
        Map<YearMonth, Integer> map = getCSVdata(getTimeSortedAnnounces());

        File file = new File("CSV_Report.csv");

        try{
            FileWriter outputFile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputFile);

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"Year-Month", "Number of posted announces"});
            for(Map.Entry<YearMonth, Integer> entry : map.entrySet()){
                data.add(new String[] {entry.getKey().toString(), entry.getValue().toString()});
            }
            writer.writeAll(data);
            writer.close();
            return "Successfully wrote to CSV_Report.csv";

        } catch (IOException e){
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }
}


