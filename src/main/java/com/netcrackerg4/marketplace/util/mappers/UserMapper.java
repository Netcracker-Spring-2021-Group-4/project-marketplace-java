package com.netcrackerg4.marketplace.util.mappers;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.user.UserCoreView;
import org.springframework.beans.BeanUtils;

public class UserMapper {
    public static UserCoreView entityToCoreView(AppUserEntity userEntity) {
        UserCoreView coreView = new UserCoreView();
        BeanUtils.copyProperties(userEntity, coreView);
        return coreView;
    }

}
