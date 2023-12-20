package offside.server.stadium.service;

import jakarta.transaction.Transactional;

import java.util.List;

import offside.server.stadium.domain.Stadium;
import offside.server.stadium.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StadiumService {
    private final StadiumRepository stadiumRepository;
    
    @Autowired
    public StadiumService(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }
    
    public List<Stadium> requestStadium(String location, String contactPhone){
        // if 문으로 다 쪼개서 서로 다른 repo 메소드를 호출
        if (location.isEmpty() && !contactPhone.isEmpty()) {
            return stadiumRepository.findAllByContactPhone(contactPhone);
        } else if (!location.isEmpty() && contactPhone.isEmpty()) {
            return stadiumRepository.findAllByLocation(location);
        } else if (location.isEmpty()) { // 둘 다 비었을 때, (contact_phone.isEmpty() 는 이미 true)
            return stadiumRepository.findAll();
        }else{
            return stadiumRepository.findAllByLocationAndContactPhone(location, contactPhone);
        }
    }
}