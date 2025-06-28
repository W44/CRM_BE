package com.crmw.CRM_BE.controllers;


import com.crmw.CRM_BE.dto.DonerRequestDto;
import com.crmw.CRM_BE.entity.Doner;
import com.crmw.CRM_BE.service.DonerService;
import com.crmw.CRM_BE.service.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.Duration;
import java.util.List;


@RestController
@RequestMapping("/api/v1/doner")
public class DonerController {

    @Autowired
    private DonerService donerService;

    @Autowired
    private RedisLockService redisLockService;


    @PostMapping
    public ResponseEntity<?> createDoner(@RequestBody DonerRequestDto donerdto) {

        if (donerdto.getId() == null || donerdto.getName() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("ID and Name is required when creating a Doner.");
        }
        Doner doner = new Doner();
        doner.setId(donerdto.getId());
        doner.setName(donerdto.getName());
        doner.setEmail(donerdto.getEmail());
        doner.setPhone(donerdto.getPhone());

        Doner saved = donerService.saveDoner(doner);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<Page<Doner>> getAllDoners(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id,asc") String[] sort,
                                                    @RequestParam(required = false) String search) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Doner> donerPage = donerService.getAllDoners(search, pageable);

        return ResponseEntity.ok(donerPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doner> getDonerById(@PathVariable Integer id) {
        return donerService.getDonerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doner> updateDoner(@PathVariable Integer id, @RequestBody DonerRequestDto donerdto) {

        Doner doner = new Doner();
        doner.setName(donerdto.getName());
        doner.setEmail(donerdto.getEmail());
        doner.setPhone(donerdto.getPhone());

        Doner updated = donerService.updateDoner(id, doner);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoner(@PathVariable Integer id) {
        boolean deleted = donerService.deleteDoner(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/unlock")
    public ResponseEntity<?> unlockDonor(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        redisLockService.releaseLock(id, username);
        return ResponseEntity.ok("Lock released.");
    }

    @PostMapping("/{id}/lock")
    public ResponseEntity<?> lockDonor(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean locked = redisLockService.tryLockDonor(id, username, 5 * 60);
        if (!locked) {
            String currentUser = redisLockService.getCurrentLocker(id);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Donor is currently being contacted by: " + currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Locked for Editing");
        }
    }

}



