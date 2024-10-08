package com.example.mapstructpractice.service.employee;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import com.example.mapstructpractice.dto.employee.CreateEmployeeRequestDto;
import com.example.mapstructpractice.dto.employee.EmployeeDto;
import com.example.mapstructpractice.mapper.EmployeeMapper;
import com.example.mapstructpractice.model.Employee;
import com.example.mapstructpractice.model.Skill;
import com.example.mapstructpractice.repository.employee.EmployeeRepository;
import com.example.mapstructpractice.repository.skill.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeMapper mapper;
    private final EmployeeRepository repository;
    private final SkillRepository skillRepository;


    @Override
    public List<EmployeeDto> findAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public EmployeeDto findById(Long id) {
        Optional<Employee> empById = repository.findById(id);
        if (empById.isPresent()) {
            return mapper.toDto(empById.get());
        }
        throw new RuntimeException("was not found employee with id: " + id);
    }

    @Override
    public EmployeeDto getByName(String name) {
        Employee employeeByName = repository.findByName(name);
        EmployeeDto employeeDto = mapper.toDto(employeeByName);
        return employeeDto;
    }

    @Override
    public EmployeeDto save(CreateEmployeeRequestDto requestDto) {
        Employee employee = mapper.toModel(requestDto);

        // Generate random social security number
        employee.setSocialSecurityNumber("abc " + (new Random().nextInt(1000)));

        // Fetch skills (for names) from repository before saving and transmit
        // it to the mapper
        List<Skill> skills = skillRepository.findAllById(requestDto.skills());
        employee.setSkillList(skills);

        Employee savedEmployee = repository.save(employee);
        return mapper.toDto(savedEmployee);
    }
}
