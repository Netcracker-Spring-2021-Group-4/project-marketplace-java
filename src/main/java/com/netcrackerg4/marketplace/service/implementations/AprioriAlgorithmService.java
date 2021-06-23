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

     @Scheduled(fixedRate = 200000)
    public void algorithm(){

      List<UUID> products = new ArrayList<>(productDao.getAllProductsSupport().entrySet().stream()
              .filter(x -> x.getValue() >= MIN_SUPPORT)
              .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()))
              .keySet());
      if(products.isEmpty()) return;



        Map<UUID[], Double> recommendationsWithLift = new HashMap<>();
        for (int i = 0; i < products .size(); i++) {
            for (int j = 0; j < products.size(); j++) {
                if(j==i) continue;

                int support = productDao.getProductsSupport(products.get(i), products.get(j));
                if (support < MIN_SUPPORT)
                    continue;
                int supportA = productDao.getProductFrequency(products.get(i));
                double confidence= (double)support/supportA;
                if(confidence>=MIN_CONFIDENCE){
                    double lift = confidence/productDao.getProductFrequency(products.get(j));
                    recommendationsWithLift.put(new UUID[]{products.get(i), products.get(j)},lift);
                    System.out.println(products.get(i)+"......"+ products.get(j)+ "  confidence=  "+confidence);
                }
            }
            }
         for (Map.Entry<UUID[],Double> entry : recommendationsWithLift.entrySet()){
             System.out.println(entry.getKey()[0].toString()+" -> "+ entry.getKey()[1].toString()+"    "+ entry.getValue());
         }

        }







    }




