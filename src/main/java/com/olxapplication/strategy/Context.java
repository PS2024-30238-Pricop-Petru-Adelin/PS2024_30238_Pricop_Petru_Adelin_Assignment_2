package com.olxapplication.strategy;

import com.olxapplication.entity.Announcement;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {
    private FileGeneratorStrategy strategy;

    public Context(FileGeneratorStrategy strategy) {
        this.strategy = strategy;
    }

//    public List<Announcement> getTimeSortedAnnounces(){
//        List<Announcement> announcements = announcementRepository.findAll();
//        announcements.sort(Comparator.comparing(Announcement::getDate));
//
//        return announcements;
//    }
//
//    public Map<YearMonth, Integer> getCSVdata(List<Announcement> announcements){
//        Map<YearMonth, Integer> map = new HashMap<>();
//        for (Announcement announcement : announcements) {
//            YearMonth yearMonth = YearMonth.of(announcement.getDate().getYear(), announcement.getDate().getMonth());
//            if(map.containsKey(yearMonth)){
//                map.put(yearMonth, map.get(yearMonth) + 1);
//            } else {
//                map.put(yearMonth, 1);
//            }
//        }
//        return map;
//    }

    public String executeStrategy(Map<YearMonth, Integer> announcements) {
        return strategy.generateFile(announcements);
    }
}
