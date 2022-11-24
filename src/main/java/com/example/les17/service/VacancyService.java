package com.example.les17.service;

import com.example.les17.dto.ApplicationDto;
import com.example.les17.dto.MessageDto;
import com.example.les17.dto.UserDto;
import com.example.les17.dto.VacancyDto;
import com.example.les17.exception.InvalidApplicationException;
import com.example.les17.exception.InvalidVacancyException;
import com.example.les17.model.Application;
import com.example.les17.model.User;
import com.example.les17.model.Vacancy;
import com.example.les17.repository.ApplicationRepository;
import com.example.les17.repository.VacancyRepository;
import com.example.les17.security.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final ApplicationRepository applicationRepository;

    public VacancyService(VacancyRepository vacancyRepository, ApplicationRepository applicationRepository) {
        this.vacancyRepository = vacancyRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<VacancyDto> getAllVacancies() {
        List<Vacancy> vacancies = new ArrayList<>();
        vacancyRepository.findAll().iterator().forEachRemaining(vacancies::add);
        return vacancies.stream().map(this::getVacancyDto).collect(Collectors.toList());
    }

    public VacancyDto createVacancy(VacancyDto v) {
        Vacancy vacancy = new Vacancy(v.getId(), v.getDescription(), v.getHourlyRate(), v.getWorkingHour(), v.getType(), v.getLocation(), v.getStatus());
        vacancy = vacancyRepository.save(vacancy);
        VacancyDto result = getVacancyDto(vacancy);
        return result;
    }

    public VacancyDto updateVacancy(VacancyDto vacancyDto) {
        if (vacancyDto.getId() == null) {
            throw new InvalidVacancyException("Invalid vacancy id is supplied");
        }

        Optional<Vacancy> vo = vacancyRepository.findById(vacancyDto.getId());

        Vacancy vacancy = vo.orElseThrow(() -> new InvalidVacancyException("Vacancy not found"));
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setHourlyRate(vacancyDto.getHourlyRate());
        vacancy.setWorkingHour(vacancyDto.getWorkingHour());
        vacancy.setType(vacancyDto.getType());
        vacancy.setLocation(vacancyDto.getLocation());
        vacancy.setStatus(vacancyDto.getStatus());
        vacancyRepository.save(vacancy);
        VacancyDto result = getVacancyDto(vacancy);
        return result;
    }

    private VacancyDto getVacancyDto(Vacancy vacancy) {
        VacancyDto result = new VacancyDto(vacancy.getId(), vacancy.getDescription(), vacancy.getHourlyRate(), vacancy.getWorkingHour(), vacancy.getType(), vacancy.getLocation(), vacancy.getStatus());
        List<Application> applications = applicationRepository.findByVacancyId(vacancy.getId());
        for (Application application : applications) {
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setId(application.getId());
            applicationDto.setStatus(application.getStatus());

            UserDto userDto = new UserDto();
            userDto.setUsername(application.getEmployee().getUsername());
            userDto.setCvUniqueId(application.getEmployee().getCvUniqueId());
            userDto.setCvFilename(application.getEmployee().getCvFilename());
            applicationDto.setUser(userDto);
            result.getApplications().add(applicationDto);
        }
        return result;
    }

    public MessageDto apply(Integer id) {
        if (id == null) {
            throw new InvalidVacancyException("Invalid vacancy id is supplied");
        }
        Optional<Vacancy> vo = vacancyRepository.findById(id);
        Vacancy vacancy = vo.orElseThrow(() -> new InvalidVacancyException("Vacancy not found"));

        User user = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        boolean isApplied = vacancy.getApplications().stream().anyMatch(ap -> ap.getVacancy().getId().equals(vacancy.getId()) && ap.getEmployee().getUsername().equals(user.getUsername()));

        if (isApplied) {
            throw new InvalidApplicationException("Already applied");
        }

        if ("CLOSE".equals(vacancy.getStatus())) {
            throw new InvalidApplicationException("Already closed");
        }

        Application application = new Application();
        application.setVacancy(vacancy);
        application.setEmployee(user);
        application.setStatus("PENDING");
        applicationRepository.save(application);
        return new MessageDto("Applied successfully");
    }

    public VacancyDto getVacancy(Integer id) {
        if (id == null) {
            throw new InvalidVacancyException("Invalid vacancy id is supplied");
        }
        Optional<Vacancy> vo = vacancyRepository.findById(id);
        Vacancy vacancy = vo.orElseThrow(() -> new InvalidVacancyException("Vacancy not found"));
        return getVacancyDto(vacancy);
    }

    public MessageDto complete(Integer id, Integer applicationId, String acceptReject) {
        if (id == null) {
            throw new InvalidVacancyException("Invalid vacancy id is supplied");
        }
        if (applicationId == null) {
            throw new InvalidApplicationException("Invalid application id is supplied");
        }

        Optional<Vacancy> vo = vacancyRepository.findById(id);
        Vacancy vacancy = vo.orElseThrow(() -> new InvalidVacancyException("Vacancy not found"));

        Optional<Application> ao = applicationRepository.findById(applicationId);
        Application application = ao.orElseThrow(() ->  new InvalidApplicationException("Application not found"));

        application.setStatus(acceptReject);
        applicationRepository.save(application);
        return new MessageDto("Completed successfully");
    }
}
