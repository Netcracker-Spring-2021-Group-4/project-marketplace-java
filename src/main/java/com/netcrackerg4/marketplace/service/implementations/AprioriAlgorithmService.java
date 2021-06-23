package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

     @Scheduled(fixedRate = 20000)
    public void algorithm(){
        System.out.println(productDao.getAllProductsSupport().toString());

      List<UUID> products = new ArrayList<>(productDao.getAllProductsSupport().entrySet().stream()
              .filter(x -> x.getValue() >= MIN_SUPPORT)
              .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()))
              .keySet());
      if(products.isEmpty()) return;



        Map<UUID,Set<UUID>> recommends = new HashMap<>();
        Map<UUID[], Double> recommendationsWithLift = new HashMap<>();
        for (int i = 0; i < products .size(); i++) {
            for (int j = 0; j < products.size(); j++) {
                if(j==i) continue;

                int support = productDao.getProductsSupport(products.get(i), products.get(j));
                if (support < MIN_SUPPORT)
                    continue;
                int supportA = productDao.getProductFrequency(products.get(i));

                if((double)support/supportA>=MIN_CONFIDENCE){
                    double lift = (double)support/productDao.getProductFrequency(products.get(j));
                    recommendationsWithLift.put(new UUID[]{products.get(i), products.get(j)},lift);
                    if(recommends.get(products.get(i)).isEmpty())
                       recommends.put(products.get(i), Collections.singleton(products.get(j)));
                    else
                        recommends.get(products.get(i)).add(products.get(j));
                }
            }
            }
        System.out.println(recommendationsWithLift.toString());
        System.out.println(recommends.toString());
        }

   //   if(recommendations.isEmpty()) return;

       // for (Map.Entry<UUID[], Integer> entry : recommendations.entrySet()) {
        //  double confidence = entry.getValue()/entry.getKey()[0]
     // }





    }




