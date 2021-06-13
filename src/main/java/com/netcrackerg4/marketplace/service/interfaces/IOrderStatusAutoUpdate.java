package com.netcrackerg4.marketplace.service.interfaces;

public interface IOrderStatusAutoUpdate {
    void initSchedulers(Runnable updSubmitted);

    void updateSubmitted();
}
