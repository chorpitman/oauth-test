package com.lunapps.components.cron;

import com.lunapps.models.AdvertStatus;
import com.lunapps.models.JobAdvert;
import com.lunapps.models.ParkAdvert;
import com.lunapps.repository.JobAdvertRepository;
import com.lunapps.repository.ParkAdvertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class ScheduledTasks {
    @Autowired
    private JobAdvertRepository jobAdvertRepository;
    @Autowired
    private ParkAdvertRepository parkAdvertRepository;

    @Scheduled(cron = "0 0 * * * *") // the top of every hour of every day.
    public void scheduleTaskUsingCronExpression() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        //disable jobAdverts
        List<JobAdvert> parkAdvert = jobAdvertRepository.findAllByAdvertStatus(AdvertStatus.ENABLE);
        System.out.println("starting process of disable adverts which date expiry" + currentTime);
        if (parkAdvert.size() != 0) {
            for (JobAdvert advert : parkAdvert) {
                if (advert.getMustDoneWorkTo().isBefore(currentTime)) {
                    advert.setAdvertStatus(AdvertStatus.DISABLE);
                    advert.setUpdatedDate(ZonedDateTime.now());
                    jobAdvertRepository.save(advert);
                }
            }
        }

        List<ParkAdvert> parkAdverts = parkAdvertRepository.findAllByAdvertStatus(AdvertStatus.ENABLE);
        System.out.println("starting process of disable adverts which date expiry" + currentTime);
        //disable parkAdverts
        if (parkAdverts.size() != 0) {
            for (ParkAdvert advert : parkAdverts) {
                if (advert.getAvailabilityTimeTo().isBefore(currentTime)) {
                    advert.setAdvertStatus(AdvertStatus.DISABLE);
                    advert.setUpdateTime(ZonedDateTime.now());
                    parkAdvertRepository.save(advert);
                }
            }
        }
    }
}