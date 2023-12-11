package com.walker.parkingmanagement.web.controller;

import com.walker.parkingmanagement.entity.Client;
import com.walker.parkingmanagement.jwt.JwtUserDetails;
import com.walker.parkingmanagement.service.ClientService;
import com.walker.parkingmanagement.service.UserService;
import com.walker.parkingmanagement.web.dto.ClientCreateDto;
import com.walker.parkingmanagement.web.dto.ClientResponseDto;
import com.walker.parkingmanagement.web.dto.mapper.ClientMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    private final ClientService clientService;
    private final UserService userService;

    @PostMapping("/create-client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> createClient(@RequestBody @Valid ClientCreateDto clientCreateDto, @AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        Client client = ClientMapper.toClient(clientCreateDto);
        client.setUser(userService.findById(jwtUserDetails.getId()));
        clientService.save(client);
        return ResponseEntity.status(201).body(ClientMapper.toDto(client));
    }
}
