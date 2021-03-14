package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;
    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId,
                                                  @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }


    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId,
                                                @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroupId(groupId, pageable);
        return studentPage;
    }

    // get student information
    @GetMapping(value = "/{id}")
    public Student getById(@PathVariable Integer id) {

        Optional<Student> optionalStudent = studentRepository.findById(id);
        return optionalStudent.orElseGet(Student::new);
    }

    //C
    @PostMapping
    public String add(@RequestBody StudentDto studentDto) {
        Optional<Group> optionalGroups = groupRepository.findById(studentDto.getGroupsId());
        if (optionalGroups.isPresent()) {
            Student student = new Student();
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            Address address = new Address();
            address.setCity(studentDto.getCity());
            address.setDistrict(studentDto.getDistrict());
            address.setStreet(studentDto.getStreet());
            student.setAddress(address);
            student.setGroup(optionalGroups.get());
            List<Subject> subjectList = new ArrayList<>();
            List<Integer> integerList = studentDto.getSubject();
            for (Integer integerStud : integerList) {
                Optional<Subject> byId = subjectRepository.findById(integerStud);
                if (!byId.isPresent())
                    return "subject id not found";
                subjectList.add(byId.get());
            }
            student.setSubjects(subjectList);
            studentRepository.save(student);
            return "student saved";
        }
        return "groups id not found";
    }


    //U
    @PutMapping(value = "/{id}")
    public String edit(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Optional<Group> optionalGroups = groupRepository.findById(studentDto.getGroupsId());
        if (optionalGroups.isPresent()) {
            Optional<Student> optionalStudent = studentRepository.findById(id);
            if (optionalStudent.isPresent()) {
                Student student = optionalStudent.get();
                student.setFirstName(studentDto.getFirstName());
                student.setLastName(studentDto.getLastName());
                Address address = new Address();
                address.setCity(studentDto.getCity());
                address.setDistrict(studentDto.getDistrict());
                address.setStreet(studentDto.getStreet());
                student.setAddress(address);
                student.setGroup(optionalGroups.get());
                List<Subject> subjectList = new ArrayList<>();
                List<Integer> integerList = studentDto.getSubject();
                for (Integer integerStud : integerList) {
                    Optional<Subject> byId = subjectRepository.findById(integerStud);
                    if (!byId.isPresent())
                        return "subject id not found";
                    subjectList.add(byId.get());
                }
                student.setSubjects(subjectList);
                studentRepository.save(student);
                return "student edited";
            }
            return "student id not found";
        }
        return "groups id not found";
    }


    //D
    @DeleteMapping(value = "/{id}")
    public String deleteById(@PathVariable Integer id) {
        try {
            studentRepository.deleteById(id);
            return "student deleted";
        } catch (Exception e) {
            return "error in deleting";
        }
    }

}
