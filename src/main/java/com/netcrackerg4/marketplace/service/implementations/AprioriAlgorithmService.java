package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.repository.interfaces.IProductDao;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AprioriAlgorithmService {

    private static final int MIN_SUPPORT=2;
    private static final double MIN_CONFIDENCE=0.6;


    private final IProductDao productDao;

    public void algorithm(){

      List<UUID> products = new ArrayList<>(productDao.getAllProductsSupport().entrySet().stream()
              .filter(x -> x.getValue() >= MIN_SUPPORT)
              .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()))
              .keySet());
      if(products.isEmpty()) return;



        Map<UUID,Set<UUID>> recommends = new HashMap<>();
        for (int i = 0; i < products .size(); i++) {
            for (int j = 0; j < products.size(); j++) {
                if(j==i) continue;

                int support = productDao.getProductsSupport(products.get(i), products.get(j));
                if (support < MIN_SUPPORT)
                    continue;
                int supportA = productDao.getProductFrequency(products.get(i));

                if((double)support/supportA>=MIN_CONFIDENCE){
                    if(recommends.get(products.get(i)).isEmpty())
                       recommends.put(products.get(i), Collections.singleton(products.get(j)));
                    else
                        recommends.get(products.get(i)).add(products.get(j));
                };
            }
            }
        }

   //   if(recommendations.isEmpty()) return;

       // for (Map.Entry<UUID[], Integer> entry : recommendations.entrySet()) {
        //  double confidence = entry.getValue()/entry.getKey()[0]
     // }





    }

/*
    private void calculateFrequentItemsets(List<UUID> itemsets) throws Exception
    {

     //   log("Passing through the data to compute the frequency of " + itemsets.size()+ " itemsets of size "+itemsets.get(0).length);

        List<int[]> frequentCandidates = new ArrayList<int[]>(); //the frequent candidates for the current itemset

        boolean match; //whether the transaction has all the items in an itemset
        int count[] = new int[itemsets.size()]; //the number of successful matches, initialized by zeros


        // load the transaction file
        BufferedReader data_in = new BufferedReader(new InputStreamReader(new FileInputStream(transaFile)));

        boolean[] trans = new boolean[numItems];

        // for each transaction
        for (int i = 0; i < numTransactions; i++) {

            // boolean[] trans = extractEncoding1(data_in.readLine());
            String line = data_in.readLine();
            line2booleanArray(line, trans);

            // check each candidate
            for (int c = 0; c < itemsets.size(); c++) {
                match = true; // reset match to false
                // tokenize the candidate so that we know what items need to be
                // present for a match
                int[] cand = itemsets.get(c);
                //int[] cand = candidatesOptimized[c];
                // check each item in the itemset to see if it is present in the
                // transaction
                for (int xx : cand) {
                    if (trans[xx] == false) {
                        match = false;
                        break;
                    }
                }
                if (match) { // if at this point it is a match, increase the count
                    count[c]++;
                    //log(Arrays.toString(cand)+" is contained in trans "+i+" ("+line+")");
                }
            }

        }

        data_in.close();

        for (int i = 0; i < itemsets.size(); i++) {
            // if the count% is larger than the minSup%, add to the candidate to
            // the frequent candidates
            if ((count[i] / (double) (numTransactions)) >= minSup) {
                foundFrequentItemSet(itemsets.get(i),count[i]);
                frequentCandidates.add(itemsets.get(i));
            }
            //else log("-- Remove candidate: "+ Arrays.toString(candidates.get(i)) + "  is: "+ ((count[i] / (double) numTransactions)));
        }

        //new candidates are only the frequent candidates
        itemsets = frequentCandidates;
    }
 */


