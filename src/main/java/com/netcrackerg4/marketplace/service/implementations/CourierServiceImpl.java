package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.response.CourierOrderResponse;
import com.netcrackerg4.marketplace.repository.interfaces.ICourierDao;
import com.netcrackerg4.marketplace.service.interfaces.ICourierService;
import com.netcrackerg4.marketplace.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CourierServiceImpl implements ICourierService {
    private IUserService userService;
    private final ICourierDao courierDao;

    @Override
    public List<CourierOrderResponse> getDayTimeslots(LocalDate date, String email) {

        UUID courierId = userService.findByEmail(email).orElseThrow().getUserId();
        return courierDao.getCourierSlots(courierId,date);
    }
}
