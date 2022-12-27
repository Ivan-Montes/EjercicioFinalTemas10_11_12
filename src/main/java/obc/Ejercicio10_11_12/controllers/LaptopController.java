package obc.Ejercicio10_11_12.controllers;

import obc.Ejercicio10_11_12.entities.Laptop;
import obc.Ejercicio10_11_12.repos.LaptopRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LaptopController {

    @Value("${app.message}")
    private String app_prop_msg;
    private LaptopRepository laptopRepository;

    public LaptopController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @GetMapping("/api/laptops")
    public List<Laptop> getLaptopList(){
        System.out.println("Mensaje guardado en application.properties => " + app_prop_msg);
        return laptopRepository.findAll();
    }

    @PostMapping("/api/createLaptop")
    public Laptop createLaptop(@RequestBody Laptop laptop){
        return laptopRepository.save(laptop);
    }

    @GetMapping("/api/findAll")
    public ResponseEntity<List<Laptop>>findAll(){
        ResponseEntity<List<Laptop>> responseEntity;
        List<Laptop> listaLaptop = laptopRepository.findAll();

        if ( listaLaptop.isEmpty() ){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listaLaptop);

    }
    @GetMapping("/api/findOneById/{id}")
    public ResponseEntity<Laptop> findOneById(@PathVariable("id")Long id){

        Optional<Laptop>opt = laptopRepository.findById(id);

        if( !opt.isPresent() ){
           return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(opt.get());
    }

    @PostMapping("/api/create")
    public ResponseEntity<Laptop> create(@RequestBody Laptop laptop){

        if (laptop == null || laptop.getId() != null){
            return ResponseEntity.badRequest().build();
        }

            Laptop laptopCreated = laptopRepository.save(laptop);

            if (laptopCreated == null){
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(laptopCreated);

    }

    @PutMapping("/api/update")
    public ResponseEntity<Laptop> update(@RequestBody Laptop laptop){

        if ( laptop == null || laptop.getId() == null){
            return ResponseEntity.badRequest().build();
        }

        Optional<Laptop>opt = laptopRepository.findById(laptop.getId());

        if ( opt.isEmpty() ){
           return ResponseEntity.notFound().build();
        }

        Laptop laptopRecu = laptopRepository.save(laptop);

        return ResponseEntity.ok(laptopRecu);

    }

    @DeleteMapping("api/delete/{id}")
    public ResponseEntity<Laptop>delete(@PathVariable("id")Long id){
        if ( !laptopRepository.existsById(id) ){
            return ResponseEntity.notFound().build();
        }
        laptopRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("api/deleteAll")
    public ResponseEntity<Laptop>delete(){
        if (laptopRepository == null || laptopRepository.count() < 1 ){
            ResponseEntity.notFound().build();
        }
        laptopRepository.deleteAll();
        return ResponseEntity.noContent().build();

    }


}
