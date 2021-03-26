package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;


class MedicalServiceImplTest {

    public static final PatientInfo PATIENT_INFO = new PatientInfo("Иван", "Петров",
            LocalDate.of(1980, 11, 26),
            new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));

    @Test
    void test_send_blood_pressure() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any()))
                .thenReturn(PATIENT_INFO);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure("id1", currentPressure);

        Mockito.verify(alertService, Mockito.times(1)).send(argumentCaptor.capture());
    }

    @Test
    void test_send_temperature() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any()))
                .thenReturn(PATIENT_INFO);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);


        BigDecimal currentTemperature = new BigDecimal("35.0");
        medicalService.checkTemperature("id1", currentTemperature);

        Mockito.verify(alertService, Mockito.times(1)).send(argumentCaptor.capture());
    }

    @Test
    void test_nosent_temperature_normal() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any()))
                .thenReturn(PATIENT_INFO);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);


        BigDecimal currentTemperature = new BigDecimal("36.0");
        medicalService.checkTemperature("id1", currentTemperature);
        Mockito.verify(alertService, Mockito.never()).send(argumentCaptor.capture());

    }

}