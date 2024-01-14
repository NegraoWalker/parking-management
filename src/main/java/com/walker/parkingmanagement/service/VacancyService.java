package com.walker.parkingmanagement.service;

import com.walker.parkingmanagement.entity.Vacancy;
import com.walker.parkingmanagement.exception.CodeUniqueViolationException;
import com.walker.parkingmanagement.exception.EntityNotFoundException;
import com.walker.parkingmanagement.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.walker.parkingmanagement.entity.Vacancy.StatusVacancy.FREE;

@RequiredArgsConstructor
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    @Transactional
    public Vacancy save(Vacancy vacancy){
        try{
            return vacancyRepository.save(vacancy);
        }catch (DataIntegrityViolationException exception){
            throw new CodeUniqueViolationException(String.format("Vaga com código '%s' já cadastrada", vacancy.getCode()));
        }
    }

    @Transactional(readOnly = true)
    public Vacancy findyByCode(String code){
        return vacancyRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException(String.format("Vaga com código '%s' não foi encontrada",code)));
    }

    @Transactional(readOnly = true)
    public Vacancy findByVacancyFree() {
        return vacancyRepository.findFirstByStatus(FREE).orElseThrow(()-> new EntityNotFoundException("Nenhuma vaga livre foi encontrada!"));
    }
}
