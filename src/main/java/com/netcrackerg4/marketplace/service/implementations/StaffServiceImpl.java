package com.netcrackerg4.marketplace.service.implementations;
import com.netcrackerg4.marketplace.model.domain.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.user.StaffUpdateDto;
import com.netcrackerg4.marketplace.repository.interfaces.IUserDao;
import com.netcrackerg4.marketplace.service.interfaces.IStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements IStaffService {
    private final IUserDao userDao;

    @Transactional
    @Override
    public void updateUser(StaffUpdateDto staffUpdate) {
        String email = staffUpdate.getEmail();

        AppUserEntity user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with such email not found."));


        AppUserEntity userEntity = AppUserEntity.builder()
                .userId(user.getUserId())
                .email(email)
                .password(user.getPassword())
                .firstName(staffUpdate.getFirstName())
                .lastName(staffUpdate.getLastName())
                .phoneNumber(staffUpdate.getPhoneNumber())
                .status(user.getStatus())
                .roleId(user.getRoleId())
                .build();
        userDao.update(userEntity);
    }
}