package helderklemp.service;

import helderklemp.model.Employee;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by helderklemp on 16/7/17.
 */
public class EmployeeServiceImpl implements EmployeeService {

    private Collection<Employee> employees;


    public EmployeeServiceImpl(){
        employees=new LinkedHashSet<>();
    }

    @Override
    public Collection<Employee> findEmployeeByManager(Long managerId) {
        if(managerId ==null){
            return employees.stream().filter(emp -> emp.getManager() ==null).collect(Collectors.toSet());
        }else{
            return employees.stream().filter(emp ->
                    (emp.getManager()!=null && emp.getManager().getId().equals(managerId))).collect(Collectors.toSet());
        }
    }

    @Override
    public void addEmployee(Employee emp) {
        validateRecord(emp);
        employees.add(emp);
    }

    private void validateRecord(Employee emp) {
        if(isValidRecord(emp)){
            throw new IllegalArgumentException("Invalid employee,id and name are required fields");
        }
        if(isDuplicatedCEO(emp)){
            throw new IllegalArgumentException("Invalid employee,CEO already registered, the record must have a manager");
        }
        if(emp.getManager()!=null && !isValidManager(emp.getManager())){
            throw new IllegalArgumentException("Invalid employee,Manager does not exist");
        }
    }

    private boolean isValidManager(Employee emp) {
        return !findEmployeeById(emp.getId()).isEmpty();
    }

    private Collection<Employee> findEmployeeById(Long id) {
        return employees.stream().filter(emp-> emp.getId().equals(id)).collect(Collectors.toSet());
    }

    private boolean isValidRecord(Employee emp) {
        return emp==null||emp.getId()==null||emp.getName()==null;
    }

    private boolean isDuplicatedCEO(Employee emp) {
        if(emp.getManager()!=null){
            return false;
        }
        Collection<Employee> ceoResult=findEmployeeByManager(null);
        if(ceoResult.isEmpty()){
            return false;
        }
        Employee currentCEO =ceoResult.iterator().next();
        return (!emp.equals(currentCEO));
    }



}
