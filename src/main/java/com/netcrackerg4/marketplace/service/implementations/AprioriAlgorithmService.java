package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.model.domain.product.RecommendationEntity;
import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@AllArgsConstructor
@Data
@Service
public class AprioriAlgorithmService {

    private static final int MIN_SUPPORT=2;
    private static final double MIN_CONFIDENCE=0.6;


    private final IProductDao productDao;

     @Scheduled(fixedRate = 200000)
    public void algorithm(){

      Map<UUID,Integer> productsMap = productDao.getAllProductsSupport().entrySet().stream()
                 .filter(x -> x.getValue() >= MIN_SUPPORT)
                 .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

      List<UUID> products = new ArrayList<>(productsMap.keySet());
      if(products.isEmpty()) return;


        List<RecommendationEntity> recommendations = new ArrayList<>();
        for (int i = 0; i < products .size(); i++) {
            for (int j = 0; j < products.size(); j++) {
                if(j==i) continue;

                UUID prodA=products.get(i);
                UUID prodB=products.get(j);

                int support = productDao.getProductsSupport(prodA, prodB);
                if (support < MIN_SUPPORT)
                    continue;
                double confidence= (double)support/productsMap.get(prodA);
                if(confidence>=MIN_CONFIDENCE){
                    double lift = confidence/productsMap.get(prodB);
                    recommendations.add(new RecommendationEntity(prodA, prodB,lift));
                }
            }
            }
        productDao.updateRecommendations(recommendations);
      

        }







    }




