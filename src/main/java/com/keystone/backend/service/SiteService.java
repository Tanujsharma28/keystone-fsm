package com.keystone.backend.service;

import com.keystone.backend.domain.Customer;
import com.keystone.backend.domain.Site;
import com.keystone.backend.dto.SiteRequest;
import com.keystone.backend.dto.SiteResponse;
import com.keystone.backend.exception.ResourceNotFoundException;
import com.keystone.backend.repository.CustomerRepository;
import com.keystone.backend.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;
    private final CustomerRepository customerRepository;

    private SiteResponse toResponse(Site site) {
        return new SiteResponse(
                site.getId(),
                site.getCustomer().getId(),
                site.getCustomer().getName(),
                site.getName(),
                site.getAddress(),
                site.getCreatedAt()
        );
    }

    public List<SiteResponse> getAllSites() {
        return siteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SiteResponse getSiteById(Long id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site not found: " + id));
        return toResponse(site);
    }

    public List<SiteResponse> getSitesByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found: " + customerId);
        }
        return siteRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SiteResponse createSite(SiteRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getCustomerId()));
        Site site = new Site();
        site.setCustomer(customer);
        site.setName(request.getName());
        site.setAddress(request.getAddress());
        return toResponse(siteRepository.save(site));
    }

    public SiteResponse updateSite(Long id, SiteRequest request) {
        Site existing = siteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site not found: " + id));
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getCustomerId()));
        existing.setCustomer(customer);
        existing.setName(request.getName());
        existing.setAddress(request.getAddress());
        return toResponse(siteRepository.save(existing));
    }

    public void deleteSite(Long id) {
        Site existing = siteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site not found: " + id));
        siteRepository.delete(existing);
    }
}