package com.EDTECH.QUIZ.GAME.sevices;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.repositories.UserRepository;


@Service
public class UserCleanupScheduler {
    
    @Autowired
    private UserRepository userRepo;

    // every 5 minutes the scheduler will run and clean up the unverified  after 16 mins
    @Scheduled(fixedRate = 60000) 
    public void deleteUnverifiedUsers() {
        Calendar cal = Calendar.getInstance();
        // 16 minutes ago
        cal.add(Calendar.MINUTE, -16);
        Date cutoff = cal.getTime();
        userRepo.deleteUnverifiedUsersBefore(cutoff);
        System.out.println("Univerfied users older than 16 minutes deleted.");
    }


}
