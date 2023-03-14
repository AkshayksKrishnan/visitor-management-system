package io.bootify.l11_visitor_management.service;

import io.bootify.l11_visitor_management.domain.Address;
import io.bootify.l11_visitor_management.domain.Flat;
import io.bootify.l11_visitor_management.domain.Role;
import io.bootify.l11_visitor_management.domain.User;
import io.bootify.l11_visitor_management.model.UserDTO;
import io.bootify.l11_visitor_management.repos.AddressRepository;
import io.bootify.l11_visitor_management.repos.FlatRepository;
import io.bootify.l11_visitor_management.repos.RoleRepository;
import io.bootify.l11_visitor_management.repos.UserRepository;
import io.bootify.l11_visitor_management.util.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final FlatRepository flatRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    public UserService(final UserRepository userRepository, final FlatRepository flatRepository,
            final AddressRepository addressRepository, final RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.flatRepository = flatRepository;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map((user) -> mapToDTO(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setFlat(user.getFlat() == null ? null : user.getFlat().getId());
        userDTO.setAddress(user.getAddress() == null ? null : user.getAddress().getId());
        userDTO.setRole(user.getRole() == null ? null : user.getRole().getId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        final Flat flat = userDTO.getFlat() == null ? null : flatRepository.findById(userDTO.getFlat())
                .orElseThrow(() -> new NotFoundException("flat not found"));
        user.setFlat(flat);
        final Address address = userDTO.getAddress() == null ? null : addressRepository.findById(userDTO.getAddress())
                .orElseThrow(() -> new NotFoundException("address not found"));
        user.setAddress(address);
        final Role role = userDTO.getRole() == null ? null : roleRepository.findById(userDTO.getRole())
                .orElseThrow(() -> new NotFoundException("role not found"));
        user.setRole(role);
        return user;
    }

}
