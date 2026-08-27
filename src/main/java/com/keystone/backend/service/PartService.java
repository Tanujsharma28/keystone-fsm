package com.keystone.backend.service;

import com.keystone.backend.domain.Part;
import com.keystone.backend.dto.PartRequest;
import com.keystone.backend.dto.PartResponse;
import com.keystone.backend.exception.ResourceNotFoundException;
import com.keystone.backend.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    private PartResponse toResponse(Part part) {
        return new PartResponse(
                part.getId(),
                part.getName(),
                part.getSku(),
                part.getStockQuantity(),
                part.getUnitPrice()
        );
    }

    public List<PartResponse> getAllParts() {
        return partRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PartResponse getPartById(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + id));
        return toResponse(part);
    }

    public PartResponse createPart(PartRequest request) {
        Part part = new Part();
        part.setName(request.getName());
        part.setSku(request.getSku());
        part.setStockQuantity(request.getStockQuantity());
        part.setUnitPrice(request.getUnitPrice());
        return toResponse(partRepository.save(part));
    }

    public PartResponse updatePart(Long id, PartRequest request) {
        Part existing = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + id));
        existing.setName(request.getName());
        existing.setSku(request.getSku());
        existing.setStockQuantity(request.getStockQuantity());
        existing.setUnitPrice(request.getUnitPrice());
        return toResponse(partRepository.save(existing));
    }

    public void deletePart(Long id) {
        Part existing = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + id));
        partRepository.delete(existing);
    }
}